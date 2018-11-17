package kde.tcd;

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
        ontology.addLabel("national-inventory-of-architectural-heritage-county", null);
        String comment = "National Inventory of Architectural Heritage. ";
        ontology.addComment( comment, null);
        ontology.addProperty(DCTerms.creator, "Rajesh");
        ontology.addProperty(DCTerms.date, "15/11/2018");
        ontology.addProperty(OWL2.versionInfo, "1.0.0");
        // Resource image = ontModel.createResource("https://drive.google.com/open?id=15B6U7PdfvPCvw7Kj8xIzpj4xdBl2-BL6");
        //ontology.addProperty(DCTerms.description, image);
        ontology.addProperty(DCTerms.description, comment);

        // geogLocation Class

        OntClass geogLocation = ontModel.createClass(NAMESPACE + "GeoLocation");
        geogLocation.addLabel("GeoLocation", null);
        geogLocation.addComment("Geographic location contains a latitude and a longitude", null);

        DatatypeProperty latitude = ontModel.createDatatypeProperty(NAMESPACE + "latitude");
        DatatypeProperty longitude = ontModel.createDatatypeProperty(NAMESPACE + "longitude");
        latitude.addLabel("latitude", null);
        latitude.addComment("Geographic latitude", null);
        latitude.setDomain(geogLocation);
        latitude.setRange(XSD.xfloat);
        latitude.addLabel("longitude", null);
        latitude.addComment("Geographic longitude", null);
        longitude.setDomain(geogLocation);
        longitude.setRange(XSD.xfloat);

        geogLocation.addSuperClass(ontModel.createCardinalityRestriction(null, latitude, 1));
        geogLocation.addSuperClass(ontModel.createCardinalityRestriction(null, longitude, 1));


        /****************** irelandCounty Class *******************/

        OntClass irelandCounty = ontModel.createClass(NAMESPACE + "irelandCounty");
        irelandCounty.addLabel("irelandCounty", null);
        irelandCounty.addComment("Ireland Counties, can be distinguished by their names", null);

        // RDFS: label for name

        DatatypeProperty area = ontModel.createDatatypeProperty(NAMESPACE + "area");
        area.addLabel("area", null);
        area.addComment("Define area of a county", null);
        area.setDomain(irelandCounty);
        area.setRange(XSD.xfloat);

        SymmetricProperty adjacentTo = ontModel.createSymmetricProperty(NAMESPACE + "adjacentTo");
        adjacentTo.addLabel("adjacentTo", null);
        adjacentTo.addComment("List other counties that is geographically touch with current county", null);
        adjacentTo.setDomain(irelandCounty);
        adjacentTo.setRange(irelandCounty);

        TransitiveProperty biggerThan = ontModel.createTransitiveProperty(NAMESPACE + "biggerThan");
        biggerThan.addLabel("biggerThan", null);
        biggerThan.addComment("List other counties that current county's area is bigger than", null);
        biggerThan.setDomain(irelandCounty);
        biggerThan.setRange(irelandCounty);

        irelandCounty.addSuperClass(ontModel.createCardinalityRestriction(null, area, 1));

        /*********** subclasses of heritage **********/

        OntClass internationalHeritage = ontModel.createClass(NAMESPACE + "InternationalHeritege");
        OntClass nationalHeritage = ontModel.createClass(NAMESPACE + "NationalHeritage");
        OntClass regionalHeritage = ontModel.createClass(NAMESPACE + "RegionalHeritage");
        internationalHeritage.addLabel("InternationalHeritege", null);
        internationalHeritage.addComment("International Heritage", null);
        nationalHeritage.addLabel("NationalHeritage", null);
        nationalHeritage.addComment("NationalHeritage", null);
        regionalHeritage.addLabel("RegionalHeritage", null);
        regionalHeritage.addComment("regionalHeritage", null);

        RDFList list = ontModel.createList(new RDFNode[]{internationalHeritage, nationalHeritage, regionalHeritage});
        internationalHeritage.addDisjointWith(nationalHeritage);
        regionalHeritage.addDisjointWith(internationalHeritage);
        regionalHeritage.addDisjointWith(nationalHeritage);

        /***********Building Type Class **********/
        OntClass school = ontModel.createClass(NAMESPACE + "bridge");
        OntClass church = ontModel.createClass(NAMESPACE + "church");
        OntClass garden = ontModel.createClass(NAMESPACE + "garden");
        OntClass house = ontModel.createClass(NAMESPACE + "house");
        OntClass industry = ontModel.createClass(NAMESPACE + "industry");
        OntClass misc = ontModel.createClass(NAMESPACE + "misc");
        OntClass bridge = ontModel.createClass(NAMESPACE + "bridge");
        OntClass sports = ontModel.createClass(NAMESPACE + "sports");
        OntClass station = ontModel.createClass(NAMESPACE + "station");
        OntClass walls = ontModel.createClass(NAMESPACE + "walls");
        school.addLabel("school", null);
        church.addLabel("church", null);
        garden.addLabel("garden", null);
        house.addLabel("house", null);
        industry.addLabel("industry", null);
        misc.addLabel("misc", null);
        bridge.addLabel("bridge", null);
        sports.addLabel("sports", null);
        station.addLabel("station", null);
        walls.addLabel("walls", null);
        school.addComment("school", null);
        church.addComment("church", null);
        garden.addComment("garden", null);
        house.addComment("house", null);
        industry.addComment("industry", null);
        misc.addComment("misc", null);
        bridge.addComment("bridge", null);
        sports.addComment("sports", null);
        station.addComment("station", null);
        walls.addComment("walls", null);


        RDFList buildingTypeList = ontModel.createList(new RDFNode[]{bridge, church, garden, house, industry, misc, school, sports, station, walls});
        OntClass buildingType = ontModel.createEnumeratedClass(NAMESPACE+"BuildingType", buildingTypeList);
        buildingType.addLabel("Building Type", null);
        buildingType.addComment("Building Type", null);
        
        school.addSuperClass(buildingType);
        church.addSuperClass(buildingType);
        garden.addSuperClass(buildingType);
        house.addSuperClass(buildingType);
        industry.addSuperClass(buildingType);
        misc.addSuperClass(buildingType);
        bridge.addSuperClass(buildingType);
        sports.addSuperClass(buildingType);
        station.addSuperClass(buildingType);
        walls.addSuperClass(buildingType);
        
        
        

        /***************** Heritage Class *******************/

        OntClass heritage = ontModel.createUnionClass(NAMESPACE + "Heritage", list);
        heritage.addLabel("Heritage", null);
        heritage.addComment("The National Inventory of Architectural Heritage (NIAH) is a state initiative " +
                "under the administration of the Department of Arts, Heritage, Regional, Rural and Gaeltacht Affairs " +
                "and established on a statutory basis under the provisions of the Architectural Heritage (National Inventory) " +
                "and Historic Monuments (Miscellaneous Provisions) Act 1999", null);

        // RDFS:label for name

        DatatypeProperty address = ontModel.createDatatypeProperty(NAMESPACE + "address");
        address.addLabel("address", null);
        address.addComment("Address of Heritage, described by a text", null);
        address.setDomain(heritage);
        address.setRange(XSD.xstring);

        DatatypeProperty regNo = ontModel.createDatatypeProperty(NAMESPACE + "regNo");
        regNo.addLabel("regNo", null);
        regNo.addComment("Registration Number of a heritage", null);
        regNo.setDomain(heritage);
        regNo.setRange(XSD.xstring);

        /*DatatypeProperty name = ontModel.createDatatypeProperty(NAMESPACE + "name");
        name.addLabel("name", null);
        name.addComment("Address of Heritage, described by a text", null);
        name.setDomain(heritage);
        name.setRange(XSD.xstring);*/

        DatatypeProperty composition = ontModel.createDatatypeProperty(NAMESPACE + "composition");
        composition.addLabel("composition", null);
        composition.addComment("Composition of a heritage", null);
        composition.setDomain(heritage);
        composition.setRange(XSD.xstring);

        DatatypeProperty appraisal = ontModel.createDatatypeProperty(NAMESPACE + "appraisal");
        appraisal.addLabel("appraisal", null);
        appraisal.addComment("appraisal of a heritage", null);
        appraisal.setDomain(heritage);
        appraisal.setRange(XSD.xstring);

        DatatypeProperty dateFrom = ontModel.createDatatypeProperty(NAMESPACE + "dateFrom");
        dateFrom.addLabel("dateFrom", null);
        dateFrom.addComment("Start date of construction of a heritage", null);
        dateFrom.setDomain(heritage);
        dateFrom.setRange(XSD.xstring);

        DatatypeProperty dateTo = ontModel.createDatatypeProperty(NAMESPACE + "dateTo");
        dateTo.addLabel("dateTo", null);
        dateTo.addComment("End date of construction of a heritage", null);
        dateTo.setDomain(heritage);
        dateTo.setRange(XSD.xstring);

        /*DatatypeProperty rating = ontModel.createDatatypeProperty(NAMESPACE + "rating");
        rating.addLabel("rating", null);
        rating.addComment("Rating a heritage", null);
        rating.setDomain(heritage);
        rating.setRange(XSD.xstring);*/

        /*DatatypeProperty originalType = ontModel.createDatatypeProperty(NAMESPACE + "originalType");
        originalType.addLabel("originalType", null);
        originalType.addComment("Rriginal type a heritage", null);
        originalType.setDomain(heritage);
        originalType.setRange(XSD.xstring);*/

        DatatypeProperty xCoord = ontModel.createDatatypeProperty(NAMESPACE + "xCoord");
        xCoord.addLabel("xCoord", null);
        xCoord.addComment("x coordinate of a heritage", null);
        xCoord.setDomain(heritage);
        xCoord.setRange(XSD.xstring);

        DatatypeProperty yCoord = ontModel.createDatatypeProperty(NAMESPACE + "yCoord");
        yCoord.addLabel("yCoord", null);
        yCoord.addComment("y coordinate of a heritage", null);
        yCoord.setDomain(heritage);
        yCoord.setRange(XSD.xstring);

        DatatypeProperty imageLink = ontModel.createDatatypeProperty(NAMESPACE + "imageLink");
        imageLink.addLabel("imageLink", null);
        imageLink.addComment("imageLink of a heritage", null);
        imageLink.setDomain(heritage);
        imageLink.setRange(XSD.xstring);

        ObjectProperty location = ontModel.createObjectProperty(NAMESPACE + "location");
        location.addLabel("location", null);
        location.addComment("Geographic location of a heritage, described by a GeoLocation", null);
        location.setDomain(heritage);
        location.setRange(geogLocation);

        ObjectProperty inCounty = ontModel.createObjectProperty(NAMESPACE + "inCounty");
        inCounty.addLabel("inCounty", null);
        inCounty.addComment("Which ireland county the heritage belongs to", null);
        inCounty.setDomain(heritage);
        inCounty.setRange(irelandCounty);


        DatatypeProperty withBuildingType = ontModel.createDatatypeProperty(NAMESPACE + "withBuildingType");
        withBuildingType.addLabel("withBuildingType", null);
        withBuildingType.addComment("Building type of heritage monument", null);
        withBuildingType.setDomain(heritage);
        withBuildingType.setRange(buildingType);

        // Subclasses setup
        internationalHeritage.addSuperClass(heritage);
        nationalHeritage.addSuperClass(heritage);
        regionalHeritage.addSuperClass(heritage);

        //Describing by building type

        OntClass bridgeHeritage = ontModel.createClass(NAMESPACE + "HeritageBuildingoforigintypeBridge");
        bridgeHeritage.addLabel("Heritage Building of origin type Bridge",null);
        bridgeHeritage.addComment("Heritage building definition whose building type is Bridge",null);
        bridgeHeritage.addSuperClass(heritage);
        bridgeHeritage.addSuperClass(ontModel.createHasValueRestriction(null,withBuildingType,bridge));

        OntClass churchHeritage = ontModel.createClass(NAMESPACE + "HeritageBuildingoforigintypeChurch");
        churchHeritage.addLabel("Heritage Building of origin type Church",null);
        churchHeritage.addComment("Heritage building definition whose building type is Church",null);
        churchHeritage.addSuperClass(heritage);
        churchHeritage.addSuperClass(ontModel.createHasValueRestriction(null,withBuildingType,church));

        OntClass gardenHeritage = ontModel.createClass(NAMESPACE + "HeritageBuildingoforiginTypeGarden");
        gardenHeritage.addLabel("Heritage Building of origin type Garden",null);
        gardenHeritage.addComment("Heritage building definition whose building type is Garden",null);
        gardenHeritage.addSuperClass(heritage);
        gardenHeritage.addSuperClass(ontModel.createHasValueRestriction(null,withBuildingType,garden));

        OntClass houseHeritage = ontModel.createClass(NAMESPACE + "HeritageBuildingoforigintypeGarden");
        houseHeritage.addLabel("Heritage Building of origin type Garden",null);
        houseHeritage.addComment("Heritage building definition whose building type is Garden",null);
        houseHeritage.addSuperClass(heritage);
        houseHeritage.addSuperClass(ontModel.createHasValueRestriction(null,withBuildingType,house));

        OntClass industryHeritage = ontModel.createClass(NAMESPACE + "HeritageBuildingoforigintypGarden");
        industryHeritage.addLabel("Heritage Building of origin type Garden",null);
        industryHeritage.addComment("Heritage building definition whose building type is Garden",null);
        industryHeritage.addSuperClass(heritage);
        industryHeritage.addSuperClass(ontModel.createHasValueRestriction(null,withBuildingType,industry));

        OntClass miscHeritage = ontModel.createClass(NAMESPACE + "HeritageBuildingoforigintypeGarden");
        miscHeritage.addLabel("Heritage Building of origin type Garden",null);
        miscHeritage.addComment("Heritage building definition whose building type is Garden",null);
        miscHeritage.addSuperClass(heritage);
        miscHeritage.addSuperClass(ontModel.createHasValueRestriction(null,withBuildingType,misc));

        OntClass schoolHeritage = ontModel.createClass(NAMESPACE + "HeritageBuildingoforigintypeGarden");
        schoolHeritage.addLabel("Heritage Building of origin type Garden",null);
        schoolHeritage.addComment("Heritage building definition whose building type is Garden",null);
        schoolHeritage.addSuperClass(heritage);
        schoolHeritage.addSuperClass(ontModel.createHasValueRestriction(null,withBuildingType,school));

        OntClass sportsHeritage = ontModel.createClass(NAMESPACE + "HeritageBuildingoforiginTypeGarden");
        sportsHeritage.addLabel("Heritage Building of origin type Garden",null);
        sportsHeritage.addComment("Heritage building definition whose building type is Garden",null);
        sportsHeritage.addSuperClass(heritage);
        sportsHeritage.addSuperClass(ontModel.createHasValueRestriction(null,withBuildingType,sports));

        OntClass stationHeritage = ontModel.createClass(NAMESPACE + "HeritageBuildingoforigintypeGarden");
        stationHeritage.addLabel("Heritage Building of origin type Garden",null);
        stationHeritage.addComment("Heritage building definition whose building type is Garden",null);
        stationHeritage.addSuperClass(heritage);
        stationHeritage.addSuperClass(ontModel.createHasValueRestriction(null,withBuildingType,station));

        OntClass wallsHeritage = ontModel.createClass(NAMESPACE + "HeritageBuildingoforigintypeGarden");
        wallsHeritage.addLabel("Heritage Building of origin type Garden",null);
        wallsHeritage.addComment("Heritage building definition whose building type is Garden",null);
        wallsHeritage.addSuperClass(heritage);
        wallsHeritage.addSuperClass(ontModel.createHasValueRestriction(null,withBuildingType,walls));

        /*OntClass catholicSchool = ontModel.createClass(NAMESPACE + "CatholicSchool");
        catholicSchool.addLabel("CatholicSchool", null);
        catholicSchool.addComment("School class definition which ethos is Catholic", null);
        catholicSchool.addSuperClass(school);
        catholicSchool.addSuperClass(ontModel.createHasValueRestriction(null, withBuildingType, catholic));

        Literal zero = ontModel.createTypedLiteral(0);
        internationalHeritege.addSuperClass(ontModel.createHasValueRestriction(null, internationalCount, zero));
        nationalHeritege.addSuperClass(ontModel.createHasValueRestriction(null, nationalCount, zero));*/

        // County property

        ObjectProperty hasHeritages = ontModel.createObjectProperty(NAMESPACE + "hasHeritages");
        hasHeritages.addLabel("hasHeritages", null);
        hasHeritages.addComment("List heritages belongs to the county", null);
        hasHeritages.setDomain(irelandCounty);
        hasHeritages.setRange(heritage);
        hasHeritages.addInverseOf(inCounty);

        /************** Heritage Cardinality Restrictions **************/
        heritage.addSuperClass(ontModel.createCardinalityRestriction(null, address, 1));
        heritage.addSuperClass(ontModel.createCardinalityRestriction(null, regNo, 1));
        //heritage.addSuperClass(ontModel.createCardinalityRestriction(null, name, 1));
        heritage.addSuperClass(ontModel.createCardinalityRestriction(null, composition, 1));
        heritage.addSuperClass(ontModel.createCardinalityRestriction(null, appraisal, 1));
        heritage.addSuperClass(ontModel.createCardinalityRestriction(null, dateFrom, 1));
        heritage.addSuperClass(ontModel.createCardinalityRestriction(null, dateTo, 1));
        heritage.addSuperClass(ontModel.createCardinalityRestriction(null, xCoord, 1));
        heritage.addSuperClass(ontModel.createCardinalityRestriction(null, yCoord, 1));
        heritage.addSuperClass(ontModel.createCardinalityRestriction(null, imageLink, 1));
        heritage.addSuperClass(ontModel.createCardinalityRestriction(null, location, 1));
        heritage.addSuperClass(ontModel.createCardinalityRestriction(null, inCounty, 1));
        heritage.addSuperClass(ontModel.createCardinalityRestriction(null, withBuildingType, 1));



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
            Individual aCounty = irelandCounty.createIndividual(NAMESPACE + info.get(0).toString().replace(" ", ""));
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
                String aAddress = record.get(3) + " " + record.get(4) + " " + record.get(5);
                String aComposition = record.get(10);
                String aAppraisal = record.get(11);
                String aDateFrom = record.get(12);
                String aDateTo = record.get(13);
                String aXCoord = record.get(16);
                String aYCoord = record.get(17);
                String aImageLink = record.get(22);
                String aRating = record.get(15);
                //String aLocation = record.get();
                if(record.get(20) == null || record.get(21) == null || record.get(20).trim().equals("") || record.get(21).trim().equals("")) {
                    //System.out.println("Get a null value, record Id:" + aRegNo);
                    continue;
                }
                //System.out.println("aLatitude:" + record.get(20));
                double aLatitude = Float.parseFloat(record.get(20).trim());
                double aLongitude = Float.parseFloat(record.get(21).trim());

                Individual aLocation = geogLocation.createIndividual();
                aLocation.addLiteral(latitude, aLatitude);
                aLocation.addLiteral(longitude, aLongitude);

                Individual aCounty = irelandCounty.createIndividual(NAMESPACE + "CAVAN");
                //<TODO: Need to populate rest of county data>

                FileInputStream fs = new FileInputStream("temp/buildingType.txt");
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
                        if (entry.getValue().contains(aRating)) {
                            key = entry.getKey();
                        }
                    }
                }
               // Class.forName(key).getClass();
                Individual aBuildingType;
                if(key == null) continue;
                if (key.equals("church")) {
                    aBuildingType = church.createIndividual();
                } else if (key.equals("bridge")) {
                    aBuildingType = bridge.createIndividual();
                } else if (key.equals("garden")) {
                    aBuildingType = garden.createIndividual();
                } else if (key.equals("house")) {
                    aBuildingType = house.createIndividual();
                } else if (key.equals("industry")) {
                    aBuildingType = industry.createIndividual();
                } else if (key.equals("misc")) {
                    aBuildingType = misc.createIndividual();
                } else if (key.equals("school")) {
                    aBuildingType = school.createIndividual();
                } else if (key.equals("sports")) {
                    aBuildingType = sports.createIndividual();
                }else if (key.equals("station")) {
                    aBuildingType = station.createIndividual();
                }else if (key.equals("walls")) {
                    aBuildingType = walls.createIndividual();
                } else {
                    continue;
                }

                // Create heritage classes

                Individual aHeritage;
                if (aRating.equals("Regional")) {
                    aHeritage = regionalHeritage.createIndividual(NAMESPACE + aRegNo);
                } else if (aRating.equals("National")) {
                    aHeritage = nationalHeritage.createIndividual(NAMESPACE + aRegNo);
                } else {
                    aHeritage = internationalHeritage.createIndividual(NAMESPACE + aRegNo);
                }
                aHeritage.addOntClass(heritage);
                //change this instance
                if (key.equals("church")) {
                    aHeritage.addOntClass(churchHeritage);
                }

                aHeritage.addOntClass(heritage);
                aHeritage.addLiteral(RDFS.label, aRegNo);
                aHeritage.addLiteral(regNo, aRegNo);
                aHeritage.addLiteral(address, aAddress);
                aHeritage.addLiteral(composition, aComposition);
                aHeritage.addLiteral(appraisal, aAppraisal);
                aHeritage.addLiteral(dateFrom, aDateFrom);
                aHeritage.addLiteral(dateTo, aDateTo);
                aHeritage.addLiteral(xCoord, aXCoord);
                aHeritage.addLiteral(yCoord, aYCoord);
                aHeritage.addLiteral(imageLink, aImageLink);


                for (int i = 0; i < countyInfoList.size(); i++) {
                    ArrayList<Object> countyInfo = countyInfoList.get(i);
                    Geometry geometry = (Geometry)countyInfo.get(3);
                    Point point = new Point(aLongitude, aLatitude);

                    OperatorWithin within = OperatorWithin.local();
                    if (within.execute(point, geometry, SpatialReference.create("WGS84"), null)) {
                        aHeritage.addProperty(inCounty, countyIndiList.get(i));
                        countyIndiList.get(i).addProperty(hasHeritages, heritage);
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
