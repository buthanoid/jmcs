/*******************************************************************************
* JMMC project
*
* History
* -------
* $Log: not supported by cvs2svn $
* Revision 1.2  2005/01/24 14:45:09  gzins
* Changed SUCCESS/FAILURE to mcsSUCCESS/mcsFAILURE
* Used CVS log as modification history
*
* gzins     17-Jun-2004  completed implementation
* berezne   02-Jun-2004  created
*
*-----------------------------------------------------------------------------*/

/**
 * \file
 * Definition of errStackIsEmpty function.
 */
static char *rcsId="@(#) $Id: errStackIsEmpty.c,v 1.3 2005-06-01 13:23:49 gzins Exp $"; 
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

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
 * Checks if the global error stack is empty 
 *
 * This routines checks if the global error stack is empty. 
 * 
 * \return mcsTRUE if the global error stack is empty, otherwise mcsFALSE.
 */
mcsLOGICAL errStackIsEmpty()
{
    logTrace("errStackIsEmpty()");

    /* Returns empty flag */
    return (errLocalStackIsEmpty(&errGlobalStack));
}

/*___oOo___*/

