/*******************************************************************************
 * JMMC project
 *
 * "@(#) $Id: msgSOCKET_SERVER.cpp,v 1.9 2006-05-11 13:04:56 mella Exp $"
 *
 * History
 * -------
 * $Log: not supported by cvs2svn $
 * Revision 1.8  2005/04/04 15:09:36  gzins
 * Small changes in documentation
 *
 * Revision 1.7  2005/02/04 15:57:06  lafrasse
 * Massive documentation review an refinment (also added automatic CVS log inclusion in every files)
 *
 * Revision 1.6  2005/01/24 15:02:47  gzins
 * Added CVS logs as modification history
 *
 * gzins     07-Jan-2005  Changed SUCCESS/FAILURE to mcsSUCCESS/mcsFAILURE 
 * lafrasse  03-Dec-2004  Changed port number type from mcsINT32 to mcsUINT16
 * lafrasse  23-Nov-2004  Comment refinments, and includes cleaning
 * scetre    22-Nov-2004  Created
 *
 ******************************************************************************/

/**
 * \file
 * Interface class providing server-side specialized socket functionnalities.
 *
 * \sa msgSOCKET_SERVER
 */

static char *rcsId __attribute__ ((unused)) ="@(#) $Id: msgSOCKET_SERVER.cpp,v 1.9 2006-05-11 13:04:56 mella Exp $";

/* 
 * System Headers 
 */
#include <iostream>
using namespace std;


/*
 * MCS Headers 
 */
#include "mcs.h"
#include "log.h"
#include "err.h"


/*
 * Local Headers 
 */
#include "msgSOCKET.h"
#include "msgSOCKET_SERVER.h"
#include "msgPrivate.h"


/*
 * Class constructor
 */
msgSOCKET_SERVER::msgSOCKET_SERVER()
{
}


/*
 * Class destructor
 */
msgSOCKET_SERVER::~msgSOCKET_SERVER()
{
}


/*
 * Public methods
 */
/**
 * Create a new socket, bind it on the given port number and start listening.
 *
 * \param port the local port number on which the socket should listen
 *
 * \return mcsSUCCESS on successfull completion, mcsFAILURE otherwise
 */
mcsCOMPL_STAT msgSOCKET_SERVER::Open(mcsUINT16 port)
{
    logExtDbg("msgSOCKET_SERVER::Open()");

    // Create a new socket
    if (Create() == mcsFAILURE)
    {
        return mcsFAILURE;
    }

    // Bind the new socket to the given port number
    if (Bind(port) == mcsFAILURE)
    {
        return mcsFAILURE;
    }

    // Start listening the network with the new socket
    if (Listen() == mcsFAILURE)
    {
        return mcsFAILURE;
    }
        
    return mcsSUCCESS;
}


/*
 * Protected methods
 */



/*
 * Private methods
 */



/*___oOo___*/