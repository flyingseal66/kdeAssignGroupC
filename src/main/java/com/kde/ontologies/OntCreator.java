package com.kde.ontologies;

import com.esri.core.geometry.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.*;

import java.io.*;
import java.util.*;

/**
 * Create ontologies
 *
 * @author  Baolei, Rajesh, Sourojit
 */
public class OntCreator {
    private final static String BASE_URI = "http://www.gov.ie/groupC/NIAH";
    private final static String NAMESPACE = BASE_URI +"#";

    public static void createOntology() throws IOException {
        OntModel ontModel = ModelFactory.createOntologyModel();
        ontModel.setNsPrefix("base", NAMESPACE);

        Ontology ontology = ontModel.createOntology(BASE_URI);
        ontology.addLabel("national-inventory-of-architectural-Heritage-county", null);
        String comment = "National Inventory of Architectural Heritage. ";
        ontology.addComment( comment, null);
        ontology.addProperty(DCTerms.creator, "Baolei, Rajesh, Sourojit");
        ontology.addProperty(DCTerms.date, "15/11/2018");
        ontology.addProperty(OWL2.versionInfo, "1.0.0");
        ontology.addProperty(DCTerms.description, comment);

        // GeogLocation Class

        OntClass GeogLocation = ontModel.createClass(NAMESPACE + "GeoLocation");
        GeogLocation.addLabel("GeoLocation", null);
        GeogLocation.addComment("Geographic location contains a latitude and a longitude", null);

        DatatypeProperty latitude = ontModel.createDatatypeProperty(NAMESPACE + "latitude");
        DatatypeProperty longitude = ontModel.createDatatypeProperty(NAMESPACE + "longitude");
        latitude.addLabel("latitude", null);
        latitude.addComment("Geographic latitude", null);
        latitude.setDomain(GeogLocation);
        latitude.setRange(XSD.xfloat);
        longitude.addLabel("longitude", null);
        longitude.addComment("Geographic longitude", null);
        longitude.setDomain(GeogLocation);
        longitude.setRange(XSD.xfloat);

        GeogLocation.addSuperClass(ontModel.createCardinalityRestriction(null, latitude, 1));
        GeogLocation.addSuperClass(ontModel.createCardinalityRestriction(null, longitude, 1));


        /****************** IrelandCounty Class *******************/

        OntClass IrelandCounty = ontModel.createClass(NAMESPACE + "IrelandCounty");
        IrelandCounty.addLabel("IrelandCounty", null);
        IrelandCounty.addComment("Ireland Counties, can be distinguished by their names", null);

        // RDFS: label for name

        DatatypeProperty area = ontModel.createDatatypeProperty(NAMESPACE + "area");
        area.addLabel("area", null);
        area.addComment("Define area of a county", null);
        area.setDomain(IrelandCounty);
        area.setRange(XSD.xfloat);

        SymmetricProperty adjacentTo = ontModel.createSymmetricProperty(NAMESPACE + "adjacentTo");
        adjacentTo.addLabel("adjacentTo", null);
        adjacentTo.addComment("List other counties that is geographically touch with current county", null);
        adjacentTo.setDomain(IrelandCounty);
        adjacentTo.setRange(IrelandCounty);

        TransitiveProperty biggerThan = ontModel.createTransitiveProperty(NAMESPACE + "biggerThan");
        biggerThan.addLabel("biggerThan", null);
        biggerThan.addComment("List other counties that current county's area is bigger than", null);
        biggerThan.setDomain(IrelandCounty);
        biggerThan.setRange(IrelandCounty);

        IrelandCounty.addSuperClass(ontModel.createCardinalityRestriction(null, area, 1));

        /*********** subclasses of Heritage **********/

        OntClass InternationalHeritage = ontModel.createClass(NAMESPACE + "InternationalHeritage");
        OntClass NationalHeritage = ontModel.createClass(NAMESPACE + "NationalHeritage");
        OntClass RegionalHeritage = ontModel.createClass(NAMESPACE + "RegionalHeritage");
        InternationalHeritage.addLabel("InternationalHeritage", null);
        InternationalHeritage.addComment("International Heritage", null);
        NationalHeritage.addLabel("NationalHeritage", null);
        NationalHeritage.addComment("NationalHeritage", null);
        RegionalHeritage.addLabel("RegionalHeritage", null);
        RegionalHeritage.addComment("RegionalHeritage", null);

        RDFList list = ontModel.createList(new RDFNode[]{InternationalHeritage, NationalHeritage, RegionalHeritage});
        InternationalHeritage.addDisjointWith(NationalHeritage);
        RegionalHeritage.addDisjointWith(InternationalHeritage);
        RegionalHeritage.addDisjointWith(NationalHeritage);

        /***********Building Type Class **********/
        OntClass School = ontModel.createClass(NAMESPACE + "School");
        OntClass Church = ontModel.createClass(NAMESPACE + "Church");
        OntClass Garden = ontModel.createClass(NAMESPACE + "Garden");
        OntClass House = ontModel.createClass(NAMESPACE + "House");
        OntClass Industry = ontModel.createClass(NAMESPACE + "Industry");
        OntClass Misc = ontModel.createClass(NAMESPACE + "Misc");
        OntClass Bridge = ontModel.createClass(NAMESPACE + "Bridge");
        OntClass Sports = ontModel.createClass(NAMESPACE + "Sports");
        OntClass Station = ontModel.createClass(NAMESPACE + "Station");
        OntClass Walls = ontModel.createClass(NAMESPACE + "Walls");
        School.addLabel("School", null);
        Church.addLabel("Church", null);
        Garden.addLabel("Garden", null);
        House.addLabel("House", null);
        Industry.addLabel("Industry", null);
        Misc.addLabel("Misc", null);
        Bridge.addLabel("Bridge", null);
        Sports.addLabel("Sports", null);
        Station.addLabel("Station", null);
        Walls.addLabel("Walls", null);
        School.addComment("School", null);
        Church.addComment("Church", null);
        Garden.addComment("Garden", null);
        House.addComment("House", null);
        Industry.addComment("Industry", null);
        Misc.addComment("Misc", null);
        Bridge.addComment("Bridge", null);
        Sports.addComment("Sports", null);
        Station.addComment("Station", null);
        Walls.addComment("Walls", null);


        RDFList buildingTypeList = ontModel.createList(new RDFNode[]{Bridge, Church, Garden, House, Industry, Misc, School, Sports, Station, Walls});
        OntClass BuildingType = ontModel.createEnumeratedClass(NAMESPACE+"BuildingType", buildingTypeList);
        BuildingType.addLabel("Building Type", null);
        BuildingType.addComment("Building Type", null);
        
        School.addSuperClass(BuildingType);
        Church.addSuperClass(BuildingType);
        Garden.addSuperClass(BuildingType);
        House.addSuperClass(BuildingType);
        Industry.addSuperClass(BuildingType);
        Misc.addSuperClass(BuildingType);
        Bridge.addSuperClass(BuildingType);
        Sports.addSuperClass(BuildingType);
        Station.addSuperClass(BuildingType);
        Walls.addSuperClass(BuildingType);
        
        
        

        /***************** Heritage Class *******************/

        OntClass Heritage = ontModel.createUnionClass(NAMESPACE + "Heritage", list);
        Heritage.addLabel("Heritage", null);
        Heritage.addComment("The National Inventory of Architectural Heritage (NIAH) is a state initiative " +
                "under the administration of the Department of Arts, Heritage, Regional, Rural and Gaeltacht Affairs " +
                "and established on a statutory basis under the provisions of the Architectural Heritage (National Inventory) " +
                "and Historic Monuments (Miscellaneous Provisions) Act 1999", null);

        // RDFS:label for name

        DatatypeProperty address = ontModel.createDatatypeProperty(NAMESPACE + "address");
        address.addLabel("address", null);
        address.addComment("Address of Heritage, described by a text", null);
        address.setDomain(Heritage);
        address.setRange(XSD.xstring);

        DatatypeProperty regNo = ontModel.createDatatypeProperty(NAMESPACE + "regNo");
        regNo.addLabel("regNo", null);
        regNo.addComment("Registration Number of a Heritage", null);
        regNo.setDomain(Heritage);
        regNo.setRange(XSD.xstring);

        DatatypeProperty heritageName = ontModel.createDatatypeProperty(NAMESPACE + "heritageName");
        heritageName.addLabel("heritageName", null);
        heritageName.addComment("Name of Heritage, described by a text", null);
        heritageName.setDomain(Heritage);
        heritageName.setRange(XSD.xstring);

        DatatypeProperty composition = ontModel.createDatatypeProperty(NAMESPACE + "composition");
        composition.addLabel("composition", null);
        composition.addComment("Composition of a Heritage", null);
        composition.setDomain(Heritage);
        composition.setRange(XSD.xstring);

        DatatypeProperty appraisal = ontModel.createDatatypeProperty(NAMESPACE + "appraisal");
        appraisal.addLabel("appraisal", null);
        appraisal.addComment("appraisal of a Heritage", null);
        appraisal.setDomain(Heritage);
        appraisal.setRange(XSD.xstring);

        DatatypeProperty dateFrom = ontModel.createDatatypeProperty(NAMESPACE + "dateFrom");
        dateFrom.addLabel("dateFrom", null);
        dateFrom.addComment("Start date of construction of a Heritage", null);
        dateFrom.setDomain(Heritage);
        dateFrom.setRange(XSD.xstring);

        DatatypeProperty dateTo = ontModel.createDatatypeProperty(NAMESPACE + "dateTo");
        dateTo.addLabel("dateTo", null);
        dateTo.addComment("End date of construction of a Heritage", null);
        dateTo.setDomain(Heritage);
        dateTo.setRange(XSD.xstring);

        DatatypeProperty rating = ontModel.createDatatypeProperty(NAMESPACE + "rating");
        rating.addLabel("rating", null);
        rating.addComment("Rating a Heritage", null);
        rating.setDomain(Heritage);
        rating.setRange(XSD.xstring);

        DatatypeProperty originalType = ontModel.createDatatypeProperty(NAMESPACE + "originalType");
        originalType.addLabel("originalType", null);
        originalType.addComment("Original type a Heritage", null);
        originalType.setDomain(Heritage);
        originalType.setRange(XSD.xstring);

        DatatypeProperty xCoord = ontModel.createDatatypeProperty(NAMESPACE + "xCoord");
        xCoord.addLabel("xCoord", null);
        xCoord.addComment("x coordinate of a Heritage", null);
        xCoord.setDomain(Heritage);
        xCoord.setRange(XSD.xstring);

        DatatypeProperty yCoord = ontModel.createDatatypeProperty(NAMESPACE + "yCoord");
        yCoord.addLabel("yCoord", null);
        yCoord.addComment("y coordinate of a Heritage", null);
        yCoord.setDomain(Heritage);
        yCoord.setRange(XSD.xstring);

        DatatypeProperty imageLink = ontModel.createDatatypeProperty(NAMESPACE + "imageLink");
        imageLink.addLabel("imageLink", null);
        imageLink.addComment("imageLink of a Heritage", null);
        imageLink.setDomain(Heritage);
        imageLink.setRange(XSD.xstring);

        ObjectProperty location = ontModel.createObjectProperty(NAMESPACE + "location");
        location.addLabel("location", null);
        location.addComment("Geographic location of a Heritage, described by a GeoLocation", null);
        location.setDomain(Heritage);
        location.setRange(GeogLocation);

        ObjectProperty inCounty = ontModel.createObjectProperty(NAMESPACE + "inCounty");
        inCounty.addLabel("inCounty", null);
        inCounty.addComment("Which ireland county the Heritage belongs to", null);
        inCounty.setDomain(Heritage);
        inCounty.setRange(IrelandCounty);


        DatatypeProperty withBuildingType = ontModel.createDatatypeProperty(NAMESPACE + "withBuildingType");
        withBuildingType.addLabel("withBuildingType", null);
        withBuildingType.addComment("Building type of Heritage monument", null);
        withBuildingType.setDomain(Heritage);
        withBuildingType.setRange(BuildingType);

        // Subclasses setup
        InternationalHeritage.addSuperClass(Heritage);
        NationalHeritage.addSuperClass(Heritage);
        RegionalHeritage.addSuperClass(Heritage);

        //Describing by building type

        OntClass BridgeHeritage = ontModel.createClass(NAMESPACE + "BridgeHeritage");
        BridgeHeritage.addLabel("Heritage Building of origin type Bridge",null);
        BridgeHeritage.addComment("Heritage building definition whose building type is Bridge",null);
        BridgeHeritage.addSuperClass(Heritage);
        BridgeHeritage.addSuperClass(ontModel.createHasValueRestriction(null,withBuildingType,Bridge));

        OntClass ChurchHeritage = ontModel.createClass(NAMESPACE + "ChurchHeritage");
        ChurchHeritage.addLabel("Heritage Building of origin type Church",null);
        ChurchHeritage.addComment("Heritage building definition whose building type is Church",null);
        ChurchHeritage.addSuperClass(Heritage);
        ChurchHeritage.addSuperClass(ontModel.createHasValueRestriction(null,withBuildingType,Church));

        OntClass GardenHeritage = ontModel.createClass(NAMESPACE + "GardenHeritage");
        GardenHeritage.addLabel("Heritage Building of origin type Garden",null);
        GardenHeritage.addComment("Heritage building definition whose building type is Garden",null);
        GardenHeritage.addSuperClass(Heritage);
        GardenHeritage.addSuperClass(ontModel.createHasValueRestriction(null,withBuildingType,Garden));

        OntClass HouseHeritage = ontModel.createClass(NAMESPACE + "HouseHeritage");
        HouseHeritage.addLabel("Heritage Building of origin type House",null);
        HouseHeritage.addComment("Heritage building definition whose building type is House",null);
        HouseHeritage.addSuperClass(Heritage);
        HouseHeritage.addSuperClass(ontModel.createHasValueRestriction(null,withBuildingType,House));

        OntClass IndustryHeritage = ontModel.createClass(NAMESPACE + "IndustryHeritage");
        IndustryHeritage.addLabel("Heritage Building of origin type Industry",null);
        IndustryHeritage.addComment("Heritage building definition whose building type is Industry",null);
        IndustryHeritage.addSuperClass(Heritage);
        IndustryHeritage.addSuperClass(ontModel.createHasValueRestriction(null,withBuildingType,Industry));

        OntClass MiscHeritage = ontModel.createClass(NAMESPACE + "MiscHeritage");
        MiscHeritage.addLabel("Heritage Building of origin type Misc",null);
        MiscHeritage.addComment("Heritage building definition whose building type is Misc",null);
        MiscHeritage.addSuperClass(Heritage);
        MiscHeritage.addSuperClass(ontModel.createHasValueRestriction(null,withBuildingType,Misc));

        OntClass SchoolHeritage = ontModel.createClass(NAMESPACE + "SchoolHeritage");
        SchoolHeritage.addLabel("Heritage Building of origin type School",null);
        SchoolHeritage.addComment("Heritage building definition whose building type is School",null);
        SchoolHeritage.addSuperClass(Heritage);
        SchoolHeritage.addSuperClass(ontModel.createHasValueRestriction(null,withBuildingType,School));

        OntClass SportsHeritage = ontModel.createClass(NAMESPACE + "SportsHeritage");
        SportsHeritage.addLabel("Heritage Building of origin type Sports",null);
        SportsHeritage.addComment("Heritage building definition whose building type is Sports",null);
        SportsHeritage.addSuperClass(Heritage);
        SportsHeritage.addSuperClass(ontModel.createHasValueRestriction(null,withBuildingType,Sports));

        OntClass StationHeritage = ontModel.createClass(NAMESPACE + "StationHeritage");
        StationHeritage.addLabel("Heritage Building of origin type Station",null);
        StationHeritage.addComment("Heritage building definition whose building type is Station",null);
        StationHeritage.addSuperClass(Heritage);
        StationHeritage.addSuperClass(ontModel.createHasValueRestriction(null,withBuildingType,Station));

        OntClass WallsHeritage = ontModel.createClass(NAMESPACE + "WallsHeritage");
        WallsHeritage.addLabel("Heritage Building of origin type Walls",null);
        WallsHeritage.addComment("Heritage building definition whose building type is Walls",null);
        WallsHeritage.addSuperClass(Heritage);
        WallsHeritage.addSuperClass(ontModel.createHasValueRestriction(null,withBuildingType,Walls));



        // County property

        ObjectProperty hasHeritages = ontModel.createObjectProperty(NAMESPACE + "hasHeritages");
        hasHeritages.addLabel("hasHeritages", null);
        hasHeritages.addComment("List heritages belongs to the county", null);
        hasHeritages.setDomain(IrelandCounty);
        hasHeritages.setRange(Heritage);
        hasHeritages.addInverseOf(inCounty);

        /************** Heritage Cardinality Restrictions **************/
        Heritage.addSuperClass(ontModel.createCardinalityRestriction(null, address, 1));
        Heritage.addSuperClass(ontModel.createCardinalityRestriction(null, regNo, 1));
        Heritage.addSuperClass(ontModel.createCardinalityRestriction(null, heritageName, 1));
        Heritage.addSuperClass(ontModel.createCardinalityRestriction(null, composition, 1));
        Heritage.addSuperClass(ontModel.createCardinalityRestriction(null, appraisal, 1));
        Heritage.addSuperClass(ontModel.createCardinalityRestriction(null, dateFrom, 1));
        Heritage.addSuperClass(ontModel.createCardinalityRestriction(null, dateTo, 1));
        Heritage.addSuperClass(ontModel.createCardinalityRestriction(null, rating, 1));
        Heritage.addSuperClass(ontModel.createCardinalityRestriction(null, originalType, 1));
        Heritage.addSuperClass(ontModel.createCardinalityRestriction(null, xCoord, 1));
        Heritage.addSuperClass(ontModel.createCardinalityRestriction(null, yCoord, 1));
        Heritage.addSuperClass(ontModel.createCardinalityRestriction(null, imageLink, 1));
        Heritage.addSuperClass(ontModel.createCardinalityRestriction(null, location, 1));
        Heritage.addSuperClass(ontModel.createCardinalityRestriction(null, inCounty, 1));
        Heritage.addSuperClass(ontModel.createCardinalityRestriction(null, withBuildingType, 1));




        writeToFile(ontModel);


        /* ---------------------   Split Line  ---------------------------- */
        /********************** create individuals *************************/

        Model countyRDF = RDFDataMgr.loadModel(FileUtils.COUNTY_PATH);

        ArrayList<ArrayList<Object>> countyInfoList = new ArrayList<>();
        ResIterator countyResIter = countyRDF.listResourcesWithProperty(RDFS.label);

        Property hasGeometry = countyRDF.getProperty("http://www.opengis.net/ont/geosparql#hasGeometry");
        Property asWKT = countyRDF.getProperty("http://www.opengis.net/ont/geosparql#asWKT");

        while (countyResIter.hasNext()) {
            Resource res = countyResIter.next();

            // labels
            NodeIterator labelsIter = countyRDF.listObjectsOfProperty(res, RDFS.label);
            List<RDFNode> labels = labelsIter.toList();
            String idLabel = "";
            String gaLabel = "";
            String enLabel = "";

            for (RDFNode label : labels) {
                Literal name = label.asLiteral();
                if (name.getLanguage().equals("ga")) {
                    gaLabel = name.getString();
                } else if (name.getLanguage().equals("en")) {
                    enLabel = name.getString();
                } else {
                    idLabel = name.getString();
                }
            }

            // WKT
            Resource geoResource = countyRDF.listObjectsOfProperty(res, hasGeometry).next().asResource();
            String wkt = countyRDF.listObjectsOfProperty(geoResource, asWKT).next().toString();
            wkt = wkt.substring(0, wkt.indexOf("^^"));
            OperatorImportFromWkt importer = OperatorImportFromWkt.local();
            Geometry geometry = importer.execute(WktImportFlags.wktImportDefaults, Geometry.Type.Unknown, wkt, null);

            ArrayList<Object> info = new ArrayList<>();
            info.add(idLabel);
            info.add(enLabel);
            info.add(gaLabel);
            info.add(geometry);
            // TODO: AREA unit correctness, add approximate scale for now. based on dublin
            float scale = 7365.0f;
            info.add((float)geometry.calculateArea2D() * scale);

            countyInfoList.add(info);
        }

        countyInfoList.sort(new Comparator<ArrayList<Object>>() {
            @Override
            public int compare(ArrayList<Object> o1, ArrayList<Object> o2) {
                float area1 = (float)o1.get(4);
                float area2 = (float)o2.get(4);
                return Float.compare(area2, area1);
            }
        });

        ArrayList<Individual> countyIndiList = new ArrayList<>();
        for (ArrayList<Object> info : countyInfoList) {
            Individual aCounty = IrelandCounty.createIndividual(NAMESPACE + info.get(0).toString().replace(" ", ""));
            aCounty.addLabel((String)info.get(0), null);
            aCounty.addLabel((String)info.get(1), "en");
            aCounty.addLabel((String)info.get(2), "ga");
            aCounty.addLiteral(area, (float)info.get(4));

            countyIndiList.add(aCounty);
        }

        for (Individual countyIndi : countyIndiList) {
            int curIdx = countyIndiList.indexOf(countyIndi);
            Geometry curGeometry = (Geometry)countyInfoList.get(curIdx).get(3);

            for (int i = curIdx + 1; i < countyIndiList.size(); i++) {
                if (i == curIdx) {
                    continue;
                }
                Individual otherCountyIndi =  countyIndiList.get(i);
                if (i > curIdx) {
                    countyIndi.addProperty(biggerThan, otherCountyIndi);
                }
                Geometry otherGeometry = (Geometry)countyInfoList.get(i).get(3);
                // TODO: Intersection not all correct, for instance, dublin
                OperatorIntersects intersects = OperatorIntersects.local();
                if (intersects.execute(curGeometry, otherGeometry, SpatialReference.create("WGS84"), null)) {
                    countyIndi.addProperty(adjacentTo, otherCountyIndi);
                }
            }
        }

        try {
            FileReader in = new FileReader(FileUtils.HERITAGE_CSV_PATH);
            CSVParser schoolCSV = CSVFormat.DEFAULT.parse(in);
            List<CSVRecord> records = schoolCSV.getRecords();
            records.remove(0);

            for (CSVRecord record : records) {
                String aRegNo = record.get(0);
                String aHeritageName = record.get(1);
                String aAddress = record.get(3) + " " + record.get(4) + " " + record.get(5);
                String aComposition = record.get(10);
                String aAppraisal = record.get(11);
                String aDateFrom = record.get(12);
                String aDateTo = record.get(13);
                String aXCoord = record.get(16);
                String aYCoord = record.get(17);
                String aImageLink = record.get(22);
                String aoriginalType = record.get(15);
                String aRating = record.get(14);
                //String aLocation = record.get();
                if(record.get(20) == null || record.get(21) == null || record.get(20).trim().equals("") || record.get(21).trim().equals("")) {
                    //System.out.println("Get a null value, record Id:" + aRegNo);
                    continue;
                }
                //System.out.println("aLatitude:" + record.get(20));
                float aLatitude = Float.parseFloat(record.get(20).trim());
                float aLongitude = Float.parseFloat(record.get(21).trim());

                Individual aLocation = GeogLocation.createIndividual();
                aLocation.addLiteral(latitude, aLatitude);
                aLocation.addLiteral(longitude, aLongitude);

                //Individual aCounty = IrelandCounty.createIndividual(NAMESPACE + "CAVAN");
                //<TODO: Need to populate rest of county data>

                FileInputStream fs = new FileInputStream("temp/BuildingType.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(fs));
                String line = "";
                Map<String, List<String>> map=new HashMap<>();
                ArrayList<String> typeList=new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    String mine = line;
                    String type="";
                    if(mine.startsWith("</") ){
                        map.put(mine.substring(mine.indexOf("/")+1, mine.indexOf(">")), typeList);
                        typeList=new ArrayList<>();
                    }else if (mine.startsWith("<")) {
                       // System.out.println(mine);
                    }else {
                        typeList.add(mine);
                    }
                }
                String key = null;
                if (map != null) {

                    for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                        if (entry.getValue().contains(aoriginalType)) {
                            key = entry.getKey();
                        }
                    }
                }
               // Class.forName(key).getClass();
                Individual aBuildingType;
                if(key == null) continue;
                if (key.equals("church")) {
                    aBuildingType = Church.createIndividual();
                } else if (key.equals("bridge")) {
                    aBuildingType = Bridge.createIndividual();
                } else if (key.equals("garden")) {
                    aBuildingType = Garden.createIndividual();
                } else if (key.equals("house")) {
                    aBuildingType = House.createIndividual();
                } else if (key.equals("industry")) {
                    aBuildingType = Industry.createIndividual();
                } else if (key.equals("misc")) {
                    aBuildingType = Misc.createIndividual();
                } else if (key.equals("school")) {
                    aBuildingType = School.createIndividual();
                } else if (key.equals("sports")) {
                    aBuildingType = Sports.createIndividual();
                }else if (key.equals("station")) {
                    aBuildingType = Station.createIndividual();
                }else if (key.equals("walls")) {
                    aBuildingType = Walls.createIndividual();
                } else {
                    continue;
                }

                // Create Heritage classes

                Individual aHeritage;
                if (aRating.equals("Regional")) {
                    aHeritage = RegionalHeritage.createIndividual(NAMESPACE + aRegNo);
                } else if (aRating.equals("National")) {
                    aHeritage = NationalHeritage.createIndividual(NAMESPACE + aRegNo);
                } else {
                    aHeritage = InternationalHeritage.createIndividual(NAMESPACE + aRegNo);
                }
                aHeritage.addOntClass(Heritage);
                aHeritage.addLiteral(heritageName, aHeritageName);
                aHeritage.addLiteral(RDFS.label, aRegNo);
                aHeritage.addLiteral(regNo, aRegNo);
                aHeritage.addLiteral(address, aAddress);
                aHeritage.addLiteral(composition, aComposition);
                aHeritage.addLiteral(appraisal, aAppraisal);
                aHeritage.addLiteral(dateFrom, aDateFrom);
                aHeritage.addLiteral(dateTo, aDateTo);
                aHeritage.addLiteral(originalType, aoriginalType);
                aHeritage.addLiteral(rating, aRating);
                aHeritage.addLiteral(xCoord, aXCoord);
                aHeritage.addLiteral(yCoord, aYCoord);
                aHeritage.addLiteral(imageLink, aImageLink);
                aHeritage.addProperty(withBuildingType, aBuildingType);


                for (int i = 0; i < countyInfoList.size(); i++) {
                    ArrayList<Object> countyInfo = countyInfoList.get(i);
                    Geometry geometry = (Geometry)countyInfo.get(3);
                    Point point = new Point(aLongitude, aLatitude);

                    OperatorWithin within = OperatorWithin.local();
                    if (within.execute(point, geometry, SpatialReference.create("WGS84"), null)) {
                        aHeritage.addProperty(inCounty, countyIndiList.get(i));
                        countyIndiList.get(i).addProperty(hasHeritages, Heritage);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        writeToFile(ontModel);
    }

    public static void writeToFile(OntModel ontModel) {
        try {
            ontModel.write(new FileWriter(FileUtils.ONTOLOGY_PATH), "TURTLE");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
