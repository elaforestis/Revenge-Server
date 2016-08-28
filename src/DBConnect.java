import java.io.ByteArrayInputStream;
import java.net.Socket;
import java.sql.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnect{
	static final Logger log = Logger.getLogger( DBConnect.class.getName() );
	public static final String DBPass = "CJTZeKvy8Gmyd7Sz";
	public static final String rootPass = "jmeZt5BX3MDSh82a";
	public final String[] nomoi = {"Αιτωλοακαρνανίας","Αργολίδας","Αρκαδίας","Άρτας","Αττικής","Αχαΐας","Βοιωτίας","Γρεβενών","Δράμας","Δωδεκανήσου","Εβοίας","Έβρου","Ευρυτανίας","Ζακύνθου","Ηλείας","Ημαθίας","Ηρακλείου","Θεσπρωτίας","Θεσσαλονίκης","Ιωαννίνων","Καβάλας","Καρδίτσας","Καστοριάς","Κέρκυρας","Κεφαλληνίας","Κιλκίς","Κοζάνης","Κορινθίας","Κυκλάδων","Λακωνίας","Λάρισας","Λασιθίου","Λέσβου","Λευκάδας","Μαγνησίας","Μεσσηνίας","Ξάνθης","Πέλλας","Πιερίας","Πρέβεζας","Ρεθύμνου","Ροδόπης","Σάμου","Σερρών","Τρικάλων","Φθιώτιδας","Φλώρινας","Φωκίδας","Χαλκιδικής","Χανίων","Χίου"};
	public final String[] perioxes = {"﻿Άγιος Ανδρέας Γαβαλού", "Άγιος Βλάσιος", "Άγιος Κωνσταντίνος", "Αγρίνιο", "Αιτωλικό", "Άκτιο", "Αμφιλοχία", "Αντίρριο", "Άνω Χώρα", "Αστακός", "Βόνιτσα", "Γαβαλού", "Γραμμένη Οξυά", "Δοκίμιο", "Εμπεσός", "Ευηνοχώρι", "Θέρμο", "Καινούργιο", "Κατούνα", "Κατοχή", "Κλεπά", "Ματαράγκα", "Μεγάλη Χώρα", "Μενίδι", "Μεσολόγγι", "Μύτικας", "Ναύπακτος", "Νέα Αβόρανη", "Νεοχώρι Αιτωλοακαρνανίας", "Πάλαιρος", "Παναιτώλιο", "Παραβόλα", "Πλάτανος Ναυπάκτου", "Σίμος", "Στράτος", "Τερψιθέα", "Φυτείες", "Χρυσοπηγή","﻿Αγία Τριάδα Αργολίδας", "Αλέα", "Άργος", "Αχλαδόκαμπος", "Δρέπανο Αργολίδας", "Ερμιόνη", "Κουτσοπόδι", "Κρανίδι", "Λυγουριό", "Ναύπλιο", "Νέα Επίδαυρος", "Νέα Κίος", "Παλαιά Επίδαυρος", "Τολό"                        ,"﻿Άγιος Ανδρέας", "Άγιος Νικόλαος Αρκαδίας", "Άγιος Πέτρος", "Άστρος", "Βαλτεσινίκο", "Βυτίνα", "Δημητσάνα", "Ελαιοχώρι", "Κανδήλα", "Καρίταινα", "Καστρί", "Κάτω Ασέα", "Κοντοβάζαινα", "Κοσμάς", "Κοτύλιο", "Λαγκάδια", "Λεβίδι", "Λεοντάρι Αρκαδίας", "Λεωνίδι", "Μεγαλόπολη", "Νεστάνη", "Παλούμπα", "Παράλιο Άστρος", "Πουρναριά", "Στάδιο", "Τρίπολη", "Τρόπαια", "Τυρός"          ,"﻿Άγναντα", "Αμμότοπος", "Άνω Καλεντίνη", "Άρτα", "Βουργαρέλι", "Δημάρι", "Κομπότι", "Μελισσουργοί", "Μηλιανά", "Νεοχώρι Άρτας", "Πέτα", "Ροδαυγή", "Φιλοθέη Άρτας", "Χαλκιάδες"                        ,"﻿Αγία Βαρβάρα", "Αγία Κυριακή", "Αγία Μαρίνα Αίγινας", "Αγία Μαρίνα Γραμματικού", "Αγία Μαρίνα Κορωπίου", "Αγία Μαρίνα Νέας Μάκρης", "Αγία Παρασκευή", "Άγιοι Ανάργυροι", "Άγιοι Απόστολοι", "Άγιος Δημήτριος", "Άγιος Ιωάννης Ρέντης", "Άγιος Νικόλαος Αναβύσσου", "Άγιος Νικόλαος Λούτσας", "Άγιος Παντελεήμονας", "Άγιος Σεραφείμ", "Άγιος Στέφανος", "Αθήνα", "Αιάντειο", "Αιγάλεω", "Αίγινα", "Αλεποχώρι", "Άλιμος", "Αμπελάκια", "Ανάβυσσος", "Ανθούσα", "Άνοιξη", "Άνω Λιόσια", "Άνω Νέα Παλάτια", "Αργυρούπολη", "Αρτέμιδα", "Ασπρόπυργος", "Αυλάκι", "Αυλώνα", "Αφίδνες", "Αχαρνές", "Βάρη", "Βαρνάβας", "Βίλια", "Βούλα", "Βουλιαγμένη", "Βραυρώνα", "Βριλήσσια", "Βύρωνας", "Γαλάζια Ακτή", "Γαλατάς Αττικής", "Γαλάτσι", "Γέρακας", "Γλυκά Νερά", "Γλυφάδα", "Γραμματικό", "Δασκαλειό", "Δάφνη", "Διόνυσος", "Δραπετσώνα", "Δροσιά", "Εκάλη", "Ελευσίνα", "Ελληνικό", "Ερυθρές", "Ζεφύρι", "Ζούμπερι", "Ζωγράφος", "Ηλιούπολη", "Θρακομακεδόνες", "Θυμάρι", "Ίλιον", "Καισαριανή", "Κάλαμος Αττικής", "Καλέτζι", "Καλλιθέα", "Καλλιτεχνούπολη", "Καλύβια Θορικού", "Καματερό", "Κάντζα", "Καπανδρίτι", "Κάτω Σούλι", "Κερατέα", "Κερατσίνι", "Κηφισιά", "Κινέτα", "Κόκκινο Λιμανάκι", "Κορυδαλλός", "Κορωπί", "Κουβαράς", "Κρυονέρι", "Κύθηρα", "Κυψέλη Αίγινας", "Λαγονήσι", "Λάκκα", "Λαύριο", "Λουτρόπυργος", "Λυκόβρυση", "Μαγούλα", "Μάνδρα", "Μαραθώνας", "Μαρκόπουλο", "Μαρούσι", "Μάτι", "Μέγαρα", "Μέθανα", "Μελίσσια", "Μεσαγρός", "Μεταμόρφωση", "Μοσχάτο", "Νέα Ερυθραία", "Νέα Ιωνία", "Νέα Μάκρη", "Νέα Παλάτια", "Νέα Πεντέλη", "Νέα Πέραμος Αττικής", "Νέα Πολιτεία", "Νέα Σμύρνη", "Νέα Φιλαδέλφεια", "Νέα Χαλκηδόνα", "Νέο Ηράκλειο", "Νέο Ψυχικό", "Νέος Βουτζάς", "Νίκαια", "Ντράφι", "Παιανία", "Παλαιά Φώκαια", "Παλαιό Φάληρο", "Παλλήνη", "Παλούκια", "Παπάγος", "Πειραιάς", "Πεντέλη", "Πέραμα", "Πέρδικα", "Περιστέρι", "Πετρούπολη", "Πεύκη", "Πικέρμι", "Πολυδένδρι", "Πόρος Τροιζηνίας", "Πόρτο Ράφτη", "Ποταμός Κυθήρων", "Ραφήνα", "Ροδόπολη Αττικής", "Σαλαμίνα", "Σαρωνίδα", "Σελήνια", "Σκάλα Ωρωπού", "Σουβάλα", "Σπάτα", "Σπέτσες", "Σταμάτα", "Συκάμινο", "Ταύρος", "Ύδρα", "Υμηττός", "Φιλοθέη", "Φυλή", "Χαϊδάρι", "Χαλάνδρι", "Χαλκούτσι", "Χαμολιά", "Χολαργός", "Ψυχικό", "Ωρωπός", "﻿Άβυθος", "Αγία Παρασκευή Αχαΐας", "Άγιος Βασίλειος", "Αιγείρα", "Αίγιο", "Ακράτα", "Ακταίο", "Άραξος", "Αραχωβίτικα", "Αροανία", "Βιομηχανική Περιοχή Πάτρας", "Βραχνέικα", "Δάφνη Αχαΐας", "Δεμένικα Σαραβάλιου", "Διακοπτό", "Δρέπανο Αχαΐας", "Ερυμάνθεια", "Καλάβρυτα", "Καμάρες", "Καμίνια", "Κάτω Αχαία", "Κάτω Καστρίτσι", "Κάτω Οβρυά", "Κλειτορία", "Κουλουράς", "Κράθιο", "Κρήνη Πάτρας", "Λάππα", "Μιντιλόγλι", "Μυρτιά", "Νέο Σούλι", "Οβρυά", "Παραλία Πατρών", "Πάτρα", "Πλάτανος Αχαΐας", "Προφήτης Ηλίας", "Ρίο", "Ροδοδάφνη", "Σαγαίικα", "Σαραβάλι", "Συχαινά", "Τέμενη", "Τερψιθέα Αχαΐας", "Χαλανδρίτσα", "Ψαθόπυργος",                                                                                                                   "﻿Άγιος Γεώργιος Βοιωτίας", "Αλίαρτος", "Αντίκυρα", "Αράχωβα", "Βάγια", "Δαύλεια", "Δήλεσι", "Δίστομο", "Δόμβραινα", "Θήβα", "Κυριάκι", "Λιβαδειά", "Οινόη Βοιωτίας", "Οινόφυτα", "Ορχομενός", "Παραλία Διστόμου", "Παρόριο", "Σχηματάρι", "Τανάγρα"                   ,"﻿Άγιος Γεώργιος", "Γρεβενά", "Δεσκάτη", "Πολυνέρι", "Σκούμτσια"                                 ,"﻿Άγιος Αθανάσιος Δράμας", "Αμπελάκια Δράμας", "Αργυρούπολη Δράμας", "Δοξάτο", "Δράμα", "Καλαμπάκι", "Κάτω Νευροκόπι", "Κύρια", "Μαυρόβατος", "Νέα Αμισός", "Νέα Σεβάστεια", "Νικηφόρος", "Παρανέστι", "Περίχωρα", "Προσοτσάνη", "Σιταγροί", "Φτελιά", "Φωτολίβος", "Χωριστή"                   ,"﻿Αγαθονήσι", "Ανάληψη Αστυπάλαιας", "Αντιμάχεια", "Αρχάγγελος", "Αστυπάλαια", "Αφάντου", "Γεννάδι", "Έμπωνας", "Ιαλυσός", "Καλυθιές", "Κάλυμνος", "Κάρπαθος", "Κάσος", "Κέφαλος Κω", "Κολύμπια", "Κοσκινού", "Κρεμαστή", "Κως", "Λειψοί", "Λέρος", "Λίνδος", "Μαριτσά", "Μεγίστη", "Νίσυρος", "Παλαιά Πόλη", "Παραδείσι", "Παστίδα", "Πάτμος", "Ρόδος", "Σγουρού", "Σορωνή", "Σύμη", "Τήλος", "Φαληράκι", "Χάλκη", "Ψαλίδι"  ,"﻿Αγία Άννα", "Άγιος Νικόλαος Εύβοιας", "Αλιβέρι", "Αμάρυνθος", "Ανθηδώνα", "Αυλωνάρι", "Βαθύ Αυλίδας", "Βασιλικό", "Δροσιά Ευβοίας", "Δύο Δένδρα", "Έξω Παναγίτσα", "Ερέτρια", "Ιστιαία", "Καθενοί", "Κάρυστος", "Κονίστρες", "Κριεζά", "Κύμη", "Λίμνη", "Λουτρά Αιδηψού", "Μαντούδι", "Μαρμάρι", "Μπούρτζι", "Νέα Αρτάκη", "Νέα Λάμψακος", "Οξύλιθος", "Παραλία Αυλίδας", "Πολιτικά", "Σκύρος", "Στενή Δίρφυος", "Στύρα", "Υλίκη", "Φάρος", "Χαλκίδα", "Ψαχνά", "Ωρεοί"  ,"﻿Αλεξανδρούπολη", "Απαλός", "Γέφυρα Κήπων", "Διδυμότειχο", "Δίκαια", "Καστανιές", "Κυπρίνος", "Λάβαρα", "Μαΐστρος", "Μεταξάδες", "Νέα Βύσσα", "Νέα Χιλή", "Ορεστιάδα", "Πέπλος", "Ρίζια", "Σαμοθράκη", "Σουφλί", "Συκορράχη", "Τυχερό", "Φέρες"                  ,"﻿Άγραφα", "Γρανίτσα", "Καρπενήσι", "Κερασοχώρι", "Κρίκελλο", "Μαυρόλογγος", "Προυσός", "Ραπτόπουλο", "Φουρνά"                             ,"﻿Βολίμες", "Γαϊτάνι", "Ζάκυνθος", "Καταστάρι", "Μαχαιράδο", "Τσιλιβί"                                ,"﻿Αμαλιάδα", "Ανδραβίδα", "Ανδρίτσαινα", "Αρχαία Ολυμπία", "Βάρδα", "Βαρθολομιό", "Γαστούνη", "Επιτάλιο", "Εφύρα", "Ζαχάρω", "Καβάσιλας", "Καλλιθέα Ηλείας", "Καράτουλας", "Κατάκολο", "Κρέστενα", "Κυλλήνη", "Λάλας", "Λάμπεια", "Λεχαινά", "Μυρσίνη", "Νέα Φιγαλεία", "Πύργος Ηλείας", "Τραγανό", "Χάβαρι"              ,"﻿Αγγελοχώρι Ημαθίας", "Άγιος Γεώργιος Ημαθίας", "Αλεξάνδρεια", "Βεργίνα", "Βέροια", "Ειρηνούπολη", "Κοπανός", "Λαζοχώρι", "Μακροχώρι Ημαθίας", "Μελίκη", "Νάουσα", "Πατρίδα", "Πλατύ", "Σταυρός Ημαθίας"                        ,"﻿Αγία Βαρβάρα Ηρακλείου", "Άγιοι Δέκα", "Άγιος Μύρωνας", "Αηδονοχώρι", "Άνω Βιάννος", "Αρκαλοχώρι", "Αρχάνες", "Ασήμι", "Βασιλείες", "Γάζι", "Γούρνες Πεδιάδος", "Δάφνες", "Έμπαρος", "Επισκοπή Ηρακλείου", "Ζαρός", "Ηράκλειο", "Καρτερός Ελαίας", "Καστέλλι", "Κάτω Γούβες", "Κνωσσός", "Κουτουλουφάρι", "Κρουσώνας", "Λιμένας Χερσονήσου", "Μαλάδες", "Μάλια", "Μοίρες", "Μοχός", "Νέα Αλικαρνασσός", "Νέο Στάδιο", "Πόρος Ηρακλείου", "Πύργος Μονοφατσίου", "Σταλίδα", "Τυμπάκι", "Φοινικιά"    ,"﻿Γλυκή", "Γραικοχώρι", "Ηγουμενίτσα", "Λεπτοκαρυά Θεσπρωτίας", "Μαργαρίτι", "Μόρφιο", "Νέα Σελεύκεια", "Παραμυθιά", "Φιλιάτες"                             ,"﻿Αγγελοχώρι", "Αγία Τριάδα", "Άγιος Αθανάσιος", "Άγιος Παύλος", "Αγχίαλος", "Άδενδρο", "Αμπελόκηποι", "Ανατολικό", "Άνω Περαία", "Ασβεστοχώρι", "Ασκός", "Ασπροβάλτα", "Άσσηρος", "Βαθύλακκος", "Βασιλικά", "Βιομηχανική Περιοχή Θεσσαλονίκης", "Γαλήνη Θεσσαλονίκης", "Γέφυρα Θεσσαλονίκης", "Διαβατά", "Δρυμός", "Ελευθέριο", "Εξοχή", "Επανομή", "Ευκαρπία", "Εύοσμος", "Ζαγκλιβέρι", "Θέρμη", "Θεσσαλονίκη", "Ιωνία Θεσσαλονίκης", "Καλαμαριά", "Καλοχώρι", "Καρδιά", "Κάτω Σχολάρι", "Κουφάλια", "Κύμινα", "Λαγκαδάς", "Λαγκαδίκια", "Λητή", "Μελισσοχώρι", "Μενεμένη", "Νέα Απολλωνία", "Νέα Κερασιά", "Νέα Μαγνησία", "Νέα Μηχανιώνα", "Νέα Ραιδεστός", "Νεάπολη Θεσσαλονίκης", "Νέο Ρύσιο", "Νέοι Επιβάτες", "Νικόπολη", "Ξυλόπολη", "Πανόραμα", "Περαία", "Πεύκα Ρετζίκι", "Πλαγιάρι", "Πολίχνη", "Πυλαία", "Σίνδος", "Σουρωτή", "Σοχός", "Σταυρός", "Σταυρούπολη", "Συκιές", "Ταγαράδες", "Τριάδι Θέρμης", "Τριανδρία", "Τρίλοφος", "Φίλυρο", "Χαλάστρα", "Χαλκηδόνα", "Χορτιάτης", "Ωραιόκαστρο",                                                                                          "﻿Αμπελοχώρι", "Ανατολή", "Ασπράγγελοι", "Βαλανιδιά", "Βροσίνα", "Βρυσούλα", "Γρεβενίτιο", "Δελβινάκι", "Δερβίζιανα", "Δολιανά", "Ελεούσα", "Ζίτσα", "Ιωάννινα", "Καλέντζι", "Καρδαμίτσια", "Κατσικάς", "Κεφαλόβρυσο", "Κόνιτσα", "Κουκλέσι", "Μέτσοβο", "Μπάφρα", "Παλαιοσέλι", "Πεδινή", "Πέραμα Ιωαννίνων", "Περίβλεπτος", "Πράμαντα", "Πυρσόγιαννη", "Σεριζιανά", "Σταυράκι", "Τσεπέλοβο"        ,"﻿Αμυγδαλεώνας", "Ελευθερούπολη", "Ζυγός", "Θάσος", "Θεολόγος", "Καβάλα", "Κεραμωτή", "Κεχρόκαμπος", "Κρηνίδες", "Λιμενάρια", "Μουσθένη", "Νέα Ηρακλείτσα", "Νέα Καρβάλη", "Νέα Πέραμος Καβάλας", "Νικήσιανη", "Παλαιό Τσιφλίκι", "Πρίνος", "Χρυσούπολη"                    ,"﻿Αγναντερό", "Ανθηρό", "Βραγκιανά", "Ιτέα Καρδίτσας", "Καρδίτσα", "Καρδιτσομάγουλα", "Λεοντάρι Καρδίτσας", "Μεσενικόλας", "Μουζάκι", "Παλαμάς", "Προάστιο Καρδίτσας", "Ρεντίνα", "Σοφάδες", "Φανάρι"                        ,"﻿Ακρίτες", "Άργος Ορεστικού", "Βίτσι", "Βογατσικό", "Δισπηλιό", "Επταχώρι", "Καστοριά", "Κλεισούρα", "Κλήμα", "Κορησός", "Μακροχώρι", "Μανιάκοι", "Μαυροχώρι", "Μεσοποταμία", "Νέα Λεύκη", "Νεστόριο", "Χλόη"                     ,"﻿Γάιος", "Γουβιά", "Καρουσάδες", "Καστελλάνοι Μέσης", "Κέρκυρα", "Λευκίμμη", "Ποταμός", "Σκριπερό"                              ,"﻿Αγία Ευφημία", "Αργοστόλι", "Βασιλικιάδες", "Ιθάκη", "Ληξούρι", "Πόρος", "Σάμη", "Σταυρός Ιθάκης", "Φισκάρδο"                             ,"﻿Αξιούπολη", "Γουμένισσα", "Ευρωπός", "Κιλκίς", "Μικρόκαμπος", "Μουριές", "Πολύκαστρο", "Ριζανά", "Συνοριακός Σταθμός Ευζώνων", "Χέρσο"                            ,"﻿Αιανή", "Βελβεντός", "Εμπόριο", "Εράτυρα", "Κοζάνη", "Κρόκος", "Νεάπολη Κοζάνης", "Πεντάβρυσος", "Πεντάλοφος", "Προάστιο Κοζάνης", "Πτολεμαίδα", "Πύργοι", "Σέρβια", "Σιάτιστα", "Σκαλοχώρι", "Τσοτύλι"                      ,"﻿Άγιοι Θεόδωροι", "Αθίκια", "Αρχαία Κόρινθος", "Άσσος", "Βέλο", "Βραχάτι", "Γκούρα", "Δερβένι", "Ζευγολατειό", "Ίσθμια", "Καλιάνοι", "Κάτω Άσσος", "Κάτω Διμηνιό", "Κιάτο", "Κοκκώνι", "Κόρινθος", "Λέχαιο", "Λουτράκι", "Νεμέα", "Νεράντζα", "Ξυλόκαστρο", "Περαχώρα", "Περιγιάλι", "Σούλι", "Σοφικό", "Χιλιομόδι"            ,"﻿Αμοργός", "Ανάφη", "Άνδρος", "Αντίπαρος", "Άνω Σύρος", "Γαύριο", "Εμπορειό", "Ερμούπολη", "Θήρα", "Ίος", "Ιουλίδα", "Κίμωλος", "Κόρθιο", "Κορωνίδα", "Κύθνος", "Μήλος", "Μονόλιθος", "Μύκονος", "Νάξος", "Νάουσα Πάρου", "Οία", "Πάνορμος Τήνου", "Πάρος", "Σέριφος", "Σίκινος", "Σίφνος", "Σύρος", "Τάλαντα", "Τήνος", "Φολέγανδρος", "Χαλκείο"       ,"﻿Αρεόπολη", "Βλαχιώτης", "Γεράκι", "Γερολιμένας", "Γύθειο", "Καρυές Λακωνίας", "Καστόρειο", "Κροκεές", "Μολάοι", "Μονεμβασιά", "Νεάπολη Λακωνίας", "Νιάτα", "Ξηροκάμπι", "Παπαδιάνικα", "Σκάλα", "Σπάρτη"                      ,"﻿Αγιά", "Αμπελώνας", "Βερδικούσσα", "Γιάννουλη", "Γόννοι", "Ελασσόνα", "Κρανέα Ελασσόνας", "Λάρισα", "Λιβάδι", "Νίκαια Λάρισας", "Πλατύκαμπος", "Πυργετός", "Σκοπιά Φαρσάλων", "Συκούριο", "Τύρναβος", "Φαλάνη", "Φάρσαλα"                     ,"﻿Άγιος Νικόλαος Λασιθίου", "Άνω Σύμη", "Ελούντα", "Ιεράπετρα", "Κριτσά", "Μάλες", "Νεάπολη Κρήτης", "Σητεία", "Σταυροχώρι", "Τζερμιάδο", "Τουρλωτή", "Φουρνή", "Χανδράς"                         ,"﻿Αγία Παρασκευή Λέσβου", "Αγιάσος", "Άγιος Ευστράτιος", "Άντισσα", "Βαρειά", "Ερεσός", "Καλλιθέα Μύρινας", "Καλλονή", "Μανταμάδος", "Μήθυμνα", "Μούδρος", "Μύρινα", "Μυτιλήνη", "Παππάδος", "Πέραμα Λέσβου", "Πέτρα Λέσβου", "Πλωμάρι", "Πολίχνιτος"                    ,"﻿Βαθύ", "Βασιλική", "Κάλαμος", "Καρυά Λευκάδας", "Λευκάδα", "Νυδρί"                                ,"﻿Αγία Παρασκευή Βόλου", "Άγιος Γεώργιος Ιωλκού", "Αγριά", "Αλμυρός", "Αλόννησος", "Ανάβρα", "Ανακασιά", "Άνω Λεχώνια", "Αργαλαστή", "Αργυρόνησο", "Βελεστίνο", "Βόλος", "Διμήνιο Βόλου", "Ευξεινούπολη", "Ζαγορά", "Καλά Νερά", "Καλλιθέα Μαγνησίας", "Κάτω Λεχώνια", "Κήπια", "Μηλιές", "Μηλίνα", "Νέα Αγχίαλος", "Νέα Ιωνία Βόλου", "Νέες Παγασές", "Πορταριά", "Σκιάθος", "Σκόπελος", "Σούρπη", "Τρίκερι", "Τσαγκαράδα"        ,"﻿Άγιος Νικόλαος Μεσσηνίας", "Ανδρούσα", "Αριστομένης", "Ασπρόχωμα", "Βέργα", "Γαργαλιάνοι", "Διαβολίτσι", "Δώριο", "Θουρία", "Καλαμάτα", "Κάμπος", "Καρδαμύλη", "Κοπανάκι", "Κορώνη", "Κυπαρισσία", "Λογγά", "Μεθώνη Μεσσηνίας", "Μελιγαλάς", "Μεσσήνη", "Παραλία Καλαμάτας", "Πεταλίδι", "Πέτρα", "Πύλος", "Φαρές", "Φιλιατρά", "Χατζής", "Χώρα Τριφυλίας", "Ψάρι"          ,"﻿Άβδηρα", "Γενισέα", "Εύλαλο", "Εχίνος", "Μυρτούσσα", "Ξάνθη", "Πασχαλιά", "Πόρτο Λάγος", "Σταυρούπολη Ξάνθης", "Χρύσα"                            ,"﻿Αγροσυκιά", "Άθυρα", "Αριδαία", "Άρνισσα", "Γιαννιτσά", "Έδεσσα", "Εξαπλάτανος", "Καρυώτισσα", "Κρύα Βρύση", "Πέλλα", "Σκύδρα"                           ,"﻿Άγιος Δημήτριος Πιερίας", "Αιγίνιο", "Αλώνια", "Ανδρομάχη", "Αρωνάς", "Βριά", "Βροντού", "Γανοχώρα", "Ελατοχώρι", "Καλλιθέα Πιερίας", "Καρίτσα", "Κατερίνη", "Κατερινόσκαλα", "Κάτω Μηλιά", "Κίτρος", "Κολινδρός", "Κονταριώτισσα", "Κορινός", "Κούκκος", "Λαγορράχη", "Λεπτοκαρυά", "Λιτόχωρο", "Μακρύγιαλος", "Μεθώνη", "Νέα Έφεσος", "Νέα Τραπεζούντα", "Νέοι Πόροι", "Νεοκαισάρεια", "Νέος Παντελεήμονας", "Ολυμπιακή Ακτή", "Παλαιό Κεραμίδι", "Παραλία Κατερίνης", "Περίσταση", "Πλαταμώνας", "Ρητίνη", "Σβορώνος", "Σφενδάμι" ,"﻿Θεσπρωτικό", "Καναλλάκι", "Κοντάτες", "Λούρος", "Πάργα", "Πρέβεζα", "Φιλιππιάδα"                               ,"﻿Αγία Γαλήνη", "Αμάρι", "Ανώγεια", "Γαράζο", "Επισκοπή Ρεθύμνου", "Μύρθιος", "Πάνορμος", "Πέραμα Ρεθύμνου", "Πλατανές", "Πρινές Ρεθύμνου", "Ρέθυμνο", "Σπήλι"                          ,"﻿Αίγειρος", "Ίασμος", "Κομοτηνή", "Ξυλαγανή", "Οργάνη", "Σάπες"                                ,"﻿Άγιος Κήρυκος", "Βαθύ Σάμου", "Εύδηλος", "Καλάμι", "Καρλόβασι", "Μαραθόκαμπος", "Μυτιληνιοί", "Πυθαγόρειο", "Πύργος Σάμου", "Ράχες", "Σάμος", "Φούρνοι"                          ,"﻿Αλιστράτη", "Δραβήσκος", "Ηράκλεια", "Κάτω Πορόια", "Κεφαλοχώρι", "Λευκώνας", "Μαυροθάλασσα", "Νέα Ζίχνη", "Νέο Πετρίτσι", "Νέος Σκοπός", "Νιγρίτα", "Πεντάπολη", "Πρώτη Σερρών", "Ροδολίβος", "Ροδόπολη", "Σέρρες", "Σιδηρόκαστρο", "Στρυμονικό", "Τερπνή", "Χρυσό"                  ,"﻿Αγιόφυλλο", "Καλαμπάκα", "Καστανιά", "Κηπάκι", "Λεπτοκαρυά Τρικάλων", "Μεσοχώρα", "Μυρόφυλλο", "Οιχαλία", "Πύλη", "Ριζαρειό", "Τρίκαλα", "Φαρκαδώνα"                          ,"﻿Αγία Παρασκευή Φθιώτιδας", "Άγιος Γεώργιος Φθιώτιδας", "Άγιος Κωνσταντίνος Φθιώτιδας", "Αμφίκλεια", "Αταλάντη", "Δομοκός", "Ελάτεια", "Καμμένα Βούρλα", "Κάτω Τιθορέα", "Λαμία", "Λάρυμνα", "Λιβανάτες", "Μακρακώμη", "Μαλεσίνα", "Μαρτίνο", "Μεγάλη Βρύση", "Μπράλος", "Μώλος", "Πελασγία", "Ροδίτσα", "Σπερχειάδα", "Στυλίδα", "Υπάτη"               ,"﻿Άγιος Γερμανός", "Αμύνταιο", "Αντάρτικο", "Βαρικό", "Βεύη", "Λέχοβο", "Μελίτη", "Ξινό Νερό", "Σκλήθρο", "Φιλώτας", "Φλώρινα"                           ,"﻿Άμφισσα", "Γαλαξίδι", "Γραβιά", "Δελφοί", "Δεσφίνα", "Ερατεινή", "Ευπάλιο", "Ιτέα Φωκίδας", "Κίρρα", "Κροκύλειο", "Λιδορίκι", "Μαυρολιθάρι"                          ,"﻿Άγιος Νικόλαος Χαλκιδικής", "Αρναία", "Βεργιά", "Γαλάτιστα", "Γερακινή", "Δάφνη Αγίου Όρους", "Ιερισσός", "Καρυές", "Κασσάνδρεια", "Κρήμνη", "Μεγάλη Παναγιά", "Νέα Ηράκλεια", "Νέα Καλλικράτεια", "Νέα Μουδανιά", "Νέα Πλάγια", "Νέα Ποτείδαια", "Νέα Τρίγλια", "Νέος Μαρμαράς", "Νικήτη", "Ολυμπιάδα", "Ορμύλια", "Πευκοχώρι", "Πολύγυρος", "Συκιά", "Σωζόπολη", "Φλογητά", "Χανιώτης"           ,"﻿Άγιος Ιωάννης Χανίων", "Αλικιανός", "Βαθή Χανίων", "Βαμβακόπουλο", "Βάμος", "Βουκολιές", "Βρύσες", "Γαλατάς", "Δαράτσος", "Καλύβες", "Κάνδανος", "Κάτω Γαλατάς", "Κάτω Δαράτσος", "Κίσσαμος", "Κόκκινο Μετόχι Χανίων", "Κολυμβάρι", "Κορακιές", "Κουνουπιδιανά", "Μουρνιές", "Νεροκούρος", "Παζινός", "Παλαιοχώρα", "Περιβόλια Κυδωνίας", "Πλατανιάς", "Ροδοβάνι", "Σούδα", "Τσικαλαριά", "Χανιά", "Χώρα Σφακίων"         ,"﻿Βολισσός", "Βροντάδος", "Θυμιανά", "Καλαμωτή", "Καλλιμασιά", "Καρδάμυλα", "Οινούσσες", "Χίος", "Ψαρά"};
	String who;
	Connection conGeneral;
	String conNomosTemp;
	Connection conNomos;
	
	public DBConnect(String who,Connection general,Connection nomos) {
		this.who=who;
			conGeneral = general;
			conNomos = nomos;

	try{
		Class.forName("com.mysql.jdbc.Driver");
	}catch(Exception ex){
		ex.printStackTrace();
	}
	}

	public static byte[] getPassword(String username){
		try{
			//Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			Statement st = Main.generalRootConn.createStatement();
			String query = "select * from accounts"+" WHERE Username=\""+username+"\"";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				if(rs.getString("Username").equalsIgnoreCase(username)){
					Blob pass = rs.getBlob("Password");
					byte[] passBytes = null;
					passBytes = pass.getBytes(1, (int) pass.length());
					return passBytes;
				}
			}
			return null;
		}catch(Exception ex){
			log.log(Level.SEVERE,ex.toString(),ex);
			
		}
		return null;
	}
	public static byte[] getSalt(String username){
		try{
			//Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			Statement st = Main.generalRootConn.createStatement();
			String query = "select * from accounts"+" WHERE Username=\""+username+"\"";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				if(rs.getString("Username").equalsIgnoreCase(username)){
					Blob pass = rs.getBlob("Salt");
					byte[] passBytes = null;
					passBytes = pass.getBytes(1, (int) pass.length());
					return passBytes;
				}
			}
			return null;
		}catch(Exception ex){
			log.log(Level.SEVERE,ex.toString(),ex);
			
		}
		return null;
	}
	public static String getEmail(String username){
		try{
			//Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			Statement st = Main.generalRootConn.createStatement();
			String query = "select * from accounts"+" WHERE Username=\""+username+"\"";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				if(rs.getString("Username").equalsIgnoreCase(username)){
					String returnVal=rs.getString("Email");
					return returnVal;
				}
			}
			return "";
		}catch(Exception ex){
			log.log(Level.SEVERE,ex.toString(),ex);
			
		}
		return "";
	}
