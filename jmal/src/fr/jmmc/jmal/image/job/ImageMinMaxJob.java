/*******************************************************************************
 * JMMC project ( http://www.jmmc.fr ) - Copyright (C) CNRS.
 ******************************************************************************/
package fr.jmmc.jmal.image.job;

import fr.jmmc.jmal.image.job.ImageMinMaxJob.MinMaxResult;
import java.util.List;

/**
 * Basic Job dedicated to find _min/max of image values
 * @author bourgesl
 */
public final class ImageMinMaxJob extends AbstractImageJob<MinMaxResult> {
    /* members */

    /** flag to ignore zero values */
    final boolean _excludeZero;

    /**
     * Create the image Job without excluding zero values
     *
     * @param array data array (2D)
     * @param width image width
     * @param height image height
     */
    public ImageMinMaxJob(final float[][] array, final int width, final int height) {
        this(array, width, height, false);
    }

    /**
     * Create the image Job
     *
     * @param array data array (2D)
     * @param width image width
     * @param height image height
     * @param excludeZero true to indicate to ignore zero values
     */
    public ImageMinMaxJob(final float[][] array, final int width, final int height,
                          final boolean excludeZero) {
        super("ImageMinMaxJob", array, width, height);
        this._excludeZero = excludeZero;
    }

    /**
     * Create the image Job given a parent job
     *
     * @param parentJob parent Job producing same result
     * @param jobIndex job index used to process data interlaced
     * @param jobCount total number of concurrent jobs
     */
    protected ImageMinMaxJob(final ImageMinMaxJob parentJob, final int jobIndex, final int jobCount) {
        super(parentJob, jobIndex, jobCount);
        this._excludeZero = parentJob._excludeZero;
    }

    /**
     * Initialize a new child job
     * @param jobIndex job index used to process data interlaced
     * @param jobCount total number of concurrent jobs
     * @return child job
     */
    @Override
    protected ImageMinMaxJob initializeChildJob(final int jobIndex, final int jobCount) {
        return new ImageMinMaxJob(this, jobIndex, jobCount);
    }

    /**
     * Initialize the result object (one per job)
     * @return result Object
     */
    @Override
    protected MinMaxResult initializeResult() {
        return new MinMaxResult();
    }

    /**
     * Merge partial result objects to produce the final result object
     * @param partialResults partial result objects
     */
    @Override
    protected void merge(final List<MinMaxResult> partialResults) {

        for (MinMaxResult partial : partialResults) {
            // nData:
            _result._nData += partial._nData;
            // min:
            if (partial._min < _result._min) {
                _result._min = partial._min;
            }
            // max:
            if (partial._max > _result._max) {
                _result._max = partial._max;
            }
            // sum:
            _result._sum += partial._sum;
        }
    }

    /**
     * Process the given value at the given row and column index
     * 
     * @param col row index
     * @param row column index
     */
    @Override
    protected void processValue(final int col, final int row, final float value) {
        if (_excludeZero && value == 0f) {
            return;
        }
        // nData:
        _result._nData++;
        // min:
        if (value < _result._min) {
            _result._min = value;
        }
        // max:
        if (value > _result._max) {
            _result._max = value;
        }
        // sum:
        _result._sum += value;
    }

    /**
     * Return the number of data
     * @return number of data
     */
    public int getNData() {
        return _result._nData;
    }

    /**
     * Return the minimum value in data
     * @return minimum value in data
     */
    public float getMin() {
        return _result._min;
    }

    /**
     * Return the maximum value in data
     * @return maximum value in data
     */
    public float getMax() {
        return _result._max;
    }

    /**
     * Return the sum of values
     * @return sum of values
     */
    public double getSum() {
        return _result._sum;
    }

    /**
     * Result container
     */
    protected static class MinMaxResult {

        /** number of data */
        protected int _nData = 0;
        /** minimum value */
        protected float _min = Float.POSITIVE_INFINITY;
        /** maximum value */
        protected float _max = Float.NEGATIVE_INFINITY;
        /** sum of values */
        protected double _sum = 0d;

        /**
         * Protected Constructor
         */
        MinMaxResult() {
            super();
        }
    }
}