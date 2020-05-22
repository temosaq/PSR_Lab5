import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Updates.inc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class SerwisMongoDB {
    public static void main(String[] args) {
        String user = "student01";
        String password = "student01";
        String host = "localhost";
        int port = 27017;
        String database = "database01";
        String clientURI = "mongodb://" + user + ":" + password + "@" + host + ":" + port + "/" + database;
        MongoClientURI uri = new MongoClientURI(clientURI);

        MongoClient mongoClient = new MongoClient(uri);

        MongoDatabase db = mongoClient.getDatabase(database);

        db.getCollection("people").drop();

        MongoCollection<Document> collection = db.getCollection("people");

        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println("");
            System.out.println("<--------------SERWIS SAMOCHODOWY-------------->");
            System.out.println("-- 1. Dodaj serwisy");
            System.out.println("-- 2. Wyświetl serwis po ID");
            System.out.println("-- 3. Wyświetl serwisy z określoną powierzchnią");
            System.out.println("-- 4. Wyświetl wszystkie serwisy");
            System.out.println("-- 5. Aktualizuj po ID");
            System.out.println("-- 6. Zwiększ powierzchnię serwisów");
            System.out.println("-- 7. Usuń serwis po ID");
            System.out.println("-- 8. Usuń wszystkie serwisy");
            System.out.println("-- 9. Zakończ");


            System.out.println("Podaj numer operacji do wykonania: ");
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    Document autoMix= new Document("_id", 1)
                            .append("lastname", "Ledek")
                            .append("names", "Sebastian")
                            .append("age", 29)
                            .append("area", 200)
                            .append("wokers", Arrays.asList("Koziol", "Kowalewski", "Nowacki"))
                            .append("cars", Arrays.asList("audi", "volkswagen", "skoda", "seat"))
                            .append("salary", Arrays.asList(new Document("welder", 3200), new Document("mechanic", 3700), new Document("spraying", 3400)));
                    collection.insertOne(autoMix);

                    Document frelix= new Document("_id", 2)
                            .append("lastname", "Kwiatkowski")
                            .append("names", "Eugeniusz")
                            .append("age", 34)
                            .append("area", 150)
                            .append("wokers", Arrays.asList("Sepioł", "Lara", "Klaus", "Bereszynski"))
                            .append("cars", Arrays.asList("bmw", "mercedes"))
                            .append("salary", Arrays.asList(new Document("welder", 4000), new Document("mechanic", 4600), new Document("spraying", 4100)));
                    collection.insertOne(frelix);

                    Document japan= new Document("_id", 3)
                            .append("lastname", "Dolot")
                            .append("names", "Zbigniew")
                            .append("area", 120)
                            .append("age", 38)
                            .append("wokers", Arrays.asList("Dolot"))
                            .append("cars", Arrays.asList("honda", "hyundai", "suzuki"))
                            .append("salary", Arrays.asList(new Document("mechanic", 4000)));
                    collection.insertOne(japan);

                    System.out.println("Dodano");

                    break;

                case "2":
                    System.out.println("Podaj ID serwisu do wyświetlenia: ");
                    int idSerwis = scanner.nextInt();
                    Document myDoc = collection.find(eq("_id", idSerwis)).first();
                    System.out.println("Serwis o ID = "+ idSerwis+" " + myDoc.toJson());
                    break;

                case "3":
                    System.out.println("Znajdź serwisy z powierzchnią większą od: ");
                    int areaS = scanner.nextInt();
                    for (Document d : collection.find((
                            gt("area", areaS))))
                        System.out.println("Serwis o powierzchni większej niż: " + areaS + " "  + d.toJson());
                    break;

                case "4":
                    for (Document doc : collection.find())
                        System.out.println("find() " + doc.toJson());
                    break;

                case "5":
                    for (Document doc : collection.find())
                        System.out.println("find() " + doc.toJson());

                    System.out.println("Podaj ID serwisu do aktualizacji");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Podaj nowe nazwisko: ");
                    String newLastname=scanner.nextLine();
                    System.out.println("Podaj nowe imię: ");
                    String newNames=scanner.nextLine();
                    collection.updateOne(eq("_id", id), new Document("$set", new Document("lastname", newLastname).append("names", newNames)));
                    System.out.println("updateOne(eq(\"_id\"," + id + "), new Document(\"$set\", new Document(\"lastname\", \""+newLastname+"\").append(\"names\", \""+ newNames+"\")) " );
                    break;

                case "6":
                    System.out.println("Podaj o ile metrów zwiększyć powierzchnię serwisu");
                    int toAdd = scanner.nextInt();
                    UpdateResult updateResult = collection.updateMany(exists("area"), inc("area", toAdd));
                    System.out.println("Liczba serwisów o zwiększonej powierzchni: " + updateResult.getModifiedCount());
                    for (Document doc : collection.find())
                        System.out.println("updateMany(exists(\"area\"), inc(\"area\", " +toAdd + ")) " + doc.toJson());
                    break;

                case "7":
                    System.out.println("Podaj id serwisu do usunięcia:");
                    collection.deleteOne(eq("_id", scanner.nextLong()));
                    for (Document doc : collection.find())
                        System.out.println("deleteOne(eq(\"_i\", _id)) " + doc.toJson());
                    break;

                case "8":
                    System.out.println("Usunięto wszystkie serwisy");
                    DeleteResult deleteResult = collection.deleteMany(gt("_id", 0));
                    System.out.println("Liczba usuniętych serwisów:" + deleteResult.getDeletedCount());
                    for (Document doc : collection.find())
                        System.out.println("deleteMany(gt(\"_id\", 0)) " + doc.toJson());
                    break;

                case "9":
                    mongoClient.close();
                    return;

                default:
                    System.out.println("Błędna instrukcja");
                }
        } while (true);

    }
}

