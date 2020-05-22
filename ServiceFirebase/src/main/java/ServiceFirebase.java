import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class ServiceFirebase {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {


        FileInputStream serviceAccount =
                new FileInputStream("src/main/resources/servicefirebase.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://serwisfirebase.firebaseio.com")
                .build();
        FirebaseApp.initializeApp(options);

        Firestore db = FirestoreClient.getFirestore();

        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println("");
            System.out.println("<--------------SERWIS SAMOCHODOWY-------------->");
            System.out.println("-- 1. Dodaj serwisy");
            System.out.println("-- 2. Wyświetl serwisy po ID");
            System.out.println("-- 3. Wyświetl serwisy z określoną powierzchnią");
            System.out.println("-- 4. Wyświetl wszystkie serwisy");
            System.out.println("-- 5. Aktualizuj serwis");
            System.out.println("-- 6. Przetwarzanie");
            System.out.println("-- 7. Usuń serwis");



            System.out.println("Podaj numer operacji do wykonania: ");
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    Map<String, Object> automix = new HashMap<>();
                    automix.put("lastname", "Ledek");
                    automix.put("names", "Sebastian");
                    automix.put("age", 29);
                    automix.put("area", 200);
                    automix.put("workers", Arrays.asList("Koziol", "Kowalewski", "Nowacki"));
                    automix.put("cars", Arrays.asList("audi", "volkswagen", "skoda", "seat"));

                    Map<String, Object> frelix = new HashMap<>();
                    frelix.put("lastname", "Kwiatkowski");
                    frelix.put("names", "Eugeniusz");
                    frelix.put("age", 34);
                    frelix.put("area", 150);
                    frelix.put("wokers", Arrays.asList("Sepioł", "Lara", "Klaus", "Bereszynski"));
                    frelix.put("cars", Arrays.asList("bmw", "mercedes"));

                    Map<String, Object> japan = new HashMap<>();
                    japan.put("lastname", "Dolot");
                    japan.put("names", "Zbigniew");
                    japan.put("age", 38);
                    japan.put("area", 120);
                    japan.put("wokers", Arrays.asList("Dolot"));
                    japan.put("cars", Arrays.asList("honda", "hyundai", "suzuki"));

                    ApiFuture<WriteResult> future = db.collection("services").document("1").set(automix);
                    ApiFuture<WriteResult> futuru = db.collection("services").document("2").set(frelix);
                    ApiFuture<WriteResult> futuri = db.collection("services").document("3").set(japan);
                    System.out.println(" Dodano    " + future.get().getUpdateTime());
                    System.out.println(" Dodano    " + futuru.get().getUpdateTime());
                    System.out.println(" Dodano    " + futuri.get().getUpdateTime());
                    break;

                case "2":
                    System.out.println("Podaj nazwę serwisu, który chcesz wyświetlić: ");
                    String name = scanner.nextLine();
                    DocumentReference docRef = db.collection("services").document(name);

                    ApiFuture<DocumentSnapshot> futuree = docRef.get();

                    DocumentSnapshot document = futuree.get();
                    if (document.exists()) {
                        System.out.println("Dane serwisu: " + document.getData());
                    } else {
                        System.out.println("Nie ma takiego serwisu!");
                    }
                    break;

                case "3":
                    System.out.println("Znajdź serwisy z powierzchnią większą od: ");
                    int areaS = scanner.nextInt();
                    ApiFuture<QuerySnapshot> futuress =
                            db.collection("services").whereGreaterThan("area", areaS).get();
                    List<QueryDocumentSnapshot> documentss = futuress.get().getDocuments();
                    for (DocumentSnapshot docum : documentss) {
                        System.out.println(docum.getId() + " => " + docum.getData());
                    }
                    break;

                case "4":
                    ApiFuture<QuerySnapshot> futures = db.collection("services").get();
                    List<QueryDocumentSnapshot> documents = futures.get().getDocuments();
                    for (QueryDocumentSnapshot documen : documents) {
                        System.out.println(documen.getId() + " => " + documen.getData());
                    }
                    break;

                case "5":
                    System.out.println("Podaj ID serwisu do aktualizacji");
                    DocumentReference docReff = db.collection("services").document(scanner.nextLine());
                    Map<String, Object> initialData = new HashMap<>();
                    System.out.println("Podaj nowe imie: ");
                    final ApiFuture<WriteResult> updateFuturee = docReff
                            .update("names", scanner.nextLine());
                    break;

                case "6":
                    System.out.println("Podaj ID serwisu do przetwarzania");
                    DocumentReference areaRef = db.collection("services").document(scanner.nextLine());
                    System.out.println("O ile zwiększyć powierzchnie serwisu");
                    final ApiFuture<WriteResult> updateFuture = areaRef
                            .update("area", FieldValue.increment(scanner.nextInt()));
                    break;

                case "7":
                    System.out.println("Podaj ID serwisu do usunięcia");
                    ApiFuture<WriteResult> writeResult = db.collection("services").document(scanner.nextLine()).delete();
                    System.out.println("Usunięto: " + writeResult.get().getUpdateTime());
                break;


                default:
                    System.out.println("Błędna instrukcja");
            }
        }while (true) ;

    }
}
