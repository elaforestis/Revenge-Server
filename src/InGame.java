import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;


public class InGame extends Thread{
	static final Logger log = Logger.getLogger( InGame.class.getName() );
	public static final String[] nomoi = {"ΑΙΤΩΛΟΑΚΑΡΝΑΝΙΑΣ", "ΑΡΓΟΛΙΔΑΣ", "ΑΡΚΑΔΙΑΣ", "ΑΡΤΑΣ", "ΑΤΤΙΚΗΣ", "ΑΧΑΙΑΣ", "ΒΟΙΩΤΙΑΣ", "ΓΡΕΒΕΝΩΝ", "ΔΡΑΜΑΣ", "ΔΩΔΕΚΑΝΗΣΟΥ", "ΕΒΟΙΑΣ", "ΕΒΡΟΥ", "ΕΥΡΥΤΑΝΙΑΣ", "ΖΑΚΥΝΘΟΥ", "ΗΛΕΙΑΣ", "ΗΜΑΘΙΑΣ", "ΗΡΑΚΛΕΙΟΥ", "ΘΕΣΠΡΩΤΙΑΣ", "ΘΕΣΣΑΛΟΝΙΚΗΣ", "ΙΩΑΝΝΙΝΩΝ", "ΚΑΒΑΛΑΣ", "ΚΑΡΔΙΤΣΑΣ", "ΚΑΣΤΟΡΙΑΣ", "ΚΕΡΚΥΡΑΣ", "ΚΕΦΑΛΛΗΝΙΑΣ", "ΚΙΛΚΙΣ", "ΚΟΖΑΝΗΣ", "ΚΟΡΙΝΘΙΑΣ", "ΚΥΚΛΑΔΩΝ", "ΛΑΚΩΝΙΑΣ", "ΛΑΡΙΣΑΣ", "ΛΑΣΙΘΙΟΥ", "ΛΕΣΒΟΥ", "ΛΕΥΚΑΔΑΣ", "ΜΑΓΝΗΣΙΑΣ", "ΜΕΣΣΗΝΙΑΣ", "ΞΑΝΘΗΣ", "ΠΕΛΛΑΣ", "ΠΙΕΡΙΑΣ", "ΠΡΕΒΕΖΑΣ", "ΡΕΘΥΜΝΟΥ", "ΡΟΔΟΠΗΣ", "ΣΑΜΟΥ", "ΣΕΡΡΩΝ", "ΤΡΙΚΑΛΩΝ", "ΦΘΙΩΤΙΔΑΣ", "ΦΛΩΡΙΝΑΣ", "ΦΩΚΙΔΑΣ", "ΧΑΛΚΙΔΙΚΗΣ", "ΧΑΝΙΩΝ", "ΧΙΟΥ"};
	//public final String[] nomoi = {"Αιτωλοακαρνανίας","Αργολίδας","Αρκαδίας","Άρτας","Αττικής","Αχαΐας","Βοιωτίας","Γρεβενών","Δράμας","Δωδεκανήσου","Εβοίας","Έβρου","Ευρυτανίας","Ζακύνθου","Ηλείας","Ημαθίας","Ηρακλείου","Θεσπρωτίας","Θεσσαλονίκης","Ιωαννίνων","Καβάλας","Καρδίτσας","Καστοριάς","Κέρκυρας","Κεφαλληνίας","Κιλκίς","Κοζάνης","Κορινθίας","Κυκλάδων","Λακωνίας","Λάρισας","Λασιθίου","Λέσβου","Λευκάδας","Μαγνησίας","Μεσσηνίας","Ξάνθης","Πέλλας","Πιερίας","Πρέβεζας","Ρεθύμνου","Ροδόπης","Σάμου","Σερρών","Τρικάλων","Φθιώτιδας","Φλώρινας","Φωκίδας","Χαλκιδικής","Χανίων","Χίου"};
	public final String[] perioxes = {"﻿Άγιος Ανδρέας Γαβαλού", "Άγιος Βλάσιος", "Άγιος Κωνσταντίνος", "Αγρίνιο", "Αιτωλικό", "Άκτιο", "Αμφιλοχία", "Αντίρριο", "Άνω Χώρα", "Αστακός", "Βόνιτσα", "Γαβαλού", "Γραμμένη Οξυά", "Δοκίμιο", "Εμπεσός", "Ευηνοχώρι", "Θέρμο", "Καινούργιο", "Κατούνα", "Κατοχή", "Κλεπά", "Ματαράγκα", "Μεγάλη Χώρα", "Μενίδι", "Μεσολόγγι", "Μύτικας", "Ναύπακτος", "Νέα Αβόρανη", "Νεοχώρι Αιτωλοακαρνανίας", "Πάλαιρος", "Παναιτώλιο", "Παραβόλα", "Πλάτανος Ναυπάκτου", "Σίμος", "Στράτος", "Τερψιθέα", "Φυτείες", "Χρυσοπηγή","﻿Αγία Τριάδα Αργολίδας", "Αλέα", "Άργος", "Αχλαδόκαμπος", "Δρέπανο Αργολίδας", "Ερμιόνη", "Κουτσοπόδι", "Κρανίδι", "Λυγουριό", "Ναύπλιο", "Νέα Επίδαυρος", "Νέα Κίος", "Παλαιά Επίδαυρος", "Τολό"                        ,"﻿Άγιος Ανδρέας", "Άγιος Νικόλαος Αρκαδίας", "Άγιος Πέτρος", "Άστρος", "Βαλτεσινίκο", "Βυτίνα", "Δημητσάνα", "Ελαιοχώρι", "Κανδήλα", "Καρίταινα", "Καστρί", "Κάτω Ασέα", "Κοντοβάζαινα", "Κοσμάς", "Κοτύλιο", "Λαγκάδια", "Λεβίδι", "Λεοντάρι Αρκαδίας", "Λεωνίδι", "Μεγαλόπολη", "Νεστάνη", "Παλούμπα", "Παράλιο Άστρος", "Πουρναριά", "Στάδιο", "Τρίπολη", "Τρόπαια", "Τυρός"          ,"﻿Άγναντα", "Αμμότοπος", "Άνω Καλεντίνη", "Άρτα", "Βουργαρέλι", "Δημάρι", "Κομπότι", "Μελισσουργοί", "Μηλιανά", "Νεοχώρι Άρτας", "Πέτα", "Ροδαυγή", "Φιλοθέη Άρτας", "Χαλκιάδες"                        ,"﻿Αγία Βαρβάρα", "Αγία Κυριακή", "Αγία Μαρίνα Αίγινας", "Αγία Μαρίνα Γραμματικού", "Αγία Μαρίνα Κορωπίου", "Αγία Μαρίνα Νέας Μάκρης", "Αγία Παρασκευή", "Άγιοι Ανάργυροι", "Άγιοι Απόστολοι", "Άγιος Δημήτριος", "Άγιος Ιωάννης Ρέντης", "Άγιος Νικόλαος Αναβύσσου", "Άγιος Νικόλαος Λούτσας", "Άγιος Παντελεήμονας", "Άγιος Σεραφείμ", "Άγιος Στέφανος", "Αθήνα", "Αιάντειο", "Αιγάλεω", "Αίγινα", "Αλεποχώρι", "Άλιμος", "Αμπελάκια", "Ανάβυσσος", "Ανθούσα", "Άνοιξη", "Άνω Λιόσια", "Άνω Νέα Παλάτια", "Αργυρούπολη", "Αρτέμιδα", "Ασπρόπυργος", "Αυλάκι", "Αυλώνα", "Αφίδνες", "Αχαρνές", "Βάρη", "Βαρνάβας", "Βίλια", "Βούλα", "Βουλιαγμένη", "Βραυρώνα", "Βριλήσσια", "Βύρωνας", "Γαλάζια Ακτή", "Γαλατάς Αττικής", "Γαλάτσι", "Γέρακας", "Γλυκά Νερά", "Γλυφάδα", "Γραμματικό", "Δασκαλειό", "Δάφνη", "Διόνυσος", "Δραπετσώνα", "Δροσιά", "Εκάλη", "Ελευσίνα", "Ελληνικό", "Ερυθρές", "Ζεφύρι", "Ζούμπερι", "Ζωγράφος", "Ηλιούπολη", "Θρακομακεδόνες", "Θυμάρι", "Ίλιον", "Καισαριανή", "Κάλαμος Αττικής", "Καλέτζι", "Καλλιθέα", "Καλλιτεχνούπολη", "Καλύβια Θορικού", "Καματερό", "Κάντζα", "Καπανδρίτι", "Κάτω Σούλι", "Κερατέα", "Κερατσίνι", "Κηφισιά", "Κινέτα", "Κόκκινο Λιμανάκι", "Κορυδαλλός", "Κορωπί", "Κουβαράς", "Κρυονέρι", "Κύθηρα", "Κυψέλη Αίγινας", "Λαγονήσι", "Λάκκα", "Λαύριο", "Λουτρόπυργος", "Λυκόβρυση", "Μαγούλα", "Μάνδρα", "Μαραθώνας", "Μαρκόπουλο", "Μαρούσι", "Μάτι", "Μέγαρα", "Μέθανα", "Μελίσσια", "Μεσαγρός", "Μεταμόρφωση", "Μοσχάτο", "Νέα Ερυθραία", "Νέα Ιωνία", "Νέα Μάκρη", "Νέα Παλάτια", "Νέα Πεντέλη", "Νέα Πέραμος Αττικής", "Νέα Πολιτεία", "Νέα Σμύρνη", "Νέα Φιλαδέλφεια", "Νέα Χαλκηδόνα", "Νέο Ηράκλειο", "Νέο Ψυχικό", "Νέος Βουτζάς", "Νίκαια", "Ντράφι", "Παιανία", "Παλαιά Φώκαια", "Παλαιό Φάληρο", "Παλλήνη", "Παλούκια", "Παπάγος", "Πειραιάς", "Πεντέλη", "Πέραμα", "Πέρδικα", "Περιστέρι", "Πετρούπολη", "Πεύκη", "Πικέρμι", "Πολυδένδρι", "Πόρος Τροιζηνίας", "Πόρτο Ράφτη", "Ποταμός Κυθήρων", "Ραφήνα", "Ροδόπολη Αττικής", "Σαλαμίνα", "Σαρωνίδα", "Σελήνια", "Σκάλα Ωρωπού", "Σουβάλα", "Σπάτα", "Σπέτσες", "Σταμάτα", "Συκάμινο", "Ταύρος", "Ύδρα", "Υμηττός", "Φιλοθέη", "Φυλή", "Χαϊδάρι", "Χαλάνδρι", "Χαλκούτσι", "Χαμολιά", "Χολαργός", "Ψυχικό", "Ωρωπός", "﻿Άβυθος", "Αγία Παρασκευή Αχαΐας", "Άγιος Βασίλειος", "Αιγείρα", "Αίγιο", "Ακράτα", "Ακταίο", "Άραξος", "Αραχωβίτικα", "Αροανία", "Βιομηχανική Περιοχή Πάτρας", "Βραχνέικα", "Δάφνη Αχαΐας", "Δεμένικα Σαραβάλιου", "Διακοπτό", "Δρέπανο Αχαΐας", "Ερυμάνθεια", "Καλάβρυτα", "Καμάρες", "Καμίνια", "Κάτω Αχαία", "Κάτω Καστρίτσι", "Κάτω Οβρυά", "Κλειτορία", "Κουλουράς", "Κράθιο", "Κρήνη Πάτρας", "Λάππα", "Μιντιλόγλι", "Μυρτιά", "Νέο Σούλι", "Οβρυά", "Παραλία Πατρών", "Πάτρα", "Πλάτανος Αχαΐας", "Προφήτης Ηλίας", "Ρίο", "Ροδοδάφνη", "Σαγαίικα", "Σαραβάλι", "Συχαινά", "Τέμενη", "Τερψιθέα Αχαΐας", "Χαλανδρίτσα", "Ψαθόπυργος",                                                                                                                   "﻿Άγιος Γεώργιος Βοιωτίας", "Αλίαρτος", "Αντίκυρα", "Αράχωβα", "Βάγια", "Δαύλεια", "Δήλεσι", "Δίστομο", "Δόμβραινα", "Θήβα", "Κυριάκι", "Λιβαδειά", "Οινόη Βοιωτίας", "Οινόφυτα", "Ορχομενός", "Παραλία Διστόμου", "Παρόριο", "Σχηματάρι", "Τανάγρα"                   ,"﻿Άγιος Γεώργιος", "Γρεβενά", "Δεσκάτη", "Πολυνέρι", "Σκούμτσια"                                 ,"﻿Άγιος Αθανάσιος Δράμας", "Αμπελάκια Δράμας", "Αργυρούπολη Δράμας", "Δοξάτο", "Δράμα", "Καλαμπάκι", "Κάτω Νευροκόπι", "Κύρια", "Μαυρόβατος", "Νέα Αμισός", "Νέα Σεβάστεια", "Νικηφόρος", "Παρανέστι", "Περίχωρα", "Προσοτσάνη", "Σιταγροί", "Φτελιά", "Φωτολίβος", "Χωριστή"                   ,"﻿Αγαθονήσι", "Ανάληψη Αστυπάλαιας", "Αντιμάχεια", "Αρχάγγελος", "Αστυπάλαια", "Αφάντου", "Γεννάδι", "Έμπωνας", "Ιαλυσός", "Καλυθιές", "Κάλυμνος", "Κάρπαθος", "Κάσος", "Κέφαλος Κω", "Κολύμπια", "Κοσκινού", "Κρεμαστή", "Κως", "Λειψοί", "Λέρος", "Λίνδος", "Μαριτσά", "Μεγίστη", "Νίσυρος", "Παλαιά Πόλη", "Παραδείσι", "Παστίδα", "Πάτμος", "Ρόδος", "Σγουρού", "Σορωνή", "Σύμη", "Τήλος", "Φαληράκι", "Χάλκη", "Ψαλίδι"  ,"﻿Αγία Άννα", "Άγιος Νικόλαος Εύβοιας", "Αλιβέρι", "Αμάρυνθος", "Ανθηδώνα", "Αυλωνάρι", "Βαθύ Αυλίδας", "Βασιλικό", "Δροσιά Ευβοίας", "Δύο Δένδρα", "Έξω Παναγίτσα", "Ερέτρια", "Ιστιαία", "Καθενοί", "Κάρυστος", "Κονίστρες", "Κριεζά", "Κύμη", "Λίμνη", "Λουτρά Αιδηψού", "Μαντούδι", "Μαρμάρι", "Μπούρτζι", "Νέα Αρτάκη", "Νέα Λάμψακος", "Οξύλιθος", "Παραλία Αυλίδας", "Πολιτικά", "Σκύρος", "Στενή Δίρφυος", "Στύρα", "Υλίκη", "Φάρος", "Χαλκίδα", "Ψαχνά", "Ωρεοί"  ,"﻿Αλεξανδρούπολη", "Απαλός", "Γέφυρα Κήπων", "Διδυμότειχο", "Δίκαια", "Καστανιές", "Κυπρίνος", "Λάβαρα", "Μαΐστρος", "Μεταξάδες", "Νέα Βύσσα", "Νέα Χιλή", "Ορεστιάδα", "Πέπλος", "Ρίζια", "Σαμοθράκη", "Σουφλί", "Συκορράχη", "Τυχερό", "Φέρες"                  ,"﻿Άγραφα", "Γρανίτσα", "Καρπενήσι", "Κερασοχώρι", "Κρίκελλο", "Μαυρόλογγος", "Προυσός", "Ραπτόπουλο", "Φουρνά"                             ,"﻿Βολίμες", "Γαϊτάνι", "Ζάκυνθος", "Καταστάρι", "Μαχαιράδο", "Τσιλιβί"                                ,"﻿Αμαλιάδα", "Ανδραβίδα", "Ανδρίτσαινα", "Αρχαία Ολυμπία", "Βάρδα", "Βαρθολομιό", "Γαστούνη", "Επιτάλιο", "Εφύρα", "Ζαχάρω", "Καβάσιλας", "Καλλιθέα Ηλείας", "Καράτουλας", "Κατάκολο", "Κρέστενα", "Κυλλήνη", "Λάλας", "Λάμπεια", "Λεχαινά", "Μυρσίνη", "Νέα Φιγαλεία", "Πύργος Ηλείας", "Τραγανό", "Χάβαρι"              ,"﻿Αγγελοχώρι Ημαθίας", "Άγιος Γεώργιος Ημαθίας", "Αλεξάνδρεια", "Βεργίνα", "Βέροια", "Ειρηνούπολη", "Κοπανός", "Λαζοχώρι", "Μακροχώρι Ημαθίας", "Μελίκη", "Νάουσα", "Πατρίδα", "Πλατύ", "Σταυρός Ημαθίας"                        ,"﻿Αγία Βαρβάρα Ηρακλείου", "Άγιοι Δέκα", "Άγιος Μύρωνας", "Αηδονοχώρι", "Άνω Βιάννος", "Αρκαλοχώρι", "Αρχάνες", "Ασήμι", "Βασιλείες", "Γάζι", "Γούρνες Πεδιάδος", "Δάφνες", "Έμπαρος", "Επισκοπή Ηρακλείου", "Ζαρός", "Ηράκλειο", "Καρτερός Ελαίας", "Καστέλλι", "Κάτω Γούβες", "Κνωσσός", "Κουτουλουφάρι", "Κρουσώνας", "Λιμένας Χερσονήσου", "Μαλάδες", "Μάλια", "Μοίρες", "Μοχός", "Νέα Αλικαρνασσός", "Νέο Στάδιο", "Πόρος Ηρακλείου", "Πύργος Μονοφατσίου", "Σταλίδα", "Τυμπάκι", "Φοινικιά"    ,"﻿Γλυκή", "Γραικοχώρι", "Ηγουμενίτσα", "Λεπτοκαρυά Θεσπρωτίας", "Μαργαρίτι", "Μόρφιο", "Νέα Σελεύκεια", "Παραμυθιά", "Φιλιάτες"                             ,"﻿Αγγελοχώρι", "Αγία Τριάδα", "Άγιος Αθανάσιος", "Άγιος Παύλος", "Αγχίαλος", "Άδενδρο", "Αμπελόκηποι", "Ανατολικό", "Άνω Περαία", "Ασβεστοχώρι", "Ασκός", "Ασπροβάλτα", "Άσσηρος", "Βαθύλακκος", "Βασιλικά", "Βιομηχανική Περιοχή Θεσσαλονίκης", "Γαλήνη Θεσσαλονίκης", "Γέφυρα Θεσσαλονίκης", "Διαβατά", "Δρυμός", "Ελευθέριο", "Εξοχή", "Επανομή", "Ευκαρπία", "Εύοσμος", "Ζαγκλιβέρι", "Θέρμη", "Θεσσαλονίκη", "Ιωνία Θεσσαλονίκης", "Καλαμαριά", "Καλοχώρι", "Καρδιά", "Κάτω Σχολάρι", "Κουφάλια", "Κύμινα", "Λαγκαδάς", "Λαγκαδίκια", "Λητή", "Μελισσοχώρι", "Μενεμένη", "Νέα Απολλωνία", "Νέα Κερασιά", "Νέα Μαγνησία", "Νέα Μηχανιώνα", "Νέα Ραιδεστός", "Νεάπολη Θεσσαλονίκης", "Νέο Ρύσιο", "Νέοι Επιβάτες", "Νικόπολη", "Ξυλόπολη", "Πανόραμα", "Περαία", "Πεύκα Ρετζίκι", "Πλαγιάρι", "Πολίχνη", "Πυλαία", "Σίνδος", "Σουρωτή", "Σοχός", "Σταυρός", "Σταυρούπολη", "Συκιές", "Ταγαράδες", "Τριάδι Θέρμης", "Τριανδρία", "Τρίλοφος", "Φίλυρο", "Χαλάστρα", "Χαλκηδόνα", "Χορτιάτης", "Ωραιόκαστρο",                                                                                          "﻿Αμπελοχώρι", "Ανατολή", "Ασπράγγελοι", "Βαλανιδιά", "Βροσίνα", "Βρυσούλα", "Γρεβενίτιο", "Δελβινάκι", "Δερβίζιανα", "Δολιανά", "Ελεούσα", "Ζίτσα", "Ιωάννινα", "Καλέντζι", "Καρδαμίτσια", "Κατσικάς", "Κεφαλόβρυσο", "Κόνιτσα", "Κουκλέσι", "Μέτσοβο", "Μπάφρα", "Παλαιοσέλι", "Πεδινή", "Πέραμα Ιωαννίνων", "Περίβλεπτος", "Πράμαντα", "Πυρσόγιαννη", "Σεριζιανά", "Σταυράκι", "Τσεπέλοβο"        ,"﻿Αμυγδαλεώνας", "Ελευθερούπολη", "Ζυγός", "Θάσος", "Θεολόγος", "Καβάλα", "Κεραμωτή", "Κεχρόκαμπος", "Κρηνίδες", "Λιμενάρια", "Μουσθένη", "Νέα Ηρακλείτσα", "Νέα Καρβάλη", "Νέα Πέραμος Καβάλας", "Νικήσιανη", "Παλαιό Τσιφλίκι", "Πρίνος", "Χρυσούπολη"                    ,"﻿Αγναντερό", "Ανθηρό", "Βραγκιανά", "Ιτέα Καρδίτσας", "Καρδίτσα", "Καρδιτσομάγουλα", "Λεοντάρι Καρδίτσας", "Μεσενικόλας", "Μουζάκι", "Παλαμάς", "Προάστιο Καρδίτσας", "Ρεντίνα", "Σοφάδες", "Φανάρι"                        ,"﻿Ακρίτες", "Άργος Ορεστικού", "Βίτσι", "Βογατσικό", "Δισπηλιό", "Επταχώρι", "Καστοριά", "Κλεισούρα", "Κλήμα", "Κορησός", "Μακροχώρι", "Μανιάκοι", "Μαυροχώρι", "Μεσοποταμία", "Νέα Λεύκη", "Νεστόριο", "Χλόη"                     ,"﻿Γάιος", "Γουβιά", "Καρουσάδες", "Καστελλάνοι Μέσης", "Κέρκυρα", "Λευκίμμη", "Ποταμός", "Σκριπερό"                              ,"﻿Αγία Ευφημία", "Αργοστόλι", "Βασιλικιάδες", "Ιθάκη", "Ληξούρι", "Πόρος", "Σάμη", "Σταυρός Ιθάκης", "Φισκάρδο"                             ,"﻿Αξιούπολη", "Γουμένισσα", "Ευρωπός", "Κιλκίς", "Μικρόκαμπος", "Μουριές", "Πολύκαστρο", "Ριζανά", "Συνοριακός Σταθμός Ευζώνων", "Χέρσο"                            ,"﻿Αιανή", "Βελβεντός", "Εμπόριο", "Εράτυρα", "Κοζάνη", "Κρόκος", "Νεάπολη Κοζάνης", "Πεντάβρυσος", "Πεντάλοφος", "Προάστιο Κοζάνης", "Πτολεμαίδα", "Πύργοι", "Σέρβια", "Σιάτιστα", "Σκαλοχώρι", "Τσοτύλι"                      ,"﻿Άγιοι Θεόδωροι", "Αθίκια", "Αρχαία Κόρινθος", "Άσσος", "Βέλο", "Βραχάτι", "Γκούρα", "Δερβένι", "Ζευγολατειό", "Ίσθμια", "Καλιάνοι", "Κάτω Άσσος", "Κάτω Διμηνιό", "Κιάτο", "Κοκκώνι", "Κόρινθος", "Λέχαιο", "Λουτράκι", "Νεμέα", "Νεράντζα", "Ξυλόκαστρο", "Περαχώρα", "Περιγιάλι", "Σούλι", "Σοφικό", "Χιλιομόδι"            ,"﻿Αμοργός", "Ανάφη", "Άνδρος", "Αντίπαρος", "Άνω Σύρος", "Γαύριο", "Εμπορειό", "Ερμούπολη", "Θήρα", "Ίος", "Ιουλίδα", "Κίμωλος", "Κόρθιο", "Κορωνίδα", "Κύθνος", "Μήλος", "Μονόλιθος", "Μύκονος", "Νάξος", "Νάουσα Πάρου", "Οία", "Πάνορμος Τήνου", "Πάρος", "Σέριφος", "Σίκινος", "Σίφνος", "Σύρος", "Τάλαντα", "Τήνος", "Φολέγανδρος", "Χαλκείο"       ,"﻿Αρεόπολη", "Βλαχιώτης", "Γεράκι", "Γερολιμένας", "Γύθειο", "Καρυές Λακωνίας", "Καστόρειο", "Κροκεές", "Μολάοι", "Μονεμβασιά", "Νεάπολη Λακωνίας", "Νιάτα", "Ξηροκάμπι", "Παπαδιάνικα", "Σκάλα", "Σπάρτη"                      ,"﻿Αγιά", "Αμπελώνας", "Βερδικούσσα", "Γιάννουλη", "Γόννοι", "Ελασσόνα", "Κρανέα Ελασσόνας", "Λάρισα", "Λιβάδι", "Νίκαια Λάρισας", "Πλατύκαμπος", "Πυργετός", "Σκοπιά Φαρσάλων", "Συκούριο", "Τύρναβος", "Φαλάνη", "Φάρσαλα"                     ,"﻿Άγιος Νικόλαος Λασιθίου", "Άνω Σύμη", "Ελούντα", "Ιεράπετρα", "Κριτσά", "Μάλες", "Νεάπολη Κρήτης", "Σητεία", "Σταυροχώρι", "Τζερμιάδο", "Τουρλωτή", "Φουρνή", "Χανδράς"                         ,"﻿Αγία Παρασκευή Λέσβου", "Αγιάσος", "Άγιος Ευστράτιος", "Άντισσα", "Βαρειά", "Ερεσός", "Καλλιθέα Μύρινας", "Καλλονή", "Μανταμάδος", "Μήθυμνα", "Μούδρος", "Μύρινα", "Μυτιλήνη", "Παππάδος", "Πέραμα Λέσβου", "Πέτρα Λέσβου", "Πλωμάρι", "Πολίχνιτος"                    ,"﻿Βαθύ", "Βασιλική", "Κάλαμος", "Καρυά Λευκάδας", "Λευκάδα", "Νυδρί"                                ,"﻿Αγία Παρασκευή Βόλου", "Άγιος Γεώργιος Ιωλκού", "Αγριά", "Αλμυρός", "Αλόννησος", "Ανάβρα", "Ανακασιά", "Άνω Λεχώνια", "Αργαλαστή", "Αργυρόνησο", "Βελεστίνο", "Βόλος", "Διμήνιο Βόλου", "Ευξεινούπολη", "Ζαγορά", "Καλά Νερά", "Καλλιθέα Μαγνησίας", "Κάτω Λεχώνια", "Κήπια", "Μηλιές", "Μηλίνα", "Νέα Αγχίαλος", "Νέα Ιωνία Βόλου", "Νέες Παγασές", "Πορταριά", "Σκιάθος", "Σκόπελος", "Σούρπη", "Τρίκερι", "Τσαγκαράδα"        ,"﻿Άγιος Νικόλαος Μεσσηνίας", "Ανδρούσα", "Αριστομένης", "Ασπρόχωμα", "Βέργα", "Γαργαλιάνοι", "Διαβολίτσι", "Δώριο", "Θουρία", "Καλαμάτα", "Κάμπος", "Καρδαμύλη", "Κοπανάκι", "Κορώνη", "Κυπαρισσία", "Λογγά", "Μεθώνη Μεσσηνίας", "Μελιγαλάς", "Μεσσήνη", "Παραλία Καλαμάτας", "Πεταλίδι", "Πέτρα", "Πύλος", "Φαρές", "Φιλιατρά", "Χατζής", "Χώρα Τριφυλίας", "Ψάρι"          ,"﻿Άβδηρα", "Γενισέα", "Εύλαλο", "Εχίνος", "Μυρτούσσα", "Ξάνθη", "Πασχαλιά", "Πόρτο Λάγος", "Σταυρούπολη Ξάνθης", "Χρύσα"                            ,"﻿Αγροσυκιά", "Άθυρα", "Αριδαία", "Άρνισσα", "Γιαννιτσά", "Έδεσσα", "Εξαπλάτανος", "Καρυώτισσα", "Κρύα Βρύση", "Πέλλα", "Σκύδρα"                           ,"﻿Άγιος Δημήτριος Πιερίας", "Αιγίνιο", "Αλώνια", "Ανδρομάχη", "Αρωνάς", "Βριά", "Βροντού", "Γανοχώρα", "Ελατοχώρι", "Καλλιθέα Πιερίας", "Καρίτσα", "Κατερίνη", "Κατερινόσκαλα", "Κάτω Μηλιά", "Κίτρος", "Κολινδρός", "Κονταριώτισσα", "Κορινός", "Κούκκος", "Λαγορράχη", "Λεπτοκαρυά", "Λιτόχωρο", "Μακρύγιαλος", "Μεθώνη", "Νέα Έφεσος", "Νέα Τραπεζούντα", "Νέοι Πόροι", "Νεοκαισάρεια", "Νέος Παντελεήμονας", "Ολυμπιακή Ακτή", "Παλαιό Κεραμίδι", "Παραλία Κατερίνης", "Περίσταση", "Πλαταμώνας", "Ρητίνη", "Σβορώνος", "Σφενδάμι" ,"﻿Θεσπρωτικό", "Καναλλάκι", "Κοντάτες", "Λούρος", "Πάργα", "Πρέβεζα", "Φιλιππιάδα"                               ,"﻿Αγία Γαλήνη", "Αμάρι", "Ανώγεια", "Γαράζο", "Επισκοπή Ρεθύμνου", "Μύρθιος", "Πάνορμος", "Πέραμα Ρεθύμνου", "Πλατανές", "Πρινές Ρεθύμνου", "Ρέθυμνο", "Σπήλι"                          ,"﻿Αίγειρος", "Ίασμος", "Κομοτηνή", "Ξυλαγανή", "Οργάνη", "Σάπες"                                ,"﻿Άγιος Κήρυκος", "Βαθύ Σάμου", "Εύδηλος", "Καλάμι", "Καρλόβασι", "Μαραθόκαμπος", "Μυτιληνιοί", "Πυθαγόρειο", "Πύργος Σάμου", "Ράχες", "Σάμος", "Φούρνοι"                          ,"﻿Αλιστράτη", "Δραβήσκος", "Ηράκλεια", "Κάτω Πορόια", "Κεφαλοχώρι", "Λευκώνας", "Μαυροθάλασσα", "Νέα Ζίχνη", "Νέο Πετρίτσι", "Νέος Σκοπός", "Νιγρίτα", "Πεντάπολη", "Πρώτη Σερρών", "Ροδολίβος", "Ροδόπολη", "Σέρρες", "Σιδηρόκαστρο", "Στρυμονικό", "Τερπνή", "Χρυσό"                  ,"﻿Αγιόφυλλο", "Καλαμπάκα", "Καστανιά", "Κηπάκι", "Λεπτοκαρυά Τρικάλων", "Μεσοχώρα", "Μυρόφυλλο", "Οιχαλία", "Πύλη", "Ριζαρειό", "Τρίκαλα", "Φαρκαδώνα"                          ,"﻿Αγία Παρασκευή Φθιώτιδας", "Άγιος Γεώργιος Φθιώτιδας", "Άγιος Κωνσταντίνος Φθιώτιδας", "Αμφίκλεια", "Αταλάντη", "Δομοκός", "Ελάτεια", "Καμμένα Βούρλα", "Κάτω Τιθορέα", "Λαμία", "Λάρυμνα", "Λιβανάτες", "Μακρακώμη", "Μαλεσίνα", "Μαρτίνο", "Μεγάλη Βρύση", "Μπράλος", "Μώλος", "Πελασγία", "Ροδίτσα", "Σπερχειάδα", "Στυλίδα", "Υπάτη"               ,"﻿Άγιος Γερμανός", "Αμύνταιο", "Αντάρτικο", "Βαρικό", "Βεύη", "Λέχοβο", "Μελίτη", "Ξινό Νερό", "Σκλήθρο", "Φιλώτας", "Φλώρινα"                           ,"﻿Άμφισσα", "Γαλαξίδι", "Γραβιά", "Δελφοί", "Δεσφίνα", "Ερατεινή", "Ευπάλιο", "Ιτέα Φωκίδας", "Κίρρα", "Κροκύλειο", "Λιδορίκι", "Μαυρολιθάρι"                          ,"﻿Άγιος Νικόλαος Χαλκιδικής", "Αρναία", "Βεργιά", "Γαλάτιστα", "Γερακινή", "Δάφνη Αγίου Όρους", "Ιερισσός", "Καρυές", "Κασσάνδρεια", "Κρήμνη", "Μεγάλη Παναγιά", "Νέα Ηράκλεια", "Νέα Καλλικράτεια", "Νέα Μουδανιά", "Νέα Πλάγια", "Νέα Ποτείδαια", "Νέα Τρίγλια", "Νέος Μαρμαράς", "Νικήτη", "Ολυμπιάδα", "Ορμύλια", "Πευκοχώρι", "Πολύγυρος", "Συκιά", "Σωζόπολη", "Φλογητά", "Χανιώτης"           ,"﻿Άγιος Ιωάννης Χανίων", "Αλικιανός", "Βαθή Χανίων", "Βαμβακόπουλο", "Βάμος", "Βουκολιές", "Βρύσες", "Γαλατάς", "Δαράτσος", "Καλύβες", "Κάνδανος", "Κάτω Γαλατάς", "Κάτω Δαράτσος", "Κίσσαμος", "Κόκκινο Μετόχι Χανίων", "Κολυμβάρι", "Κορακιές", "Κουνουπιδιανά", "Μουρνιές", "Νεροκούρος", "Παζινός", "Παλαιοχώρα", "Περιβόλια Κυδωνίας", "Πλατανιάς", "Ροδοβάνι", "Σούδα", "Τσικαλαριά", "Χανιά", "Χώρα Σφακίων"         ,"﻿Βολισσός", "Βροντάδος", "Θυμιανά", "Καλαμωτή", "Καλλιμασιά", "Καρδάμυλα", "Οινούσσες", "Χίος", "Ψαρά"};
	private String username1;
	private Socket clientSocket1;
	private Socket nsock1;
	private int games1;
	
