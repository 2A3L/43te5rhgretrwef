import json-20211205.jar
import mongo-java-driver-3.2.2.jar
//import for jsonwriter and filewriter
import java.io.*;


class CRUD {

    /*Declarando colores*/
    public static final String CRESET = "\u001B[0m";
    public static final String CRED = "\u001B[31m";
    public static final String CGREEN = "\u001B[32m";
    public static final String CYELLOW = "\u001B[33m";
    public static final String CPURPLE = "\u001B[35m";
    public static final String CCYAN = "\u001B[36m";
    public static final String CWHITE = "\u001B[37m";
    public static final String CBLACK = "\u001B[30m";
    public static final String CDARKAQUA = "\u001B[36m";
    public static final String CLIGHTGREEN = "\u001B[92m";

    /*method main*/
    public static void main(String[] args) {
        println("\r"+CDARKAQUA + "_____________________________________" + CRESET);
        MongoClient mongo = crearConexion();
        DB db = mongo.getDB("Difarma")
        //leerDatos(db, "DifarmaQA");
        //filtrarDatos(db, "DifarmaQA", "#higiene");
        //duplicarColeccion(db, "DifarmaQA", "test2", "#santiago");
        //insertArchivo(db,"DifarmaQA", "DifarmaQA.json");
        eliminarDatoID(db, "DifarmaQA", "AU-47");
        eliminarDatoID(db, "DifarmaQA", "AU-48");
        eliminarDatoID(db, "DifarmaQA", "AU-49");
        //crea un List<String>
        /*List<String> lista = new ArrayList<String>();
        lista.add("#chile");
        lista.add("#difarma");
        lista.add("#higiene");

        insert(db, "DifarmaQA", lista, "Pasta de dientes", 1, 2750 );
        insert(db, "DifarmaQA", lista, "Crema solar", 1, 2500 );
        insert(db, "DifarmaQA", lista, "Desodorante", 1, 1250 );
        insert(db, "DifarmaQA", lista, "Cepillo", 1, 3250 );*/
        //insert(db, "DifarmaQA", lista, "Jabón", 1, 2750 );
    }
    /*metodo insertar desde un json datos*/
    public static void insertArchivo(DB db, String collection, String archivo) {
        try {
            def coll = db.getCollection(collection);
            collection.insert(leerTxt(archivo));

            System.out.println("Done");

        } catch (Exception e) {
            System.out.println(CLIGHTGREEN+"Error al leer el archivo. \n"+CRESET+e);
        }
    }

            
    /*leer txt*/
    public static String[] leerTxt(String path)
    {
        List<String> lista = new ArrayList<String>();
        try{
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            while((linea = br.readLine()) != null)
            {
                lista.add(linea);
            }
            //impime la lista
            System.out.println(CRED+lista);

            br.close();
            fr.close();
            return lista;
        }
        catch(Exception e){
            System.out.println("\n Error: \n"+CRED+e+CRESET);
        }
    }
    /*method insert*/
    public static void insert(DB db, String collection, List<String> lista, String nombre, int ver, int valor) {
        try{
            def coll = db.getCollection(collection);
            def doc = new BasicDBObject();

            //id auntoincremental respetando el id anterior

            //identifica si el id existe
            def id = coll.findOne(new BasicDBObject("_id", "AU-"+coll.getCount()));
            if(id == null)
            {
                //crea un objeto
                doc.put("_id", "AU-"+coll.getCount());
                doc.put("nombre", nombre);
                doc.put("version", ver);
                doc.put("valor", valor);
                doc.put("tags", lista);
                coll.insert(doc);
                System.out.println(CPURPLE+"===============\n"+CLIGHTGREEN+"se agregó un id igual al indice."+CRESET);
            }
            else
            {  
               def num = 0;
               while (id != null)
               {
                   num++;
                   def ids=coll.getCount()+num;
                   id = coll.findOne(new BasicDBObject("_id", "AU-"+ids));
                   //println (id);
                   //println (ids);
               } 
               println (num);
                   //crea un objeto
                    def ids=coll.getCount()+num;
                    doc.put("_id", "AU-"+ids);
                    doc.put("nombre", nombre);
                    doc.put("version", ver);
                    doc.put("valor", valor);
                    doc.put("tags", lista);
                    coll.insert(doc);
                    System.out.println(CPURPLE+"===============\n"+CLIGHTGREEN+"se agregó un id diferente al indice."+CRESET);            
            }
            
            //si doc tiene algun put dentro de el imprime un mensaje
            if(doc.size() > 0)
            {
                System.out.println("se insertó: " + CYELLOW + doc + CRESET);
            }            
        }
        catch(Exception e){
            System.out.println("\n Error: \n"+CRED+e+CRESET);
        }

    }   
    /*method create*/
    public static MongoClient crearConexion()
    {
        try
        {
            def mongo = new MongoClient("localhost", 27017);
            System.out.println(CGREEN+"Conexion exitosa"+CRESET);
            return mongo;
        }
        catch(Exception e){
            System.out.println(CRED+"Error al conectar con la base de datos");
        }

    }
    //leer datos de la base de datos
    public static void leerDatos(DB db, String collection)
    {
        try{
            def coll = db.getCollection(collection);
            def cursor = coll.find();
            while(cursor.hasNext())
            {
                def doc = cursor.next();
                System.out.println(CYELLOW+doc+CRESET);
            }
        }
        catch(Exception e){
            System.out.println("\n Error: \n"+CRED+e+CRESET);
        }
    }
    //filtrar datos una lista de la base de datos
    public static void filtrarDatos(DB db, String collection, String tag)
    {
        try{
            def coll = db.getCollection(collection);
            def cursor = coll.find(new BasicDBObject("tags", tag));
            while(cursor.hasNext())
            {
                def doc = cursor.next();
                System.out.println(CYELLOW+doc+CRESET);
            }
        }
        catch(Exception e){
            System.out.println("\n Error: \n"+CRED+e+CRESET);
        }
    }
    //duplicar coleccion en la base de datos
    public static void duplicarColeccion(DB db, String collection1, String collection2,String tag)
    {
        try{
            def coll = db.getCollection(collection1);
            def coll2 = db.getCollection(collection2);
            def cursor = coll.find(new BasicDBObject("tags", tag));
            while(cursor.hasNext())
            {
                def doc = cursor.next();
                coll2.insert(doc);
                System.out.println(CYELLOW+doc+CRESET);
            }
        }
        catch(Exception e){
            System.out.println("\n Error: \n"+CRED+e+CRESET);
        }
    }

