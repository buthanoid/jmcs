/*******************************************************************************
 * JMMC project
 * 
 * "@(#) $Id: errUserGet.c,v 1.1 2005-01-27 14:12:55 gzins Exp $"
 *
 * History
 * -------
 * $Log: not supported by cvs2svn $
 ******************************************************************************/

/**
 * \file
 * Definition of errGetUser function.
 */

static char *rcsId="@(#) $Id: errUserGet.c,v 1.1 2005-01-27 14:12:55 gzins Exp $"; 
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

/* 
 * System Headers
 */
#include <stdio.h>

/*
 * MCS Headers 
 */
#include "log.h"
#include "err.h"

/* 
 * Local Headers
 */
#include "err.h"
#include "errPrivate.h"

/*
 * Public functions definition
 */

/**
 * Get the last end-user oriented error message stored in error stack.
 *
 * It looks for the last end-user oriented error message placed in the stack;
 * i.e. added using errUserAdd(). If not found in stack, it returns the last
 * added error. If the stack is empty, NULL is returned.
 *
 * \return last user error message or NULL if stack is empty.
 *
 * \sa errUserAdd
 */
char *errUserGet(void)
{
    logExtDbg("errUserGet()");

    /* Return user message stored in the global stack */
    return (errUserGetInLocalStack(&errGlobalStack));
}



/*___oOo___*/
