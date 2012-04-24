/*******************************************************************************
 * JMMC project ( http://www.jmmc.fr ) - Copyright (C) CNRS.
 ******************************************************************************/
package org.ivoa.util.runner;

import org.ivoa.util.LogUtil;
import org.slf4j.Logger;

/**
 * This class implements the JobListener interface (no op)
 * @author bourgesl
 */
public final class EmptyJobListener implements JobListener {

    /** logger */
    private static Logger logger = LogUtil.getLogger();

    /**
     * Perform the job event from the given root context.
     * Called when the job has the following transitions : pending / running / finished
     * @param rootCtx root context
     */
    @Override
    public void performJobEvent(final RootContext rootCtx) {
        logger.debug("performJobEvent: {}", rootCtx);
    }

    /**
     * Perform the task event from the given run context
     * @param rootCtx root context
     * @param runCtx  current run context
     */
    @Override
    public void performTaskEvent(final RootContext rootCtx, final RunContext runCtx) {
        logger.debug("performTaskEvent: {} - {}", rootCtx, runCtx);
    }

    /**
     * Perform the event from the given run context
     * @param rootCtx root context
     * @param runCtx  current run context
     * @return boolean: true if the processing should continue, false if the job should be terminated
     */
    @Override
    public boolean performTaskDone(final RootContext rootCtx, final RunContext runCtx) {
        logger.debug("performTaskDone: {} - {}", rootCtx, runCtx);
        return runCtx.getState() == RunState.STATE_FINISHED_OK;
    }
}