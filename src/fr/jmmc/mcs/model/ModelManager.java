/*******************************************************************************
 * JMMC project
 *
 * "@(#) $Id: ModelManager.java,v 1.3 2010-02-08 16:56:26 bourgesl Exp $"
 *
 * History
 * -------
 * $Log: not supported by cvs2svn $
 * Revision 1.2  2010/02/03 16:05:46  bourgesl
 * Added fast thread interruption checks for asynchronous uv map computation
 *
 * Revision 1.1  2010/01/29 15:52:45  bourgesl
 * Beginning of the Target Model Java implementation = ModelManager and ModelFunction implementations (punct, disk)
 *
 */
package fr.jmmc.mcs.model;

import fr.jmmc.mcs.model.function.DiskModelFunction;
import fr.jmmc.mcs.model.function.PunctModelFunction;
import fr.jmmc.mcs.model.targetmodel.Model;
import fr.jmmc.mcs.model.targetmodel.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.commons.math.complex.Complex;

/**
 * This class constitutes the main interface to target models (supported models, new model, computeModels)
 * @author bourgesl
 */
public class ModelManager {

  /** Class Name */
  private static final String className_ = "fr.jmmc.mcs.model.ModelManager";
  /** Class logger */
  private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(
          className_);
  /** singleton pattern */
  private static ModelManager instance = new ModelManager();
  // members :
  /** List of model type */
  private final List<String> modelTypes = new ArrayList<String>();
  /** Map : model type, ModelFunction instance */
  private final Map<String, ModelFunction> modelFunctions = new HashMap<String, ModelFunction>();

  /**
   * Return the ModelManager singleton
   * @return ModelManager singleton
   */
  public static ModelManager getInstance() {
    return instance;
  }

  /**
   * Constructor
   */
  private ModelManager() {
    super();
    this.registerFunctions();
  }

  private void registerFunctions() {
    // 1 - Punct Model :
    this.addFunction(new PunctModelFunction());
    // 2 - Disk Model :
    this.addFunction(new DiskModelFunction());

    if (logger.isLoggable(Level.FINE)) {
      logger.fine("functions : " + modelFunctions);
    }
  }

  private void addFunction(final ModelFunction mf) {
    final String type = mf.getType();
    this.modelFunctions.put(type, mf);
    this.modelTypes.add(type);
  }

  /**
   * Return the list of model types
   * @return list of model types
   */
  public List<String> getSupportedModels() {
    return this.modelTypes;
  }

  /**
   * Return a new model of the given type
   * @param type type of the model
   * @return new model or null if the given type is unknown
   */
  public Model createModel(final String type) {
    return getModelFunction(type).newModel();
  }

  private ModelFunction getModelFunction(final String type) throws IllegalStateException {
    final ModelFunction mf = this.modelFunctions.get(type);
    if (mf == null) {
      throw new IllegalStateException("no model function registered for the type = " + type);
    }
    return mf;
  }

  /**
   * Compute the complex visiblity of given models for the given Ufreq and Vfreq arrays
   * 
   * @param ufreq U frequencies in rad-1
   * @param vfreq V frequencies in rad-1
   * @param models list of models to compute
   * @return normalized complex visibility
   */
  public Complex[] computeModels(final double[] ufreq, final double[] vfreq, final List<Model> models) {
    Complex[] vis = null;

    if (ufreq != null && vfreq != null && models != null && !models.isEmpty()) {

      /** Get the current thread to check if the computation is interrupted */
      final Thread currentThread = Thread.currentThread();

      vis = new Complex[ufreq.length];

      // initialize the visiblity array :
      Arrays.fill(vis, Complex.ZERO);

      // fast interrupt :
      if (currentThread.isInterrupted()) {
        return null;
      }

      // For now : no composite model supported (hierarchy) !

      ModelFunction mf;
      for (Model model : models) {
        mf = getModelFunction(model.getType());

        // add the model contribution :
        mf.compute(ufreq, vfreq, model, vis);

        // fast interrupt :
        if (currentThread.isInterrupted()) {
          return null;
        }
      }
    }

    return vis;
  }

  public static void normalize(final Complex[] vis) {
    double val;

    // 1 - Find maximum amplitude :
    double maxAmp = 0d;
    for (int i = 0, size = vis.length; i < size; i++) {
      // amplitude = complex modulus (abs in commons-math) :
      val = (float) vis[i].abs();
      if (val > maxAmp) {
        maxAmp = val;
      }
    }

    if (logger.isLoggable(Level.FINE)) {
      logger.fine("maxAmp : " + maxAmp);
    }

    // 2 - normalize :
    if (maxAmp != 0d) {
      final double factor = 1d / maxAmp;
      for (int i = 0, size = vis.length; i < size; i++) {
        // amplitude = complex modulus (abs in commons-math) :
        vis[i] = vis[i].multiply(factor);
      }
    }
  }


  /**
   * Return the parameter of the given type among the parameters of the given model
   * @param type type of the parameter
   * @param model model to use
   * @return parameter or null if the parameter was not found
   */
  public static Parameter getParameter(final Model model, final String type) {
    Parameter param = null;

    for (Parameter p : model.getParameter()) {
      if (type.equals(p.getType())) {
        param = p;
        break;
      }
    }

    return param;
  }

  /**
   * Set the parameter value
   * @param model model to use
   * @param type type of the parameter
   * @param value value to set
   */
  public static void setParameterValue(final Model model, final String type, final double value) {
    final Parameter parameter = getParameter(model, type);
    if (parameter != null) {
      parameter.setValue(value);
    }
  }
}