    //metodo para extraer un dato y eliminarlo por tag
    public static void eliminarDatoTAG(DB db, String collection, String tag)
    {
        try{
            def coll = db.getCollection(collection);
            def cursor = coll.find(new BasicDBObject("tags", tag));
            while(cursor.hasNext())
            {
                def doc = cursor.next();
                coll.remove(doc);
                System.out.println(CYELLOW+doc+CRESET);
            }
        }
        catch(Exception e){
            System.out.println("\n Error: \n"+CRED+e+CRESET);
        }
    }

    //metodo para extraer un dato y eliminarlo por id
    public static void eliminarDatoID(DB db, String collection, String id)
    {
        try{
            def coll = db.getCollection(collection);
            def cursor = coll.find(new BasicDBObject("_id", id));
            while(cursor.hasNext())
            {
                def doc = cursor.next();
                coll.remove(doc);
                //printl "el dato que se eliminó es:"+doc
                System.out.println(CLIGHTGREEN+"el dato que se eliminó es:\n"+CYELLOW+doc+CRESET);
                //Comprueba si el archivo existe
                if(new File("json"+collection+".json").exists())
                {
                    //si existe seleccionalo sin borrarlo
                    def file = new File("json"+collection+".json");
                    //escribe en la ultima linea
                    def writer = new FileWriter(file, true);
                    writer.write("\n"+doc.toString());
                    writer.close();
                    System.out.println(CGREEN+"El archivo se creo correctamente"+CRESET);
                }
                else
                {
                    System.out.println(CRED+"El archivo no existe"+CRESET);
                }
            }
        }
        catch(Exception e){
            System.out.println("\n Error: \n"+CRED+e+CRESET);
        }
    }    
}