package kde.tcd;

import com.esri.core.geometry.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Create ontologies
 *
 * @author  Baolei, Rajesh
 */
public class OntCreator {
    private final static String BASE_URI = "http://www.cs7is1.com/nationalinventoryofarchitecturalheritagecounty";
    private final static String NAMESPACE = BASE_URI +"#";

    public static void createOntology() {
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

        // GeoLocation Class

        OntClass geoLocation = ontModel.createClass(NAMESPACE + "GeoLocation");
        geoLocation.addLabel("GeoLocation", null);
        geoLocation.addComment("Geographic location contains a latitude and a longitude", null);

        DatatypeProperty latitude = ontModel.createDatatypeProperty(NAMESPACE + "latitude");
        DatatypeProperty longitude = ontModel.createDatatypeProperty(NAMESPACE + "longitude");
        latitude.addLabel("latitude", null);
        latitude.addComment("Geographic latitude", null);
        latitude.setDomain(geoLocation);
        latitude.setRange(XSD.xfloat);
        latitude.addLabel("longitude", null);
        latitude.addComment("Geographic longitude", null);
        longitude.setDomain(geoLocation);
        longitude.setRange(XSD.xfloat);

        geoLocation.addSuperClass(ontModel.createCardinalityRestriction(null, latitude, 1));
        geoLocation.addSuperClass(ontModel.createCardinalityRestriction(null, longitude, 1));


        /****************** County Class *******************/

        OntClass county = ontModel.createClass(NAMESPACE + "County");
        county.addLabel("County", null);
        county.addComment("Ireland Counties, can be distinguished by their names", null);

        // RDFS: label for name

        DatatypeProperty area = ontModel.createDatatypeProperty(NAMESPACE + "area");
        area.addLabel("area", null);
        area.addComment("Define area of a county", null);
        area.setDomain(county);
        area.setRange(XSD.xfloat);

        SymmetricProperty adjacentTo = ontModel.createSymmetricProperty(NAMESPACE + "adjacentTo");
        adjacentTo.addLabel("adjacentTo", null);
        adjacentTo.addComment("List other counties that is geographically touch with current county", null);
        adjacentTo.setDomain(county);
        adjacentTo.setRange(county);

        TransitiveProperty biggerThan = ontModel.createTransitiveProperty(NAMESPACE + "biggerThan");
        biggerThan.addLabel("biggerThan", null);
        biggerThan.addComment("List other counties that current county's area is bigger than", null);
        biggerThan.setDomain(county);
        biggerThan.setRange(county);

        county.addSuperClass(ontModel.createCardinalityRestriction(null, area, 1));

        /****************** Ethos Class *********************/

        OntClass catholic = ontModel.createClass(NAMESPACE + "Regional");
        OntClass churchOfIreland = ontModel.createClass(NAMESPACE + "International");
        OntClass multiDenominational = ontModel.createClass(NAMESPACE + "National");
        catholic.addLabel("Regional", null);
        catholic.addComment("One of the property in rating", null);
        churchOfIreland.addLabel("International", null);
        churchOfIreland.addComment("One of the property in rating", null);
        multiDenominational.addLabel("National", null);
        multiDenominational.addComment("One of the property in rating", null);

        RDFList ratingList = ontModel.createList(new RDFNode[]{catholic, churchOfIreland, multiDenominational});
        OntClass rating = ontModel.createEnumeratedClass(NAMESPACE + "RATING", ratingList);
        rating.addLabel("rating", null);
        rating.addComment("Type of architectural-heritage ", null);

        catholic.addSuperClass(rating);
        churchOfIreland.addSuperClass(rating);
        multiDenominational.addSuperClass(rating);

        /*********** subclasses of heritage **********/

        OntClass internationalHeritege = ontModel.createClass(NAMESPACE + "InternationalHeritege");
        OntClass nationalHeritege = ontModel.createClass(NAMESPACE + "NationalHeritage");
        OntClass regionalHeritage = ontModel.createClass(NAMESPACE + "RegionalHeritage");
        internationalHeritege.addLabel("InternationalHeritege", null);
        internationalHeritege.addComment("International, with count equals to 0", null);
        nationalHeritege.addLabel("NationalHeritage", null);
        nationalHeritege.addComment("NationalHeritage, with international count equals to 0", null);
        regionalHeritage.addLabel("RegionalHeritage", null);
        regionalHeritage.addComment("regionalHeritage", null);

        RDFList list = ontModel.createList(new RDFNode[]{internationalHeritege, nationalHeritege, regionalHeritage});
        internationalHeritege.addDisjointWith(nationalHeritege);
        regionalHeritage.addDisjointWith(internationalHeritege);
        regionalHeritage.addDisjointWith(nationalHeritege);

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
        regNo.setRange(XSD.nonNegativeInteger);

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
        dateFrom.addComment("Start date of a heritage", null);
        dateFrom.setDomain(heritage);
        dateFrom.setRange(XSD.xstring);

        DatatypeProperty dateTo = ontModel.createDatatypeProperty(NAMESPACE + "dateTo");
        dateTo.addLabel("dateTo", null);
        dateTo.addComment("End date of a heritage", null);
        dateTo.setDomain(heritage);
        dateTo.setRange(XSD.xstring);

        /*DatatypeProperty rating = ontModel.createDatatypeProperty(NAMESPACE + "rating");
        rating.addLabel("rating", null);
        rating.addComment("Rating a heritage", null);
        rating.setDomain(heritage);
        rating.setRange(XSD.xstring);*/

        DatatypeProperty originalType = ontModel.createDatatypeProperty(NAMESPACE + "originalType");
        originalType.addLabel("originalType", null);
        originalType.addComment("Rriginal type a heritage", null);
        originalType.setDomain(heritage);
        originalType.setRange(XSD.xstring);

        DatatypeProperty xCoord = ontModel.createDatatypeProperty(NAMESPACE + "xCoord");
        xCoord.addLabel("xCoord", null);
        xCoord.addComment("x coordinate a heritage", null);
        xCoord.setDomain(heritage);
        xCoord.setRange(XSD.xstring);

        DatatypeProperty yCoord = ontModel.createDatatypeProperty(NAMESPACE + "yCoord");
        yCoord.addLabel("yCoord", null);
        yCoord.addComment("y coordinate a heritage", null);
        yCoord.setDomain(heritage);
        yCoord.setRange(XSD.xstring);

        DatatypeProperty imageLink = ontModel.createDatatypeProperty(NAMESPACE + "imageLink");
        imageLink.addLabel("imageLink", null);
        imageLink.addComment("y coordinate a heritage", null);
        imageLink.setDomain(heritage);
        imageLink.setRange(XSD.xstring);

        ObjectProperty location = ontModel.createObjectProperty(NAMESPACE + "location");
        location.addLabel("location", null);
        location.addComment("Geographic location of a heritage, described by a GeoLocation", null);
        location.setDomain(heritage);
        location.setRange(geoLocation);

        ObjectProperty inCounty = ontModel.createObjectProperty(NAMESPACE + "inCounty");
        inCounty.addLabel("inCounty", null);
        inCounty.addComment("Which ireland county the hertage belongs to", null);
        inCounty.setDomain(heritage);
        inCounty.setRange(county);


        DatatypeProperty withEthos = ontModel.createDatatypeProperty(NAMESPACE + "withEthos");
        withEthos.addLabel("withEthos", null);
        withEthos.addComment("Ethos type of the school", null);
        withEthos.setDomain(heritage);
        withEthos.setRange(catholic);

        // Subclasses setup
        internationalHeritege.addSuperClass(heritage);
        nationalHeritege.addSuperClass(heritage);
        regionalHeritage.addSuperClass(heritage);

        /*OntClass catholicSchool = ontModel.createClass(NAMESPACE + "CatholicSchool");
        catholicSchool.addLabel("CatholicSchool", null);
        catholicSchool.addComment("School class definition which ethos is Catholic", null);
        catholicSchool.addSuperClass(school);
        catholicSchool.addSuperClass(ontModel.createHasValueRestriction(null, withEthos, catholic));

        Literal zero = ontModel.createTypedLiteral(0);
        internationalHeritege.addSuperClass(ontModel.createHasValueRestriction(null, internationalCount, zero));
        nationalHeritege.addSuperClass(ontModel.createHasValueRestriction(null, nationalCount, zero));*/

        // County property

        ObjectProperty hasHeritages = ontModel.createObjectProperty(NAMESPACE + "hasHeritages");
        hasHeritages.addLabel("hasSchools", null);
        hasHeritages.addComment("List schools belongs to the county", null);
        hasHeritages.setDomain(county);
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
        heritage.addSuperClass(ontModel.createCardinalityRestriction(null, originalType, 1));
        heritage.addSuperClass(ontModel.createCardinalityRestriction(null, xCoord, 1));
        heritage.addSuperClass(ontModel.createCardinalityRestriction(null, yCoord, 1));
        heritage.addSuperClass(ontModel.createCardinalityRestriction(null, imageLink, 1));
        heritage.addSuperClass(ontModel.createCardinalityRestriction(null, location, 1));
        heritage.addSuperClass(ontModel.createCardinalityRestriction(null, inCounty, 1));
        heritage.addSuperClass(ontModel.createCardinalityRestriction(null, withEthos, 1));



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
            Individual aCounty = county.createIndividual(NAMESPACE + info.get(0).toString().replace(" ", ""));
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
                String aRollNumber = record.get(1);
                String aLabel = record.get(2);
                String aAddress = record.get(3);
                if (!record.get(4).equals("")) {
                    aAddress += ", " + record.get(4);
                    if (!record.get(5).equals("")) {
                        aAddress += ", " + record.get(5);
                        if (!record.get(6).equals("")) {
                            aAddress += ", " + record.get(6);
                        }
                    }
                }
                String aBoyCount = record.get(12);
                String aGirlCount =record.get(13);
                String aStudentCount = record.get(14);
                //boolean aInIsland = record.get(9).equals("Y");
                //boolean aIsDeis = record.get(10).equals("Y");
                //boolean aIsGaeltacht = record.get(11).equals("Y");
                String aLatitude = record.get(18);
                String aLongitude =record.get(17);

                Individual aLocation = geoLocation.createIndividual();
                //aLocation.addLiteral(latitude, aLatitude);
                //aLocation.addLiteral(longitude, aLongitude);

                Individual aCounty = county.createIndividual(NAMESPACE + "CAVAN");

                String ethosString = record.get(8);
                Individual aEthosType;
                if (ethosString.equals("CATHOLIC")) {
                    aEthosType = catholic.createIndividual();
                } else if (ethosString.equals("CHURCH OF IRELAND")) {
                    aEthosType = churchOfIreland.createIndividual();
                } else {
                    aEthosType = multiDenominational.createIndividual();
                }

                // Create school classes

                Individual aheritage;
                if (aBoyCount == "0") {
                    aheritage = internationalHeritege.createIndividual(NAMESPACE + aRollNumber.replace(" ", ""));
                } else if (aGirlCount == "0") {
                    aheritage = nationalHeritege.createIndividual(NAMESPACE + aRollNumber.replace(" ", ""));
                } else {
                    aheritage = regionalHeritage.createIndividual(NAMESPACE + "aRollNumber");
                }
                aheritage.addOntClass(heritage);
                /*if (ethosString.equals("CATHOLIC")) {
                    aheritage.addOntClass(catholicSchool);
                }*/

                //aheritage.addLiteral(RDFS.label, aLabel);
                //aheritage.addLiteral(address, aAddress);
                /*aheritage.addLiteral(boyCount, ontModel.createTypedLiteral(aBoyCount, XSDDatatype.XSDnonNegativeInteger));
                aheritage.addLiteral(girlCount, ontModel.createTypedLiteral(aGirlCount, XSDDatatype.XSDnonNegativeInteger));
                aheritage.addLiteral(studentCount, ontModel.createTypedLiteral(aStudentCount, XSDDatatype.XSDpositiveInteger));*/
                //aheritage.addLiteral(inIsland, aInIsland);
                //aheritage.addLiteral(isDEIS, aIsDeis);
                //aheritage.addLiteral(isGaeltacht, aIsGaeltacht);
                aheritage.addProperty(withEthos, aEthosType);
                aheritage.addProperty(location, aLocation);

                for (int i = 0; i < countyInfoList.size(); i++) {
                    ArrayList<Object> countyInfo = countyInfoList.get(i);
                    Geometry geometry = (Geometry)countyInfo.get(3);
                    Point point = new Point(6, 6);

                    OperatorWithin within = OperatorWithin.local();
                    if (within.execute(point, geometry, SpatialReference.create("WGS84"), null)) {
                        aheritage.addProperty(inCounty, countyIndiList.get(i));
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