	private String username2;
	private Socket clientSocket2;
	private Socket nsock2;
	private int games2;
	
	Player player1,p1;
	Player player2,p2;
	
	int ms1; //ta dio ms
	int ms2;
	
	long score1; //i diafora metaksi ton dio ms
	long score2;
	
	int finalScore1; //prostheteoi pontoi sto hs
	int finalScore2;
	
	boolean newfastest1 =false;
	boolean newfastest2 = false;
	
	InGame(Player p1, Player p2){
		player1 = p1;
		player2 = p2;
		
		games1=player1.games;
		username1 = player1.username;
		clientSocket1 = player1.clientSocket;
		nsock1 = player1.nSocket;
		
		games2=player2.games;
		username2 = player2.username;
		clientSocket2 = player2.clientSocket;
		nsock2 = player2.nSocket;
		start();
	}
	
	
	public void run(){
		ServerGui.increaseIngame();
		
		//game in progress
		String message1 = "";
		try{
			InputStreamReader inputStreamReader1 = new InputStreamReader(clientSocket1.getInputStream());
			BufferedReader br1= new BufferedReader(inputStreamReader1); // get the client message
			message1 = br1.readLine();
		} catch (IOException ex) {
			System.out.println(username1+" left the game");
			message1=null;
		}
		String message2 = "";
		try{
			InputStreamReader inputStreamReader2 = new InputStreamReader(clientSocket2.getInputStream());
			BufferedReader br2= new BufferedReader(inputStreamReader2); // get the client message
			message2 = br2.readLine();
		} catch (IOException ex) {
			System.out.println(username2+" left the game");
			message2=null;
		}
		System.out.println(message1 +" "+ message2);
		PrintWriter pw3 = null;
		PrintWriter pw4 = null;
		try {
			pw3 = new PrintWriter(new OutputStreamWriter(clientSocket1.getOutputStream(), StandardCharsets.UTF_8), true);
			pw4 = new PrintWriter(new OutputStreamWriter(clientSocket2.getOutputStream(), StandardCharsets.UTF_8), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.toString(),e);
			
		}
		//game ended
		Matchmaking.onlinePlayers.remove(player1.username);
		Matchmaking.onlinePlayers.remove(player2.username);
		if(message1==null && message2==null){
			//do nothing both dced
		}
		else if(message1==null || Integer.parseInt(message1)==-1){ //player 1 dced
			if(player2.winsRow==2){
				//TODO
			}else if(player2.winsRow==4){
				//TODO
			}else if(player2.winsRow==9){
				//TODO
			}
			if(Integer.parseInt(message2)<player2.fastestTime){
				finalScore2 = 200;
				player2.d.update(player2,200,games2,Integer.parseInt(message2),System.currentTimeMillis());
			}else{
				finalScore2 = 200;
				player2.d.update(player2,200,games2,-1,System.currentTimeMillis());
			}
			finalScore1 = -200;
			player1.d.update(player1, -200, games1-1,-1,System.currentTimeMillis());
			pw4.println("gameresult:"+"Νίκη! Κερδίσατε με σκορ "+Long.parseLong(message2)+"ms. Ο/Η αντίπαλός σας, <font color=purple>"+username1+"</font>, αποσυνδέθηκε.<br>Κερδισμένοι πόντοι:<br><font color=green>200</font>");
			Player p2 =  new Player(username2,clientSocket2,nsock2,true);
			p2.d=player2.d;
			p2.generalTable = player2.generalTable;
			player2.finalize=false;
			p2.nomosTable = player2.nomosTable;
			p2.games = games2++;
			p2.nomos = player2.nomos;
			p2.winsRow=player2.winsRow+1;
			p2.perioxi = player2.perioxi;
			p2.avatar = player2.avatar;
			p2.previousOpponentSocket = clientSocket1;
			p2.friends = player2.friends;
			p2.points = player2.points+finalScore2;
			if(newfastest2){
				p2.fastestTime = ms2;
			}else{
				p2.fastestTime = player2.fastestTime;
			}
			
			//////////////////////////////////////////////////////
			if(!player1.nomos.trim().equalsIgnoreCase(player2.nomos.trim())){
				int nomosIndexp1 = -1;
				int nomosIndexp2 = -1;
				for(int i= 0;i<nomoi.length;i++){
					if(nomoi[i].equalsIgnoreCase(player1.nomos.trim())){
						nomosIndexp1=i;
					}
					if(nomoi[i].equalsIgnoreCase(player2.nomos.trim())){
						nomosIndexp2=i;
					}
				}
				if(System.currentTimeMillis() - player1.lastGameSystemMilis > 86400000){
					LiveRankings.activeTodayNomoi[nomosIndexp1]=LiveRankings.activeTodayNomoi[nomosIndexp1]+1;
				}
				if(System.currentTimeMillis() - player2.lastGameSystemMilis > 86400000){
					LiveRankings.activeTodayNomoi[nomosIndexp2]=LiveRankings.activeTodayNomoi[nomosIndexp2]+1;
				}
				LiveRankings.gamesTodayNomoi[nomosIndexp1]=LiveRankings.gamesTodayNomoi[nomosIndexp1]+1;
				LiveRankings.gamesTodayNomoi[nomosIndexp2]=LiveRankings.gamesTodayNomoi[nomosIndexp2]+1;
				LiveRankings.nikesTodayNomoi[nomosIndexp2]=LiveRankings.nikesTodayNomoi[nomosIndexp2]+1;
				LiveRankings.ittesTodayNomoi[nomosIndexp1]=LiveRankings.ittesTodayNomoi[nomosIndexp1]+1;
			}
			if(!player1.perioxi.trim().equalsIgnoreCase(player2.perioxi.trim())){
				int perioxiIndexp1 = -1;
				int perioxiIndexp2 = -1;
				for(int i= 0;i<perioxes.length;i++){
					if(perioxes[i].equalsIgnoreCase(player1.perioxi.trim())){
						perioxiIndexp1=i;
					}
					if(perioxes[i].equalsIgnoreCase(player2.perioxi.trim())){
						perioxiIndexp2=i;
					}
				}
				if(System.currentTimeMillis() - player1.lastGameSystemMilis > 86400000){
					LiveRankings.activeTodayPerioxes[perioxiIndexp1]=LiveRankings.activeTodayPerioxes[perioxiIndexp1]+1;
				}
				if(System.currentTimeMillis() - player2.lastGameSystemMilis > 86400000){
					LiveRankings.activeTodayPerioxes[perioxiIndexp2]=LiveRankings.activeTodayPerioxes[perioxiIndexp2]+1;
				}
				LiveRankings.gamesTodayPerioxes[perioxiIndexp1]=LiveRankings.gamesTodayPerioxes[perioxiIndexp1]+1;
				LiveRankings.gamesTodayPerioxes[perioxiIndexp2]=LiveRankings.gamesTodayPerioxes[perioxiIndexp2]+1;
				LiveRankings.nikesTodayPerioxes[perioxiIndexp2]=LiveRankings.nikesTodayPerioxes[perioxiIndexp2]+1;
				LiveRankings.ittesTodayPerioxes[perioxiIndexp1]=LiveRankings.ittesTodayPerioxes[perioxiIndexp1]+1;
			}
			//////////////////////////////////////////////////////
			p2.lastGameSystemMilis = System.currentTimeMillis();
		}else if (message2==null || Integer.parseInt(message2)==-1){ //player 2 dced
			if(player1.winsRow==2){
				//TODO
			}else if(player1.winsRow==4){
				//TODO
			}else if(player1.winsRow==9){
				//TODO
			}
			if(Integer.parseInt(message1)<player1.fastestTime){
				finalScore1=200;
				player1.d.update(player1,200,games1,Integer.parseInt(message1),System.currentTimeMillis());
			}else{
				finalScore1=200;
				player1.d.update(player1,200,games1,-1,System.currentTimeMillis());
			}
			finalScore2=-200;
			player2.d.update(player2, -200, games2-1,-1,System.currentTimeMillis());
			pw3.println("gameresult:"+"Νίκη! Κερδίσατε με σκορ "+Long.parseLong(message1)+"ms. Ο/Η αντίπαλός σας, <font color=purple>"+username2+"</font>, αποσυνδέθηκε.<br>Κερδισμένοι πόντοι:<br><font color=green>200</font>");
			Player p1 = new Player(username1,clientSocket1,nsock1,true);
			p1.games = games1++;
			p1.d=player1.d;
			p1.generalTable = player1.generalTable;
			p1.nomosTable = player1.nomosTable;
			p1.winsRow = player1.winsRow+1;
			player1.finalize=false;
			p1.nomos = player1.nomos;
			p1.perioxi = player1.perioxi;
			p1.previousOpponentSocket = clientSocket2;
			p1.avatar = player1.avatar;
			p1.friends = player1.friends;
			p1.points = player1.points+finalScore1;
			
			if(newfastest1){
				p1.fastestTime = ms1;
			}else{
				p1.fastestTime = player1.fastestTime;
			}
			//////////////////////////////////////////////////////
			if(!player1.nomos.trim().equalsIgnoreCase(player2.nomos.trim())){
				int nomosIndexp1 = -1;
				int nomosIndexp2 = -1;
				for(int i= 0;i<nomoi.length;i++){
					if(nomoi[i].equalsIgnoreCase(player1.nomos.trim())){
						nomosIndexp1=i;
					}
					if(nomoi[i].equalsIgnoreCase(player2.nomos.trim())){
						nomosIndexp2=i;
					}
				}
				if(System.currentTimeMillis() - player1.lastGameSystemMilis > 86400000){
					LiveRankings.activeTodayNomoi[nomosIndexp1]=LiveRankings.activeTodayNomoi[nomosIndexp1]+1;
				}
				if(System.currentTimeMillis() - player2.lastGameSystemMilis > 86400000){
					LiveRankings.activeTodayNomoi[nomosIndexp2]=LiveRankings.activeTodayNomoi[nomosIndexp2]+1;
				}
				LiveRankings.gamesTodayNomoi[nomosIndexp1]=LiveRankings.gamesTodayNomoi[nomosIndexp1]+1;
				LiveRankings.gamesTodayNomoi[nomosIndexp2]=LiveRankings.gamesTodayNomoi[nomosIndexp2]+1;
				LiveRankings.nikesTodayNomoi[nomosIndexp1]=LiveRankings.nikesTodayNomoi[nomosIndexp1]+1;
				LiveRankings.ittesTodayNomoi[nomosIndexp2]=LiveRankings.ittesTodayNomoi[nomosIndexp2]+1;
			}
			if(!player1.perioxi.trim().equalsIgnoreCase(player2.perioxi.trim())){
				int perioxiIndexp1 = -1;
				int perioxiIndexp2 = -1;
				for(int i= 0;i<perioxes.length;i++){
					if(perioxes[i].equalsIgnoreCase(player1.perioxi.trim())){
						perioxiIndexp1=i;
					}
					if(perioxes[i].equalsIgnoreCase(player2.perioxi.trim())){
						perioxiIndexp2=i;
					}
				}
				if(System.currentTimeMillis() - player1.lastGameSystemMilis > 86400000){
					LiveRankings.activeTodayPerioxes[perioxiIndexp1]=LiveRankings.activeTodayPerioxes[perioxiIndexp1]+1;
				}
				if(System.currentTimeMillis() - player2.lastGameSystemMilis > 86400000){
					LiveRankings.activeTodayPerioxes[perioxiIndexp2]=LiveRankings.activeTodayPerioxes[perioxiIndexp2]+1;
				}
				LiveRankings.gamesTodayPerioxes[perioxiIndexp1]=LiveRankings.gamesTodayPerioxes[perioxiIndexp1]+1;
				LiveRankings.gamesTodayPerioxes[perioxiIndexp2]=LiveRankings.gamesTodayPerioxes[perioxiIndexp2]+1;
				LiveRankings.nikesTodayPerioxes[perioxiIndexp1]=LiveRankings.nikesTodayPerioxes[perioxiIndexp1]+1;
				LiveRankings.ittesTodayPerioxes[perioxiIndexp2]=LiveRankings.ittesTodayPerioxes[perioxiIndexp2]+1;
			}
			//////////////////////////////////////////////////////
			p1.lastGameSystemMilis = System.currentTimeMillis();

		}else{ //nobody dced
			
			ms1=Integer.parseInt(message1);
			ms2=Integer.parseInt(message2);
			if(Long.parseLong(message1)<Long.parseLong(message2)){	//player 1 wins

				if(player1.winsRow==2){
					//TODO
				}else if(player1.winsRow==4){
					//TODO
				}else if(player1.winsRow==9){
					//TODO
				}
				
				
				score1 = Long.parseLong(message2)-Long.parseLong(message1);
				score2 = Long.parseLong(message1)-Long.parseLong(message2);
				pw3.println("gameresult:"+"Νίκη! Κερδίσατε με σκορ<br><font color=yellow>"+Long.parseLong(message1)+"ms</font><br>Ο/Η αντίπαλός σας, <font color=purple>"+username2+"</font>, έκανε σκορ<br><font color=yellow>"+Long.parseLong(message2)+"ms</font><br>Κερδισμένοι πόντοι:<br><font color =green>"+((score1/10)+100)+"</font><br><i>(Bonus νίκης: 100 πόντοι)</i>");
				pw4.println("gameresult:"+"Ήττα... Χάσατε με σκορ<br><font color=yellow>"+Long.parseLong(message2)+"ms</font><br>Ο/Η αντίπαλός σας, <font color=purple>"+username1+"</font>, έκανε σκορ<br><font color=yellow>"+Long.parseLong(message1)+"ms</font><br>Χαμένοι πόντοι:<br><font color =red>"+(Math.abs(score2)/10)+"</font>");
				if(Integer.parseInt(message1)<player1.fastestTime){
					newfastest1=true;
					finalScore1=(int) ((score1/10)+100);
					player1.d.update(player1, (score1/10)+100,games1,Integer.parseInt(message1),System.currentTimeMillis());
				}else{
					finalScore1=(int) ((score1/10)+100);
					player1.d.update(player1, (score1/10)+100,games1,-1,System.currentTimeMillis());
				}

				if(Integer.parseInt(message2)<player2.fastestTime){
					newfastest2=true;
					finalScore2=(int) (score2/10);
					player2.d.update(player2, score2/10,games2,Integer.parseInt(message2),System.currentTimeMillis());
				}else{
					finalScore2=(int) (score2/10);
					player2.d.update(player2, score2/10, games2, -1,System.currentTimeMillis());
				}
				player1.winsRow++;
				player2.winsRow=0;
				newPlayersBoth();
				//////////////////////////////////////////////////////
				if(!player1.nomos.trim().equalsIgnoreCase(player2.nomos.trim())){
					int nomosIndexp1 = -1;
					int nomosIndexp2 = -1;
					for(int i= 0;i<nomoi.length;i++){
						if(nomoi[i].equalsIgnoreCase(player1.nomos.trim())){
							nomosIndexp1=i;
						}
						if(nomoi[i].equalsIgnoreCase(player2.nomos.trim())){
							nomosIndexp2=i;
						}
					}
					if(System.currentTimeMillis() - player1.lastGameSystemMilis > 86400000){
						LiveRankings.activeTodayNomoi[nomosIndexp1]=LiveRankings.activeTodayNomoi[nomosIndexp1]+1;
					}
					if(System.currentTimeMillis() - player2.lastGameSystemMilis > 86400000){
						LiveRankings.activeTodayNomoi[nomosIndexp2]=LiveRankings.activeTodayNomoi[nomosIndexp2]+1;
					}
					LiveRankings.gamesTodayNomoi[nomosIndexp1]=LiveRankings.gamesTodayNomoi[nomosIndexp1]+1;
					LiveRankings.gamesTodayNomoi[nomosIndexp2]=LiveRankings.gamesTodayNomoi[nomosIndexp2]+1;
					LiveRankings.nikesTodayNomoi[nomosIndexp1]=LiveRankings.nikesTodayNomoi[nomosIndexp1]+1;
					LiveRankings.ittesTodayNomoi[nomosIndexp2]=LiveRankings.ittesTodayNomoi[nomosIndexp2]+1;
				}
				if(!player1.perioxi.trim().equalsIgnoreCase(player2.perioxi.trim())){
					int perioxiIndexp1 = -1;
					int perioxiIndexp2 = -1;
					for(int i= 0;i<perioxes.length;i++){
						if(perioxes[i].equalsIgnoreCase(player1.perioxi.trim())){
							perioxiIndexp1=i;
						}
						if(perioxes[i].equalsIgnoreCase(player2.perioxi.trim())){
							perioxiIndexp2=i;
						}
					}
					if(System.currentTimeMillis() - player1.lastGameSystemMilis > 86400000){
						LiveRankings.activeTodayPerioxes[perioxiIndexp1]=LiveRankings.activeTodayPerioxes[perioxiIndexp1]+1;
					}
					if(System.currentTimeMillis() - player2.lastGameSystemMilis > 86400000){
						LiveRankings.activeTodayPerioxes[perioxiIndexp2]=LiveRankings.activeTodayPerioxes[perioxiIndexp2]+1;
					}
					LiveRankings.gamesTodayPerioxes[perioxiIndexp1]=LiveRankings.gamesTodayPerioxes[perioxiIndexp1]+1;
					LiveRankings.gamesTodayPerioxes[perioxiIndexp2]=LiveRankings.gamesTodayPerioxes[perioxiIndexp2]+1;
					LiveRankings.nikesTodayPerioxes[perioxiIndexp1]=LiveRankings.nikesTodayPerioxes[perioxiIndexp1]+1;
					LiveRankings.ittesTodayPerioxes[perioxiIndexp2]=LiveRankings.ittesTodayPerioxes[perioxiIndexp2]+1;
				}
				//////////////////////////////////////////////////////
				p1.lastGameSystemMilis = System.currentTimeMillis();
				p2.lastGameSystemMilis = System.currentTimeMillis();

			}else if(Long.parseLong(message2)<Long.parseLong(message1)){ //player 2 wins
				
				if(player2.winsRow==2){
					//TODO
				}else if(player2.winsRow==4){
					//TODO
				}else if(player2.winsRow==9){
					//TODO
				}
				
				
				score1 = Long.parseLong(message2)-Long.parseLong(message1);
				score2 = Long.parseLong(message1)-Long.parseLong(message2);
				pw3.println("gameresult:"+"Ήττα... Χάσατε με σκορ<br><font color=yellow>"+Long.parseLong(message1)+"ms</font><br>Ο/Η αντίπαλός σας, <font color=purple>"+username2+"</font>, έκανε σκορ<br><font color=yellow>"+Long.parseLong(message2)+"ms</font><br>Χαμένοι πόντοι:<br><font color =red>"+(Math.abs(score1)/10)+"</font>");
				pw4.println("gameresult:"+"Νίκη! Κερδίσατε με σκορ<br><font color=yellow>"+Long.parseLong(message2)+"ms</font><br>Ο/Η αντίπαλός σας, <font color=purple>"+username1+"</font>, έκανε σκορ<br><font color=yellow>"+Long.parseLong(message1)+"ms</font><br>Κερδισμένοι πόντοι:<br><font color =green>"+((score2/10)+100)+"</font><br><i>(Bonus νίκης: 100 πόντοι)</i>");
				if(Integer.parseInt(message1)<player1.fastestTime){
					newfastest1=true;
					finalScore1=(int) (score1/10);
					player1.d.update(player1, score1/10,games1,Integer.parseInt(message1),System.currentTimeMillis());
				}else{
					finalScore1=(int) (score1/10);
					player1.d.update(player1, score1/10,games1,-1,System.currentTimeMillis());
				}

				if(Integer.parseInt(message2)<player2.fastestTime){
					newfastest2=true;
					finalScore2=(int) ((score2/10)+100);
					player2.d.update(player2, (score2/10)+100,games2,Integer.parseInt(message2),System.currentTimeMillis());
				}else{
					finalScore2=(int) ((score2/10)+100);
					player2.d.update(player2, (score2/10)+100, games2, -1,System.currentTimeMillis());
				}
				player2.winsRow++;
				player1.winsRow=0;
				newPlayersBoth();
				//////////////////////////////////////////////////////
				if(!player1.nomos.trim().equalsIgnoreCase(player2.nomos.trim())){
					int nomosIndexp1 = -1;
					int nomosIndexp2 = -1;
					for(int i= 0;i<nomoi.length;i++){
						if(nomoi[i].equalsIgnoreCase(player1.nomos.trim())){
							nomosIndexp1=i;
						}
						if(nomoi[i].equalsIgnoreCase(player2.nomos.trim())){
							nomosIndexp2=i;
						}
					}
					if(System.currentTimeMillis() - player1.lastGameSystemMilis > 86400000){
						LiveRankings.activeTodayNomoi[nomosIndexp1]=LiveRankings.activeTodayNomoi[nomosIndexp1]+1;
					}
					if(System.currentTimeMillis() - player2.lastGameSystemMilis > 86400000){
						LiveRankings.activeTodayNomoi[nomosIndexp2]=LiveRankings.activeTodayNomoi[nomosIndexp2]+1;
					}
					LiveRankings.gamesTodayNomoi[nomosIndexp1]=LiveRankings.gamesTodayNomoi[nomosIndexp1]+1;
					LiveRankings.gamesTodayNomoi[nomosIndexp2]=LiveRankings.gamesTodayNomoi[nomosIndexp2]+1;
					LiveRankings.nikesTodayNomoi[nomosIndexp2]=LiveRankings.nikesTodayNomoi[nomosIndexp2]+1;
					LiveRankings.ittesTodayNomoi[nomosIndexp1]=LiveRankings.ittesTodayNomoi[nomosIndexp1]+1;
				}
				if(!player1.perioxi.trim().equalsIgnoreCase(player2.perioxi.trim())){
					int perioxiIndexp1 = -1;
					int perioxiIndexp2 = -1;
					for(int i= 0;i<perioxes.length;i++){
						if(perioxes[i].equalsIgnoreCase(player1.perioxi.trim())){
							perioxiIndexp1=i;
						}
						if(perioxes[i].equalsIgnoreCase(player2.perioxi.trim())){
							perioxiIndexp2=i;
						}
					}
					if(System.currentTimeMillis() - player1.lastGameSystemMilis > 86400000){
						LiveRankings.activeTodayPerioxes[perioxiIndexp1]=LiveRankings.activeTodayPerioxes[perioxiIndexp1]+1;
					}
					if(System.currentTimeMillis() - player2.lastGameSystemMilis > 86400000){
						LiveRankings.activeTodayPerioxes[perioxiIndexp2]=LiveRankings.activeTodayPerioxes[perioxiIndexp2]+1;
					}
					LiveRankings.gamesTodayPerioxes[perioxiIndexp1]=LiveRankings.gamesTodayPerioxes[perioxiIndexp1]+1;
					LiveRankings.gamesTodayPerioxes[perioxiIndexp2]=LiveRankings.gamesTodayPerioxes[perioxiIndexp2]+1;
					LiveRankings.nikesTodayPerioxes[perioxiIndexp2]=LiveRankings.nikesTodayPerioxes[perioxiIndexp2]+1;
					LiveRankings.ittesTodayPerioxes[perioxiIndexp1]=LiveRankings.ittesTodayPerioxes[perioxiIndexp1]+1;
				}
				//////////////////////////////////////////////////////
				p1.lastGameSystemMilis = System.currentTimeMillis();
				p2.lastGameSystemMilis = System.currentTimeMillis();
			}else{ //TIE!!!
				pw3.println("gameresult:"+"Ισοπαλία! Και οι δύο κάνατε σκορ<br><font color=yellow>"+Long.parseLong(message2)+"ms</font><br>Απίστευτο! Κερδίζετε <br><font color=green>5.000</font><br> πόντους για την τύχη σας!");
				pw4.println("gameresult:"+"Ισοπαλία! Και οι δύο κάνατε σκορ<br><font color=yellow>"+Long.parseLong(message2)+"ms</font><br>Απίστευτο! Κερδίζετε <br><font color=green>5.000</font><br> πόντους για την τύχη σας!");
				
				if(Integer.parseInt(message1)<player1.fastestTime){
					newfastest1=true;
					finalScore1=5000;
					player1.d.update(player1, 5000,games1,Integer.parseInt(message1),System.currentTimeMillis());
				}else{
					finalScore1=5000;
					player1.d.update(player1, 5000,games1,-1,System.currentTimeMillis());
				}

				if(Integer.parseInt(message2)<player2.fastestTime){
					newfastest2=true;
					finalScore2=5000;
					player2.d.update(player2, 5000,games2,Integer.parseInt(message2),System.currentTimeMillis());
				}else{
					finalScore2=5000;
					player2.d.update(player2, 5000, games2, -1,System.currentTimeMillis());
				}
				newPlayersBoth();
				//////////////////////////////////////////////////////
				if(!player1.nomos.trim().equalsIgnoreCase(player2.nomos.trim())){
					int nomosIndexp1 = -1;
					int nomosIndexp2 = -1;
					for(int i= 0;i<nomoi.length;i++){
						if(nomoi[i].equalsIgnoreCase(player1.nomos.trim())){
							nomosIndexp1=i;
						}
						if(nomoi[i].equalsIgnoreCase(player2.nomos.trim())){
							nomosIndexp2=i;
						}
					}
					if(System.currentTimeMillis() - player1.lastGameSystemMilis > 86400000){
						LiveRankings.activeTodayNomoi[nomosIndexp1]=LiveRankings.activeTodayNomoi[nomosIndexp1]+1;
					}
					if(System.currentTimeMillis() - player2.lastGameSystemMilis > 86400000){
						LiveRankings.activeTodayNomoi[nomosIndexp2]=LiveRankings.activeTodayNomoi[nomosIndexp2]+1;
					}
					LiveRankings.gamesTodayNomoi[nomosIndexp1]=LiveRankings.gamesTodayNomoi[nomosIndexp1]+1;
					LiveRankings.gamesTodayNomoi[nomosIndexp2]=LiveRankings.gamesTodayNomoi[nomosIndexp2]+1;
					LiveRankings.nikesTodayNomoi[nomosIndexp1]=LiveRankings.nikesTodayNomoi[nomosIndexp1]+1;
					LiveRankings.nikesTodayNomoi[nomosIndexp2]=LiveRankings.nikesTodayNomoi[nomosIndexp2]+1;
				}
				if(!player1.perioxi.trim().equalsIgnoreCase(player2.perioxi.trim())){
					int perioxiIndexp1 = -1;
					int perioxiIndexp2 = -1;
					for(int i= 0;i<perioxes.length;i++){
						if(perioxes[i].equalsIgnoreCase(player1.perioxi.trim())){
							perioxiIndexp1=i;
						}
						if(perioxes[i].equalsIgnoreCase(player2.perioxi.trim())){
							perioxiIndexp2=i;
						}
					}
					if(System.currentTimeMillis() - player1.lastGameSystemMilis > 86400000){
						LiveRankings.activeTodayPerioxes[perioxiIndexp1]=LiveRankings.activeTodayPerioxes[perioxiIndexp1]+1;
					}
					if(System.currentTimeMillis() - player2.lastGameSystemMilis > 86400000){
						LiveRankings.activeTodayPerioxes[perioxiIndexp2]=LiveRankings.activeTodayPerioxes[perioxiIndexp2]+1;
					}
					LiveRankings.gamesTodayPerioxes[perioxiIndexp1]=LiveRankings.gamesTodayPerioxes[perioxiIndexp1]+1;
					LiveRankings.gamesTodayPerioxes[perioxiIndexp2]=LiveRankings.gamesTodayPerioxes[perioxiIndexp2]+1;
					LiveRankings.nikesTodayPerioxes[perioxiIndexp1]=LiveRankings.nikesTodayPerioxes[perioxiIndexp1]+1;
					LiveRankings.nikesTodayPerioxes[perioxiIndexp2]=LiveRankings.nikesTodayPerioxes[perioxiIndexp2]+1;
				}
				//////////////////////////////////////////////////////
				p1.lastGameSystemMilis = System.currentTimeMillis();
				p2.lastGameSystemMilis = System.currentTimeMillis();
			}
			
//
		}
		ServerGui.decreaseIngame();
		player1=null;
		player2=null;
	}
	private void newPlayersBoth(){
		p1 = new Player(username1,clientSocket1,nsock1,true);
		p1.games = games1+1;
		p1.nomos = player1.nomos;
		p1.d=player1.d;
		p1.winsRow = player1.winsRow;
		p1.generalTable = player1.generalTable;
		p1.nomosTable = player1.nomosTable;
		p1.perioxi = player1.perioxi;
		p1.avatar = player1.avatar;
		p1.friends = player1.friends;
		p1.points = player1.points+finalScore1;
		p1.previousOpponentSocket = clientSocket2;
		player1.finalize=false;
		if(newfastest1){
			p1.fastestTime = ms1;
		}else{
			p1.fastestTime = player1.fastestTime;
		}
	
		p2 =  new Player(username2,clientSocket2,nsock2,true);
		p2.games = games2+1;
		p2.d=player2.d;
		p2.generalTable = player2.generalTable;
		p2.nomosTable = player2.nomosTable;
		p2.winsRow=player2.winsRow;
		p2.nomos = player2.nomos;
		p2.perioxi = player2.perioxi;
		player2.finalize=false;
		p2.avatar = player2.avatar;
		p2.friends = player2.friends;
		p2.points = player2.points+finalScore2;
		p2.previousOpponentSocket = clientSocket1;
		
		if(newfastest2){
			p2.fastestTime = ms2;
		}else{
			p2.fastestTime = player2.fastestTime;
		}
		
		p1.previousOpponent = p2;
		p2.previousOpponent = p1;


	}

}
