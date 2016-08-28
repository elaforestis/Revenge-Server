import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

 

public class Main {
	public static volatile Handler handler;
	public static volatile Formatter formatter;

	static final Logger log = Logger.getLogger( Main.class.getName() );
	private static volatile ServerSocket nSSocket;
	private static volatile ServerSocket serverSocket;
	private static Socket nSocket;
	private static Socket clientSocket;


	public static final String[] NOMOILOWERCASE = {"Αιτωλοακαρνανίας","Αργολίδας","Αρκαδίας","Άρτας","Αττικής","Αχαΐας","Βοιωτίας","Γρεβενών","Δράμας","Δωδεκανήσου","Εβοίας","Έβρου","Ευρυτανίας","Ζακύνθου","Ηλείας","Ημαθίας","Ηρακλείου","Θεσπρωτίας","Θεσσαλονίκης","Ιωαννίνων","Καβάλας","Καρδίτσας","Καστοριάς","Κέρκυρας","Κεφαλληνίας","Κιλκίς","Κοζάνης","Κορινθίας","Κυκλάδων","Λακωνίας","Λάρισας","Λασιθίου","Λέσβου","Λευκάδας","Μαγνησίας","Μεσσηνίας","Ξάνθης","Πέλλας","Πιερίας","Πρέβεζας","Ρεθύμνου","Ροδόπης","Σάμου","Σερρών","Τρικάλων","Φθιώτιδας","Φλώρινας","Φωκίδας","Χαλκιδικής","Χανίων","Χίου"};
    public static final String[] NOMOI = {"ΑΙΤΩΛΟΑΚΑΡΝΑΝΙΑΣ", "ΑΡΓΟΛΙΔΑΣ", "ΑΡΚΑΔΙΑΣ", "ΑΡΤΑΣ", "ΑΤΤΙΚΗΣ", "ΑΧΑΙΑΣ", "ΒΟΙΩΤΙΑΣ", "ΓΡΕΒΕΝΩΝ", "ΔΡΑΜΑΣ", "ΔΩΔΕΚΑΝΗΣΟΥ", "ΕΒΟΙΑΣ", "ΕΒΡΟΥ", "ΕΥΡΥΤΑΝΙΑΣ", "ΖΑΚΥΝΘΟΥ", "ΗΛΕΙΑΣ", "ΗΜΑΘΙΑΣ", "ΗΡΑΚΛΕΙΟΥ", "ΘΕΣΠΡΩΤΙΑΣ", "ΘΕΣΣΑΛΟΝΙΚΗΣ", "ΙΩΑΝΝΙΝΩΝ", "ΚΑΒΑΛΑΣ", "ΚΑΡΔΙΤΣΑΣ", "ΚΑΣΤΟΡΙΑΣ", "ΚΕΡΚΥΡΑΣ", "ΚΕΦΑΛΛΗΝΙΑΣ", "ΚΙΛΚΙΣ", "ΚΟΖΑΝΗΣ", "ΚΟΡΙΝΘΙΑΣ", "ΚΥΚΛΑΔΩΝ", "ΛΑΚΩΝΙΑΣ", "ΛΑΡΙΣΑΣ", "ΛΑΣΙΘΙΟΥ", "ΛΕΣΒΟΥ", "ΛΕΥΚΑΔΑΣ", "ΜΑΓΝΗΣΙΑΣ", "ΜΕΣΣΗΝΙΑΣ", "ΞΑΝΘΗΣ", "ΠΕΛΛΑΣ", "ΠΙΕΡΙΑΣ", "ΠΡΕΒΕΖΑΣ", "ΡΕΘΥΜΝΟΥ", "ΡΟΔΟΠΗΣ", "ΣΑΜΟΥ", "ΣΕΡΡΩΝ", "ΤΡΙΚΑΛΩΝ", "ΦΘΙΩΤΙΔΑΣ", "ΦΛΩΡΙΝΑΣ", "ΦΩΚΙΔΑΣ", "ΧΑΛΚΙΔΙΚΗΣ", "ΧΑΝΙΩΝ", "ΧΙΟΥ"};
    public static final String[][] PERIOXES = {{"﻿Άγιος Ανδρέας Γαβαλού", "Άγιος Βλάσιος", "Άγιος Κωνσταντίνος", "Αγρίνιο", "Αιτωλικό", "Άκτιο", "Αμφιλοχία", "Αντίρριο", "Άνω Χώρα", "Αστακός", "Βόνιτσα", "Γαβαλού", "Γραμμένη Οξυά", "Δοκίμιο", "Εμπεσός", "Ευηνοχώρι", "Θέρμο", "Καινούργιο", "Κατούνα", "Κατοχή", "Κλεπά", "Ματαράγκα", "Μεγάλη Χώρα", "Μενίδι", "Μεσολόγγι", "Μύτικας", "Ναύπακτος", "Νέα Αβόρανη", "Νεοχώρι Αιτωλοακαρνανίας", "Πάλαιρος", "Παναιτώλιο", "Παραβόλα", "Πλάτανος Ναυπάκτου", "Σίμος", "Στράτος", "Τερψιθέα", "Φυτείες", "Χρυσοπηγή"},{"﻿Αγία Τριάδα Αργολίδας", "Αλέα", "Άργος", "Αχλαδόκαμπος", "Δρέπανο Αργολίδας", "Ερμιόνη", "Κουτσοπόδι", "Κρανίδι", "Λυγουριό", "Ναύπλιο", "Νέα Επίδαυρος", "Νέα Κίος", "Παλαιά Επίδαυρος", "Τολό"                        },{"﻿Άγιος Ανδρέας", "Άγιος Νικόλαος Αρκαδίας", "Άγιος Πέτρος", "Άστρος", "Βαλτεσινίκο", "Βυτίνα", "Δημητσάνα", "Ελαιοχώρι", "Κανδήλα", "Καρίταινα", "Καστρί", "Κάτω Ασέα", "Κοντοβάζαινα", "Κοσμάς", "Κοτύλιο", "Λαγκάδια", "Λεβίδι", "Λεοντάρι Αρκαδίας", "Λεωνίδι", "Μεγαλόπολη", "Νεστάνη", "Παλούμπα", "Παράλιο Άστρος", "Πουρναριά", "Στάδιο", "Τρίπολη", "Τρόπαια", "Τυρός"          },{"﻿Άγναντα", "Αμμότοπος", "Άνω Καλεντίνη", "Άρτα", "Βουργαρέλι", "Δημάρι", "Κομπότι", "Μελισσουργοί", "Μηλιανά", "Νεοχώρι Άρτας", "Πέτα", "Ροδαυγή", "Φιλοθέη Άρτας", "Χαλκιάδες"                        },{"﻿Αγία Βαρβάρα", "Αγία Κυριακή", "Αγία Μαρίνα Αίγινας", "Αγία Μαρίνα Γραμματικού", "Αγία Μαρίνα Κορωπίου", "Αγία Μαρίνα Νέας Μάκρης", "Αγία Παρασκευή", "Άγιοι Ανάργυροι", "Άγιοι Απόστολοι", "Άγιος Δημήτριος", "Άγιος Ιωάννης Ρέντης", "Άγιος Νικόλαος Αναβύσσου", "Άγιος Νικόλαος Λούτσας", "Άγιος Παντελεήμονας", "Άγιος Σεραφείμ", "Άγιος Στέφανος", "Αθήνα", "Αιάντειο", "Αιγάλεω", "Αίγινα", "Αλεποχώρι", "Άλιμος", "Αμπελάκια", "Ανάβυσσος", "Ανθούσα", "Άνοιξη", "Άνω Λιόσια", "Άνω Νέα Παλάτια", "Αργυρούπολη", "Αρτέμιδα", "Ασπρόπυργος", "Αυλάκι", "Αυλώνα", "Αφίδνες", "Αχαρνές", "Βάρη", "Βαρνάβας", "Βίλια", "Βούλα", "Βουλιαγμένη", "Βραυρώνα", "Βριλήσσια", "Βύρωνας", "Γαλάζια Ακτή", "Γαλατάς Αττικής", "Γαλάτσι", "Γέρακας", "Γλυκά Νερά", "Γλυφάδα", "Γραμματικό", "Δασκαλειό", "Δάφνη", "Διόνυσος", "Δραπετσώνα", "Δροσιά", "Εκάλη", "Ελευσίνα", "Ελληνικό", "Ερυθρές", "Ζεφύρι", "Ζούμπερι", "Ζωγράφος", "Ηλιούπολη", "Θρακομακεδόνες", "Θυμάρι", "Ίλιον", "Καισαριανή", "Κάλαμος Αττικής", "Καλέτζι", "Καλλιθέα", "Καλλιτεχνούπολη", "Καλύβια Θορικού", "Καματερό", "Κάντζα", "Καπανδρίτι", "Κάτω Σούλι", "Κερατέα", "Κερατσίνι", "Κηφισιά", "Κινέτα", "Κόκκινο Λιμανάκι", "Κορυδαλλός", "Κορωπί", "Κουβαράς", "Κρυονέρι", "Κύθηρα", "Κυψέλη Αίγινας", "Λαγονήσι", "Λάκκα", "Λαύριο", "Λουτρόπυργος", "Λυκόβρυση", "Μαγούλα", "Μάνδρα", "Μαραθώνας", "Μαρκόπουλο", "Μαρούσι", "Μάτι", "Μέγαρα", "Μέθανα", "Μελίσσια", "Μεσαγρός", "Μεταμόρφωση", "Μοσχάτο", "Νέα Ερυθραία", "Νέα Ιωνία", "Νέα Μάκρη", "Νέα Παλάτια", "Νέα Πεντέλη", "Νέα Πέραμος Αττικής", "Νέα Πολιτεία", "Νέα Σμύρνη", "Νέα Φιλαδέλφεια", "Νέα Χαλκηδόνα", "Νέο Ηράκλειο", "Νέο Ψυχικό", "Νέος Βουτζάς", "Νίκαια", "Ντράφι", "Παιανία", "Παλαιά Φώκαια", "Παλαιό Φάληρο", "Παλλήνη", "Παλούκια", "Παπάγος", "Πειραιάς", "Πεντέλη", "Πέραμα", "Πέρδικα", "Περιστέρι", "Πετρούπολη", "Πεύκη", "Πικέρμι", "Πολυδένδρι", "Πόρος Τροιζηνίας", "Πόρτο Ράφτη", "Ποταμός Κυθήρων", "Ραφήνα", "Ροδόπολη Αττικής", "Σαλαμίνα", "Σαρωνίδα", "Σελήνια", "Σκάλα Ωρωπού", "Σουβάλα", "Σπάτα", "Σπέτσες", "Σταμάτα", "Συκάμινο", "Ταύρος", "Ύδρα", "Υμηττός", "Φιλοθέη", "Φυλή", "Χαϊδάρι", "Χαλάνδρι", "Χαλκούτσι", "Χαμολιά", "Χολαργός", "Ψυχικό", "Ωρωπός", },{"﻿Άβυθος", "Αγία Παρασκευή Αχαΐας", "Άγιος Βασίλειος", "Αιγείρα", "Αίγιο", "Ακράτα", "Ακταίο", "Άραξος", "Αραχωβίτικα", "Αροανία", "Βιομηχανική Περιοχή Πάτρας", "Βραχνέικα", "Δάφνη Αχαΐας", "Δεμένικα Σαραβάλιου", "Διακοπτό", "Δρέπανο Αχαΐας", "Ερυμάνθεια", "Καλάβρυτα", "Καμάρες", "Καμίνια", "Κάτω Αχαία", "Κάτω Καστρίτσι", "Κάτω Οβρυά", "Κλειτορία", "Κουλουράς", "Κράθιο", "Κρήνη Πάτρας", "Λάππα", "Μιντιλόγλι", "Μυρτιά", "Νέο Σούλι", "Οβρυά", "Παραλία Πατρών", "Πάτρα", "Πλάτανος Αχαΐας", "Προφήτης Ηλίας", "Ρίο", "Ροδοδάφνη", "Σαγαίικα", "Σαραβάλι", "Συχαινά", "Τέμενη", "Τερψιθέα Αχαΐας", "Χαλανδρίτσα", "Ψαθόπυργος",                                                                                                                    },{"﻿Άγιος Γεώργιος Βοιωτίας", "Αλίαρτος", "Αντίκυρα", "Αράχωβα", "Βάγια", "Δαύλεια", "Δήλεσι", "Δίστομο", "Δόμβραινα", "Θήβα", "Κυριάκι", "Λιβαδειά", "Οινόη Βοιωτίας", "Οινόφυτα", "Ορχομενός", "Παραλία Διστόμου", "Παρόριο", "Σχηματάρι", "Τανάγρα"                   },{"﻿Άγιος Γεώργιος", "Γρεβενά", "Δεσκάτη", "Πολυνέρι", "Σκούμτσια"                                 },{"﻿Άγιος Αθανάσιος Δράμας", "Αμπελάκια Δράμας", "Αργυρούπολη Δράμας", "Δοξάτο", "Δράμα", "Καλαμπάκι", "Κάτω Νευροκόπι", "Κύρια", "Μαυρόβατος", "Νέα Αμισός", "Νέα Σεβάστεια", "Νικηφόρος", "Παρανέστι", "Περίχωρα", "Προσοτσάνη", "Σιταγροί", "Φτελιά", "Φωτολίβος", "Χωριστή"                   },{"﻿Αγαθονήσι", "Ανάληψη Αστυπάλαιας", "Αντιμάχεια", "Αρχάγγελος", "Αστυπάλαια", "Αφάντου", "Γεννάδι", "Έμπωνας", "Ιαλυσός", "Καλυθιές", "Κάλυμνος", "Κάρπαθος", "Κάσος", "Κέφαλος Κω", "Κολύμπια", "Κοσκινού", "Κρεμαστή", "Κως", "Λειψοί", "Λέρος", "Λίνδος", "Μαριτσά", "Μεγίστη", "Νίσυρος", "Παλαιά Πόλη", "Παραδείσι", "Παστίδα", "Πάτμος", "Ρόδος", "Σγουρού", "Σορωνή", "Σύμη", "Τήλος", "Φαληράκι", "Χάλκη", "Ψαλίδι"  },{"﻿Αγία Άννα", "Άγιος Νικόλαος Εύβοιας", "Αλιβέρι", "Αμάρυνθος", "Ανθηδώνα", "Αυλωνάρι", "Βαθύ Αυλίδας", "Βασιλικό", "Δροσιά Ευβοίας", "Δύο Δένδρα", "Έξω Παναγίτσα", "Ερέτρια", "Ιστιαία", "Καθενοί", "Κάρυστος", "Κονίστρες", "Κριεζά", "Κύμη", "Λίμνη", "Λουτρά Αιδηψού", "Μαντούδι", "Μαρμάρι", "Μπούρτζι", "Νέα Αρτάκη", "Νέα Λάμψακος", "Οξύλιθος", "Παραλία Αυλίδας", "Πολιτικά", "Σκύρος", "Στενή Δίρφυος", "Στύρα", "Υλίκη", "Φάρος", "Χαλκίδα", "Ψαχνά", "Ωρεοί"  },{"﻿Αλεξανδρούπολη", "Απαλός", "Γέφυρα Κήπων", "Διδυμότειχο", "Δίκαια", "Καστανιές", "Κυπρίνος", "Λάβαρα", "Μαΐστρος", "Μεταξάδες", "Νέα Βύσσα", "Νέα Χιλή", "Ορεστιάδα", "Πέπλος", "Ρίζια", "Σαμοθράκη", "Σουφλί", "Συκορράχη", "Τυχερό", "Φέρες"                  },{"﻿Άγραφα", "Γρανίτσα", "Καρπενήσι", "Κερασοχώρι", "Κρίκελλο", "Μαυρόλογγος", "Προυσός", "Ραπτόπουλο", "Φουρνά"                             },{"﻿Βολίμες", "Γαϊτάνι", "Ζάκυνθος", "Καταστάρι", "Μαχαιράδο", "Τσιλιβί"                                },{"﻿Αμαλιάδα", "Ανδραβίδα", "Ανδρίτσαινα", "Αρχαία Ολυμπία", "Βάρδα", "Βαρθολομιό", "Γαστούνη", "Επιτάλιο", "Εφύρα", "Ζαχάρω", "Καβάσιλας", "Καλλιθέα Ηλείας", "Καράτουλας", "Κατάκολο", "Κρέστενα", "Κυλλήνη", "Λάλας", "Λάμπεια", "Λεχαινά", "Μυρσίνη", "Νέα Φιγαλεία", "Πύργος Ηλείας", "Τραγανό", "Χάβαρι"              },{"﻿Αγγελοχώρι Ημαθίας", "Άγιος Γεώργιος Ημαθίας", "Αλεξάνδρεια", "Βεργίνα", "Βέροια", "Ειρηνούπολη", "Κοπανός", "Λαζοχώρι", "Μακροχώρι Ημαθίας", "Μελίκη", "Νάουσα", "Πατρίδα", "Πλατύ", "Σταυρός Ημαθίας"                        },{"﻿Αγία Βαρβάρα Ηρακλείου", "Άγιοι Δέκα", "Άγιος Μύρωνας", "Αηδονοχώρι", "Άνω Βιάννος", "Αρκαλοχώρι", "Αρχάνες", "Ασήμι", "Βασιλείες", "Γάζι", "Γούρνες Πεδιάδος", "Δάφνες", "Έμπαρος", "Επισκοπή Ηρακλείου", "Ζαρός", "Ηράκλειο", "Καρτερός Ελαίας", "Καστέλλι", "Κάτω Γούβες", "Κνωσσός", "Κουτουλουφάρι", "Κρουσώνας", "Λιμένας Χερσονήσου", "Μαλάδες", "Μάλια", "Μοίρες", "Μοχός", "Νέα Αλικαρνασσός", "Νέο Στάδιο", "Πόρος Ηρακλείου", "Πύργος Μονοφατσίου", "Σταλίδα", "Τυμπάκι", "Φοινικιά"    },{"﻿Γλυκή", "Γραικοχώρι", "Ηγουμενίτσα", "Λεπτοκαρυά Θεσπρωτίας", "Μαργαρίτι", "Μόρφιο", "Νέα Σελεύκεια", "Παραμυθιά", "Φιλιάτες"                             },{"﻿Αγγελοχώρι", "Αγία Τριάδα", "Άγιος Αθανάσιος", "Άγιος Παύλος", "Αγχίαλος", "Άδενδρο", "Αμπελόκηποι", "Ανατολικό", "Άνω Περαία", "Ασβεστοχώρι", "Ασκός", "Ασπροβάλτα", "Άσσηρος", "Βαθύλακκος", "Βασιλικά", "Βιομηχανική Περιοχή Θεσσαλονίκης", "Γαλήνη Θεσσαλονίκης", "Γέφυρα Θεσσαλονίκης", "Διαβατά", "Δρυμός", "Ελευθέριο", "Εξοχή", "Επανομή", "Ευκαρπία", "Εύοσμος", "Ζαγκλιβέρι", "Θέρμη", "Θεσσαλονίκη", "Ιωνία Θεσσαλονίκης", "Καλαμαριά", "Καλοχώρι", "Καρδιά", "Κάτω Σχολάρι", "Κουφάλια", "Κύμινα", "Λαγκαδάς", "Λαγκαδίκια", "Λητή", "Μελισσοχώρι", "Μενεμένη", "Νέα Απολλωνία", "Νέα Κερασιά", "Νέα Μαγνησία", "Νέα Μηχανιώνα", "Νέα Ραιδεστός", "Νεάπολη Θεσσαλονίκης", "Νέο Ρύσιο", "Νέοι Επιβάτες", "Νικόπολη", "Ξυλόπολη", "Πανόραμα", "Περαία", "Πεύκα Ρετζίκι", "Πλαγιάρι", "Πολίχνη", "Πυλαία", "Σίνδος", "Σουρωτή", "Σοχός", "Σταυρός", "Σταυρούπολη", "Συκιές", "Ταγαράδες", "Τριάδι Θέρμης", "Τριανδρία", "Τρίλοφος", "Φίλυρο", "Χαλάστρα", "Χαλκηδόνα", "Χορτιάτης", "Ωραιόκαστρο",                                                                                          },{"﻿Αμπελοχώρι", "Ανατολή", "Ασπράγγελοι", "Βαλανιδιά", "Βροσίνα", "Βρυσούλα", "Γρεβενίτιο", "Δελβινάκι", "Δερβίζιανα", "Δολιανά", "Ελεούσα", "Ζίτσα", "Ιωάννινα", "Καλέντζι", "Καρδαμίτσια", "Κατσικάς", "Κεφαλόβρυσο", "Κόνιτσα", "Κουκλέσι", "Μέτσοβο", "Μπάφρα", "Παλαιοσέλι", "Πεδινή", "Πέραμα Ιωαννίνων", "Περίβλεπτος", "Πράμαντα", "Πυρσόγιαννη", "Σεριζιανά", "Σταυράκι", "Τσεπέλοβο"        },{"﻿Αμυγδαλεώνας", "Ελευθερούπολη", "Ζυγός", "Θάσος", "Θεολόγος", "Καβάλα", "Κεραμωτή", "Κεχρόκαμπος", "Κρηνίδες", "Λιμενάρια", "Μουσθένη", "Νέα Ηρακλείτσα", "Νέα Καρβάλη", "Νέα Πέραμος Καβάλας", "Νικήσιανη", "Παλαιό Τσιφλίκι", "Πρίνος", "Χρυσούπολη"                    },{"﻿Αγναντερό", "Ανθηρό", "Βραγκιανά", "Ιτέα Καρδίτσας", "Καρδίτσα", "Καρδιτσομάγουλα", "Λεοντάρι Καρδίτσας", "Μεσενικόλας", "Μουζάκι", "Παλαμάς", "Προάστιο Καρδίτσας", "Ρεντίνα", "Σοφάδες", "Φανάρι"                        },{"﻿Ακρίτες", "Άργος Ορεστικού", "Βίτσι", "Βογατσικό", "Δισπηλιό", "Επταχώρι", "Καστοριά", "Κλεισούρα", "Κλήμα", "Κορησός", "Μακροχώρι", "Μανιάκοι", "Μαυροχώρι", "Μεσοποταμία", "Νέα Λεύκη", "Νεστόριο", "Χλόη"                     },{"﻿Γάιος", "Γουβιά", "Καρουσάδες", "Καστελλάνοι Μέσης", "Κέρκυρα", "Λευκίμμη", "Ποταμός", "Σκριπερό"                              },{"﻿Αγία Ευφημία", "Αργοστόλι", "Βασιλικιάδες", "Ιθάκη", "Ληξούρι", "Πόρος", "Σάμη", "Σταυρός Ιθάκης", "Φισκάρδο"                             },{"﻿Αξιούπολη", "Γουμένισσα", "Ευρωπός", "Κιλκίς", "Μικρόκαμπος", "Μουριές", "Πολύκαστρο", "Ριζανά", "Συνοριακός Σταθμός Ευζώνων", "Χέρσο"                            },{"﻿Αιανή", "Βελβεντός", "Εμπόριο", "Εράτυρα", "Κοζάνη", "Κρόκος", "Νεάπολη Κοζάνης", "Πεντάβρυσος", "Πεντάλοφος", "Προάστιο Κοζάνης", "Πτολεμαίδα", "Πύργοι", "Σέρβια", "Σιάτιστα", "Σκαλοχώρι", "Τσοτύλι"                      },{"﻿Άγιοι Θεόδωροι", "Αθίκια", "Αρχαία Κόρινθος", "Άσσος", "Βέλο", "Βραχάτι", "Γκούρα", "Δερβένι", "Ζευγολατειό", "Ίσθμια", "Καλιάνοι", "Κάτω Άσσος", "Κάτω Διμηνιό", "Κιάτο", "Κοκκώνι", "Κόρινθος", "Λέχαιο", "Λουτράκι", "Νεμέα", "Νεράντζα", "Ξυλόκαστρο", "Περαχώρα", "Περιγιάλι", "Σούλι", "Σοφικό", "Χιλιομόδι"            },{"﻿Αμοργός", "Ανάφη", "Άνδρος", "Αντίπαρος", "Άνω Σύρος", "Γαύριο", "Εμπορειό", "Ερμούπολη", "Θήρα", "Ίος", "Ιουλίδα", "Κίμωλος", "Κόρθιο", "Κορωνίδα", "Κύθνος", "Μήλος", "Μονόλιθος", "Μύκονος", "Νάξος", "Νάουσα Πάρου", "Οία", "Πάνορμος Τήνου", "Πάρος", "Σέριφος", "Σίκινος", "Σίφνος", "Σύρος", "Τάλαντα", "Τήνος", "Φολέγανδρος", "Χαλκείο"       },{"﻿Αρεόπολη", "Βλαχιώτης", "Γεράκι", "Γερολιμένας", "Γύθειο", "Καρυές Λακωνίας", "Καστόρειο", "Κροκεές", "Μολάοι", "Μονεμβασιά", "Νεάπολη Λακωνίας", "Νιάτα", "Ξηροκάμπι", "Παπαδιάνικα", "Σκάλα", "Σπάρτη"                      },{"﻿Αγιά", "Αμπελώνας", "Βερδικούσσα", "Γιάννουλη", "Γόννοι", "Ελασσόνα", "Κρανέα Ελασσόνας", "Λάρισα", "Λιβάδι", "Νίκαια Λάρισας", "Πλατύκαμπος", "Πυργετός", "Σκοπιά Φαρσάλων", "Συκούριο", "Τύρναβος", "Φαλάνη", "Φάρσαλα"                     },{"﻿Άγιος Νικόλαος Λασιθίου", "Άνω Σύμη", "Ελούντα", "Ιεράπετρα", "Κριτσά", "Μάλες", "Νεάπολη Κρήτης", "Σητεία", "Σταυροχώρι", "Τζερμιάδο", "Τουρλωτή", "Φουρνή", "Χανδράς"                         },{"﻿Αγία Παρασκευή Λέσβου", "Αγιάσος", "Άγιος Ευστράτιος", "Άντισσα", "Βαρειά", "Ερεσός", "Καλλιθέα Μύρινας", "Καλλονή", "Μανταμάδος", "Μήθυμνα", "Μούδρος", "Μύρινα", "Μυτιλήνη", "Παππάδος", "Πέραμα Λέσβου", "Πέτρα Λέσβου", "Πλωμάρι", "Πολίχνιτος"                    },{"﻿Βαθύ", "Βασιλική", "Κάλαμος", "Καρυά Λευκάδας", "Λευκάδα", "Νυδρί"                                },{"﻿Αγία Παρασκευή Βόλου", "Άγιος Γεώργιος Ιωλκού", "Αγριά", "Αλμυρός", "Αλόννησος", "Ανάβρα", "Ανακασιά", "Άνω Λεχώνια", "Αργαλαστή", "Αργυρόνησο", "Βελεστίνο", "Βόλος", "Διμήνιο Βόλου", "Ευξεινούπολη", "Ζαγορά", "Καλά Νερά", "Καλλιθέα Μαγνησίας", "Κάτω Λεχώνια", "Κήπια", "Μηλιές", "Μηλίνα", "Νέα Αγχίαλος", "Νέα Ιωνία Βόλου", "Νέες Παγασές", "Πορταριά", "Σκιάθος", "Σκόπελος", "Σούρπη", "Τρίκερι", "Τσαγκαράδα"        },{"﻿Άγιος Νικόλαος Μεσσηνίας", "Ανδρούσα", "Αριστομένης", "Ασπρόχωμα", "Βέργα", "Γαργαλιάνοι", "Διαβολίτσι", "Δώριο", "Θουρία", "Καλαμάτα", "Κάμπος", "Καρδαμύλη", "Κοπανάκι", "Κορώνη", "Κυπαρισσία", "Λογγά", "Μεθώνη Μεσσηνίας", "Μελιγαλάς", "Μεσσήνη", "Παραλία Καλαμάτας", "Πεταλίδι", "Πέτρα", "Πύλος", "Φαρές", "Φιλιατρά", "Χατζής", "Χώρα Τριφυλίας", "Ψάρι"          },{"﻿Άβδηρα", "Γενισέα", "Εύλαλο", "Εχίνος", "Μυρτούσσα", "Ξάνθη", "Πασχαλιά", "Πόρτο Λάγος", "Σταυρούπολη Ξάνθης", "Χρύσα"                            },{"﻿Αγροσυκιά", "Άθυρα", "Αριδαία", "Άρνισσα", "Γιαννιτσά", "Έδεσσα", "Εξαπλάτανος", "Καρυώτισσα", "Κρύα Βρύση", "Πέλλα", "Σκύδρα"                           },{"﻿Άγιος Δημήτριος Πιερίας", "Αιγίνιο", "Αλώνια", "Ανδρομάχη", "Αρωνάς", "Βριά", "Βροντού", "Γανοχώρα", "Ελατοχώρι", "Καλλιθέα Πιερίας", "Καρίτσα", "Κατερίνη", "Κατερινόσκαλα", "Κάτω Μηλιά", "Κίτρος", "Κολινδρός", "Κονταριώτισσα", "Κορινός", "Κούκκος", "Λαγορράχη", "Λεπτοκαρυά", "Λιτόχωρο", "Μακρύγιαλος", "Μεθώνη", "Νέα Έφεσος", "Νέα Τραπεζούντα", "Νέοι Πόροι", "Νεοκαισάρεια", "Νέος Παντελεήμονας", "Ολυμπιακή Ακτή", "Παλαιό Κεραμίδι", "Παραλία Κατερίνης", "Περίσταση", "Πλαταμώνας", "Ρητίνη", "Σβορώνος", "Σφενδάμι" },{"﻿Θεσπρωτικό", "Καναλλάκι", "Κοντάτες", "Λούρος", "Πάργα", "Πρέβεζα", "Φιλιππιάδα"                               },{"﻿Αγία Γαλήνη", "Αμάρι", "Ανώγεια", "Γαράζο", "Επισκοπή Ρεθύμνου", "Μύρθιος", "Πάνορμος", "Πέραμα Ρεθύμνου", "Πλατανές", "Πρινές Ρεθύμνου", "Ρέθυμνο", "Σπήλι"                          },{"﻿Αίγειρος", "Ίασμος", "Κομοτηνή", "Ξυλαγανή", "Οργάνη", "Σάπες"                                },{"﻿Άγιος Κήρυκος", "Βαθύ Σάμου", "Εύδηλος", "Καλάμι", "Καρλόβασι", "Μαραθόκαμπος", "Μυτιληνιοί", "Πυθαγόρειο", "Πύργος Σάμου", "Ράχες", "Σάμος", "Φούρνοι"                          },{"﻿Αλιστράτη", "Δραβήσκος", "Ηράκλεια", "Κάτω Πορόια", "Κεφαλοχώρι", "Λευκώνας", "Μαυροθάλασσα", "Νέα Ζίχνη", "Νέο Πετρίτσι", "Νέος Σκοπός", "Νιγρίτα", "Πεντάπολη", "Πρώτη Σερρών", "Ροδολίβος", "Ροδόπολη", "Σέρρες", "Σιδηρόκαστρο", "Στρυμονικό", "Τερπνή", "Χρυσό"                  },{"﻿Αγιόφυλλο", "Καλαμπάκα", "Καστανιά", "Κηπάκι", "Λεπτοκαρυά Τρικάλων", "Μεσοχώρα", "Μυρόφυλλο", "Οιχαλία", "Πύλη", "Ριζαρειό", "Τρίκαλα", "Φαρκαδώνα"                          },{"﻿Αγία Παρασκευή Φθιώτιδας", "Άγιος Γεώργιος Φθιώτιδας", "Άγιος Κωνσταντίνος Φθιώτιδας", "Αμφίκλεια", "Αταλάντη", "Δομοκός", "Ελάτεια", "Καμμένα Βούρλα", "Κάτω Τιθορέα", "Λαμία", "Λάρυμνα", "Λιβανάτες", "Μακρακώμη", "Μαλεσίνα", "Μαρτίνο", "Μεγάλη Βρύση", "Μπράλος", "Μώλος", "Πελασγία", "Ροδίτσα", "Σπερχειάδα", "Στυλίδα", "Υπάτη"               },{"﻿Άγιος Γερμανός", "Αμύνταιο", "Αντάρτικο", "Βαρικό", "Βεύη", "Λέχοβο", "Μελίτη", "Ξινό Νερό", "Σκλήθρο", "Φιλώτας", "Φλώρινα"                           },{"﻿Άμφισσα", "Γαλαξίδι", "Γραβιά", "Δελφοί", "Δεσφίνα", "Ερατεινή", "Ευπάλιο", "Ιτέα Φωκίδας", "Κίρρα", "Κροκύλειο", "Λιδορίκι", "Μαυρολιθάρι"                          },{"﻿Άγιος Νικόλαος Χαλκιδικής", "Αρναία", "Βεργιά", "Γαλάτιστα", "Γερακινή", "Δάφνη Αγίου Όρους", "Ιερισσός", "Καρυές", "Κασσάνδρεια", "Κρήμνη", "Μεγάλη Παναγιά", "Νέα Ηράκλεια", "Νέα Καλλικράτεια", "Νέα Μουδανιά", "Νέα Πλάγια", "Νέα Ποτείδαια", "Νέα Τρίγλια", "Νέος Μαρμαράς", "Νικήτη", "Ολυμπιάδα", "Ορμύλια", "Πευκοχώρι", "Πολύγυρος", "Συκιά", "Σωζόπολη", "Φλογητά", "Χανιώτης"           },{"﻿Άγιος Ιωάννης Χανίων", "Αλικιανός", "Βαθή Χανίων", "Βαμβακόπουλο", "Βάμος", "Βουκολιές", "Βρύσες", "Γαλατάς", "Δαράτσος", "Καλύβες", "Κάνδανος", "Κάτω Γαλατάς", "Κάτω Δαράτσος", "Κίσσαμος", "Κόκκινο Μετόχι Χανίων", "Κολυμβάρι", "Κορακιές", "Κουνουπιδιανά", "Μουρνιές", "Νεροκούρος", "Παζινός", "Παλαιοχώρα", "Περιβόλια Κυδωνίας", "Πλατανιάς", "Ροδοβάνι", "Σούδα", "Τσικαλαριά", "Χανιά", "Χώρα Σφακίων"         },{"﻿Βολισσός", "Βροντάδος", "Θυμιανά", "Καλαμωτή", "Καλλιμασιά", "Καρδάμυλα", "Οινούσσες", "Χίος", "Ψαρά"}};
 
