function switchStyle(node, stylename)
{
   node.className=stylename;
}

var openedTabID = "";

function closeAllMenu()
{
	closeMenu(openedTabID);
}


function detectMouseOut(event, obj)
{
	var current, related;
	if (window.event)
	{
		current = obj;
		related = window.event.toElement;
	}
	else
	{
		current = event.currentTarget;
		related = event.relatedTarget;
	}
	if (current != related)
	{
		closeMenu(openedTabID);
	}
}



function selectMenu(nodeID)
{
	if(document.getElementById)
	{
		closeMenu(openedTabID);
		openMenu(nodeID);
		openedTabID = nodeID;		
	}
	else
	{
		return true;
	}
}

function switchVisibility(nodeID)
{
	if(document.getElementById)
	{
		var s = document.getElementById(nodeID).style;
		if(s.display == "block")
		{
			s.display = "none";
		}
		else
		{
			s.display = "block";
		}
		return false;
	}
	else
	{
		return true;
	}
}

function openMenu(nodeID)
{
	var s = document.getElementById(nodeID).style;
	s.display = "block";
	return false;
}

function closeMenu(nodeID)
{
	if(nodeID.length != 0)
	{
		document.getElementById(nodeID).style.display = "none";
		openedTabID = "";
		pointingMenuID = "";		
	}
}
