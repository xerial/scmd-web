//--------------------------------------
//SCMDServer
//
//SearchSimilarShapeAction.java 
//Since: 2005/02/05
//
//$URL: http://scmd.gi.k.u-tokyo.ac.jp/devel/svn/phenome/trunk/SCMDWeb/src/lab/cb/scmd/web/action/SelectShapeAction.java $ 
//$LastChangedBy: leo $ 
//--------------------------------------
package lab.cb.scmd.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.cb.scmd.db.common.PageStatus;
import lab.cb.scmd.db.common.XMLQuery;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.bean.ORFGroup;
import lab.cb.scmd.web.bean.SelectedShape;
import lab.cb.scmd.web.bean.YeastGene;
import lab.cb.scmd.web.bean.YeastGeneWithDistance;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.datagen.MorphologicalSearch;
import lab.cb.scmd.web.datagen.ParamPair;
import lab.cb.scmd.web.xml.DOMParser;
import lab.cb.scmd.web.xml.XMLReaderThread;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SearchSimilarShapeAction extends Action {
    
    public SearchSimilarShapeAction() {
        super();
    }
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        SelectedShape shape = (SelectedShape) form;

        shape.setInputValue(shape.getParameterValue());

        MorphologicalSearch morphologicalSearch = new MorphologicalSearch(SCMDConfiguration.getTableQueryInstance());

        ParamPair[] paramsets = convertParamPair(shape);
        morphologicalSearch.setParamAndValues(paramsets);
        // TODO: 現在、上位20遺伝子限定
        List orfListWithDistance = morphologicalSearch.selectByEuclidianDistance(0, 20);
        List orfnameList = new Vector();
        HashMap orfdist = new HashMap();
        for(int i = 0; i < orfListWithDistance.size(); i++ ) {
            YeastGeneWithDistance yeastGene = (YeastGeneWithDistance)orfListWithDistance.get(i);  
            orfnameList.add(yeastGene.getOrf());
            orfdist.put(yeastGene.getOrf(), yeastGene.getDistance());
        }

        PageStatus pageStatus = new PageStatus(1, 1);
        Vector orfList = null;
        try{
            XMLQuery query = SCMDConfiguration.getXMLQueryInstance();
            DOMParser parser = new DOMParser();
            XMLReaderThread reader = new XMLReaderThread(parser);
            reader.start();
            query.getORFInfo(reader.getOutputStream(), orfnameList, 1);
            reader.join();
            
            Document document = parser.getDocument();
            NodeList nodeList = document.getElementsByTagName("orf");
            orfList = new Vector(nodeList.getLength());
            for(int i=0; i<nodeList.getLength(); i++)
            {
                orfList.add(new YeastGeneWithDistance((Element) nodeList.item(i)));
            }
            for(int i=0; i<orfList.size(); i++)
            {
                YeastGeneWithDistance genedist = (YeastGeneWithDistance)orfList.get(i);
                genedist.setDistance((Double)orfdist.get(genedist.getOrf()));
            }
            
            NodeList pageList = document.getElementsByTagName("page");
            if(pageList.getLength() > 0)
            {
                pageStatus = new PageStatus((Element) pageList.item(0));
            }
            if(orfList.size() == 0)
            {
                request.setAttribute("shape", shape);
                return mapping.findForward("notfound");
            }
        }
        catch(InterruptedException e)
        {
            System.out.println(e.getMessage());
        }
        catch(SCMDException e)
        {
            e.what();
        }

        request.setAttribute("shape", shape);
        request.setAttribute("orfList", orfList);
        request.setAttribute("pageStatus", pageStatus);
        
        return mapping.findForward("success");
    }

    /**
     * @param argMap
     * @return
     * 
     */
    private ParamPair[] convertParamPair(SelectedShape shape) {
        Map argMap = shape.getCurrentImageArgumentMap();

        ArrayList paramPairList = new ArrayList();
        int groupid;
        /*
         * groupidが指定されているときは、そのgroupで検索。
         * 指定されていないときには、areaRatioの芽の大きさからgroupを設定
         */
        if( argMap.get("groupid") == null ) {
           if(argMap.get("areaRatio") == null ) {
               groupid = 1; // nobud
           } else {
               double ratio = (Double)argMap.get("areaRatio");
               if( ratio <= 0.0 ) {
                   groupid = 1; // nobud
               } else if( ratio < 0.25 ) {
                   groupid = 2; // small
               } else if( ratio < 0.49 ) {
                   groupid = 3; // medium
               } else {
                   groupid = 4; // large
               }
           }
        } else {
           groupid = Integer.parseInt((String)argMap.get("groupid"));
        }
        Set key = argMap.keySet();
        Iterator it = key.iterator();
        while(it.hasNext()) {
            String keyName = (String)it.next();
            ParamPair pair = new ParamPair();
            pair.setGroupid(groupid);
            if (keyName.equals("growthDirection")) {
                pair.setParamid(34);
                pair.setValue((Double)argMap.get("growthDirection"));
                pair.setWeight(shape.getWeightGrowthDirection());
            } else if (keyName.equals("longAxis")) {
                pair.setParamid(31);
                pair.setValue((Double)argMap.get("longAxis"));
                pair.setWeight(shape.getWeightLongAxis());
            } else if (keyName.equals("neckPosition")) {
                pair.setParamid(33);
                pair.setValue((Double)argMap.get("neckPosition"));
                pair.setWeight(shape.getWeightNeckPosition());
            } else if (keyName.equals("roundness")) {
                pair.setParamid(43);
                pair.setValue((Double)argMap.get("roundness"));
                pair.setWeight(shape.getWeightRoundness());
            } else 
                continue;
            paramPairList.add(pair);
        }
        ParamPair[] paramPairs = new ParamPair[paramPairList.size()];
        for(int i = 0; i < paramPairList.size(); i++ ) {
            paramPairs[i] = (ParamPair)paramPairList.get(i);
        }
        return paramPairs;
    }
        

}
