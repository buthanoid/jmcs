/*******************************************************************************
 * JMMC project ( http://www.jmmc.fr ) - Copyright (C) CNRS.
 ******************************************************************************/

/**
 * \file
 * Definition of errCloseStack function.
 */
static char *rcsId __attribute__ ((unused)) = "@(#) $Id: errCloseStack.c,v 1.4 2006-01-10 14:40:39 mella Exp $"; 


/* 
 * System Headers
 */
#include <stdio.h>
#include <string.h>

/*
 * Common Software Headers
 */
#include "mcs.h"
#include "log.h"

/*
 * Local Headers
 */
#include "err.h"
#include "errPrivate.h"

/**
 * Logs errors and resets the global error structure.
 *
 * It logs all errors which have been placed in the global error stack and
 * reset it. This has to be done when the last error of the sequence cannot be
 * recovered.
 * \return mcsSUCCESS or mcsFAILURE if an error occured.
 *
 * \sa errAdd, errResetStack
 */
mcsCOMPL_STAT errCloseStack()
{
    logTrace("errCloseStack()");

    return (errCloseLocalStack(&errGlobalStack));
}

/*___oOo___*/
