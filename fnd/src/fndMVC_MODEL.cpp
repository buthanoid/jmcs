/*******************************************************************************
 * JMMC project
 *
 * "@(#) $Id: fndMVC_MODEL.cpp,v 1.1 2005-06-13 10:24:06 scetre Exp $"
 *
 * History
 * -------
 * $Log: not supported by cvs2svn $
 ******************************************************************************/

/**
 * \file
 *  Definition of fndMVC_MODEL class.
 */

static char *rcsId="@(#) $Id: fndMVC_MODEL.cpp,v 1.1 2005-06-13 10:24:06 scetre Exp $"; 
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

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
#include "fndMVC_MODEL.h"
#include "fndPrivate.h"

/**
 * Class constructor
 */
fndMVC_MODEL::fndMVC_MODEL()
{
}

/**
 * Class destructor
 */
fndMVC_MODEL::~fndMVC_MODEL()
{
}

/*
 * Public methods
 */
/**
 * Add a view in the list of views associated to this model
 *
 * \param view the view to add in the list
 */
mcsCOMPL_STAT fndMVC_MODEL::AddView(fndMVC_VIEW *view)
{
    logTrace("fndMVC_MODEL::AddView()");

    _viewList.push_back(view);
    
    return mcsSUCCESS;
}

/**
 * Delete a view from the list of views associated to this model
 *
 * \param view the view to delete from the list
 *
 * \return always mcsSUCCESS
 */
mcsCOMPL_STAT fndMVC_MODEL::DeleteView(fndMVC_VIEW *view)
{
    logTrace("fndMVC_MODEL::DeleteView()");

    // Create an iterator of a fndViewList in order to be able to move in the
    // list
    fndViewList::iterator viewListIterator;
    // Put this iterator at the beginning of the list
    viewListIterator = _viewList.begin();
    mcsLOGICAL isFound = mcsFALSE;
    
    // Check if the view to remove is in the list of view
    while ((viewListIterator != _viewList.end()) || (isFound != mcsTRUE))
    {
        // If the view is equal to one of the list
        if ((*viewListIterator) == view)
        {
            // remove the view of the list
            _viewList.erase(viewListIterator);
            // Changed isFound as true
            isFound = mcsTRUE;
        }
        else
        {
            viewListIterator ++;
        }
    }
    
    return mcsSUCCESS;
}

/**
 * Delete all views from the list of views associated to this model
 *
 * \return always mcsSUCCESS
 */
mcsCOMPL_STAT fndMVC_MODEL::DeleteViews()
{
    logTrace("fndMVC_MODEL::DeleteViews()");

    // erase all element between the beginning and the end of the list
    _viewList.erase(_viewList.begin(), _viewList.end());
        
    return mcsSUCCESS;
}

/**
 * Notify all views from the list of views associated to this model
 *
 * \return mcsSUCCESS on successful completion. Otherwise mcsFAILURE is 
 * returned.
 */
mcsCOMPL_STAT fndMVC_MODEL::NotifyViews()
{
    logTrace("fndMVC_MODEL::NotifyViews()");

    // Create an iterator of a fndViewList in order to be able to move in the
    // list
    fndViewList::iterator viewListIterator;
    // Put this iterator at the beginning of the list
    viewListIterator = _viewList.begin();

    // For each view of the list of views, Update them
    while (viewListIterator != _viewList.end())
    {
        if ((*viewListIterator)->Update() == mcsFAILURE)
        {
            return mcsFAILURE;
        }
        viewListIterator ++;
    }

    return mcsSUCCESS;
}

/**
 * Get the number of views associated to this model
 *
 * \return number of views associated to this model
 */
mcsINT32 fndMVC_MODEL::GetNbViews()
{
    logTrace("fndMVC_MODEL::GetNbViews()");
    
    return _viewList.size();
}


/*
 * Protected methods
 */


/*
 * Private methods
 */


/*___oOo___*/
