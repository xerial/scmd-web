function switchStyle(node, stylename)
{
   node.className=stylename;
}

var openedTabID = "";

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
	document.getElementById(nodeID).style.display = "block";
}

function closeMenu(nodeID)
{
	if(nodeID.length != 0)
	{
		document.getElementById(nodeID).style.display = "none";
	}
}