/*	public boolean existsUsername(String username) {
		try{
			if(conGeneral==null){
				conGeneral  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8",who,rootPass);
			}
			Statement st = conGeneral.createStatement();
			String query = "select * from accounts";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				String user = rs.getString("Username");
				if(user.equalsIgnoreCase(username)){
					return true;
				}
			}
			return false;
		}catch(Exception ex){
			log.log(Level.SEVERE,ex.toString(),ex);
		}
		return false;
	}*/
	public boolean existsUsername(String username){
		return Main.allUsernames.contains(username);
	}
	public boolean login(String username, byte[] password,Socket sock,Socket nSock){
		try{
			Connection con;
			if(who.equals("root")){
				con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8",who,rootPass);
			}else{
				con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			}
			conGeneral = con;
			
			Statement st = con.createStatement();
			String query = "select * from accounts"+" WHERE Username=\""+username+"\"";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				String user = rs.getString("Username");
				Blob pass = rs.getBlob("Password");
				byte[] passBytes = null;
				passBytes = pass.getBytes(1, (int) pass.length());
				pass.free();
				//System.out.println(Arrays.toString(passBytes));
				//System.out.println(Arrays.toString(password));
				if(user.equalsIgnoreCase(username) && Arrays.equals(passBytes, password)){
					Player p = new Player(user,sock,nSock,false);
					
					String nomos = rs.getString("Perioxi").split("-")[0].replace("1", "").replace("2", "").replace("3", "").replace("4", "");
					p.points = rs.getInt("Points");
					p.games = rs.getInt("Games");
					p.fastestTime = rs.getInt("Fastest");
					p.friends = rs.getString("Friends");
					p.winsRow = rs.getInt("WinsInARow");
					p.avatar = rs.getInt("Avatar");
					p.nomos = nomos.trim();
					p.perioxi = rs.getString("Perioxi").split("-")[1].trim();
					p.rank = getRank(username);
					if(who.equals("root")){
						conNomos  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revenge_"+nomos.toLowerCase().replace("ς", "σ").replace("1","").replace("2","").replace("3","").replace("4","")+"?useUnicode=yes&characterEncoding=UTF-8",who,rootPass);
					}else{
						conNomos  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revenge_"+nomos.toLowerCase().replace("ς", "σ").replace("1","").replace("2","").replace("3","").replace("4","")+"?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
					}
					
					p.nomosRank = getNomosRank(username, p.nomos);
					p.perioxiRank = getPerioxiRank(username, p.nomos, p.perioxi);
					p.lastGameSystemMilis = Long.parseLong(rs.getString("LastGame"));
					
					//try {
						p.generalTable = con;
						p.nomosTable=conNomos;
						//p.nomosTable  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revenge_"+nomos.toLowerCase().replace("ς", "σ").replace("1","").replace("2","").replace("3","").replace("4","")+"?useUnicode=yes&characterEncoding=UTF-8",username,DBConnect.DBPass);
/*					} catch (SQLException e) {
						log.log(Level.SEVERE,e.toString(),e);
					}*/
					p.d = new DBConnect(user,p.generalTable,p.nomosTable);
					return true;
				}
			}
			con.close();
			return false;
		}catch(Exception ex){
			log.log(Level.SEVERE,ex.toString(),ex);
		}
		return false;
	}
	public void accountCreate(String username,byte[] password,byte[] salt,String nomos,String perioxi,String avatar,String email,Socket sock,Socket nSock){
		try {
			Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revenge_"+nomos.toLowerCase().replace("ς", "σ").replace("1","").replace("2","").replace("3","").replace("4","")+"?useUnicode=yes&characterEncoding=UTF-8",who,rootPass);
			con.createStatement();
			String query = "INSERT INTO "+perioxi.toLowerCase().replace(" ", "_")+"(Username,Points,Games,Fastest,PointsToday) VALUES(?,?,?,?,?)";
			PreparedStatement pst = con.prepareStatement(query); 
			pst.setString(1, username);
			pst.setInt(2, 0);
			pst.setInt(3, 0);
			pst.setInt(4, 2147000000);
			pst.setInt(5, 0);
			pst.executeUpdate();

			
			con.createStatement();
			query = "INSERT INTO general (Username,Points,Games,Perioxi,Fastest,PointsToday) VALUES(?,?,?,?,?,?)";
			pst = con.prepareStatement(query); 
			pst.setString(1, username);
			pst.setInt(2, 0);
			pst.setInt(3, 0);
			pst.setString(4, perioxi);
			pst.setInt(5, 2147000000);
			pst.setInt(6, 0);
			pst.executeUpdate();
			con.close();
			
			con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8",who,rootPass);
			con.createStatement();
			query = "INSERT INTO accounts(Username,Password,Salt,Points,Games,Perioxi,Fastest,Friends,Avatar,Email,LastGame,PointsToday,WinsInARow) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pst = con.prepareStatement(query); 
			pst.setString(1, username);
			pst.setBlob(2, new ByteArrayInputStream(password), password.length);
			
			pst.setBlob(3, new ByteArrayInputStream(salt), salt.length);
			
			pst.setInt(4, 0);
			pst.setInt(5, 0);
			pst.setString(6, nomos.replace("1","").replace("2","").replace("3","").replace("4","")+"-"+perioxi);
			pst.setInt(7, 2147000000);
			pst.setString(8,"");
			pst.setInt(9, Integer.parseInt(avatar));
			pst.setString(10, email);
			pst.setString(11, "0");
			pst.setInt(12, 0);
			pst.setInt(13, 0);
			pst.executeUpdate();
			con.close();
			
			Main.allUsernames.add(username);
			login(username, password, sock, nSock);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.toString(),e);
			
		}
	}
	public void update(Player p,long newScore,int games,int newFastestTime,long gameTimeSystemMilis){
		try {
			//Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			conGeneral.createStatement();
			String query = null;
			int pointsTodayExisting = getPointsToday(p.username);
			int newPointsToday = (int)(pointsTodayExisting+newScore);
			int newPoints = (int)(p.points+newScore);
			if(newPointsToday<0){
				newPointsToday=0;
			}
			if(newPoints<0){
				newPoints=0;
			}
			if(newFastestTime==-1){
				if(newScore>0){
					query = "UPDATE accounts SET WinsInARow="+(p.winsRow+1)+",Points="+(newPoints)+",Games="+ (games+1) +",LastGame="+gameTimeSystemMilis+",PointsToday="+(newPointsToday)+" WHERE Username=\""+p.username+"\"";
				}else{
					query = "UPDATE accounts SET WinsInARow="+0+",Points="+(newPoints)+",Games="+ (games+1) +",LastGame="+gameTimeSystemMilis+",PointsToday="+(newPointsToday)+" WHERE Username=\""+p.username+"\"";
				}
			}else{
				if(newScore>0){
					query = "UPDATE accounts SET WinsInARow="+(p.winsRow+1)+",Points="+(newPoints)+",Games="+ (games+1)  +",LastGame="+gameTimeSystemMilis+",Fastest="+newFastestTime +",PointsToday="+(newPointsToday)+" WHERE Username=\""+p.username+"\"";
				}else{
					query = "UPDATE accounts SET WinsInARow="+0+",Points="+(newPoints)+",Games="+ (games+1)  +",LastGame="+gameTimeSystemMilis+",Fastest="+newFastestTime +",PointsToday="+(newPointsToday)+" WHERE Username=\""+p.username+"\"";
				}
			}
			PreparedStatement pst = conGeneral.prepareStatement(query); 
			pst.executeUpdate();
			

			conNomos.createStatement();
			if(newFastestTime==-1){
				query = "UPDATE "+p.perioxi.toLowerCase().replace(" ", "_")+" SET Points="+(newPoints)+",Games="+ (games+1) +",PointsToday="+(newPointsToday)+" WHERE Username=\""+p.username+"\"";
			}else{
				query = "UPDATE "+p.perioxi.toLowerCase().replace(" ", "_")+" SET Points="+(newPoints)+",Games="+ (games+1) +",Fastest="+newFastestTime +",PointsToday="+(newPointsToday)+" WHERE Username=\""+p.username+"\"";
			}
			pst = conNomos.prepareStatement(query); 
			pst.executeUpdate();
			if(newFastestTime==-1){
				query = "UPDATE general SET Points="+(newPoints)+",Games="+ (games+1) +",PointsToday="+(newPointsToday)+" WHERE Username=\""+p.username+"\"";
			}else{
				query = "UPDATE general SET Points="+(newPoints)+",Games="+ (games+1) +",Fastest="+newFastestTime +",PointsToday="+(newPointsToday)+" WHERE Username=\""+p.username+"\"";
			}
			pst = conNomos.prepareStatement(query); 
			pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.toString(),e);
			
		}
	}
	public int getPoints(String username){
		try{
			//Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			Statement st = conGeneral.createStatement();
			String query = "select * from accounts"+" WHERE Username=\""+username+"\"";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				if(rs.getString("Username").equalsIgnoreCase(username)){
					int returnVal=rs.getInt("Points");
					return returnVal;
				}
			}
			return 0;
		}catch(Exception ex){
			log.log(Level.SEVERE,ex.toString(),ex);
			
		}
		return 0;
	}
	public int getPointsToday(String username){
		try{
			//Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			Statement st = conGeneral.createStatement();
			String query = "select * from accounts"+" WHERE Username=\""+username+"\"";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				if(rs.getString("Username").equalsIgnoreCase(username)){
					int returnVal=rs.getInt("PointsToday");
					return returnVal;
				}
			}
			return 0;
		}catch(Exception ex){
			log.log(Level.SEVERE,ex.toString(),ex);
			
		}
		return 0;
	}

	public int getGames(String user) {
		try{
			//Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			Statement st = conGeneral.createStatement();
			String query = "select * from accounts"+" WHERE Username=\""+user+"\"";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				if(rs.getString("Username").equalsIgnoreCase(user)){
					int returnVal = rs.getInt("Games");
					return returnVal;
				}
			}
		}catch(Exception ex){
			log.log(Level.SEVERE,ex.toString(),ex);
			
		}
		return 0;
	}

	public String getPerioxi(String user) {
		try{
			//Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			Statement st = conGeneral.createStatement();
			String query = "select * from accounts"+" WHERE Username=\""+user+"\"";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				if(rs.getString("Username").equalsIgnoreCase(user)){
					String returnVal=rs.getString("Perioxi").split("-")[1];
					return returnVal;
				}
			}
		}catch(Exception ex){
			log.log(Level.SEVERE,ex.toString(),ex);
			
		}
		return "0";
	}
	
	public String getAvatar(String user) {
		try{
			//Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			Statement st = conGeneral.createStatement();
			String query = "select * from accounts"+" WHERE Username=\""+user+"\"";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				if(rs.getString("Username").equalsIgnoreCase(user)){
					String returnVal=rs.getString("Avatar");
					return returnVal;
				}
			}
		}catch(Exception ex){
			log.log(Level.SEVERE,ex.toString(),ex);
			
		}
		return "0";
	}

	public String getNomos(String user) {
		try{
			//Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			Statement st = conGeneral.createStatement();
			String query = "select * from accounts"+" WHERE Username=\""+user+"\"";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				if(rs.getString("Username").equalsIgnoreCase(user)){
					String returnVal=rs.getString("Perioxi").split("-")[0].replace("1", "").replace("2", "").replace("3", "").replace("4", "");
					return returnVal;
				}
			}
		}catch(Exception ex){
			log.log(Level.SEVERE,ex.toString(),ex);
			
		}
		return "0";
	}

	public int getRank(String username) {
		try {
			//Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			Statement st = conGeneral.createStatement();
			//String query = "select * from accounts"+" WHERE Username=\""+username+"\""+" ORDER BY Points DESC";
			//String query = "SELECT `ID`, (SELECT COUNT(*) FROM `accounts` WHERE `Username` <= "+"\""+username+"\""+") AS `position`, `Username` FROM `accounts` WHERE `Username` = " +"\""+username+"\"";
			String query  ="SELECT z.rank FROM (SELECT *, @rownum := @rownum + 1 AS rank FROM accounts t, (SELECT @rownum := 0) r ORDER BY Points DESC) as z WHERE `Username` = " +"\""+username+"\"";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				//if(rs.getString("Username").equalsIgnoreCase(username)){
					return Integer.parseInt(rs.getString("rank"));
				//}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.toString(),e);
			
		}

		return -5;
	}
	
	public int getDailyRank(String username) {
		try {
			//Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			Statement st = conGeneral.createStatement();
			//String query = "select * from accounts ORDER BY PointsToday DESC";
			String query  ="SELECT z.rank FROM (SELECT *, @rownum := @rownum + 1 AS rank FROM accounts t, (SELECT @rownum := 0) r ORDER BY PointsToday DESC) as z WHERE `Username` = " +"\""+username+"\"";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				//if(rs.getString("Username").equalsIgnoreCase(username)){
					return Integer.parseInt(rs.getString("rank"));
				//}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.toString(),e);
			
		}

		return -5;
	}
	public int getNomosRank(String username, String nomos) {
		try {
			//Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revenge_"+nomos.toLowerCase().replace("ς", "σ").replace("1","").replace("2","").replace("3","").replace("4","")+"?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			conNomosTemp = conNomos.getCatalog();
			conNomos.setCatalog("revenge_"+nomos.toLowerCase().replace("ς", "σ"));
			Statement st = conNomos.createStatement();
			//String query = "select * from general ORDER BY Points DESC";
			String query  ="SELECT z.rank FROM (SELECT *, @rownum := @rownum + 1 AS rank FROM general t, (SELECT @rownum := 0) r ORDER BY Points DESC) as z WHERE `Username` = " +"\""+username+"\"";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				//if(rs.getString("Username").equalsIgnoreCase(username)){
					return Integer.parseInt(rs.getString("rank"));
				//}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.toString(),e);
			
		}finally{
			try {
				conNomos.setCatalog(conNomosTemp);
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.toString(),e);
			}
		}

		return -5;
	}
	public int getNomosDailyRank(String username, String nomos) {
		try {
			//Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revenge_"+nomos.toLowerCase().replace("ς", "σ").replace("1","").replace("2","").replace("3","").replace("4","")+"?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			conNomosTemp = conNomos.getCatalog();
			conNomos.setCatalog("revenge_"+nomos.toLowerCase().replace("ς", "σ"));
			Statement st = conNomos.createStatement();
			//String query = "select * from general ORDER BY PointsToday DESC";
			String query  ="SELECT z.rank FROM (SELECT *, @rownum := @rownum + 1 AS rank FROM general t, (SELECT @rownum := 0) r ORDER BY PointsToday DESC) as z WHERE `Username` = " +"\""+username+"\"";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				//if(rs.getString("Username").equalsIgnoreCase(username)){
					return Integer.parseInt(rs.getString("rank"));
				//}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.toString(),e);
			
		}finally{
			try {
				conNomos.setCatalog(conNomosTemp);
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.toString(),e);
			}
		}

		return -5;
	}
	
	public int[] getTopNomoi(){
		int[] nomoiPontoi = new int[Main.NOMOILOWERCASE.length];
		try {
			Connection con  = Main.generalRootConn;
			String query = "SELECT * from topNomoi";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			int i =0;
				while(rs.next()){
					nomoiPontoi[i] = rs.getInt("Points");
					i++;
				}
			return nomoiPontoi;
		} catch (SQLException e) {
			log.log(Level.SEVERE,e.toString(),e);
			// TODO Auto-generated catch block
			
		}
		return null;
	}
	public int[] getTopPerioxes(){
		int[] perioxesPontoi = new int[perioxes.length];
		try {
			Connection con  = Main.generalRootConn;
			String query = "SELECT * from topPerioxes";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			int i =0;
				while(rs.next()){
					perioxesPontoi[i] = rs.getInt("Points");
					i++;
				}
			return perioxesPontoi;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.toString(),e);
			
		}
		return null;
	}
	
	public String[][] getTop100() {
		String[][] top100 = new String[100][3];
		try {
			Connection con  = Main.generalRootConn;
			Statement st = con.createStatement();
			String query = "select * from accounts ORDER BY Points DESC LIMIT 100";
			ResultSet rs = st.executeQuery(query);
			for(int i=0;i<100;i++){
				if(rs.next()){
					top100[i][0] = rs.getString("Username");
					top100[i][1] = rs.getString("Points");
					top100[i][2] = rs.getString("Perioxi");
				}
			}
			return top100;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.toString(),e);
			
		}
		return null;
	}
	public String[][] getTop100Daily() {
		String[][] top100 = new String[100][3];
		try {
			Connection con  = Main.generalRootConn;
			Statement st = con.createStatement();
			String query = "select * from accounts ORDER BY PointsToday DESC LIMIT 100";
			ResultSet rs = st.executeQuery(query);
			for(int i=0;i<100;i++){
				if(rs.next()){
					top100[i][0] = rs.getString("Username");
					top100[i][1] = rs.getString("PointsToday");
					top100[i][2] = rs.getString("Perioxi");
				}
			}
			return top100;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.toString(),e);
			
		}
		return null;
	}

	public String[][][][] getAreaTop100() {
		String[][][][] AreaTop100 = new String[Main.NOMOI.length][200][100][2];
		try {
			Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revenge_"+Main.NOMOI[0].toLowerCase().replace("ς", "σ")+"?useUnicode=yes&characterEncoding=UTF-8","root",rootPass);
	out:	for(int j=0;j<Main.NOMOI.length;j++){
				if(j!=0){
					con.setCatalog("revenge_"+Main.NOMOI[j].toLowerCase().replace("ς", "σ"));
				}
				Statement st = con.createStatement();
				for(int k=0;k<200;k++){
					try{
						String query = "SELECT * from "+Main.PERIOXES[j][k].toLowerCase().replace(" ", "_")+" ORDER BY Points DESC LIMIT 100";
						ResultSet rs = st.executeQuery(query);
						for(int i=0;i<100;i++){
							if(rs.next()){
								AreaTop100[j][k][i][0] = rs.getString("Username");
								AreaTop100[j][k][i][1] = rs.getString("Points");
							}
						}
					}catch(ArrayIndexOutOfBoundsException e){
						continue out;
					}
				}
			}
			con.close();
			return AreaTop100;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.toString(),e);
			
		}
		return null;
	}
	
	public String[][][][] getAreaTop100Daily() {
		String[][][][] AreaTop100 = new String[Main.NOMOI.length][200][100][2];
		try {
			Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revenge_"+Main.NOMOI[0].toLowerCase().replace("ς", "σ")+"?useUnicode=yes&characterEncoding=UTF-8","root",rootPass);
	out:	for(int j=0;j<Main.NOMOI.length;j++){
				if(j!=0){
					con.setCatalog("revenge_"+Main.NOMOI[j].toLowerCase().replace("ς", "σ"));
				}
				Statement st = con.createStatement();
				for(int k=0;k<200;k++){
					try{
						String query = "SELECT * from "+Main.PERIOXES[j][k].toLowerCase().replace(" ", "_")+" ORDER BY PointsToday DESC LIMIT 100";
						ResultSet rs = st.executeQuery(query);
						for(int i=0;i<100;i++){
							if(rs.next()){
								AreaTop100[j][k][i][0] = rs.getString("Username");
								AreaTop100[j][k][i][1] = rs.getString("PointsToday");
							}
						}
					}catch(ArrayIndexOutOfBoundsException e){
						continue out;
					}
				}
			}
			con.close();
			return AreaTop100;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.toString(),e);
			
		}
		return null;
	}
	
	public String[][][] getNomosTop100() {
		String[][][] NomosTop100 = new String[Main.NOMOI.length][100][3];
		try {
			Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revenge_"+Main.NOMOI[0].toLowerCase().replace("ς", "σ")+"?useUnicode=yes&characterEncoding=UTF-8","root",rootPass);
	out:	for(int j=0;j<Main.NOMOI.length;j++){
				if(j!=0){
					con.setCatalog("revenge_"+Main.NOMOI[j].toLowerCase().replace("ς", "σ"));
				}
				Statement st = con.createStatement();
					try{
						String query = "SELECT * from general ORDER BY Points DESC LIMIT 100";
						ResultSet rs = st.executeQuery(query);
						for(int i=0;i<100;i++){
							if(rs.next()){
								NomosTop100[j][i][0] = rs.getString("Username");
								NomosTop100[j][i][1] = rs.getString("Points");
								NomosTop100[j][i][2] = rs.getString("Perioxi");
							}
						}
					}catch(ArrayIndexOutOfBoundsException e){
						continue out;
					}
			}
			con.close();
			return NomosTop100;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.toString(),e);
			
		}
		return null;
	}
	
	public String[][][] getNomosTop100Daily() {
		String[][][] NomosTop100 = new String[Main.NOMOI.length][100][3];
		try {
			Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revenge_"+Main.NOMOI[0].toLowerCase().replace("ς", "σ")+"?useUnicode=yes&characterEncoding=UTF-8","root",rootPass);
	out:	for(int j=0;j<Main.NOMOI.length;j++){
				if(j!=0){
					con.setCatalog("revenge_"+Main.NOMOI[j].toLowerCase().replace("ς", "σ"));
				}
				Statement st = con.createStatement();
					try{
						String query = "SELECT * from general ORDER BY PointsToday DESC LIMIT 100";
						ResultSet rs = st.executeQuery(query);
						for(int i=0;i<100;i++){
							if(rs.next()){
								NomosTop100[j][i][0] = rs.getString("Username");
								NomosTop100[j][i][1] = rs.getString("PointsToday");
								NomosTop100[j][i][2] = rs.getString("Perioxi");
							}
						}
					}catch(ArrayIndexOutOfBoundsException e){
						continue out;
					}
			}
			con.close();
			return NomosTop100;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.toString(),e);
			
		}
		return null;
	}

	public int getFastestTime(String u) {
		try{
			//Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			Statement st = conGeneral.createStatement();
			String query = "select * from accounts"+" WHERE Username=\""+u+"\"";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				if(rs.getString("Username").equalsIgnoreCase(u)){
					int returnVal =rs.getInt("Fastest");
					return returnVal;
				}
			}
		}catch(Exception ex){
			log.log(Level.SEVERE,ex.toString(),ex);
			
		}
		return 0;
	}
	public int getPerioxiRank(String username, String nomos, String perioxi) {
		try {
			//Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revenge_"+nomos.toLowerCase().replace("ς", "σ").replace("1","").replace("2","").replace("3","").replace("4","")+"?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			conNomosTemp = conNomos.getCatalog();
			conNomos.setCatalog("revenge_"+nomos.toLowerCase().replace("ς", "σ"));
			Statement st = conNomos.createStatement();
			//String query = "select * from "+ perioxi.toLowerCase().replace(" ", "_")+" ORDER BY Points DESC";
			String query  ="SELECT z.rank FROM (SELECT *, @rownum := @rownum + 1 AS rank FROM "+ perioxi.toLowerCase().replace(" ", "_")+" t, (SELECT @rownum := 0) r ORDER BY Points DESC) as z WHERE `Username` = " +"\""+username+"\"";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				//if(rs.getString("Username").equalsIgnoreCase(username)){
					return Integer.parseInt(rs.getString("rank"));
				//}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.toString(),e);
			
		}finally{
			try {
				conNomos.setCatalog(conNomosTemp);
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.toString(),e);
			}
		}

		return -5;
	}
	public int getPerioxiDailyRank(String username, String nomos, String perioxi) {
		try {
			//Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revenge_"+nomos.toLowerCase().replace("ς", "σ").replace("1","").replace("2","").replace("3","").replace("4","")+"?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			conNomosTemp = conNomos.getCatalog();
			conNomos.setCatalog("revenge_"+nomos.toLowerCase().replace("ς", "σ"));
			Statement st = conNomos.createStatement();
			//String query = "select * from "+ perioxi.toLowerCase().replace(" ", "_")+" ORDER BY PointsToday DESC";
			String query  ="SELECT z.rank FROM (SELECT *, @rownum := @rownum + 1 AS rank FROM "+ perioxi.toLowerCase().replace(" ", "_")+" t, (SELECT @rownum := 0) r ORDER BY PointsToday DESC) as z WHERE `Username` = " +"\""+username+"\"";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				//if(rs.getString("Username").equalsIgnoreCase(username)){
					return Integer.parseInt(rs.getString("rank"));
				//}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.toString(),e);
			
		}finally{
			try {
				conNomos.setCatalog(conNomosTemp);
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.toString(),e);
			}
		}

		return -5;
	}
	public String getFriends(String u) {
		try{
			//Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			Statement st = conGeneral.createStatement();
			String query = "select * from accounts"+" WHERE Username=\""+u+"\"";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				if(rs.getString("Username").equalsIgnoreCase(u)){
					String returnVal=rs.getString("Friends");
					return returnVal;
				}
			}
		}catch(Exception ex){
			log.log(Level.SEVERE,ex.toString(),ex);
			
		}
		return null;
	}

	public void updateFriends(String user, String newFriends) {
		try {
			//Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			conGeneral.createStatement();
		    String query = "UPDATE accounts SET Friends=\""+newFriends+"\" WHERE Username=\""+user+"\"";
			PreparedStatement pst = conGeneral.prepareStatement(query); 
			pst.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.toString(),e);
			
		}
		
	}

	public void submitNomoi(String[] nomoiWinnersNames) {
		try {
			Connection con  = Main.generalRootConn;
			con.createStatement();
			for(int i = 0;i<3;i++){
				if(!nomoiWinnersNames[i].equals("nobody")){
					//getting existing points
					int previousPts = 0;
					for(int k =0;k<Rankings.nomoiTemp.length;k++){
						if(Rankings.nomoiTemp[k].equalsIgnoreCase(nomoiWinnersNames[i])){
							previousPts+=Rankings.topNomoi[k];
							break;
						}
					}
					//
				    String query = "UPDATE topNomoi SET Points=\""+((3-i)+previousPts)+"\" WHERE Nomos=\""+nomoiWinnersNames[i]+"\"";
					PreparedStatement pst = con.prepareStatement(query); 
					pst.executeUpdate();
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.toString(),e);
			
		}
		
	}

	public void submitPerioxes(String[] perioxesWinnersNames) {
		try {
			Connection con  = Main.generalRootConn;
			con.createStatement();
			for(int i = 0;i<10;i++){
				if(!perioxesWinnersNames.equals("nobody")){
					//getting existing points
					int previousPts = 0;
					for(int k =0;k<Rankings.perioxesTemp.length;k++){
						if(Rankings.perioxesTemp[k].equalsIgnoreCase(perioxesWinnersNames[i])){
							previousPts+=Rankings.topPerioxes[k];
							break;
						}
					}
					//
				    String query = "UPDATE topPerioxes SET Points=\""+((10-i)+previousPts)+"\" WHERE Perioxi=\""+perioxesWinnersNames[i]+"\"";
					PreparedStatement pst = con.prepareStatement(query); 
					pst.executeUpdate();
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.toString(),e);
			
		}
		
	}

	public void resetDailies() {
		try {
			Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8","root",rootPass);
			con.createStatement();
			PreparedStatement pst = con.prepareStatement("UPDATE accounts SET PointsToday=0"); 
			pst.executeUpdate();
			
			try {
				Connection con2  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revenge_"+Main.NOMOI[0].toLowerCase().replace("ς", "σ")+"?useUnicode=yes&characterEncoding=UTF-8","root",rootPass);
		out:	for(int j=0;j<Main.NOMOI.length;j++){
					if(j!=0){
						con2.setCatalog("revenge_"+Main.NOMOI[j].toLowerCase().replace("ς", "σ"));
					}
					//Statement st = con2.createStatement();
					PreparedStatement pst2 = con2.prepareStatement("UPDATE general SET PointsToday=0");
					pst2.executeUpdate();
					
					for(int k=0;k<200;k++){
						try{
							PreparedStatement pst3 = con2.prepareStatement("UPDATE "+Main.PERIOXES[j][k].toLowerCase().replace(" ", "_")+" SET PointsToday=0");
							pst3.executeUpdate();
						}catch(Exception e){
							continue out;
						}
					}
				}//
				con.close();
				con2.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.log(Level.SEVERE,e.toString(),e);
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE,e.toString(),e);
			
		}
		
	}

	public void changePass(String username, byte[] newPass,byte[] newSalt) {
		try{
			conGeneral.createStatement();
			
			//String query = "UPDATE accounts SET Password=\""+newPass+"\" WHERE Username=\""+username+"\"";
			String query = "UPDATE accounts SET Password = ? , Salt = ? WHERE Username=\""+username+"\"";
			
			PreparedStatement pst = conGeneral.prepareStatement(query); 
			
			pst.setBlob(1, new ByteArrayInputStream(newPass), newPass.length);
			
			pst.setBlob(2, new ByteArrayInputStream(newSalt), newSalt.length);
			
			pst.executeUpdate();
		}catch(Exception e){
			log.log(Level.SEVERE,e.toString(),e);
		}
		
	}

	public void changeEmail(String username, String newEmail) {
		try{
			conGeneral.createStatement();
			String query = "UPDATE accounts SET Email=\""+newEmail+"\" WHERE Username=\""+username+"\"";
			PreparedStatement pst = conGeneral.prepareStatement(query); 
			pst.executeUpdate();
		}catch(Exception e){
			log.log(Level.SEVERE,e.toString(),e);
		}
		
	}

	public static void getAllUsernames() {
		try{
			//Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8",who,DBPass);
			Statement st = Main.generalRootConn.createStatement();
			String query = "select Username from accounts";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
					Main.allUsernames.add(rs.getString("Username"));
			}
		}catch(Exception ex){
			log.log(Level.SEVERE,ex.toString(),ex);
			
		}
	}

	public static void setPassword(String user, String tempPass) {
		try{
			Main.generalRootConn.createStatement();
			
			//String query = "UPDATE accounts SET Password=\""+newPass+"\" WHERE Username=\""+username+"\"";
			String query = "UPDATE accounts SET Password = ? WHERE Username=\""+user+"\"";
			PreparedStatement pst = Main.generalRootConn.prepareStatement(query); 
			pst.setBlob(1, new ByteArrayInputStream(tempPass.getBytes()), tempPass.getBytes().length);
			pst.executeUpdate();
		}catch(Exception e){
			log.log(Level.SEVERE,e.toString(),e);
		}
	}



}