    static volatile Matchmaking mm;
    static volatile OnlinePlayersThread opt;
    static volatile Rankings ra;
    static volatile KillDced kd;
    static volatile LiveRankings lr;
    public static volatile Session session;
    public static volatile Transport transport;
    static volatile Thread mmThread,optThread,raThread,lrThread,kdThread;
    static volatile boolean shuttingDown = false;
    
    public volatile static serverStart mainThread;
    public volatile static serverStop killThread;
    public volatile static ArrayList<String> allUsernames = new ArrayList<String>();
    public static Connection generalRootConn;
    
	public static void main(String[] args) {
		PipedOutputStream pOut = new PipedOutputStream();
		PipedOutputStream pOuterr = new PipedOutputStream();
		System.setOut(new PrintStream(pOut));
		System.setErr(new PrintStream(pOuterr));
		PipedInputStream pIn = null;
		try {
			pIn = new PipedInputStream(pOut);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PipedInputStream pInerr = null;
		try {
			pInerr = new PipedInputStream(pOuterr);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(pIn));
		BufferedReader readererr = new BufferedReader(new InputStreamReader(pInerr));
		new Thread(new Thread(){
			public void run(){
				while(true){
					try {
						if(reader.ready()){
					        String line = reader.readLine();
					        if(line != null) {
					            ServerGui.log(line+"\n");
					        }
						}
						if(readererr.ready()){
							String line = readererr.readLine();
							if(line!=null){
								ServerGui.log(line+"\n");
							}
						}
				    } catch (IOException ex) {
				        ex.printStackTrace();
				    }
				}
			}
		}).start();
		
			new ServerGui();
		  try {
			handler = new FileHandler("./revenge.log");
			handler.setLevel(Level.ALL);
			formatter = new SimpleFormatter();
			handler.setFormatter(formatter);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		  log.addHandler(handler);
		  log.setLevel(Level.ALL);
		  DBConnect.log.addHandler(handler);
		  DBConnect.log.setLevel(Level.ALL);
		  InGame.log.addHandler(handler);
		  InGame.log.setLevel(Level.ALL);
		  LiveRankings.log.addHandler(handler);
		  LiveRankings.log.setLevel(Level.ALL);
		  Login.log.addHandler(handler);
		  Login.log.setLevel(Level.ALL);
		  Matchmaking_GameFoundThreads.log.addHandler(handler);
		  Matchmaking_GameFoundThreads.log.setLevel(Level.ALL);
		  Matchmaking.log.addHandler(handler);
		  Matchmaking.log.setLevel(Level.ALL);
		  notificationHandler.log.addHandler(handler);
		  notificationHandler.log.setLevel(Level.ALL);
		  OnlinePlayersThread.log.addHandler(handler);
		  OnlinePlayersThread.log.setLevel(Level.ALL);
		  Player.log.addHandler(handler);
		  Player.log.setLevel(Level.ALL);
		  Rankings.log.addHandler(handler);
		  Rankings.log.setLevel(Level.ALL);
		  Signup.log.addHandler(handler);
		  Signup.log.setLevel(Level.ALL);
		  

	}
	
	   static {
		      registerHandler();
		   }
		   private static void registerHandler() {
		      Thread.setDefaultUncaughtExceptionHandler(
		            new Thread.UncaughtExceptionHandler() {
		               public void uncaughtException(Thread t, Throwable e) {
		                  reportError(e);
		               }
		            });
		   }


		  public static void reportError(Throwable t) {
			  log.log(Level.SEVERE, t.toString(), t);
		  }
	
	public static void stop(){
		killThread = new serverStop();
		killThread.start();
	}
	public static void start(){
		mainThread = new serverStart();
		mainThread.start();
	}

	static class serverStart extends Thread{
		public void run(){
			shuttingDown=false;

			
			try {
				generalRootConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/revengeGeneral?useUnicode=yes&characterEncoding=UTF-8","root",DBConnect.rootPass);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				log.log(Level.SEVERE, e1.toString(),e1);
			}
			System.out.println("Starting server....\n");
			mm = new Matchmaking();
			mmThread = new Thread(mm);
			mmThread.start();		
			System.out.println("Matchmaking initialized!");
			
			DBConnect.getAllUsernames();
			
			opt = new OnlinePlayersThread();
			optThread = new Thread(opt);
			optThread.start();		
			System.out.println("Online players initialized!");
			
			kd = new KillDced();
			kdThread = new Thread(kd);
			kdThread.start();		
			System.out.println("Disconnect killer initialized!");
			
			System.out.println("Initializing ranking system...");
			ra = new Rankings();
			raThread = new Thread(ra);
			raThread.start();
			
			lr = new LiveRankings();
			lrThread = new Thread(lr);
			lrThread.start();
			

			System.out.println("Setting up e-mail connection...");
			/////////////////////////////////////////////
			   transport = null;

		      // Sender's email ID needs to be mentioned


		      // Get system properties
		      Properties props = System.getProperties();
		      props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.host", "smtp.gmail.com");
				props.put("mail.smtp.port", "587");
			    String from = "@gmail.com";
		      // Get the default Session object.
				session = Session.getInstance(props,
						  new javax.mail.Authenticator() {
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(from, "pass");
							}
						  });
		      try{
		    	  transport = session.getTransport("smtp");
		    	  transport.connect(props.getProperty("mail.smtp.host"), 
		                  Integer.parseInt(props.getProperty("mail.smtp.port")),
		                  props.getProperty("mail.smtp.user"),
		                  props.getProperty("mail.smtp.password"));   
		      }catch (MessagingException mex) {
		         mex.printStackTrace();
		      }
		      ///////////////////////////////////
		      System.out.println("E-mail service connected!");
				try {


					serverSocket = new ServerSocket(4444); // Server socket
					nSSocket = new ServerSocket(4445);
					//nSSocket.setSoTimeout(1);
					System.out.println("\nServer started.Listening to ports 4444 - 4445.");
				} catch (IOException e) {
					System.out.println("Could not listen on port: 4444");
					e.printStackTrace();
				}
			while (true) {
				try {
					clientSocket =serverSocket.accept();
					nSocket = nSSocket.accept();
					System.out.println("new connection");
					Runnable connectionHandler = new ConnectionHandler(clientSocket,nSocket);
					new Thread(connectionHandler).start();
				} catch(SocketTimeoutException e){
					try {
						clientSocket.close();
					} catch (IOException e2) {
						log.log(Level.SEVERE,e2.toString(),e2);
					}
					
					log.log(Level.SEVERE,e.toString(),e);
					
					System.out.println("Problem accepting connection");
					if(shuttingDown){
						break;
					}
				} catch (Exception ex) {
					log.log(Level.SEVERE,ex.toString(),ex);
					
					System.out.println("Problem accepting connection");
					if(shuttingDown){
						break;
					}
				}
				
			}


		}
	}
	static class serverStop extends Thread{
		@SuppressWarnings("deprecation")
		public void run(){
			shuttingDown = true;
				try{
					serverSocket.close();		
				}catch(Exception e){
					log.log(Level.SEVERE,e.toString(),e);
				}
				try{
					nSSocket.close();	
				}catch(Exception e){
					log.log(Level.SEVERE,e.toString(),e);
				}
				mmThread.stop();
				kdThread.stop();
				optThread.stop();
				raThread.stop();
				if(lrThread.getState()==Thread.State.TIMED_WAITING){
					lrThread.stop();
				}else{
					lrThread.interrupt();
				}
				if(raThread.getState()==Thread.State.TIMED_WAITING){
					raThread.stop();
				}else{
					raThread.interrupt();
				}
			
			//record todays nomos + perioxi winners to db
			double[] revengeNomoi = LiveRankings.revengeTodayNomoi;
			String[] revengeNomoiNames = LiveRankings.nomoiTemp;
			double[] revengePerioxes = LiveRankings.revengeTodayPerioxes;
			String[] revengePerioxesNames = LiveRankings.perioxesTemp;
			
			String[] nomoiWinners = new String[3];
			String[] perioxesWinners = new String[10];
			
			for(int i =0;i<3;i++){
				if(revengeNomoi[i]!=0){
					nomoiWinners[i] = revengeNomoiNames[i];
				}else{
					nomoiWinners[i] = "nobody";
				}
			}
			for(int i=0;i<10;i++){
				if(revengePerioxes[i]!=0){
					perioxesWinners[i] = revengePerioxesNames[i];
				}else{
					perioxesWinners[i] = "nobody";
				}
			}
			DBConnect dbc = new DBConnect("root",null,null);
			ServerGui.log("\nRecording today's data...");
			dbc.submitNomoi(nomoiWinners);
			dbc.submitPerioxes(perioxesWinners);
			ServerGui.log("\nResetting today's top 100...");
			dbc.resetDailies();
			try{
				Thread.sleep(10000);
			}catch(Exception e){
				log.log(Level.SEVERE,e.toString(),e);
			}
			ServerGui.log("Server shut down");
			System.exit(0);
		}
	}
 
}
/*
todo:
	check for decline replay even after replay has been requested by other user
	fix timeout on server socket
	fix low res screens highscores
	fix xperia ingame vs color?
	hide keyboard without pressing back
	chat auto scroll down
	dont show null in highscores
	graphics:signup boy/girl + select nomos background + kare
	is data always there or somethimes nothing gets sent because of arrays=null?
	
	bonuses 3/3 5/5 10/10 (logic complete, need to find the bonuses)
	try indexed usernames
	prevent sql+openfire outside access
	spelling + grammar check
	implement anti-boosting with friend to get hs
	implement anti-hacking(dont allow score inputs < certain minimum value)(find from beta testing)
	facebook signup

todo first patch:
	add current user to top 100 + top 100 daily + top 100 of his area + top 100 of his nomo(with his rank + points)
	multiplayer

bugs:
	none!
*/