function switchStyle(node, stylename)
{
   node.className=stylename;
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
