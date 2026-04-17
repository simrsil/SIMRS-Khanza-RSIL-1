package rekammedis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import kepegawaian.DlgCariPetugas;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 *
 * @author perpustakaan
 */
public final class RMPenilaianAwalKeperawatanBayiAnak extends javax.swing.JDialog {

    private final DefaultTableModel tabMode, tabModeMasalah, tabModeDetailMasalah, tabModeImunisasi, tabModeImunisasi2, tabModeRencana, tabModeDetailRencana, tabModeKebutuhanEdukasi, tabModeRencanaEdukasi, tabModeDetailKebutuhanEdukasi, tabModeDetailRencanaEdukasi;
    private Connection koneksi = koneksiDB.condb();
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private PreparedStatement ps, ps2, ps3;
    private ResultSet rs, rs2, rs3;
    private int i = 0, jml = 0, index = 0;
    private DlgCariPetugas petugas;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean ceksukses = false;
    private boolean[] pilih;
    private String[] kode, masalah;
    private String masalahkeperawatan = "", htmlke1 = "", htmlke2 = "", htmlke3 = "", htmlke4 = "", htmlke5 = "", htmlke6 = "", finger = "";
    private boolean ke1 = false, ke2 = false, ke3 = false, ke4 = false, ke5 = false, ke6 = false;
    private File file;
    private FileWriter fileWriter;
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNode root;
    private JsonNode response;
    private FileReader myObj;
    private String TANGGALMUNDUR = "yes";

    /**
     * Creates new form DlgRujuk
     *
     * @param parent
     * @param modal
     */
    public RMPenilaianAwalKeperawatanBayiAnak(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        DlgRiwayatImunisasi.setSize(465, 112);

        tabMode = new DefaultTableModel(null, new Object[]{
            "No.Rawat", "No.RM", "Nama Pasien", "J.K.", "Agama", "Bahasa", "Cacat Fisik", "Tgl.Lahir", "Tgl.Asuhan", "Informasi", "TD", "Nadi", "RR", "Suhu",
            "GCS", "BB", "TB", "LP", "LK", "LD", "Keluhan Utama", "Riwayat Penyakit Dahulu", "Riwayat Penyakit Keluarga", "Riwayat Pengobatan", "Alergi",
            "Anak Ke", "Dari", "Cara Kelahiran", "Ket.Cara Kelahiran", "Umur Kelahiran", "Kelainan Bawaan", "Ket.Kelainan Bawaan", "Tengkurap",
            "Duduk", "Berdiri", "Gigi Pertama", "Berjalan", "Bicara", "Membaca", "Menulis", "Gangguan Emosi", "Alat Bantu", "Ket.Alat Bantu", "Prothesa",
            "Ket.Prothesa", "ADL", "Status Psikologis", "Ket.Psikologis", "Hubungan Keluarga", "Pengasuh", "Ket.Pengasuh", "Ekonomi", "Budaya", "Ket.Budaya",
            "Edukasi", "Ket.Edukasi", "Kemampuan Baca Tulis", "Butuh Penerjemah", "Ket Butuh Penerjemah", "Terdapat Hambatan Belajar", "Hambatan Belajar", "Ket Hambatan Belajar",
            "Hambatan Cara Bicara", "Hambatan Bahasa Isyarat", "Cara Belajar Disukai", "Kesediaan Menerima Informasi", "Ket Kesediaan Menerima Informasi",
            "Pemahaman Nutrisi", "Pemahaman Penyakit", "Pemahaman Pengobatan", "Pemahaman Perawatan", "Keyakinan Nilai", "Keterbatasan Fisik", "Hambatan Emosional",
            "Motivasi", "Humpty Dumpty Skala 1", "Nilai H.D.1", "Humpty Dumpty Skala 2",
            "Nilai H.D.2", "Humpty Dumpty Skala 3", "Nilai H.D.3", "Humpty Dumpty Skala 4", "Nilai H.D.4", "Humpty Dumpty Skala 5", "Nilai H.D.5", "Humpty Dumpty Skala 6", "Nilai H.D.6",
            "Humpty Dumpty Skala 7", "Nilai H.D.7", "Total Nilai H.D.", "Keterangan Hasil Pengkajian H.D.",
            "S.G. 1", "N.G. 1", "S.G. 2", "N.G. 2", "S.G. 3", "N.G. 3", "S.G. 4", "N.G. 4", "T.S. Gizi", "Skala Wajah", "N.S. Wajah", "Skala Kaki",
            "N.S. Kaki", "Skala Aktifitas", "N.S. Aktifitas", "Skala Menangis", "N.S. Menangis", "Skala Bersuara", "N.S. Bersuara", "Skala Nyeri",
            "Kondisi Nyeri", "Lokasi", "Durasi", "Frekuensi", "Nyeri Hilang Bila", "Ket Nyeri Hilang", "Diberitahukan Dokter", "Jam Diberitahukan", "Informasi Perencanaan Pulang",
            "Lama Rata-Rata", "Tanggal Pulang", "Kondisi Saat Pulang", "Perawatan Lanjutan", "Cara Transportasi", "Transportasi Digunakan", "Rencana Keperawatan Lainnya", "NIP", "Nama Petugas"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tbObat.setModel(tabMode);

        tbObat.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 125; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(105);
            } else if (i == 1) {
                column.setPreferredWidth(65);
            } else if (i == 2) {
                column.setPreferredWidth(160);
            } else if (i == 3) {
                column.setPreferredWidth(50);
            } else if (i == 4) {
                column.setPreferredWidth(60);
            } else if (i == 5) {
                column.setPreferredWidth(90);
            } else if (i == 6) {
                column.setPreferredWidth(90);
            } else if (i == 7) {
                column.setPreferredWidth(65);
            } else if (i == 8) {
                column.setPreferredWidth(120);
            } else if (i == 9) {
                column.setPreferredWidth(90);
            } else if (i == 10) {
                column.setPreferredWidth(35);
            } else if (i == 11) {
                column.setPreferredWidth(40);
            } else if (i == 12) {
                column.setPreferredWidth(35);
            } else if (i == 13) {
                column.setPreferredWidth(40);
            } else if (i == 14) {
                column.setPreferredWidth(35);
            } else if (i == 15) {
                column.setPreferredWidth(35);
            } else if (i == 16) {
                column.setPreferredWidth(35);
            } else if (i == 17) {
                column.setPreferredWidth(35);
            } else if (i == 18) {
                column.setPreferredWidth(35);
            } else if (i == 19) {
                column.setPreferredWidth(35);
            } else if (i == 20) {
                column.setPreferredWidth(180);
            } else if (i == 21) {
                column.setPreferredWidth(180);
            } else if (i == 22) {
                column.setPreferredWidth(180);
            } else if (i == 23) {
                column.setPreferredWidth(180);
            } else if (i == 24) {
                column.setPreferredWidth(100);
            } else if (i == 25) {
                column.setPreferredWidth(47);
            } else if (i == 26) {
                column.setPreferredWidth(30);
            } else if (i == 27) {
                column.setPreferredWidth(80);
            } else if (i == 28) {
                column.setPreferredWidth(130);
            } else if (i == 29) {
                column.setPreferredWidth(85);
            } else if (i == 30) {
                column.setPreferredWidth(90);
            } else if (i == 31) {
                column.setPreferredWidth(130);
            } else if (i == 32) {
                column.setPreferredWidth(70);
            } else if (i == 33) {
                column.setPreferredWidth(70);
            } else if (i == 34) {
                column.setPreferredWidth(70);
            } else if (i == 35) {
                column.setPreferredWidth(70);
            } else if (i == 36) {
                column.setPreferredWidth(70);
            } else if (i == 37) {
                column.setPreferredWidth(70);
            } else if (i == 38) {
                column.setPreferredWidth(70);
            } else if (i == 39) {
                column.setPreferredWidth(70);
            } else if (i == 40) {
                column.setPreferredWidth(120);
            } else if (i == 41) {
                column.setPreferredWidth(60);
            } else if (i == 42) {
                column.setPreferredWidth(120);
            } else if (i == 43) {
                column.setPreferredWidth(60);
            } else if (i == 44) {
                column.setPreferredWidth(120);
            } else if (i == 45) {
                column.setPreferredWidth(55);
            } else if (i == 46) {
                column.setPreferredWidth(90);
            } else if (i == 47) {
                column.setPreferredWidth(120);
            } else if (i == 48) {
                column.setPreferredWidth(110);
            } else if (i == 49) {
                column.setPreferredWidth(90);
            } else if (i == 50) {
                column.setPreferredWidth(120);
            } else if (i == 51) {
                column.setPreferredWidth(55);
            } else if (i == 52) {
                column.setPreferredWidth(55);
            } else if (i == 53) {
                column.setPreferredWidth(120);
            } else if (i == 54) {
                column.setPreferredWidth(60);
            } else if (i == 55) {
                column.setPreferredWidth(120);
            } else if (i == 56) {
                column.setPreferredWidth(100);
            } else if (i == 57) {
                column.setPreferredWidth(100);
            } else if (i == 58) {
                column.setPreferredWidth(115);
            } else if (i == 59) {
                column.setPreferredWidth(190);
            } else if (i == 60) {
                column.setPreferredWidth(100);
            } else if (i == 61) {
                column.setPreferredWidth(65);
            } else if (i == 62) {
                column.setPreferredWidth(40);
            } else if (i == 63) {
                column.setPreferredWidth(40);
            } else if (i == 64) {
                column.setPreferredWidth(40);
            } else if (i == 65) {
                column.setPreferredWidth(40);
            } else if (i == 66) {
                column.setPreferredWidth(40);
            } else if (i == 67) {
                column.setPreferredWidth(40);
            } else if (i == 68) {
                column.setPreferredWidth(40);
            } else if (i == 69) {
                column.setPreferredWidth(40);
            } else if (i == 70) {
                column.setPreferredWidth(50);
            } else if (i == 71) {
                column.setPreferredWidth(180);
            } else if (i == 72) {
                column.setPreferredWidth(62);
            } else if (i == 73) {
                column.setPreferredWidth(170);
            } else if (i == 74) {
                column.setPreferredWidth(55);
            } else if (i == 75) {
                column.setPreferredWidth(180);
            } else if (i == 76) {
                column.setPreferredWidth(70);
            } else if (i == 77) {
                column.setPreferredWidth(170);
            } else if (i == 78) {
                column.setPreferredWidth(75);
            } else if (i == 79) {
                column.setPreferredWidth(170);
            } else if (i == 80) {
                column.setPreferredWidth(75);
            } else if (i == 81) {
                column.setPreferredWidth(65);
            } else if (i == 82) {
                column.setPreferredWidth(85);
            } else if (i == 83) {
                column.setPreferredWidth(110);
            } else if (i == 84) {
                column.setPreferredWidth(65);
            } else if (i == 85) {
                column.setPreferredWidth(65);
            } else if (i == 86) {
                column.setPreferredWidth(105);
            } else if (i == 87) {
                column.setPreferredWidth(110);
            } else if (i == 88) {
                column.setPreferredWidth(110);
            } else if (i == 89) {
                column.setPreferredWidth(100);
            } else if (i == 90) {
                column.setPreferredWidth(250);
            } else if (i == 91) {
                column.setPreferredWidth(90);
            } else if (i == 92) {
                column.setPreferredWidth(150);
            } else if (i == 93) {
                column.setPreferredWidth(75);
            } else if (i == 94) {
                column.setPreferredWidth(170);
            } else if (i == 95) {
                column.setPreferredWidth(75);
            } else if (i == 96) {
                column.setPreferredWidth(65);
            } else if (i == 97) {
                column.setPreferredWidth(85);
            } else if (i == 98) {
                column.setPreferredWidth(170);
            } else if (i == 99) {
                column.setPreferredWidth(75);
            } else if (i == 100) {
                column.setPreferredWidth(65);
            } else if (i == 101) {
                column.setPreferredWidth(85);
            } else if (i == 102) {
                column.setPreferredWidth(110);
            } else if (i == 103) {
                column.setPreferredWidth(65);
            } else if (i == 104) {
                column.setPreferredWidth(65);
            } else if (i == 105) {
                column.setPreferredWidth(105);
            } else if (i == 106) {
                column.setPreferredWidth(110);
            } else if (i == 107) {
                column.setPreferredWidth(110);
            } else if (i == 108) {
                column.setPreferredWidth(100);
            } else if (i == 109) {
                column.setPreferredWidth(250);
            } else if (i == 110) {
                column.setPreferredWidth(90);
            } else if (i == 111) {
                column.setPreferredWidth(150);
            } else if (i == 112) {
                column.setPreferredWidth(75);
            } else if (i == 113) {
                column.setPreferredWidth(170);
            } else if (i == 114) {
                column.setPreferredWidth(75);
            } else if (i == 115) {
                column.setPreferredWidth(65);
            } else if (i == 116) {
                column.setPreferredWidth(85);
            } else if (i == 117) {
                column.setPreferredWidth(90);
            } else if (i == 118) {
                column.setPreferredWidth(150);
            } else if (i == 119) {
                column.setPreferredWidth(75);
            } else if (i == 120) {
                column.setPreferredWidth(170);
            } else if (i == 121) {
                column.setPreferredWidth(75);
            } else if (i == 122) {
                column.setPreferredWidth(65);
            } else if (i == 123) {
                column.setPreferredWidth(85);
            } else if (i == 124) {
                column.setPreferredWidth(85);
            }
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());

        tabModeMasalah = new DefaultTableModel(null, new Object[]{
            "P", "KODE", "MASALAH KEPERAWATAN"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
                if (colIndex == 0) {
                    a = true;
                }
                return a;
            }
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        tbMasalahKeperawatan.setModel(tabModeMasalah);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbMasalahKeperawatan.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbMasalahKeperawatan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 3; i++) {
            TableColumn column = tbMasalahKeperawatan.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(20);
            } else if (i == 1) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 2) {
                column.setPreferredWidth(350);
            }
        }
        tbMasalahKeperawatan.setDefaultRenderer(Object.class, new WarnaTable());

        tabModeDetailMasalah = new DefaultTableModel(null, new Object[]{
            "Kode", "Masalah Keperawatan"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tbMasalahDetailMasalah.setModel(tabModeDetailMasalah);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbMasalahDetailMasalah.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbMasalahDetailMasalah.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 2; i++) {
            TableColumn column = tbMasalahDetailMasalah.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 1) {
                column.setPreferredWidth(420);
            }
        }
        tbMasalahDetailMasalah.setDefaultRenderer(Object.class, new WarnaTable());

        tabModeImunisasi = new DefaultTableModel(null, new Object[]{
            "Kode", "Nama Imunisasi", "Ke 1", "Ke 2", "Ke 3", "Ke 4", "Ke 5", "Ke 6"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
            Class[] types = new Class[]{
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class,
                java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        tbImunisasi.setModel(tabModeImunisasi);
        tbImunisasi.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbImunisasi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 8; i++) {
            TableColumn column = tbImunisasi.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 1) {
                column.setPreferredWidth(250);
            } else {
                column.setPreferredWidth(50);
            }
        }
        tbImunisasi.setDefaultRenderer(Object.class, new WarnaTable());

        tabModeImunisasi2 = new DefaultTableModel(null, new Object[]{
            "Kode", "Nama Imunisasi", "Ke 1", "Ke 2", "Ke 3", "Ke 4", "Ke 5", "Ke 6"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
            Class[] types = new Class[]{
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class,
                java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        tbImunisasi2.setModel(tabModeImunisasi2);
        tbImunisasi2.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbImunisasi2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 8; i++) {
            TableColumn column = tbImunisasi2.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 1) {
                column.setPreferredWidth(205);
            } else {
                column.setPreferredWidth(35);
            }
        }
        tbImunisasi2.setDefaultRenderer(Object.class, new WarnaTable());

        tabModeRencana = new DefaultTableModel(null, new Object[]{
            "P", "KODE", "RENCANA KEPERAWATAN"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
                if (colIndex == 0) {
                    a = true;
                }
                return a;
            }
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        tbRencanaKeperawatan.setModel(tabModeRencana);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbRencanaKeperawatan.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbRencanaKeperawatan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 3; i++) {
            TableColumn column = tbRencanaKeperawatan.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(20);
            } else if (i == 1) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 2) {
                column.setPreferredWidth(350);
            }
        }
        tbRencanaKeperawatan.setDefaultRenderer(Object.class, new WarnaTable());

        tabModeDetailRencana = new DefaultTableModel(null, new Object[]{
            "Kode", "Rencana Keperawatan"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tbRencanaDetail.setModel(tabModeDetailRencana);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbRencanaDetail.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbRencanaDetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 2; i++) {
            TableColumn column = tbRencanaDetail.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 1) {
                column.setPreferredWidth(420);
            }
        }
        tbRencanaDetail.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte) 17).getKata(TNoRw));
        TD.setDocument(new batasInput((byte) 8).getKata(TD));
        Nadi.setDocument(new batasInput((byte) 5).getKata(Nadi));
        RR.setDocument(new batasInput((byte) 5).getKata(RR));
        Suhu.setDocument(new batasInput((byte) 5).getKata(Suhu));
        GCS.setDocument(new batasInput((byte) 5).getKata(GCS));
        BB.setDocument(new batasInput((byte) 5).getKata(BB));
        TB.setDocument(new batasInput((byte) 5).getKata(TB));
        LP.setDocument(new batasInput((byte) 5).getKata(LP));
        LK.setDocument(new batasInput((byte) 5).getKata(LK));
        LD.setDocument(new batasInput((byte) 5).getKata(LD));
        KeluhanUtama.setDocument(new batasInput((int) 150).getKata(KeluhanUtama));
        RPD.setDocument(new batasInput((int) 100).getKata(RPD));
        RPK.setDocument(new batasInput((int) 100).getKata(RPK));
        RPO.setDocument(new batasInput((int) 100).getKata(RPO));
        Alergi.setDocument(new batasInput((int) 25).getKata(Alergi));
        Anakke.setDocument(new batasInput((byte) 2).getKata(Anakke));
        DariSaudara.setDocument(new batasInput((byte) 2).getKata(DariSaudara));
        KetCaraKelahiran.setDocument(new batasInput((byte) 30).getKata(KetCaraKelahiran));
        KetKelainanBawaan.setDocument(new batasInput((byte) 30).getKata(KetKelainanBawaan));
        UsiaTengkurap.setDocument(new batasInput((byte) 15).getKata(UsiaTengkurap));
        UsiaDuduk.setDocument(new batasInput((byte) 15).getKata(UsiaDuduk));
        UsiaBerdiri.setDocument(new batasInput((byte) 15).getKata(UsiaBerdiri));
        UsiaGigi.setDocument(new batasInput((byte) 15).getKata(UsiaGigi));
        UsiaBerjalan.setDocument(new batasInput((byte) 15).getKata(UsiaBerjalan));
        UsiaBicara.setDocument(new batasInput((byte) 15).getKata(UsiaBicara));
        UsiaMembaca.setDocument(new batasInput((byte) 15).getKata(UsiaMembaca));
        UsiaMenulis.setDocument(new batasInput((byte) 15).getKata(UsiaMenulis));
        GangguanEmosi.setDocument(new batasInput((int) 50).getKata(GangguanEmosi));
        KetBantu.setDocument(new batasInput((int) 50).getKata(KetBantu));
        KetProthesa.setDocument(new batasInput((int) 50).getKata(KetProthesa));
        KetBudaya.setDocument(new batasInput((int) 50).getKata(KetBudaya));
        KetPsiko.setDocument(new batasInput((int) 70).getKata(KetPsiko));
        KetPengasuh.setDocument(new batasInput((int) 40).getKata(KetPengasuh));
        KetEdukasi.setDocument(new batasInput((int) 50).getKata(KetEdukasi));
        Lokasi.setDocument(new batasInput((int) 50).getKata(Lokasi));
        Durasi.setDocument(new batasInput((int) 25).getKata(Durasi));
        Frekuensi.setDocument(new batasInput((int) 25).getKata(Frekuensi));
        KetNyeri.setDocument(new batasInput((int) 40).getKata(KetNyeri));
        KetDokter.setDocument(new batasInput((int) 15).getKata(KetDokter));
        Rencana.setDocument(new batasInput((int) 200).getKata(Rencana));

        TCari.setDocument(new batasInput((int) 100).getKata(TCari));

        HTMLEditorKit kit = new HTMLEditorKit();
        LoadHTML.setEditable(true);
        LoadHTML.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule(
                ".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"
                + ".isi2 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#323232;}"
                + ".isi3 td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"
                + ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"
                + ".isi5 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#AA0000;}"
                + ".isi6 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#FF0000;}"
                + ".isi7 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#C8C800;}"
                + ".isi8 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#00AA00;}"
                + ".isi9 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#969696;}"
        );
        Document doc = kit.createDefaultDocument();
        LoadHTML.setDocument(doc);

        tabModeKebutuhanEdukasi = new DefaultTableModel(null, new Object[]{
            "P", "KODE", "KEBUTUHAN EDUKASI"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
                if (colIndex == 0) {
                    a = true;
                }
                return a;
            }
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        tbKebutuhanEdukasi.setModel(tabModeKebutuhanEdukasi);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbKebutuhanEdukasi.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbKebutuhanEdukasi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 3; i++) {
            TableColumn column = tbKebutuhanEdukasi.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(20);
            } else if (i == 1) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 2) {
                column.setPreferredWidth(350);
            }
        }
        tbKebutuhanEdukasi.setDefaultRenderer(Object.class, new WarnaTable());
        tabModeRencanaEdukasi = new DefaultTableModel(null, new Object[]{
            "P", "KODE", "RENCANA EDUKASI"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
                if (colIndex == 0) {
                    a = true;
                }
                return a;
            }
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        tbRencanaEdukasi.setModel(tabModeRencanaEdukasi);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbRencanaEdukasi.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbRencanaEdukasi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 3; i++) {
            TableColumn column = tbRencanaEdukasi.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(20);
            } else if (i == 1) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 2) {
                column.setPreferredWidth(350);
            }
        }
        tbRencanaEdukasi.setDefaultRenderer(Object.class, new WarnaTable());
        tabModeDetailKebutuhanEdukasi = new DefaultTableModel(null, new Object[]{
            "Kode", "Kebutuhan Edukasi"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tbDetailKebutuhanEdukasi.setModel(tabModeDetailKebutuhanEdukasi);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbDetailKebutuhanEdukasi.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbDetailKebutuhanEdukasi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 2; i++) {
            TableColumn column = tbDetailKebutuhanEdukasi.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 1) {
                column.setPreferredWidth(420);
            }
        }
        tbDetailKebutuhanEdukasi.setDefaultRenderer(Object.class, new WarnaTable());
        tabModeDetailRencanaEdukasi = new DefaultTableModel(null, new Object[]{
            "Kode", "Rencana Edukasi"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tbDetailRencanaEdukasi.setModel(tabModeDetailRencanaEdukasi);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbDetailRencanaEdukasi.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbDetailRencanaEdukasi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 2; i++) {
            TableColumn column = tbDetailRencanaEdukasi.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 1) {
                column.setPreferredWidth(420);
            }
        }
        tbDetailKebutuhanEdukasi.setDefaultRenderer(Object.class, new WarnaTable());

        try {
            TANGGALMUNDUR = koneksiDB.TANGGALMUNDUR();
        } catch (Exception e) {
            TANGGALMUNDUR = "yes";
        }

        ChkAccor.setSelected(false);
        isMenu();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LoadHTML = new widget.editorpane();
        DlgRiwayatImunisasi = new javax.swing.JDialog();
        internalFrame4 = new widget.InternalFrame();
        panelBiasa2 = new widget.PanelBiasa();
        jLabel99 = new widget.Label();
        BtnKeluarImunisasi = new widget.Button();
        BtnSimpanImunisasi = new widget.Button();
        BtnImunisasi = new widget.Button();
        NmImunisasi = new widget.TextBox();
        KdImunisasi = new widget.TextBox();
        jLabel43 = new widget.Label();
        ImunisasiKe = new widget.ComboBox();
        BtnHapusImunisasi = new widget.Button();
        TanggalRegistrasi = new widget.TextBox();
        internalFrame1 = new widget.InternalFrame();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        BtnPrint = new widget.Button();
        BtnAll = new widget.Button();
        BtnKeluar = new widget.Button();
        TabRawat = new javax.swing.JTabbedPane();
        internalFrame2 = new widget.InternalFrame();
        scrollInput = new widget.ScrollPane();
        FormInput = new widget.PanelBiasa();
        TNoRw = new widget.TextBox();
        TPasien = new widget.TextBox();
        TNoRM = new widget.TextBox();
        label14 = new widget.Label();
        KdPetugas = new widget.TextBox();
        NmPetugas = new widget.TextBox();
        BtnPetugas = new widget.Button();
        jLabel8 = new widget.Label();
        TglLahir = new widget.TextBox();
        Jk = new widget.TextBox();
        jLabel10 = new widget.Label();
        label11 = new widget.Label();
        jLabel11 = new widget.Label();
        jLabel16 = new widget.Label();
        Nadi = new widget.TextBox();
        jLabel17 = new widget.Label();
        jLabel18 = new widget.Label();
        Suhu = new widget.TextBox();
        jLabel22 = new widget.Label();
        TD = new widget.TextBox();
        jLabel20 = new widget.Label();
        jLabel23 = new widget.Label();
        jLabel25 = new widget.Label();
        RR = new widget.TextBox();
        jLabel26 = new widget.Label();
        jLabel36 = new widget.Label();
        Informasi = new widget.ComboBox();
        jLabel53 = new widget.Label();
        TglAsuhan = new widget.Tanggal();
        jLabel28 = new widget.Label();
        GCS = new widget.TextBox();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel24 = new widget.Label();
        TB = new widget.TextBox();
        jLabel27 = new widget.Label();
        jLabel29 = new widget.Label();
        LK = new widget.TextBox();
        jLabel30 = new widget.Label();
        BB = new widget.TextBox();
        jLabel31 = new widget.Label();
        jLabel32 = new widget.Label();
        jLabel33 = new widget.Label();
        LP = new widget.TextBox();
        jLabel35 = new widget.Label();
        jLabel37 = new widget.Label();
        LD = new widget.TextBox();
        jLabel38 = new widget.Label();
        jLabel9 = new widget.Label();
        jLabel39 = new widget.Label();
        Alergi = new widget.TextBox();
        scrollPane1 = new widget.ScrollPane();
        KeluhanUtama = new widget.TextArea();
        jLabel40 = new widget.Label();
        scrollPane2 = new widget.ScrollPane();
        RPD = new widget.TextArea();
        jLabel41 = new widget.Label();
        scrollPane3 = new widget.ScrollPane();
        RPK = new widget.TextArea();
        jLabel42 = new widget.Label();
        scrollPane4 = new widget.ScrollPane();
        RPO = new widget.TextArea();
        jLabel94 = new widget.Label();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel95 = new widget.Label();
        jLabel44 = new widget.Label();
        Anakke = new widget.TextBox();
        DariSaudara = new widget.TextBox();
        jLabel45 = new widget.Label();
        jLabel46 = new widget.Label();
        jLabel55 = new widget.Label();
        CaraKelahiran = new widget.ComboBox();
        KetCaraKelahiran = new widget.TextBox();
        KelainanBawaan = new widget.ComboBox();
        jLabel56 = new widget.Label();
        KetKelainanBawaan = new widget.TextBox();
        UmurKelahiran = new widget.ComboBox();
        jLabel57 = new widget.Label();
        jLabel58 = new widget.Label();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel96 = new widget.Label();
        Scroll6 = new widget.ScrollPane();
        tbImunisasi = new widget.Table();
        BtnTambahImunisasi = new widget.Button();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel97 = new widget.Label();
        jLabel59 = new widget.Label();
        UsiaTengkurap = new widget.TextBox();
        jLabel60 = new widget.Label();
        UsiaDuduk = new widget.TextBox();
        jLabel61 = new widget.Label();
        UsiaBerdiri = new widget.TextBox();
        jLabel62 = new widget.Label();
        UsiaGigi = new widget.TextBox();
        jLabel63 = new widget.Label();
        UsiaBerjalan = new widget.TextBox();
        jLabel64 = new widget.Label();
        UsiaBicara = new widget.TextBox();
        jLabel65 = new widget.Label();
        UsiaMembaca = new widget.TextBox();
        UsiaMenulis = new widget.TextBox();
        jLabel66 = new widget.Label();
        GangguanEmosi = new widget.TextBox();
        jLabel67 = new widget.Label();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel125 = new widget.Label();
        jLabel127 = new widget.Label();
        AlatBantu = new widget.ComboBox();
        KetBantu = new widget.TextBox();
        Prothesa = new widget.ComboBox();
        KetProthesa = new widget.TextBox();
        jLabel128 = new widget.Label();
        ADL = new widget.ComboBox();
        jLabel129 = new widget.Label();
        jLabel130 = new widget.Label();
        CacatFisik = new widget.TextBox();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel131 = new widget.Label();
        StatusPsiko = new widget.ComboBox();
        KetPsiko = new widget.TextBox();
        jLabel132 = new widget.Label();
        Bahasa = new widget.TextBox();
        jLabel133 = new widget.Label();
        jLabel134 = new widget.Label();
        jLabel135 = new widget.Label();
        HubunganKeluarga = new widget.ComboBox();
        Pengasuh = new widget.ComboBox();
        KetPengasuh = new widget.TextBox();
        jLabel136 = new widget.Label();
        Ekonomi = new widget.ComboBox();
        jLabel137 = new widget.Label();
        jLabel138 = new widget.Label();
        StatusBudaya = new widget.ComboBox();
        KetBudaya = new widget.TextBox();
        jLabel139 = new widget.Label();
        Edukasi = new widget.ComboBox();
        KetEdukasi = new widget.TextBox();
        jLabel140 = new widget.Label();
        Agama = new widget.TextBox();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel141 = new widget.Label();
        jLabel149 = new widget.Label();
        jSeparator10 = new javax.swing.JSeparator();
        SG1 = new widget.ComboBox();
        jLabel150 = new widget.Label();
        jLabel152 = new widget.Label();
        SG2 = new widget.ComboBox();
        jLabel154 = new widget.Label();
        jLabel155 = new widget.Label();
        jLabel156 = new widget.Label();
        SG3 = new widget.ComboBox();
        jLabel158 = new widget.Label();
        jLabel159 = new widget.Label();
        jLabel160 = new widget.Label();
        SkalaWajah = new widget.ComboBox();
        jLabel162 = new widget.Label();
        NilaiGizi1 = new widget.TextBox();
        jSeparator11 = new javax.swing.JSeparator();
        jLabel163 = new widget.Label();
        jLabel164 = new widget.Label();
        jLabel165 = new widget.Label();
        jLabel166 = new widget.Label();
        jLabel167 = new widget.Label();
        jLabel168 = new widget.Label();
        jLabel169 = new widget.Label();
        SG4 = new widget.ComboBox();
        NilaiGizi2 = new widget.TextBox();
        NilaiGizi3 = new widget.TextBox();
        NilaiGizi4 = new widget.TextBox();
        NilaiWajah = new widget.TextBox();
        TotalNilaiGizi = new widget.TextBox();
        SkalaKaki = new widget.ComboBox();
        NilaiKaki = new widget.TextBox();
        SkalaAktifitas = new widget.ComboBox();
        NilaiAktifitas = new widget.TextBox();
        SkalaMenangis = new widget.ComboBox();
        NilaiMenangis = new widget.TextBox();
        SkalaBersuara = new widget.ComboBox();
        NilaiBersuara = new widget.TextBox();
        PanelWall = new usu.widget.glass.PanelGlass();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel170 = new widget.Label();
        SkalaNyeri = new widget.TextBox();
        Nyeri = new widget.ComboBox();
        jLabel88 = new widget.Label();
        Frekuensi = new widget.TextBox();
        jLabel83 = new widget.Label();
        Lokasi = new widget.TextBox();
        Durasi = new widget.TextBox();
        jLabel87 = new widget.Label();
        jLabel89 = new widget.Label();
        NyeriHilang = new widget.ComboBox();
        KetNyeri = new widget.TextBox();
        jLabel86 = new widget.Label();
        PadaDokter = new widget.ComboBox();
        jLabel68 = new widget.Label();
        KetDokter = new widget.TextBox();
        jSeparator13 = new javax.swing.JSeparator();
        Scroll8 = new widget.ScrollPane();
        tbMasalahKeperawatan = new widget.Table();
        BtnPanggilHapusImunisasi = new widget.Button();
        TabRencanaKeperawatan = new javax.swing.JTabbedPane();
        panelBiasa1 = new widget.PanelBiasa();
        Scroll9 = new widget.ScrollPane();
        tbRencanaKeperawatan = new widget.Table();
        scrollPane5 = new widget.ScrollPane();
        Rencana = new widget.TextArea();
        label13 = new widget.Label();
        TCariRencana = new widget.TextBox();
        BtnCariRencana = new widget.Button();
        BtnAllRencana = new widget.Button();
        BtnTambahRencana = new widget.Button();
        label12 = new widget.Label();
        TCariMasalah = new widget.TextBox();
        BtnCariMasalah = new widget.Button();
        BtnAllMasalah = new widget.Button();
        BtnTambahMasalah = new widget.Button();
        jSeparator14 = new javax.swing.JSeparator();
        jLabel217 = new widget.Label();
        KemampuanBacaTulis = new widget.ComboBox();
        jLabel218 = new widget.Label();
        ButuhPenerjemah = new widget.ComboBox();
        jLabel219 = new widget.Label();
        KeteranganButuhPenerjemah = new widget.TextBox();
        jLabel220 = new widget.Label();
        jLabel221 = new widget.Label();
        TerdapatHambatanBelajar = new widget.ComboBox();
        jLabel222 = new widget.Label();
        HambatanBelajar = new widget.ComboBox();
        KeteranganHambatanBelajar = new widget.TextBox();
        jLabel224 = new widget.Label();
        jLabel225 = new widget.Label();
        jLabel223 = new widget.Label();
        HambatanCaraBicara = new widget.ComboBox();
        HambatanBahasaIsyarat = new widget.ComboBox();
        jLabel226 = new widget.Label();
        jLabel227 = new widget.Label();
        CaraBelajarDisukai = new widget.ComboBox();
        jLabel228 = new widget.Label();
        jLabel229 = new widget.Label();
        KesediaanMenerimaInformasi = new widget.ComboBox();
        KeteranganKesediaanMenerimaInformasi = new widget.TextBox();
        jLabel230 = new widget.Label();
        jLabel231 = new widget.Label();
        PemahamanNutrisi = new widget.ComboBox();
        PemahamanPenyakit = new widget.ComboBox();
        jLabel232 = new widget.Label();
        jLabel233 = new widget.Label();
        jLabel234 = new widget.Label();
        PemahamanPerawatan = new widget.ComboBox();
        PemahamanPengobatan = new widget.ComboBox();
        jLabel235 = new widget.Label();
        jLabel273 = new widget.Label();
        jLabel274 = new widget.Label();
        KeyakinanNilai = new widget.ComboBox();
        jLabel275 = new widget.Label();
        HambatanEmosional = new widget.ComboBox();
        KeterbatasanFisik = new widget.ComboBox();
        Motivasi = new widget.ComboBox();
        jLabel276 = new widget.Label();
        jLabel277 = new widget.Label();
        jLabel214 = new widget.Label();
        jSeparator15 = new javax.swing.JSeparator();
        Scroll12 = new widget.ScrollPane();
        tbKebutuhanEdukasi = new widget.Table();
        label32 = new widget.Label();
        BtnTambahKebutuhanEdukasi = new widget.Button();
        BtnAllKebutuhanEdukasi = new widget.Button();
        BtnCariKebutuhanEdukasi = new widget.Button();
        TabRencanaKeperawatan2 = new javax.swing.JTabbedPane();
        panelBiasa3 = new widget.PanelBiasa();
        Scroll13 = new widget.ScrollPane();
        tbRencanaEdukasi = new widget.Table();
        TCariMasalah2 = new widget.TextBox();
        BtnTambahRencanaEdukasi = new widget.Button();
        BtnAllRencanaEdukasi = new widget.Button();
        BtnCariRencanaEdukasi = new widget.Button();
        label33 = new widget.Label();
        TCariRencana2 = new widget.TextBox();
        jLabel243 = new widget.Label();
        SkalaResiko1 = new widget.ComboBox();
        NilaiResiko1 = new widget.TextBox();
        jLabel244 = new widget.Label();
        SkalaResiko2 = new widget.ComboBox();
        NilaiResiko2 = new widget.TextBox();
        jLabel245 = new widget.Label();
        SkalaResiko3 = new widget.ComboBox();
        NilaiResiko3 = new widget.TextBox();
        jLabel246 = new widget.Label();
        SkalaResiko4 = new widget.ComboBox();
        NilaiResiko4 = new widget.TextBox();
        jLabel247 = new widget.Label();
        SkalaResiko5 = new widget.ComboBox();
        NilaiResiko5 = new widget.TextBox();
        jLabel248 = new widget.Label();
        SkalaResiko6 = new widget.ComboBox();
        NilaiResiko6 = new widget.TextBox();
        jLabel249 = new widget.Label();
        SkalaResiko7 = new widget.ComboBox();
        NilaiResiko7 = new widget.TextBox();
        TingkatResiko1 = new widget.Label();
        jLabel256 = new widget.Label();
        NilaiResikoTotal = new widget.TextBox();
        KeteranganTingkatRisiko = new widget.TextBox();
        jLabel267 = new widget.Label();
        jSeparator16 = new javax.swing.JSeparator();
        jLabel272 = new widget.Label();
        InformasiPerencanaanPulang = new widget.ComboBox();
        jLabel250 = new widget.Label();
        jLabel257 = new widget.Label();
        jLabel260 = new widget.Label();
        KondisiPulang = new widget.TextBox();
        jLabel259 = new widget.Label();
        label29 = new widget.Label();
        TanggalPulang = new widget.Tanggal();
        jLabel261 = new widget.Label();
        LamaRatarata = new widget.TextBox();
        jLabel262 = new widget.Label();
        scrollPane7 = new widget.ScrollPane();
        PerawatanLanjutan = new widget.TextArea();
        jLabel263 = new widget.Label();
        CaraTransportasiPulang = new widget.ComboBox();
        jLabel264 = new widget.Label();
        jLabel265 = new widget.Label();
        jLabel266 = new widget.Label();
        TransportasiYangDigunakan = new widget.ComboBox();
        internalFrame3 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbObat = new widget.Table();
        panelGlass9 = new widget.panelisi();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        PanelAccor = new widget.PanelBiasa();
        ChkAccor = new widget.CekBox();
        FormMenu = new widget.PanelBiasa();
        jLabel34 = new widget.Label();
        TNoRM1 = new widget.TextBox();
        TPasien1 = new widget.TextBox();
        BtnPrint1 = new widget.Button();
        TabRawat1 = new javax.swing.JTabbedPane();
        FormMasalahRencana = new widget.PanelBiasa();
        scrollPane9 = new widget.ScrollPane();
        tbImunisasi2 = new widget.Table();
        Scroll7 = new widget.ScrollPane();
        tbMasalahDetailMasalah = new widget.Table();
        Scroll10 = new widget.ScrollPane();
        tbRencanaDetail = new widget.Table();
        scrollPane6 = new widget.ScrollPane();
        DetailRencana = new widget.TextArea();
        FormKebutuhanRencana = new widget.PanelBiasa();
        Scroll15 = new widget.ScrollPane();
        tbDetailKebutuhanEdukasi = new widget.Table();
        Scroll16 = new widget.ScrollPane();
        tbDetailRencanaEdukasi = new widget.Table();

        LoadHTML.setBorder(null);
        LoadHTML.setName("LoadHTML"); // NOI18N

        DlgRiwayatImunisasi.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        DlgRiwayatImunisasi.setName("DlgRiwayatImunisasi"); // NOI18N
        DlgRiwayatImunisasi.setUndecorated(true);
        DlgRiwayatImunisasi.setResizable(false);

        internalFrame4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(230, 235, 225)), "::[ Riwayat Imunisasi ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 50))); // NOI18N
        internalFrame4.setName("internalFrame4"); // NOI18N
        internalFrame4.setLayout(new java.awt.BorderLayout(1, 1));

        panelBiasa2.setName("panelBiasa2"); // NOI18N
        panelBiasa2.setLayout(null);

        jLabel99.setText("Imunisasi :");
        jLabel99.setName("jLabel99"); // NOI18N
        panelBiasa2.add(jLabel99);
        jLabel99.setBounds(0, 13, 67, 23);

        BtnKeluarImunisasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/cross.png"))); // NOI18N
        BtnKeluarImunisasi.setMnemonic('U');
        BtnKeluarImunisasi.setText("Tutup");
        BtnKeluarImunisasi.setToolTipText("Alt+U");
        BtnKeluarImunisasi.setName("BtnKeluarImunisasi"); // NOI18N
        BtnKeluarImunisasi.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluarImunisasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarImunisasiActionPerformed(evt);
            }
        });
        panelBiasa2.add(BtnKeluarImunisasi);
        BtnKeluarImunisasi.setBounds(340, 50, 100, 30);

        BtnSimpanImunisasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpanImunisasi.setMnemonic('S');
        BtnSimpanImunisasi.setText("Simpan");
        BtnSimpanImunisasi.setToolTipText("Alt+S");
        BtnSimpanImunisasi.setName("BtnSimpanImunisasi"); // NOI18N
        BtnSimpanImunisasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanImunisasiActionPerformed(evt);
            }
        });
        panelBiasa2.add(BtnSimpanImunisasi);
        BtnSimpanImunisasi.setBounds(10, 50, 100, 30);

        BtnImunisasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnImunisasi.setMnemonic('2');
        BtnImunisasi.setToolTipText("Alt+2");
        BtnImunisasi.setName("BtnImunisasi"); // NOI18N
        BtnImunisasi.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnImunisasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnImunisasiActionPerformed(evt);
            }
        });
        BtnImunisasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnImunisasiKeyPressed(evt);
            }
        });
        panelBiasa2.add(BtnImunisasi);
        BtnImunisasi.setBounds(307, 13, 28, 23);

        NmImunisasi.setEditable(false);
        NmImunisasi.setName("NmImunisasi"); // NOI18N
        NmImunisasi.setPreferredSize(new java.awt.Dimension(207, 23));
        panelBiasa2.add(NmImunisasi);
        NmImunisasi.setBounds(124, 13, 180, 23);

        KdImunisasi.setEditable(false);
        KdImunisasi.setName("KdImunisasi"); // NOI18N
        KdImunisasi.setPreferredSize(new java.awt.Dimension(80, 23));
        KdImunisasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdImunisasiKeyPressed(evt);
            }
        });
        panelBiasa2.add(KdImunisasi);
        KdImunisasi.setBounds(71, 13, 50, 23);

        jLabel43.setText("Ke :");
        jLabel43.setName("jLabel43"); // NOI18N
        panelBiasa2.add(jLabel43);
        jLabel43.setBounds(343, 13, 30, 23);

        ImunisasiKe.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6" }));
        ImunisasiKe.setName("ImunisasiKe"); // NOI18N
        ImunisasiKe.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ImunisasiKeKeyPressed(evt);
            }
        });
        panelBiasa2.add(ImunisasiKe);
        ImunisasiKe.setBounds(377, 13, 60, 23);

        BtnHapusImunisasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapusImunisasi.setMnemonic('H');
        BtnHapusImunisasi.setText("Hapus");
        BtnHapusImunisasi.setToolTipText("Alt+H");
        BtnHapusImunisasi.setName("BtnHapusImunisasi"); // NOI18N
        BtnHapusImunisasi.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapusImunisasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusImunisasiActionPerformed(evt);
            }
        });
        BtnHapusImunisasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapusImunisasiKeyPressed(evt);
            }
        });
        panelBiasa2.add(BtnHapusImunisasi);
        BtnHapusImunisasi.setBounds(230, 50, 100, 30);

        internalFrame4.add(panelBiasa2, java.awt.BorderLayout.CENTER);

        DlgRiwayatImunisasi.getContentPane().add(internalFrame4, java.awt.BorderLayout.CENTER);

        TanggalRegistrasi.setHighlighter(null);
        TanggalRegistrasi.setName("TanggalRegistrasi"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Pengkajian Awal Keperawatan Rawat Jalan Bayi/Anak ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 54));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Simpan");
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setName("BtnSimpan"); // NOI18N
        BtnSimpan.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanActionPerformed(evt);
            }
        });
        BtnSimpan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpanKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnSimpan);

        BtnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        BtnBatal.setMnemonic('B');
        BtnBatal.setText("Baru");
        BtnBatal.setToolTipText("Alt+B");
        BtnBatal.setName("BtnBatal"); // NOI18N
        BtnBatal.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBatalActionPerformed(evt);
            }
        });
        BtnBatal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnBatalKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnBatal);

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setName("BtnHapus"); // NOI18N
        BtnHapus.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });
        BtnHapus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapusKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnHapus);

        BtnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnEdit.setMnemonic('G');
        BtnEdit.setText("Ganti");
        BtnEdit.setToolTipText("Alt+G");
        BtnEdit.setName("BtnEdit"); // NOI18N
        BtnEdit.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditActionPerformed(evt);
            }
        });
        BtnEdit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnEditKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnEdit);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("Cetak");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        BtnPrint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPrintKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnPrint);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setText("Semua");
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllActionPerformed(evt);
            }
        });
        BtnAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnAll);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnKeluar);

        internalFrame1.add(panelGlass8, java.awt.BorderLayout.PAGE_END);

        TabRawat.setBackground(new java.awt.Color(254, 255, 254));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRawat.setName("TabRawat"); // NOI18N

        internalFrame2.setBorder(null);
        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(new java.awt.BorderLayout(1, 1));

        scrollInput.setName("scrollInput"); // NOI18N
        scrollInput.setPreferredSize(new java.awt.Dimension(102, 557));

        FormInput.setBackground(new java.awt.Color(255, 255, 255));
        FormInput.setBorder(null);
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(870, 2420));
        FormInput.setLayout(null);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw);
        TNoRw.setBounds(74, 10, 131, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        FormInput.add(TPasien);
        TPasien.setBounds(309, 10, 260, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        FormInput.add(TNoRM);
        TNoRM.setBounds(207, 10, 100, 23);

        label14.setText("Petugas :");
        label14.setName("label14"); // NOI18N
        label14.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label14);
        label14.setBounds(0, 40, 70, 23);

        KdPetugas.setEditable(false);
        KdPetugas.setName("KdPetugas"); // NOI18N
        KdPetugas.setPreferredSize(new java.awt.Dimension(80, 23));
        FormInput.add(KdPetugas);
        KdPetugas.setBounds(74, 40, 100, 23);

        NmPetugas.setEditable(false);
        NmPetugas.setName("NmPetugas"); // NOI18N
        NmPetugas.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(NmPetugas);
        NmPetugas.setBounds(176, 40, 180, 23);

        BtnPetugas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPetugas.setMnemonic('2');
        BtnPetugas.setToolTipText("Alt+2");
        BtnPetugas.setName("BtnPetugas"); // NOI18N
        BtnPetugas.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPetugasActionPerformed(evt);
            }
        });
        BtnPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPetugasKeyPressed(evt);
            }
        });
        FormInput.add(BtnPetugas);
        BtnPetugas.setBounds(358, 40, 28, 23);

        jLabel8.setText("Tgl.Lahir :");
        jLabel8.setName("jLabel8"); // NOI18N
        FormInput.add(jLabel8);
        jLabel8.setBounds(580, 10, 60, 23);

        TglLahir.setEditable(false);
        TglLahir.setHighlighter(null);
        TglLahir.setName("TglLahir"); // NOI18N
        FormInput.add(TglLahir);
        TglLahir.setBounds(644, 10, 80, 23);

        Jk.setEditable(false);
        Jk.setHighlighter(null);
        Jk.setName("Jk"); // NOI18N
        FormInput.add(Jk);
        Jk.setBounds(774, 10, 80, 23);

        jLabel10.setText("No.Rawat :");
        jLabel10.setName("jLabel10"); // NOI18N
        FormInput.add(jLabel10);
        jLabel10.setBounds(0, 10, 70, 23);

        label11.setText("Tanggal :");
        label11.setName("label11"); // NOI18N
        label11.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label11);
        label11.setBounds(395, 40, 57, 23);

        jLabel11.setText("J.K. :");
        jLabel11.setName("jLabel11"); // NOI18N
        FormInput.add(jLabel11);
        jLabel11.setBounds(740, 10, 30, 23);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel16.setText("x/menit");
        jLabel16.setName("jLabel16"); // NOI18N
        FormInput.add(jLabel16);
        jLabel16.setBounds(298, 90, 50, 23);

        Nadi.setFocusTraversalPolicyProvider(true);
        Nadi.setName("Nadi"); // NOI18N
        Nadi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NadiKeyPressed(evt);
            }
        });
        FormInput.add(Nadi);
        Nadi.setBounds(250, 90, 45, 23);

        jLabel17.setText("Nadi :");
        jLabel17.setName("jLabel17"); // NOI18N
        FormInput.add(jLabel17);
        jLabel17.setBounds(206, 90, 40, 23);

        jLabel18.setText("Suhu :");
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput.add(jLabel18);
        jLabel18.setBounds(566, 90, 40, 23);

        Suhu.setFocusTraversalPolicyProvider(true);
        Suhu.setName("Suhu"); // NOI18N
        Suhu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SuhuKeyPressed(evt);
            }
        });
        FormInput.add(Suhu);
        Suhu.setBounds(610, 90, 45, 23);

        jLabel22.setText("TD :");
        jLabel22.setName("jLabel22"); // NOI18N
        FormInput.add(jLabel22);
        jLabel22.setBounds(0, 90, 70, 23);

        TD.setFocusTraversalPolicyProvider(true);
        TD.setName("TD"); // NOI18N
        TD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TDKeyPressed(evt);
            }
        });
        FormInput.add(TD);
        TD.setBounds(74, 90, 60, 23);

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel20.setText("°C");
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput.add(jLabel20);
        jLabel20.setBounds(658, 90, 30, 23);

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel23.setText("mmHg");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput.add(jLabel23);
        jLabel23.setBounds(137, 90, 50, 23);

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel25.setText("x/menit");
        jLabel25.setName("jLabel25"); // NOI18N
        FormInput.add(jLabel25);
        jLabel25.setBounds(470, 90, 50, 23);

        RR.setFocusTraversalPolicyProvider(true);
        RR.setName("RR"); // NOI18N
        RR.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RRKeyPressed(evt);
            }
        });
        FormInput.add(RR);
        RR.setBounds(422, 90, 45, 23);

        jLabel26.setText("RR :");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput.add(jLabel26);
        jLabel26.setBounds(378, 90, 40, 23);

        jLabel36.setText("Informasi didapat dari :");
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput.add(jLabel36);
        jLabel36.setBounds(592, 40, 130, 23);

        Informasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Autoanamnesis", "Alloanamnesis" }));
        Informasi.setName("Informasi"); // NOI18N
        Informasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                InformasiKeyPressed(evt);
            }
        });
        FormInput.add(Informasi);
        Informasi.setBounds(726, 40, 128, 23);

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel53.setText("I. KEADAAN UMUM");
        jLabel53.setName("jLabel53"); // NOI18N
        FormInput.add(jLabel53);
        jLabel53.setBounds(10, 70, 180, 23);

        TglAsuhan.setForeground(new java.awt.Color(50, 70, 50));
        TglAsuhan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "06-04-2026 10:35:39" }));
        TglAsuhan.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        TglAsuhan.setName("TglAsuhan"); // NOI18N
        TglAsuhan.setOpaque(false);
        TglAsuhan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TglAsuhanKeyPressed(evt);
            }
        });
        FormInput.add(TglAsuhan);
        TglAsuhan.setBounds(456, 40, 130, 23);

        jLabel28.setText("GCS(E,V,M) :");
        jLabel28.setName("jLabel28"); // NOI18N
        FormInput.add(jLabel28);
        jLabel28.setBounds(700, 90, 90, 23);

        GCS.setFocusTraversalPolicyProvider(true);
        GCS.setName("GCS"); // NOI18N
        GCS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                GCSKeyPressed(evt);
            }
        });
        FormInput.add(GCS);
        GCS.setBounds(794, 90, 60, 23);

        jSeparator1.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator1.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator1.setName("jSeparator1"); // NOI18N
        FormInput.add(jSeparator1);
        jSeparator1.setBounds(0, 70, 880, 1);

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel24.setText("cm");
        jLabel24.setName("jLabel24"); // NOI18N
        FormInput.add(jLabel24);
        jLabel24.setBounds(298, 120, 50, 23);

        TB.setFocusTraversalPolicyProvider(true);
        TB.setName("TB"); // NOI18N
        TB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TBKeyPressed(evt);
            }
        });
        FormInput.add(TB);
        TB.setBounds(250, 120, 45, 23);

        jLabel27.setText("TB :");
        jLabel27.setName("jLabel27"); // NOI18N
        FormInput.add(jLabel27);
        jLabel27.setBounds(206, 120, 40, 23);

        jLabel29.setText("LK :");
        jLabel29.setName("jLabel29"); // NOI18N
        FormInput.add(jLabel29);
        jLabel29.setBounds(566, 120, 40, 23);

        LK.setFocusTraversalPolicyProvider(true);
        LK.setName("LK"); // NOI18N
        LK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LKKeyPressed(evt);
            }
        });
        FormInput.add(LK);
        LK.setBounds(610, 120, 45, 23);

        jLabel30.setText("BB :");
        jLabel30.setName("jLabel30"); // NOI18N
        FormInput.add(jLabel30);
        jLabel30.setBounds(0, 120, 70, 23);

        BB.setFocusTraversalPolicyProvider(true);
        BB.setName("BB"); // NOI18N
        BB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BBKeyPressed(evt);
            }
        });
        FormInput.add(BB);
        BB.setBounds(74, 120, 45, 23);

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel31.setText("cm");
        jLabel31.setName("jLabel31"); // NOI18N
        FormInput.add(jLabel31);
        jLabel31.setBounds(842, 120, 30, 23);

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel32.setText("Kg");
        jLabel32.setName("jLabel32"); // NOI18N
        FormInput.add(jLabel32);
        jLabel32.setBounds(122, 120, 50, 23);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel33.setText("cm");
        jLabel33.setName("jLabel33"); // NOI18N
        FormInput.add(jLabel33);
        jLabel33.setBounds(470, 120, 50, 23);

        LP.setFocusTraversalPolicyProvider(true);
        LP.setName("LP"); // NOI18N
        LP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LPKeyPressed(evt);
            }
        });
        FormInput.add(LP);
        LP.setBounds(422, 120, 45, 23);

        jLabel35.setText("LP :");
        jLabel35.setName("jLabel35"); // NOI18N
        FormInput.add(jLabel35);
        jLabel35.setBounds(378, 120, 40, 23);

        jLabel37.setText("LD :");
        jLabel37.setName("jLabel37"); // NOI18N
        FormInput.add(jLabel37);
        jLabel37.setBounds(700, 120, 90, 23);

        LD.setFocusTraversalPolicyProvider(true);
        LD.setName("LD"); // NOI18N
        LD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LDKeyPressed(evt);
            }
        });
        FormInput.add(LD);
        LD.setBounds(794, 120, 45, 23);

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel38.setText("cm");
        jLabel38.setName("jLabel38"); // NOI18N
        FormInput.add(jLabel38);
        jLabel38.setBounds(658, 120, 30, 23);

        jLabel9.setText("Riwayat Pengobatan :");
        jLabel9.setName("jLabel9"); // NOI18N
        FormInput.add(jLabel9);
        jLabel9.setBounds(440, 220, 150, 23);

        jLabel39.setText("Riwayat Alergi :");
        jLabel39.setName("jLabel39"); // NOI18N
        FormInput.add(jLabel39);
        jLabel39.setBounds(0, 270, 175, 23);

        Alergi.setFocusTraversalPolicyProvider(true);
        Alergi.setName("Alergi"); // NOI18N
        Alergi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AlergiKeyPressed(evt);
            }
        });
        FormInput.add(Alergi);
        Alergi.setBounds(179, 270, 260, 23);

        scrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane1.setName("scrollPane1"); // NOI18N

        KeluhanUtama.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        KeluhanUtama.setColumns(20);
        KeluhanUtama.setRows(5);
        KeluhanUtama.setName("KeluhanUtama"); // NOI18N
        KeluhanUtama.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeluhanUtamaKeyPressed(evt);
            }
        });
        scrollPane1.setViewportView(KeluhanUtama);

        FormInput.add(scrollPane1);
        scrollPane1.setBounds(179, 170, 260, 43);

        jLabel40.setText("Keluhan Utama :");
        jLabel40.setName("jLabel40"); // NOI18N
        FormInput.add(jLabel40);
        jLabel40.setBounds(0, 170, 175, 20);

        scrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane2.setName("scrollPane2"); // NOI18N

        RPD.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        RPD.setColumns(20);
        RPD.setRows(5);
        RPD.setName("RPD"); // NOI18N
        RPD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RPDKeyPressed(evt);
            }
        });
        scrollPane2.setViewportView(RPD);

        FormInput.add(scrollPane2);
        scrollPane2.setBounds(179, 220, 260, 43);

        jLabel41.setText("Riwayat Penyakit Dahulu :");
        jLabel41.setName("jLabel41"); // NOI18N
        FormInput.add(jLabel41);
        jLabel41.setBounds(0, 220, 175, 23);

        scrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane3.setName("scrollPane3"); // NOI18N

        RPK.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        RPK.setColumns(20);
        RPK.setRows(5);
        RPK.setName("RPK"); // NOI18N
        RPK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RPKKeyPressed(evt);
            }
        });
        scrollPane3.setViewportView(RPK);

        FormInput.add(scrollPane3);
        scrollPane3.setBounds(594, 170, 260, 42);

        jLabel42.setText("Riwayat Penyakit Keluarga :");
        jLabel42.setName("jLabel42"); // NOI18N
        FormInput.add(jLabel42);
        jLabel42.setBounds(440, 170, 150, 23);

        scrollPane4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane4.setName("scrollPane4"); // NOI18N

        RPO.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        RPO.setColumns(20);
        RPO.setRows(5);
        RPO.setName("RPO"); // NOI18N
        RPO.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RPOKeyPressed(evt);
            }
        });
        scrollPane4.setViewportView(RPO);

        FormInput.add(scrollPane4);
        scrollPane4.setBounds(594, 220, 260, 42);

        jLabel94.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel94.setText("II. RIWAYAT KESEHATAN");
        jLabel94.setName("jLabel94"); // NOI18N
        FormInput.add(jLabel94);
        jLabel94.setBounds(10, 150, 180, 23);

        jSeparator3.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator3.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator3.setName("jSeparator3"); // NOI18N
        FormInput.add(jSeparator3);
        jSeparator3.setBounds(0, 150, 880, 1);

        jSeparator4.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator4.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator4.setName("jSeparator4"); // NOI18N
        FormInput.add(jSeparator4);
        jSeparator4.setBounds(0, 300, 880, 1);

        jLabel95.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel95.setText("III. RIWAYAT TUMBUH KEMBANG DAN PERINATAL CARE");
        jLabel95.setName("jLabel95"); // NOI18N
        FormInput.add(jLabel95);
        jLabel95.setBounds(10, 300, 350, 23);

        jLabel44.setText("Anak ke :");
        jLabel44.setName("jLabel44"); // NOI18N
        FormInput.add(jLabel44);
        jLabel44.setBounds(145, 320, 55, 23);

        Anakke.setFocusTraversalPolicyProvider(true);
        Anakke.setName("Anakke"); // NOI18N
        Anakke.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AnakkeKeyPressed(evt);
            }
        });
        FormInput.add(Anakke);
        Anakke.setBounds(204, 320, 40, 23);

        DariSaudara.setFocusTraversalPolicyProvider(true);
        DariSaudara.setName("DariSaudara"); // NOI18N
        DariSaudara.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DariSaudaraKeyPressed(evt);
            }
        });
        FormInput.add(DariSaudara);
        DariSaudara.setBounds(272, 320, 40, 23);

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("dari");
        jLabel45.setName("jLabel45"); // NOI18N
        FormInput.add(jLabel45);
        jLabel45.setBounds(246, 320, 24, 23);

        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel46.setText("saudara");
        jLabel46.setName("jLabel46"); // NOI18N
        FormInput.add(jLabel46);
        jLabel46.setBounds(315, 320, 50, 23);

        jLabel55.setText("Cara Kelahiran :");
        jLabel55.setName("jLabel55"); // NOI18N
        FormInput.add(jLabel55);
        jLabel55.setBounds(400, 320, 110, 23);

        CaraKelahiran.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Spontan", "Sectio Caesaria", "Lain-Lain" }));
        CaraKelahiran.setName("CaraKelahiran"); // NOI18N
        CaraKelahiran.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CaraKelahiranKeyPressed(evt);
            }
        });
        FormInput.add(CaraKelahiran);
        CaraKelahiran.setBounds(514, 320, 127, 23);

        KetCaraKelahiran.setFocusTraversalPolicyProvider(true);
        KetCaraKelahiran.setName("KetCaraKelahiran"); // NOI18N
        KetCaraKelahiran.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetCaraKelahiranKeyPressed(evt);
            }
        });
        FormInput.add(KetCaraKelahiran);
        KetCaraKelahiran.setBounds(645, 320, 209, 23);

        KelainanBawaan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Ada" }));
        KelainanBawaan.setName("KelainanBawaan"); // NOI18N
        KelainanBawaan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KelainanBawaanKeyPressed(evt);
            }
        });
        FormInput.add(KelainanBawaan);
        KelainanBawaan.setBounds(514, 350, 100, 23);

        jLabel56.setText("Kelainan Bawaan :");
        jLabel56.setName("jLabel56"); // NOI18N
        FormInput.add(jLabel56);
        jLabel56.setBounds(400, 350, 110, 23);

        KetKelainanBawaan.setFocusTraversalPolicyProvider(true);
        KetKelainanBawaan.setName("KetKelainanBawaan"); // NOI18N
        KetKelainanBawaan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetKelainanBawaanKeyPressed(evt);
            }
        });
        FormInput.add(KetKelainanBawaan);
        KetKelainanBawaan.setBounds(618, 350, 236, 23);

        UmurKelahiran.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cukup Bulan", "Kurang Bulan" }));
        UmurKelahiran.setName("UmurKelahiran"); // NOI18N
        UmurKelahiran.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UmurKelahiranKeyPressed(evt);
            }
        });
        FormInput.add(UmurKelahiran);
        UmurKelahiran.setBounds(150, 350, 140, 23);

        jLabel57.setText("Riwayat Kelahiran :");
        jLabel57.setName("jLabel57"); // NOI18N
        FormInput.add(jLabel57);
        jLabel57.setBounds(0, 320, 146, 23);

        jLabel58.setText("Umur Kelahiran :");
        jLabel58.setName("jLabel58"); // NOI18N
        FormInput.add(jLabel58);
        jLabel58.setBounds(0, 350, 146, 23);

        jSeparator5.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator5.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator5.setName("jSeparator5"); // NOI18N
        FormInput.add(jSeparator5);
        jSeparator5.setBounds(0, 380, 880, 1);

        jLabel96.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel96.setText("IV. RIWAYAT IMUNISASI");
        jLabel96.setName("jLabel96"); // NOI18N
        FormInput.add(jLabel96);
        jLabel96.setBounds(10, 380, 350, 23);

        Scroll6.setName("Scroll6"); // NOI18N
        Scroll6.setOpaque(true);

        tbImunisasi.setName("tbImunisasi"); // NOI18N
        Scroll6.setViewportView(tbImunisasi);

        FormInput.add(Scroll6);
        Scroll6.setBounds(94, 400, 760, 93);

        BtnTambahImunisasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        BtnTambahImunisasi.setMnemonic('3');
        BtnTambahImunisasi.setToolTipText("Alt+3");
        BtnTambahImunisasi.setName("BtnTambahImunisasi"); // NOI18N
        BtnTambahImunisasi.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnTambahImunisasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTambahImunisasiActionPerformed(evt);
            }
        });
        FormInput.add(BtnTambahImunisasi);
        BtnTambahImunisasi.setBounds(62, 400, 28, 23);

        jSeparator6.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator6.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator6.setName("jSeparator6"); // NOI18N
        FormInput.add(jSeparator6);
        jSeparator6.setBounds(0, 500, 880, 1);

        jLabel97.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel97.setText("V. RIWAYAT TUMBUH KEMBANG ANAK");
        jLabel97.setName("jLabel97"); // NOI18N
        FormInput.add(jLabel97);
        jLabel97.setBounds(10, 500, 350, 23);

        jLabel59.setText("a. Tengkurap, usia :");
        jLabel59.setName("jLabel59"); // NOI18N
        FormInput.add(jLabel59);
        jLabel59.setBounds(0, 520, 133, 23);

        UsiaTengkurap.setFocusTraversalPolicyProvider(true);
        UsiaTengkurap.setName("UsiaTengkurap"); // NOI18N
        UsiaTengkurap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UsiaTengkurapKeyPressed(evt);
            }
        });
        FormInput.add(UsiaTengkurap);
        UsiaTengkurap.setBounds(137, 520, 90, 23);

        jLabel60.setText("b. Duduk, usia :");
        jLabel60.setName("jLabel60"); // NOI18N
        FormInput.add(jLabel60);
        jLabel60.setBounds(244, 520, 90, 23);

        UsiaDuduk.setFocusTraversalPolicyProvider(true);
        UsiaDuduk.setName("UsiaDuduk"); // NOI18N
        UsiaDuduk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UsiaDudukKeyPressed(evt);
            }
        });
        FormInput.add(UsiaDuduk);
        UsiaDuduk.setBounds(337, 520, 90, 23);

        jLabel61.setText("c. Berdiri, usia :");
        jLabel61.setName("jLabel61"); // NOI18N
        FormInput.add(jLabel61);
        jLabel61.setBounds(441, 520, 90, 23);

        UsiaBerdiri.setFocusTraversalPolicyProvider(true);
        UsiaBerdiri.setName("UsiaBerdiri"); // NOI18N
        UsiaBerdiri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UsiaBerdiriKeyPressed(evt);
            }
        });
        FormInput.add(UsiaBerdiri);
        UsiaBerdiri.setBounds(535, 520, 90, 23);

        jLabel62.setText("d. Gigi pertama, usia :");
        jLabel62.setName("jLabel62"); // NOI18N
        FormInput.add(jLabel62);
        jLabel62.setBounds(630, 520, 130, 23);

        UsiaGigi.setFocusTraversalPolicyProvider(true);
        UsiaGigi.setName("UsiaGigi"); // NOI18N
        UsiaGigi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UsiaGigiKeyPressed(evt);
            }
        });
        FormInput.add(UsiaGigi);
        UsiaGigi.setBounds(764, 520, 90, 23);

        jLabel63.setText("e. Berjalan, usia :");
        jLabel63.setName("jLabel63"); // NOI18N
        FormInput.add(jLabel63);
        jLabel63.setBounds(0, 550, 122, 23);

        UsiaBerjalan.setFocusTraversalPolicyProvider(true);
        UsiaBerjalan.setName("UsiaBerjalan"); // NOI18N
        UsiaBerjalan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UsiaBerjalanKeyPressed(evt);
            }
        });
        FormInput.add(UsiaBerjalan);
        UsiaBerjalan.setBounds(126, 550, 90, 23);

        jLabel64.setText("f. Bicara Usia, usia :");
        jLabel64.setName("jLabel64"); // NOI18N
        FormInput.add(jLabel64);
        jLabel64.setBounds(301, 550, 110, 23);

        UsiaBicara.setFocusTraversalPolicyProvider(true);
        UsiaBicara.setName("UsiaBicara"); // NOI18N
        UsiaBicara.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UsiaBicaraKeyPressed(evt);
            }
        });
        FormInput.add(UsiaBicara);
        UsiaBicara.setBounds(415, 550, 90, 23);

        jLabel65.setText("g. Mulai bisa membaca, usia :");
        jLabel65.setName("jLabel65"); // NOI18N
        FormInput.add(jLabel65);
        jLabel65.setBounds(580, 550, 180, 23);

        UsiaMembaca.setFocusTraversalPolicyProvider(true);
        UsiaMembaca.setName("UsiaMembaca"); // NOI18N
        UsiaMembaca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UsiaMembacaKeyPressed(evt);
            }
        });
        FormInput.add(UsiaMembaca);
        UsiaMembaca.setBounds(764, 550, 90, 23);

        UsiaMenulis.setFocusTraversalPolicyProvider(true);
        UsiaMenulis.setName("UsiaMenulis"); // NOI18N
        UsiaMenulis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UsiaMenulisKeyPressed(evt);
            }
        });
        FormInput.add(UsiaMenulis);
        UsiaMenulis.setBounds(176, 580, 90, 23);

        jLabel66.setText("h. Mulai bisa menulis, usia :");
        jLabel66.setName("jLabel66"); // NOI18N
        FormInput.add(jLabel66);
        jLabel66.setBounds(0, 580, 172, 23);

        GangguanEmosi.setFocusTraversalPolicyProvider(true);
        GangguanEmosi.setName("GangguanEmosi"); // NOI18N
        GangguanEmosi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                GangguanEmosiKeyPressed(evt);
            }
        });
        FormInput.add(GangguanEmosi);
        GangguanEmosi.setBounds(614, 580, 240, 23);

        jLabel67.setText("Gangguan perkembangan mental / emosi, bila ada, jelaskan :");
        jLabel67.setName("jLabel67"); // NOI18N
        FormInput.add(jLabel67);
        jLabel67.setBounds(290, 580, 320, 23);

        jSeparator7.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator7.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator7.setName("jSeparator7"); // NOI18N
        FormInput.add(jSeparator7);
        jSeparator7.setBounds(0, 610, 880, 1);

        jLabel125.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel125.setText("VI. FUNGSIONAL");
        jLabel125.setName("jLabel125"); // NOI18N
        FormInput.add(jLabel125);
        jLabel125.setBounds(10, 610, 230, 23);

        jLabel127.setText("Prothesa :");
        jLabel127.setName("jLabel127"); // NOI18N
        FormInput.add(jLabel127);
        jLabel127.setBounds(476, 630, 60, 23);

        AlatBantu.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        AlatBantu.setName("AlatBantu"); // NOI18N
        AlatBantu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AlatBantuKeyPressed(evt);
            }
        });
        FormInput.add(AlatBantu);
        AlatBantu.setBounds(124, 630, 90, 23);

        KetBantu.setFocusTraversalPolicyProvider(true);
        KetBantu.setName("KetBantu"); // NOI18N
        KetBantu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetBantuKeyPressed(evt);
            }
        });
        FormInput.add(KetBantu);
        KetBantu.setBounds(218, 630, 220, 23);

        Prothesa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        Prothesa.setName("Prothesa"); // NOI18N
        Prothesa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ProthesaKeyPressed(evt);
            }
        });
        FormInput.add(Prothesa);
        Prothesa.setBounds(540, 630, 90, 23);

        KetProthesa.setFocusTraversalPolicyProvider(true);
        KetProthesa.setName("KetProthesa"); // NOI18N
        KetProthesa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetProthesaKeyPressed(evt);
            }
        });
        FormInput.add(KetProthesa);
        KetProthesa.setBounds(634, 630, 220, 23);

        jLabel128.setText("Alat Bantu :");
        jLabel128.setName("jLabel128"); // NOI18N
        FormInput.add(jLabel128);
        jLabel128.setBounds(0, 630, 120, 23);

        ADL.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mandiri", "Dibantu" }));
        ADL.setName("ADL"); // NOI18N
        ADL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ADLKeyPressed(evt);
            }
        });
        FormInput.add(ADL);
        ADL.setBounds(724, 660, 130, 23);

        jLabel129.setText("Aktivitas Kehidupan Sehari-hari ( ADL ) :");
        jLabel129.setName("jLabel129"); // NOI18N
        FormInput.add(jLabel129);
        jLabel129.setBounds(440, 660, 280, 23);

        jLabel130.setText("Cacat Fisik :");
        jLabel130.setName("jLabel130"); // NOI18N
        FormInput.add(jLabel130);
        jLabel130.setBounds(0, 660, 120, 23);

        CacatFisik.setEditable(false);
        CacatFisik.setFocusTraversalPolicyProvider(true);
        CacatFisik.setName("CacatFisik"); // NOI18N
        FormInput.add(CacatFisik);
        CacatFisik.setBounds(124, 660, 314, 23);

        jSeparator8.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator8.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator8.setName("jSeparator8"); // NOI18N
        FormInput.add(jSeparator8);
        jSeparator8.setBounds(0, 690, 880, 1);

        jLabel131.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel131.setText("VII. RIWAYAT PSIKO-SOSIAL, SPIRITUAL DAN BUDAYA");
        jLabel131.setName("jLabel131"); // NOI18N
        FormInput.add(jLabel131);
        jLabel131.setBounds(10, 690, 380, 23);

        StatusPsiko.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tenang", "Takut", "Tempertantrum", "Cemas", "Depresi", "Lain-lain" }));
        StatusPsiko.setName("StatusPsiko"); // NOI18N
        StatusPsiko.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                StatusPsikoKeyPressed(evt);
            }
        });
        FormInput.add(StatusPsiko);
        StatusPsiko.setBounds(134, 710, 140, 23);

        KetPsiko.setFocusTraversalPolicyProvider(true);
        KetPsiko.setName("KetPsiko"); // NOI18N
        KetPsiko.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetPsikoKeyPressed(evt);
            }
        });
        FormInput.add(KetPsiko);
        KetPsiko.setBounds(278, 710, 200, 23);

        jLabel132.setText("Status Psikologis :");
        jLabel132.setName("jLabel132"); // NOI18N
        FormInput.add(jLabel132);
        jLabel132.setBounds(0, 710, 130, 23);

        Bahasa.setEditable(false);
        Bahasa.setFocusTraversalPolicyProvider(true);
        Bahasa.setName("Bahasa"); // NOI18N
        FormInput.add(Bahasa);
        Bahasa.setBounds(684, 710, 170, 23);

        jLabel133.setText("Bahasa yang digunakan sehari-hari :");
        jLabel133.setName("jLabel133"); // NOI18N
        FormInput.add(jLabel133);
        jLabel133.setBounds(450, 710, 230, 23);

        jLabel134.setText("Status Sosial dan ekonomi :");
        jLabel134.setName("jLabel134"); // NOI18N
        FormInput.add(jLabel134);
        jLabel134.setBounds(0, 740, 176, 23);

        jLabel135.setText("a. Hubungan dengan anggota keluarga :");
        jLabel135.setName("jLabel135"); // NOI18N
        FormInput.add(jLabel135);
        jLabel135.setBounds(35, 760, 210, 23);

        HubunganKeluarga.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Baik", "Tidak Baik" }));
        HubunganKeluarga.setName("HubunganKeluarga"); // NOI18N
        HubunganKeluarga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HubunganKeluargaKeyPressed(evt);
            }
        });
        FormInput.add(HubunganKeluarga);
        HubunganKeluarga.setBounds(249, 760, 100, 23);

        Pengasuh.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Orang Tua", "Kakek/Nenek", "Keluarga Lainnya" }));
        Pengasuh.setName("Pengasuh"); // NOI18N
        Pengasuh.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PengasuhKeyPressed(evt);
            }
        });
        FormInput.add(Pengasuh);
        Pengasuh.setBounds(432, 760, 135, 23);

        KetPengasuh.setFocusTraversalPolicyProvider(true);
        KetPengasuh.setName("KetPengasuh"); // NOI18N
        KetPengasuh.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetPengasuhKeyPressed(evt);
            }
        });
        FormInput.add(KetPengasuh);
        KetPengasuh.setBounds(570, 760, 85, 23);

        jLabel136.setText("b. Pengasuh :");
        jLabel136.setName("jLabel136"); // NOI18N
        FormInput.add(jLabel136);
        jLabel136.setBounds(353, 760, 75, 23);

        Ekonomi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Baik", "Cukup", "Kurang" }));
        Ekonomi.setName("Ekonomi"); // NOI18N
        Ekonomi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EkonomiKeyPressed(evt);
            }
        });
        FormInput.add(Ekonomi);
        Ekonomi.setBounds(770, 760, 84, 23);

        jLabel137.setText("c. Ekonomi (Ortu) :");
        jLabel137.setName("jLabel137"); // NOI18N
        FormInput.add(jLabel137);
        jLabel137.setBounds(656, 760, 110, 23);

        jLabel138.setText("Kepercayaan / Budaya / Nilai-nilai khusus yang perlu diperhatikan :");
        jLabel138.setName("jLabel138"); // NOI18N
        FormInput.add(jLabel138);
        jLabel138.setBounds(0, 790, 366, 23);

        StatusBudaya.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Ada" }));
        StatusBudaya.setName("StatusBudaya"); // NOI18N
        StatusBudaya.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                StatusBudayaKeyPressed(evt);
            }
        });
        FormInput.add(StatusBudaya);
        StatusBudaya.setBounds(370, 790, 110, 23);

        KetBudaya.setFocusTraversalPolicyProvider(true);
        KetBudaya.setName("KetBudaya"); // NOI18N
        KetBudaya.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetBudayaKeyPressed(evt);
            }
        });
        FormInput.add(KetBudaya);
        KetBudaya.setBounds(484, 790, 370, 23);

        jLabel139.setText("Edukasi diberikan kepada :");
        jLabel139.setName("jLabel139"); // NOI18N
        FormInput.add(jLabel139);
        jLabel139.setBounds(226, 820, 140, 23);

        Edukasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Orang Tua", "Keluarga" }));
        Edukasi.setName("Edukasi"); // NOI18N
        Edukasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EdukasiKeyPressed(evt);
            }
        });
        FormInput.add(Edukasi);
        Edukasi.setBounds(370, 820, 110, 23);

        KetEdukasi.setFocusTraversalPolicyProvider(true);
        KetEdukasi.setName("KetEdukasi"); // NOI18N
        KetEdukasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetEdukasiKeyPressed(evt);
            }
        });
        FormInput.add(KetEdukasi);
        KetEdukasi.setBounds(484, 820, 370, 23);

        jLabel140.setText("Agama :");
        jLabel140.setName("jLabel140"); // NOI18N
        FormInput.add(jLabel140);
        jLabel140.setBounds(0, 820, 82, 23);

        Agama.setEditable(false);
        Agama.setFocusTraversalPolicyProvider(true);
        Agama.setName("Agama"); // NOI18N
        FormInput.add(Agama);
        Agama.setBounds(86, 820, 110, 23);

        jSeparator9.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator9.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator9.setName("jSeparator9"); // NOI18N
        FormInput.add(jSeparator9);
        jSeparator9.setBounds(0, 1330, 880, 1);

        jLabel141.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel141.setText("VIII. PENGKAJIAN RESIKO  JATUH  (SKALA HUMPTY DUMPTY)");
        jLabel141.setName("jLabel141"); // NOI18N
        FormInput.add(jLabel141);
        jLabel141.setBounds(10, 1330, 380, 23);

        jLabel149.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel149.setText("IX. SKRINING GIZI (STRONG KID)");
        jLabel149.setName("jLabel149"); // NOI18N
        FormInput.add(jLabel149);
        jLabel149.setBounds(20, 1610, 380, 23);

        jSeparator10.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator10.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator10.setName("jSeparator10"); // NOI18N
        FormInput.add(jSeparator10);
        jSeparator10.setBounds(10, 1610, 880, 1);

        SG1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        SG1.setName("SG1"); // NOI18N
        SG1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SG1ItemStateChanged(evt);
            }
        });
        SG1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SG1KeyPressed(evt);
            }
        });
        FormInput.add(SG1);
        SG1.setBounds(710, 1630, 80, 23);

        jLabel150.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel150.setText("1.  Apakah pasien tampak kurus");
        jLabel150.setName("jLabel150"); // NOI18N
        FormInput.add(jLabel150);
        jLabel150.setBounds(50, 1630, 610, 23);

        jLabel152.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel152.setText("Apakah terdapat salah satu dari kondisi tersebut? Diare > 5 kali/hari dan/muntah > 3 kali/hari dalam seminggu terakhir;");
        jLabel152.setName("jLabel152"); // NOI18N
        FormInput.add(jLabel152);
        jLabel152.setBounds(70, 1710, 610, 23);

        SG2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        SG2.setName("SG2"); // NOI18N
        SG2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SG2ItemStateChanged(evt);
            }
        });
        SG2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SG2KeyPressed(evt);
            }
        });
        FormInput.add(SG2);
        SG2.setBounds(710, 1670, 80, 23);

        jLabel154.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel154.setText("bila ada atau untuk bayi < 1 tahun ; berat badan tidak naik selama 3 bulan terakhir)");
        jLabel154.setName("jLabel154"); // NOI18N
        FormInput.add(jLabel154);
        jLabel154.setBounds(70, 1680, 600, 23);

        jLabel155.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel155.setText("2.");
        jLabel155.setName("jLabel155"); // NOI18N
        FormInput.add(jLabel155);
        jLabel155.setBounds(50, 1660, 20, 37);

        jLabel156.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel156.setText("Asupan makanan berkurang selama 1 minggu terakhir");
        jLabel156.setName("jLabel156"); // NOI18N
        FormInput.add(jLabel156);
        jLabel156.setBounds(70, 1720, 610, 23);

        SG3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        SG3.setName("SG3"); // NOI18N
        SG3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SG3ItemStateChanged(evt);
            }
        });
        SG3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SG3KeyPressed(evt);
            }
        });
        FormInput.add(SG3);
        SG3.setBounds(710, 1710, 80, 23);

        jLabel158.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel158.setText("Apakah terdapat penurunan berat badan selama satu bulan terakhir? (berdasarkan penilaian objektif data berat badan");
        jLabel158.setName("jLabel158"); // NOI18N
        FormInput.add(jLabel158);
        jLabel158.setBounds(70, 1660, 600, 23);

        jLabel159.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel159.setText("3.");
        jLabel159.setName("jLabel159"); // NOI18N
        FormInput.add(jLabel159);
        jLabel159.setBounds(50, 1710, 20, 37);

        jLabel160.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel160.setText("4.  Apakah terdapat penyakit atau keadaan yang menyebabkan pasien beresiko mengalami malnutrisi?");
        jLabel160.setName("jLabel160"); // NOI18N
        FormInput.add(jLabel160);
        jLabel160.setBounds(50, 1750, 610, 23);

        SkalaWajah.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tersenyum/tidak ada ekspresi khusus", "Terkadang meringis/menarik diri", "Sering menggetarkan dagu dan mengatupkan rahang" }));
        SkalaWajah.setName("SkalaWajah"); // NOI18N
        SkalaWajah.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaWajahItemStateChanged(evt);
            }
        });
        SkalaWajah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaWajahKeyPressed(evt);
            }
        });
        FormInput.add(SkalaWajah);
        SkalaWajah.setBounds(130, 1830, 310, 23);

        jLabel162.setText("Total Skor :");
        jLabel162.setName("jLabel162"); // NOI18N
        FormInput.add(jLabel162);
        jLabel162.setBounds(690, 1780, 90, 23);

        NilaiGizi1.setEditable(false);
        NilaiGizi1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        NilaiGizi1.setText("0");
        NilaiGizi1.setFocusTraversalPolicyProvider(true);
        NilaiGizi1.setName("NilaiGizi1"); // NOI18N
        FormInput.add(NilaiGizi1);
        NilaiGizi1.setBounds(810, 1630, 60, 23);

        jSeparator11.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator11.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator11.setName("jSeparator11"); // NOI18N
        FormInput.add(jSeparator11);
        jSeparator11.setBounds(20, 1790, 880, 1);

        jLabel163.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel163.setText("X. PENGKAJIAN TINGKAT NYERI");
        jLabel163.setName("jLabel163"); // NOI18N
        FormInput.add(jLabel163);
        jLabel163.setBounds(30, 1790, 380, 23);

        jLabel164.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel164.setText("Skala FLACCS :");
        jLabel164.setName("jLabel164"); // NOI18N
        FormInput.add(jLabel164);
        jLabel164.setBounds(60, 1810, 210, 23);

        jLabel165.setText("Wajah :");
        jLabel165.setName("jLabel165"); // NOI18N
        FormInput.add(jLabel165);
        jLabel165.setBounds(60, 1830, 60, 23);

        jLabel166.setText("Kaki :");
        jLabel166.setName("jLabel166"); // NOI18N
        FormInput.add(jLabel166);
        jLabel166.setBounds(60, 1860, 60, 23);

        jLabel167.setText("Aktifitas :");
        jLabel167.setName("jLabel167"); // NOI18N
        FormInput.add(jLabel167);
        jLabel167.setBounds(60, 1890, 60, 23);

        jLabel168.setText("Menangis :");
        jLabel168.setName("jLabel168"); // NOI18N
        FormInput.add(jLabel168);
        jLabel168.setBounds(500, 1830, 60, 23);

        jLabel169.setText("Bersuara :");
        jLabel169.setName("jLabel169"); // NOI18N
        FormInput.add(jLabel169);
        jLabel169.setBounds(500, 1860, 60, 23);

        SG4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        SG4.setName("SG4"); // NOI18N
        SG4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SG4ItemStateChanged(evt);
            }
        });
        SG4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SG4KeyPressed(evt);
            }
        });
        FormInput.add(SG4);
        SG4.setBounds(710, 1750, 80, 23);

        NilaiGizi2.setEditable(false);
        NilaiGizi2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        NilaiGizi2.setText("0");
        NilaiGizi2.setFocusTraversalPolicyProvider(true);
        NilaiGizi2.setName("NilaiGizi2"); // NOI18N
        FormInput.add(NilaiGizi2);
        NilaiGizi2.setBounds(810, 1670, 60, 23);

        NilaiGizi3.setEditable(false);
        NilaiGizi3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        NilaiGizi3.setText("0");
        NilaiGizi3.setFocusTraversalPolicyProvider(true);
        NilaiGizi3.setName("NilaiGizi3"); // NOI18N
        FormInput.add(NilaiGizi3);
        NilaiGizi3.setBounds(810, 1710, 60, 23);

        NilaiGizi4.setEditable(false);
        NilaiGizi4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        NilaiGizi4.setText("0");
        NilaiGizi4.setFocusTraversalPolicyProvider(true);
        NilaiGizi4.setName("NilaiGizi4"); // NOI18N
        FormInput.add(NilaiGizi4);
        NilaiGizi4.setBounds(810, 1750, 60, 23);

        NilaiWajah.setEditable(false);
        NilaiWajah.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        NilaiWajah.setText("0");
        NilaiWajah.setFocusTraversalPolicyProvider(true);
        NilaiWajah.setName("NilaiWajah"); // NOI18N
        NilaiWajah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NilaiWajahKeyPressed(evt);
            }
        });
        FormInput.add(NilaiWajah);
        NilaiWajah.setBounds(440, 1830, 40, 23);

        TotalNilaiGizi.setEditable(false);
        TotalNilaiGizi.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TotalNilaiGizi.setText("0");
        TotalNilaiGizi.setFocusTraversalPolicyProvider(true);
        TotalNilaiGizi.setName("TotalNilaiGizi"); // NOI18N
        FormInput.add(TotalNilaiGizi);
        TotalNilaiGizi.setBounds(790, 1780, 80, 23);

        SkalaKaki.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Gerakan normal/relaksasi", "Tidak tenang/tegang", "Kaki dibuat menendang/menarik" }));
        SkalaKaki.setName("SkalaKaki"); // NOI18N
        SkalaKaki.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaKakiItemStateChanged(evt);
            }
        });
        SkalaKaki.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaKakiKeyPressed(evt);
            }
        });
        FormInput.add(SkalaKaki);
        SkalaKaki.setBounds(130, 1860, 310, 23);

        NilaiKaki.setEditable(false);
        NilaiKaki.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        NilaiKaki.setText("0");
        NilaiKaki.setFocusTraversalPolicyProvider(true);
        NilaiKaki.setName("NilaiKaki"); // NOI18N
        NilaiKaki.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NilaiKakiKeyPressed(evt);
            }
        });
        FormInput.add(NilaiKaki);
        NilaiKaki.setBounds(440, 1860, 40, 23);

        SkalaAktifitas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidur posisi normal, mudah bergerak", "Gerakan menggeliat/berguling, kaku", "Melengkungkan punggung/kaku menghentak" }));
        SkalaAktifitas.setName("SkalaAktifitas"); // NOI18N
        SkalaAktifitas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaAktifitasItemStateChanged(evt);
            }
        });
        SkalaAktifitas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaAktifitasKeyPressed(evt);
            }
        });
        FormInput.add(SkalaAktifitas);
        SkalaAktifitas.setBounds(130, 1890, 310, 23);

        NilaiAktifitas.setEditable(false);
        NilaiAktifitas.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        NilaiAktifitas.setText("0");
        NilaiAktifitas.setFocusTraversalPolicyProvider(true);
        NilaiAktifitas.setName("NilaiAktifitas"); // NOI18N
        NilaiAktifitas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NilaiAktifitasKeyPressed(evt);
            }
        });
        FormInput.add(NilaiAktifitas);
        NilaiAktifitas.setBounds(440, 1890, 40, 23);

        SkalaMenangis.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak menangis (mudah bergerak)", "Mengerang/merengek", "Menangis terus menerus, terisak, menjerit" }));
        SkalaMenangis.setName("SkalaMenangis"); // NOI18N
        SkalaMenangis.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaMenangisItemStateChanged(evt);
            }
        });
        SkalaMenangis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaMenangisKeyPressed(evt);
            }
        });
        FormInput.add(SkalaMenangis);
        SkalaMenangis.setBounds(560, 1830, 266, 23);

        NilaiMenangis.setEditable(false);
        NilaiMenangis.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        NilaiMenangis.setText("0");
        NilaiMenangis.setFocusTraversalPolicyProvider(true);
        NilaiMenangis.setName("NilaiMenangis"); // NOI18N
        NilaiMenangis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NilaiMenangisKeyPressed(evt);
            }
        });
        FormInput.add(NilaiMenangis);
        NilaiMenangis.setBounds(830, 1830, 40, 23);

        SkalaBersuara.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bersuara normal/tenang", "Tenang bila dipeluk, digendong/diajak bicara", "Sulit untuk menenangkan" }));
        SkalaBersuara.setName("SkalaBersuara"); // NOI18N
        SkalaBersuara.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaBersuaraItemStateChanged(evt);
            }
        });
        SkalaBersuara.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaBersuaraKeyPressed(evt);
            }
        });
        FormInput.add(SkalaBersuara);
        SkalaBersuara.setBounds(560, 1860, 266, 23);

        NilaiBersuara.setEditable(false);
        NilaiBersuara.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        NilaiBersuara.setText("0");
        NilaiBersuara.setFocusTraversalPolicyProvider(true);
        NilaiBersuara.setName("NilaiBersuara"); // NOI18N
        NilaiBersuara.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NilaiBersuaraKeyPressed(evt);
            }
        });
        FormInput.add(NilaiBersuara);
        NilaiBersuara.setBounds(830, 1860, 40, 23);

        PanelWall.setBackground(new java.awt.Color(29, 29, 29));
        PanelWall.setBackgroundImage(new javax.swing.ImageIcon(getClass().getResource("/picture/nyeri.png"))); // NOI18N
        PanelWall.setBackgroundImageType(usu.widget.constan.BackgroundConstan.BACKGROUND_IMAGE_STRECT);
        PanelWall.setPreferredSize(new java.awt.Dimension(200, 200));
        PanelWall.setRound(false);
        PanelWall.setWarna(new java.awt.Color(110, 110, 110));
        PanelWall.setLayout(null);
        FormInput.add(PanelWall);
        PanelWall.setBounds(60, 1920, 320, 115);

        jSeparator12.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator12.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator12.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator12.setName("jSeparator12"); // NOI18N
        FormInput.add(jSeparator12);
        jSeparator12.setBounds(380, 1920, 1, 125);

        jLabel170.setText("Skala nyeri :");
        jLabel170.setName("jLabel170"); // NOI18N
        FormInput.add(jLabel170);
        jLabel170.setBounds(700, 1890, 90, 23);

        SkalaNyeri.setEditable(false);
        SkalaNyeri.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        SkalaNyeri.setText("0");
        SkalaNyeri.setFocusTraversalPolicyProvider(true);
        SkalaNyeri.setName("SkalaNyeri"); // NOI18N
        SkalaNyeri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaNyeriKeyPressed(evt);
            }
        });
        FormInput.add(SkalaNyeri);
        SkalaNyeri.setBounds(790, 1890, 80, 23);

        Nyeri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada Nyeri", "Nyeri Akut", "Nyeri Kronis" }));
        Nyeri.setName("Nyeri"); // NOI18N
        Nyeri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NyeriKeyPressed(evt);
            }
        });
        FormInput.add(Nyeri);
        Nyeri.setBounds(390, 1920, 140, 23);

        jLabel88.setText("Frekuensi :");
        jLabel88.setName("jLabel88"); // NOI18N
        FormInput.add(jLabel88);
        jLabel88.setBounds(620, 1950, 100, 23);

        Frekuensi.setFocusTraversalPolicyProvider(true);
        Frekuensi.setName("Frekuensi"); // NOI18N
        Frekuensi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                FrekuensiKeyPressed(evt);
            }
        });
        FormInput.add(Frekuensi);
        Frekuensi.setBounds(720, 1950, 150, 23);

        jLabel83.setText("Lokasi :");
        jLabel83.setName("jLabel83"); // NOI18N
        FormInput.add(jLabel83);
        jLabel83.setBounds(620, 1920, 46, 23);

        Lokasi.setFocusTraversalPolicyProvider(true);
        Lokasi.setName("Lokasi"); // NOI18N
        Lokasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LokasiKeyPressed(evt);
            }
        });
        FormInput.add(Lokasi);
        Lokasi.setBounds(670, 1920, 200, 23);

        Durasi.setFocusTraversalPolicyProvider(true);
        Durasi.setName("Durasi"); // NOI18N
        Durasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DurasiKeyPressed(evt);
            }
        });
        FormInput.add(Durasi);
        Durasi.setBounds(440, 1950, 150, 23);

        jLabel87.setText("Durasi :");
        jLabel87.setName("jLabel87"); // NOI18N
        FormInput.add(jLabel87);
        jLabel87.setBounds(390, 1950, 45, 23);

        jLabel89.setText("Nyeri hilang bila :");
        jLabel89.setName("jLabel89"); // NOI18N
        FormInput.add(jLabel89);
        jLabel89.setBounds(390, 1980, 93, 23);

        NyeriHilang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Minum Obat", "Istirahat", "Mendengar Music", "Berubah Posisi/Tidur", "Lain-lain" }));
        NyeriHilang.setName("NyeriHilang"); // NOI18N
        NyeriHilang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NyeriHilangKeyPressed(evt);
            }
        });
        FormInput.add(NyeriHilang);
        NyeriHilang.setBounds(480, 1980, 165, 23);

        KetNyeri.setFocusTraversalPolicyProvider(true);
        KetNyeri.setName("KetNyeri"); // NOI18N
        KetNyeri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetNyeriKeyPressed(evt);
            }
        });
        FormInput.add(KetNyeri);
        KetNyeri.setBounds(650, 1980, 218, 23);

        jLabel86.setText("Diberitahukan pada dokter ?");
        jLabel86.setName("jLabel86"); // NOI18N
        FormInput.add(jLabel86);
        jLabel86.setBounds(400, 2010, 190, 23);

        PadaDokter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PadaDokter.setName("PadaDokter"); // NOI18N
        PadaDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PadaDokterKeyPressed(evt);
            }
        });
        FormInput.add(PadaDokter);
        PadaDokter.setBounds(590, 2010, 80, 23);

        jLabel68.setText("Jam diberitahukan :");
        jLabel68.setName("jLabel68"); // NOI18N
        FormInput.add(jLabel68);
        jLabel68.setBounds(680, 2010, 110, 23);

        KetDokter.setFocusTraversalPolicyProvider(true);
        KetDokter.setName("KetDokter"); // NOI18N
        KetDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetDokterKeyPressed(evt);
            }
        });
        FormInput.add(KetDokter);
        KetDokter.setBounds(790, 2010, 80, 23);

        jSeparator13.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator13.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator13.setName("jSeparator13"); // NOI18N
        FormInput.add(jSeparator13);
        jSeparator13.setBounds(20, 2040, 880, 1);

        Scroll8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 253)));
        Scroll8.setName("Scroll8"); // NOI18N
        Scroll8.setOpaque(true);

        tbMasalahKeperawatan.setName("tbMasalahKeperawatan"); // NOI18N
        tbMasalahKeperawatan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbMasalahKeperawatanMouseClicked(evt);
            }
        });
        tbMasalahKeperawatan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbMasalahKeperawatanKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbMasalahKeperawatanKeyReleased(evt);
            }
        });
        Scroll8.setViewportView(tbMasalahKeperawatan);

        FormInput.add(Scroll8);
        Scroll8.setBounds(20, 2220, 400, 143);

        BtnPanggilHapusImunisasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnPanggilHapusImunisasi.setMnemonic('3');
        BtnPanggilHapusImunisasi.setToolTipText("Alt+3");
        BtnPanggilHapusImunisasi.setName("BtnPanggilHapusImunisasi"); // NOI18N
        BtnPanggilHapusImunisasi.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPanggilHapusImunisasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPanggilHapusImunisasiActionPerformed(evt);
            }
        });
        FormInput.add(BtnPanggilHapusImunisasi);
        BtnPanggilHapusImunisasi.setBounds(62, 430, 28, 23);

        TabRencanaKeperawatan.setBackground(new java.awt.Color(255, 255, 254));
        TabRencanaKeperawatan.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TabRencanaKeperawatan.setForeground(new java.awt.Color(50, 50, 50));
        TabRencanaKeperawatan.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRencanaKeperawatan.setName("TabRencanaKeperawatan"); // NOI18N

        panelBiasa1.setName("panelBiasa1"); // NOI18N
        panelBiasa1.setLayout(new java.awt.BorderLayout());

        Scroll9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 253)));
        Scroll9.setName("Scroll9"); // NOI18N
        Scroll9.setOpaque(true);

        tbRencanaKeperawatan.setName("tbRencanaKeperawatan"); // NOI18N
        Scroll9.setViewportView(tbRencanaKeperawatan);

        panelBiasa1.add(Scroll9, java.awt.BorderLayout.CENTER);

        TabRencanaKeperawatan.addTab("Rencana Keperawatan", panelBiasa1);

        scrollPane5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane5.setName("scrollPane5"); // NOI18N

        Rencana.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Rencana.setColumns(20);
        Rencana.setRows(5);
        Rencana.setName("Rencana"); // NOI18N
        Rencana.setOpaque(true);
        Rencana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RencanaKeyPressed(evt);
            }
        });
        scrollPane5.setViewportView(Rencana);

        TabRencanaKeperawatan.addTab("Rencana Keperawatan Lainnya", scrollPane5);

        FormInput.add(TabRencanaKeperawatan);
        TabRencanaKeperawatan.setBounds(440, 2220, 420, 143);

        label13.setText("Key Word :");
        label13.setName("label13"); // NOI18N
        label13.setPreferredSize(new java.awt.Dimension(60, 23));
        FormInput.add(label13);
        label13.setBounds(450, 2370, 60, 23);

        TCariRencana.setToolTipText("Alt+C");
        TCariRencana.setName("TCariRencana"); // NOI18N
        TCariRencana.setPreferredSize(new java.awt.Dimension(215, 23));
        TCariRencana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariRencanaKeyPressed(evt);
            }
        });
        FormInput.add(TCariRencana);
        TCariRencana.setBounds(510, 2370, 235, 23);

        BtnCariRencana.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCariRencana.setMnemonic('1');
        BtnCariRencana.setToolTipText("Alt+1");
        BtnCariRencana.setName("BtnCariRencana"); // NOI18N
        BtnCariRencana.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCariRencana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariRencanaActionPerformed(evt);
            }
        });
        BtnCariRencana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariRencanaKeyPressed(evt);
            }
        });
        FormInput.add(BtnCariRencana);
        BtnCariRencana.setBounds(750, 2370, 28, 23);

        BtnAllRencana.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAllRencana.setMnemonic('2');
        BtnAllRencana.setToolTipText("2Alt+2");
        BtnAllRencana.setName("BtnAllRencana"); // NOI18N
        BtnAllRencana.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAllRencana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllRencanaActionPerformed(evt);
            }
        });
        BtnAllRencana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllRencanaKeyPressed(evt);
            }
        });
        FormInput.add(BtnAllRencana);
        BtnAllRencana.setBounds(780, 2370, 28, 23);

        BtnTambahRencana.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        BtnTambahRencana.setMnemonic('3');
        BtnTambahRencana.setToolTipText("Alt+3");
        BtnTambahRencana.setName("BtnTambahRencana"); // NOI18N
        BtnTambahRencana.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnTambahRencana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTambahRencanaActionPerformed(evt);
            }
        });
        FormInput.add(BtnTambahRencana);
        BtnTambahRencana.setBounds(810, 2370, 28, 23);

        label12.setText("Key Word :");
        label12.setName("label12"); // NOI18N
        label12.setPreferredSize(new java.awt.Dimension(60, 23));
        FormInput.add(label12);
        label12.setBounds(20, 2370, 60, 23);

        TCariMasalah.setToolTipText("Alt+C");
        TCariMasalah.setName("TCariMasalah"); // NOI18N
        TCariMasalah.setPreferredSize(new java.awt.Dimension(140, 23));
        TCariMasalah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariMasalahKeyPressed(evt);
            }
        });
        FormInput.add(TCariMasalah);
        TCariMasalah.setBounds(90, 2370, 215, 23);

        BtnCariMasalah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCariMasalah.setMnemonic('1');
        BtnCariMasalah.setToolTipText("Alt+1");
        BtnCariMasalah.setName("BtnCariMasalah"); // NOI18N
        BtnCariMasalah.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCariMasalah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariMasalahActionPerformed(evt);
            }
        });
        BtnCariMasalah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariMasalahKeyPressed(evt);
            }
        });
        FormInput.add(BtnCariMasalah);
        BtnCariMasalah.setBounds(310, 2370, 28, 23);

        BtnAllMasalah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAllMasalah.setMnemonic('2');
        BtnAllMasalah.setToolTipText("2Alt+2");
        BtnAllMasalah.setName("BtnAllMasalah"); // NOI18N
        BtnAllMasalah.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAllMasalah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllMasalahActionPerformed(evt);
            }
        });
        BtnAllMasalah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllMasalahKeyPressed(evt);
            }
        });
        FormInput.add(BtnAllMasalah);
        BtnAllMasalah.setBounds(340, 2370, 28, 23);

        BtnTambahMasalah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        BtnTambahMasalah.setMnemonic('3');
        BtnTambahMasalah.setToolTipText("Alt+3");
        BtnTambahMasalah.setName("BtnTambahMasalah"); // NOI18N
        BtnTambahMasalah.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnTambahMasalah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTambahMasalahActionPerformed(evt);
            }
        });
        FormInput.add(BtnTambahMasalah);
        BtnTambahMasalah.setBounds(370, 2370, 28, 23);

        jSeparator14.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator14.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator14.setName("jSeparator14"); // NOI18N
        FormInput.add(jSeparator14);
        jSeparator14.setBounds(0, 860, 880, 1);

        jLabel217.setText("Kemampuan Baca & Tulis :");
        jLabel217.setName("jLabel217"); // NOI18N
        FormInput.add(jLabel217);
        jLabel217.setBounds(50, 880, 140, 23);

        KemampuanBacaTulis.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Baik", "Kurang", "Tidak Bisa" }));
        KemampuanBacaTulis.setName("KemampuanBacaTulis"); // NOI18N
        KemampuanBacaTulis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KemampuanBacaTulisKeyPressed(evt);
            }
        });
        FormInput.add(KemampuanBacaTulis);
        KemampuanBacaTulis.setBounds(200, 880, 100, 23);

        jLabel218.setText("Butuh Penerjamah :");
        jLabel218.setName("jLabel218"); // NOI18N
        FormInput.add(jLabel218);
        jLabel218.setBounds(300, 880, 110, 23);

        ButuhPenerjemah.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        ButuhPenerjemah.setName("ButuhPenerjemah"); // NOI18N
        ButuhPenerjemah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ButuhPenerjemahKeyPressed(evt);
            }
        });
        FormInput.add(ButuhPenerjemah);
        ButuhPenerjemah.setBounds(410, 880, 80, 23);

        jLabel219.setText("Jika Ya :");
        jLabel219.setName("jLabel219"); // NOI18N
        FormInput.add(jLabel219);
        jLabel219.setBounds(490, 880, 50, 23);

        KeteranganButuhPenerjemah.setFocusTraversalPolicyProvider(true);
        KeteranganButuhPenerjemah.setName("KeteranganButuhPenerjemah"); // NOI18N
        KeteranganButuhPenerjemah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeteranganButuhPenerjemahKeyPressed(evt);
            }
        });
        FormInput.add(KeteranganButuhPenerjemah);
        KeteranganButuhPenerjemah.setBounds(540, 880, 310, 23);

        jLabel220.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel220.setText(",");
        jLabel220.setName("jLabel220"); // NOI18N
        FormInput.add(jLabel220);
        jLabel220.setBounds(350, 910, 20, 23);

        jLabel221.setText(":");
        jLabel221.setName("jLabel221"); // NOI18N
        FormInput.add(jLabel221);
        jLabel221.setBounds(0, 910, 252, 23);

        TerdapatHambatanBelajar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        TerdapatHambatanBelajar.setName("TerdapatHambatanBelajar"); // NOI18N
        TerdapatHambatanBelajar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TerdapatHambatanBelajarKeyPressed(evt);
            }
        });
        FormInput.add(TerdapatHambatanBelajar);
        TerdapatHambatanBelajar.setBounds(260, 910, 90, 23);

        jLabel222.setText("Jika Ya :");
        jLabel222.setName("jLabel222"); // NOI18N
        FormInput.add(jLabel222);
        jLabel222.setBounds(340, 910, 60, 23);

        HambatanBelajar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "Gangguan Pendengaran", "Gangguan Penglihatan", "Gangguan Kognitif", "Gangguan Fisik", "Gangguan Emosi", "Keterbatasan Bahasa", "Keterbatasan Budaya", "Keterbatasan Spiritual", "Agama", "Lainnya" }));
        HambatanBelajar.setName("HambatanBelajar"); // NOI18N
        HambatanBelajar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HambatanBelajarKeyPressed(evt);
            }
        });
        FormInput.add(HambatanBelajar);
        HambatanBelajar.setBounds(400, 910, 177, 23);

        KeteranganHambatanBelajar.setFocusTraversalPolicyProvider(true);
        KeteranganHambatanBelajar.setName("KeteranganHambatanBelajar"); // NOI18N
        KeteranganHambatanBelajar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeteranganHambatanBelajarKeyPressed(evt);
            }
        });
        FormInput.add(KeteranganHambatanBelajar);
        KeteranganHambatanBelajar.setBounds(580, 910, 274, 23);

        jLabel224.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel224.setText("Terdapat Hambatan Dalam Pembelajaran");
        jLabel224.setName("jLabel224"); // NOI18N
        FormInput.add(jLabel224);
        jLabel224.setBounds(50, 910, 240, 23);

        jLabel225.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel225.setText(",");
        jLabel225.setName("jLabel225"); // NOI18N
        FormInput.add(jLabel225);
        jLabel225.setBounds(490, 880, 30, 23);

        jLabel223.setText(":");
        jLabel223.setName("jLabel223"); // NOI18N
        FormInput.add(jLabel223);
        jLabel223.setBounds(0, 940, 160, 23);

        HambatanCaraBicara.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Gangguan Bicara" }));
        HambatanCaraBicara.setName("HambatanCaraBicara"); // NOI18N
        HambatanCaraBicara.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HambatanCaraBicaraKeyPressed(evt);
            }
        });
        FormInput.add(HambatanCaraBicara);
        HambatanCaraBicara.setBounds(170, 940, 150, 23);

        HambatanBahasaIsyarat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        HambatanBahasaIsyarat.setName("HambatanBahasaIsyarat"); // NOI18N
        HambatanBahasaIsyarat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HambatanBahasaIsyaratKeyPressed(evt);
            }
        });
        FormInput.add(HambatanBahasaIsyarat);
        HambatanBahasaIsyarat.setBounds(480, 940, 90, 23);

        jLabel226.setText("Hambatan Bahasa Isyarat :");
        jLabel226.setName("jLabel226"); // NOI18N
        FormInput.add(jLabel226);
        jLabel226.setBounds(320, 940, 150, 23);

        jLabel227.setText("Cara Belajar Yang Disukai :");
        jLabel227.setName("jLabel227"); // NOI18N
        FormInput.add(jLabel227);
        jLabel227.setBounds(570, 940, 150, 23);

        CaraBelajarDisukai.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Audio", "Lisan", "Visual", "Demonstrasi", "Tulisan" }));
        CaraBelajarDisukai.setName("CaraBelajarDisukai"); // NOI18N
        CaraBelajarDisukai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CaraBelajarDisukaiKeyPressed(evt);
            }
        });
        FormInput.add(CaraBelajarDisukai);
        CaraBelajarDisukai.setBounds(730, 940, 130, 23);

        jLabel228.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel228.setText("Hambatan Cara Bicara");
        jLabel228.setName("jLabel228"); // NOI18N
        FormInput.add(jLabel228);
        jLabel228.setBounds(50, 940, 140, 23);

        jLabel229.setText(":");
        jLabel229.setName("jLabel229"); // NOI18N
        FormInput.add(jLabel229);
        jLabel229.setBounds(0, 970, 202, 23);

        KesediaanMenerimaInformasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        KesediaanMenerimaInformasi.setName("KesediaanMenerimaInformasi"); // NOI18N
        KesediaanMenerimaInformasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KesediaanMenerimaInformasiKeyPressed(evt);
            }
        });
        FormInput.add(KesediaanMenerimaInformasi);
        KesediaanMenerimaInformasi.setBounds(210, 970, 90, 23);

        KeteranganKesediaanMenerimaInformasi.setFocusTraversalPolicyProvider(true);
        KeteranganKesediaanMenerimaInformasi.setName("KeteranganKesediaanMenerimaInformasi"); // NOI18N
        KeteranganKesediaanMenerimaInformasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeteranganKesediaanMenerimaInformasiKeyPressed(evt);
            }
        });
        FormInput.add(KeteranganKesediaanMenerimaInformasi);
        KeteranganKesediaanMenerimaInformasi.setBounds(300, 970, 273, 23);

        jLabel230.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel230.setText("Kesediaan Menerima Informasi");
        jLabel230.setName("jLabel230"); // NOI18N
        FormInput.add(jLabel230);
        jLabel230.setBounds(50, 970, 170, 23);

        jLabel231.setText("Pemahaman Tentang Nutrisi/Diet :");
        jLabel231.setName("jLabel231"); // NOI18N
        FormInput.add(jLabel231);
        jLabel231.setBounds(580, 970, 180, 23);

        PemahamanNutrisi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        PemahamanNutrisi.setName("PemahamanNutrisi"); // NOI18N
        PemahamanNutrisi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PemahamanNutrisiKeyPressed(evt);
            }
        });
        FormInput.add(PemahamanNutrisi);
        PemahamanNutrisi.setBounds(770, 970, 90, 23);

        PemahamanPenyakit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        PemahamanPenyakit.setName("PemahamanPenyakit"); // NOI18N
        PemahamanPenyakit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PemahamanPenyakitKeyPressed(evt);
            }
        });
        FormInput.add(PemahamanPenyakit);
        PemahamanPenyakit.setBounds(210, 1000, 90, 23);

        jLabel232.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel232.setText("Pemahaman Tentang Penyakit");
        jLabel232.setName("jLabel232"); // NOI18N
        FormInput.add(jLabel232);
        jLabel232.setBounds(50, 1000, 180, 23);

        jLabel233.setText(":");
        jLabel233.setName("jLabel233"); // NOI18N
        FormInput.add(jLabel233);
        jLabel233.setBounds(0, 1000, 198, 23);

        jLabel234.setText("Pemahaman Tentang Perawatan :");
        jLabel234.setName("jLabel234"); // NOI18N
        FormInput.add(jLabel234);
        jLabel234.setBounds(580, 1000, 180, 23);

        PemahamanPerawatan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        PemahamanPerawatan.setName("PemahamanPerawatan"); // NOI18N
        PemahamanPerawatan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PemahamanPerawatanKeyPressed(evt);
            }
        });
        FormInput.add(PemahamanPerawatan);
        PemahamanPerawatan.setBounds(770, 1000, 90, 23);

        PemahamanPengobatan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        PemahamanPengobatan.setName("PemahamanPengobatan"); // NOI18N
        PemahamanPengobatan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PemahamanPengobatanKeyPressed(evt);
            }
        });
        FormInput.add(PemahamanPengobatan);
        PemahamanPengobatan.setBounds(490, 1000, 90, 23);

        jLabel235.setText("Pemahaman Tentang Pengobatan :");
        jLabel235.setName("jLabel235"); // NOI18N
        FormInput.add(jLabel235);
        jLabel235.setBounds(310, 1000, 180, 23);

        jLabel273.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel273.setText("Keterbatasan Fisik:");
        jLabel273.setName("jLabel273"); // NOI18N
        FormInput.add(jLabel273);
        jLabel273.setBounds(340, 1030, 100, 23);

        jLabel274.setText("Hambatan Emosional :");
        jLabel274.setName("jLabel274"); // NOI18N
        FormInput.add(jLabel274);
        jLabel274.setBounds(620, 1030, 110, 23);

        KeyakinanNilai.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Flexible", "Primitive", "Kaku", "Sulit Berubah", "Modern", "Mudah Berubah" }));
        KeyakinanNilai.setName("KeyakinanNilai"); // NOI18N
        KeyakinanNilai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeyakinanNilaiKeyPressed(evt);
            }
        });
        FormInput.add(KeyakinanNilai);
        KeyakinanNilai.setBounds(170, 1030, 90, 23);

        jLabel275.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel275.setText("Motivasi :");
        jLabel275.setName("jLabel275"); // NOI18N
        FormInput.add(jLabel275);
        jLabel275.setBounds(110, 1060, 60, 23);

        HambatanEmosional.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sabar", "Pemarah" }));
        HambatanEmosional.setName("HambatanEmosional"); // NOI18N
        HambatanEmosional.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HambatanEmosionalKeyPressed(evt);
            }
        });
        FormInput.add(HambatanEmosional);
        HambatanEmosional.setBounds(730, 1030, 90, 23);

        KeterbatasanFisik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada Keterbatasan", "Gangguan Penglihatan", "Gangguan Pendengaran", "Sulit Mengerti Sesuatu", "Keterbatasan Fisik" }));
        KeterbatasanFisik.setName("KeterbatasanFisik"); // NOI18N
        KeterbatasanFisik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeterbatasanFisikKeyPressed(evt);
            }
        });
        FormInput.add(KeterbatasanFisik);
        KeterbatasanFisik.setBounds(440, 1030, 150, 23);

        Motivasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aktif", "Apatis" }));
        Motivasi.setName("Motivasi"); // NOI18N
        Motivasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MotivasiKeyPressed(evt);
            }
        });
        FormInput.add(Motivasi);
        Motivasi.setBounds(170, 1060, 90, 23);

        jLabel276.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel276.setText("Keyakinan & Nilai :");
        jLabel276.setName("jLabel276"); // NOI18N
        FormInput.add(jLabel276);
        jLabel276.setBounds(70, 1030, 100, 23);

        jLabel277.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel277.setText("IV. KEBUTUHAN KOMUNIKASI DAN BELAJAR/EDUKASI (ORANGTUA)");
        jLabel277.setName("jLabel277"); // NOI18N
        FormInput.add(jLabel277);
        jLabel277.setBounds(10, 860, 400, 23);

        jLabel214.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel214.setText("IV. ASESMEN KEBUTUHAN EDUKASI DAN KOMUNIKASI");
        jLabel214.setName("jLabel214"); // NOI18N
        FormInput.add(jLabel214);
        jLabel214.setBounds(10, 1100, 400, 23);

        jSeparator15.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator15.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator15.setName("jSeparator15"); // NOI18N
        FormInput.add(jSeparator15);
        jSeparator15.setBounds(10, 1090, 880, 1);

        Scroll12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 253)));
        Scroll12.setName("Scroll12"); // NOI18N
        Scroll12.setOpaque(true);

        tbKebutuhanEdukasi.setName("tbKebutuhanEdukasi"); // NOI18N
        tbKebutuhanEdukasi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbKebutuhanEdukasiMouseClicked(evt);
            }
        });
        tbKebutuhanEdukasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbKebutuhanEdukasiKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbKebutuhanEdukasiKeyReleased(evt);
            }
        });
        Scroll12.setViewportView(tbKebutuhanEdukasi);

        FormInput.add(Scroll12);
        Scroll12.setBounds(20, 1140, 400, 143);

        label32.setText("Key Word :");
        label32.setName("label32"); // NOI18N
        label32.setPreferredSize(new java.awt.Dimension(60, 23));
        FormInput.add(label32);
        label32.setBounds(20, 1300, 60, 23);

        BtnTambahKebutuhanEdukasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        BtnTambahKebutuhanEdukasi.setMnemonic('3');
        BtnTambahKebutuhanEdukasi.setToolTipText("Alt+3");
        BtnTambahKebutuhanEdukasi.setName("BtnTambahKebutuhanEdukasi"); // NOI18N
        BtnTambahKebutuhanEdukasi.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnTambahKebutuhanEdukasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTambahKebutuhanEdukasiActionPerformed(evt);
            }
        });
        FormInput.add(BtnTambahKebutuhanEdukasi);
        BtnTambahKebutuhanEdukasi.setBounds(370, 1300, 28, 23);

        BtnAllKebutuhanEdukasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAllKebutuhanEdukasi.setMnemonic('2');
        BtnAllKebutuhanEdukasi.setToolTipText("2Alt+2");
        BtnAllKebutuhanEdukasi.setName("BtnAllKebutuhanEdukasi"); // NOI18N
        BtnAllKebutuhanEdukasi.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAllKebutuhanEdukasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllKebutuhanEdukasiActionPerformed(evt);
            }
        });
        BtnAllKebutuhanEdukasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKebutuhanEdukasiKeyPressed(evt);
            }
        });
        FormInput.add(BtnAllKebutuhanEdukasi);
        BtnAllKebutuhanEdukasi.setBounds(340, 1300, 28, 23);

        BtnCariKebutuhanEdukasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCariKebutuhanEdukasi.setMnemonic('1');
        BtnCariKebutuhanEdukasi.setToolTipText("Alt+1");
        BtnCariKebutuhanEdukasi.setName("BtnCariKebutuhanEdukasi"); // NOI18N
        BtnCariKebutuhanEdukasi.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCariKebutuhanEdukasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariKebutuhanEdukasiActionPerformed(evt);
            }
        });
        BtnCariKebutuhanEdukasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKebutuhanEdukasiKeyPressed(evt);
            }
        });
        FormInput.add(BtnCariKebutuhanEdukasi);
        BtnCariKebutuhanEdukasi.setBounds(310, 1300, 28, 23);

        TabRencanaKeperawatan2.setBackground(new java.awt.Color(255, 255, 254));
        TabRencanaKeperawatan2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TabRencanaKeperawatan2.setForeground(new java.awt.Color(50, 50, 50));
        TabRencanaKeperawatan2.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRencanaKeperawatan2.setName("TabRencanaKeperawatan2"); // NOI18N

        panelBiasa3.setName("panelBiasa3"); // NOI18N
        panelBiasa3.setLayout(new java.awt.BorderLayout());

        Scroll13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 253)));
        Scroll13.setName("Scroll13"); // NOI18N
        Scroll13.setOpaque(true);

        tbRencanaEdukasi.setName("tbRencanaEdukasi"); // NOI18N
        Scroll13.setViewportView(tbRencanaEdukasi);

        panelBiasa3.add(Scroll13, java.awt.BorderLayout.CENTER);

        TabRencanaKeperawatan2.addTab("Rencana Edukasi/Komunikasi", panelBiasa3);

        FormInput.add(TabRencanaKeperawatan2);
        TabRencanaKeperawatan2.setBounds(440, 1130, 420, 150);

        TCariMasalah2.setToolTipText("Alt+C");
        TCariMasalah2.setName("TCariMasalah2"); // NOI18N
        TCariMasalah2.setPreferredSize(new java.awt.Dimension(140, 23));
        TCariMasalah2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariMasalah2KeyPressed(evt);
            }
        });
        FormInput.add(TCariMasalah2);
        TCariMasalah2.setBounds(90, 1300, 215, 23);

        BtnTambahRencanaEdukasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        BtnTambahRencanaEdukasi.setMnemonic('3');
        BtnTambahRencanaEdukasi.setToolTipText("Alt+3");
        BtnTambahRencanaEdukasi.setName("BtnTambahRencanaEdukasi"); // NOI18N
        BtnTambahRencanaEdukasi.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnTambahRencanaEdukasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTambahRencanaEdukasiActionPerformed(evt);
            }
        });
        FormInput.add(BtnTambahRencanaEdukasi);
        BtnTambahRencanaEdukasi.setBounds(810, 1300, 28, 23);

        BtnAllRencanaEdukasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAllRencanaEdukasi.setMnemonic('2');
        BtnAllRencanaEdukasi.setToolTipText("2Alt+2");
        BtnAllRencanaEdukasi.setName("BtnAllRencanaEdukasi"); // NOI18N
        BtnAllRencanaEdukasi.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAllRencanaEdukasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllRencanaEdukasiActionPerformed(evt);
            }
        });
        BtnAllRencanaEdukasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllRencanaEdukasiKeyPressed(evt);
            }
        });
        FormInput.add(BtnAllRencanaEdukasi);
        BtnAllRencanaEdukasi.setBounds(780, 1300, 28, 23);

        BtnCariRencanaEdukasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCariRencanaEdukasi.setMnemonic('1');
        BtnCariRencanaEdukasi.setToolTipText("Alt+1");
        BtnCariRencanaEdukasi.setName("BtnCariRencanaEdukasi"); // NOI18N
        BtnCariRencanaEdukasi.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCariRencanaEdukasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariRencanaEdukasiActionPerformed(evt);
            }
        });
        BtnCariRencanaEdukasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariRencanaEdukasiKeyPressed(evt);
            }
        });
        FormInput.add(BtnCariRencanaEdukasi);
        BtnCariRencanaEdukasi.setBounds(750, 1300, 28, 23);

        label33.setText("Key Word :");
        label33.setName("label33"); // NOI18N
        label33.setPreferredSize(new java.awt.Dimension(60, 23));
        FormInput.add(label33);
        label33.setBounds(450, 1300, 60, 23);

        TCariRencana2.setToolTipText("Alt+C");
        TCariRencana2.setName("TCariRencana2"); // NOI18N
        TCariRencana2.setPreferredSize(new java.awt.Dimension(215, 23));
        TCariRencana2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariRencana2KeyPressed(evt);
            }
        });
        FormInput.add(TCariRencana2);
        TCariRencana2.setBounds(510, 1300, 235, 23);

        jLabel243.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel243.setText("1. Umur");
        jLabel243.setName("jLabel243"); // NOI18N
        FormInput.add(jLabel243);
        jLabel243.setBounds(40, 1360, 200, 23);

        SkalaResiko1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0 - 3 Tahun", "3 - 7 Tahun", "7 - 13 Tahun", "> 13 Tahun" }));
        SkalaResiko1.setName("SkalaResiko1"); // NOI18N
        SkalaResiko1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko1ItemStateChanged(evt);
            }
        });
        SkalaResiko1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SkalaResiko1ActionPerformed(evt);
            }
        });
        SkalaResiko1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko1KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko1);
        SkalaResiko1.setBounds(250, 1360, 550, 23);

        NilaiResiko1.setEditable(false);
        NilaiResiko1.setFocusTraversalPolicyProvider(true);
        NilaiResiko1.setName("NilaiResiko1"); // NOI18N
        FormInput.add(NilaiResiko1);
        NilaiResiko1.setBounds(810, 1360, 40, 23);

        jLabel244.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel244.setText("2. Jenis Kelamin");
        jLabel244.setName("jLabel244"); // NOI18N
        FormInput.add(jLabel244);
        jLabel244.setBounds(40, 1390, 200, 23);

        SkalaResiko2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Laki-laki", "Perempuan" }));
        SkalaResiko2.setName("SkalaResiko2"); // NOI18N
        SkalaResiko2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko2ItemStateChanged(evt);
            }
        });
        SkalaResiko2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko2KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko2);
        SkalaResiko2.setBounds(250, 1390, 550, 23);

        NilaiResiko2.setEditable(false);
        NilaiResiko2.setFocusTraversalPolicyProvider(true);
        NilaiResiko2.setName("NilaiResiko2"); // NOI18N
        FormInput.add(NilaiResiko2);
        NilaiResiko2.setBounds(810, 1390, 40, 23);

        jLabel245.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel245.setText("3. Diagnosa");
        jLabel245.setName("jLabel245"); // NOI18N
        FormInput.add(jLabel245);
        jLabel245.setBounds(40, 1420, 200, 23);

        SkalaResiko3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kelainan Neurologi", "Perubahan Dalam Oksigen(Masalah Saluran Nafas, Dehidrasi, Anemia, Anoreksia / Sakit Kepala, Dll)", "Kelainan Psikis / Perilaku", "Diagnosa Lain" }));
        SkalaResiko3.setName("SkalaResiko3"); // NOI18N
        SkalaResiko3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko3ItemStateChanged(evt);
            }
        });
        SkalaResiko3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko3KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko3);
        SkalaResiko3.setBounds(250, 1420, 550, 23);

        NilaiResiko3.setEditable(false);
        NilaiResiko3.setFocusTraversalPolicyProvider(true);
        NilaiResiko3.setName("NilaiResiko3"); // NOI18N
        FormInput.add(NilaiResiko3);
        NilaiResiko3.setBounds(810, 1420, 40, 23);

        jLabel246.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel246.setText("4. Gangguan Kognitif");
        jLabel246.setName("jLabel246"); // NOI18N
        FormInput.add(jLabel246);
        jLabel246.setBounds(40, 1450, 200, 23);

        SkalaResiko4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Sadar Terhadap Keterbatasan", "Lupa Keterbatasan", "Mengetahui Kemampuan Diri" }));
        SkalaResiko4.setName("SkalaResiko4"); // NOI18N
        SkalaResiko4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko4ItemStateChanged(evt);
            }
        });
        SkalaResiko4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko4KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko4);
        SkalaResiko4.setBounds(250, 1450, 550, 23);

        NilaiResiko4.setEditable(false);
        NilaiResiko4.setFocusTraversalPolicyProvider(true);
        NilaiResiko4.setName("NilaiResiko4"); // NOI18N
        FormInput.add(NilaiResiko4);
        NilaiResiko4.setBounds(810, 1450, 40, 23);

        jLabel247.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel247.setText("5. Faktor Lingkungan");
        jLabel247.setName("jLabel247"); // NOI18N
        FormInput.add(jLabel247);
        jLabel247.setBounds(40, 1480, 200, 23);

        SkalaResiko5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Riwayat Jatuh Dari Tempat Tidur Saat Bayi/Anak", "Pasien Menggunakan Alat Bantu/Box/Mebel", "Pasien Berada Di Tempat Tidur", "Di Luar Ruang Rawat" }));
        SkalaResiko5.setName("SkalaResiko5"); // NOI18N
        SkalaResiko5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko5ItemStateChanged(evt);
            }
        });
        SkalaResiko5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko5KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko5);
        SkalaResiko5.setBounds(250, 1480, 550, 23);

        NilaiResiko5.setEditable(false);
        NilaiResiko5.setFocusTraversalPolicyProvider(true);
        NilaiResiko5.setName("NilaiResiko5"); // NOI18N
        FormInput.add(NilaiResiko5);
        NilaiResiko5.setBounds(810, 1480, 40, 23);

        jLabel248.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel248.setText("6. Efek Obat Penenang/Operasi/Anastesi");
        jLabel248.setName("jLabel248"); // NOI18N
        FormInput.add(jLabel248);
        jLabel248.setBounds(40, 1510, 210, 23);

        SkalaResiko6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Dalam 24 Jam", "Dalam 48 Jam", "> 48 Jam" }));
        SkalaResiko6.setName("SkalaResiko6"); // NOI18N
        SkalaResiko6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko6ItemStateChanged(evt);
            }
        });
        SkalaResiko6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko6KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko6);
        SkalaResiko6.setBounds(250, 1510, 550, 23);

        NilaiResiko6.setEditable(false);
        NilaiResiko6.setFocusTraversalPolicyProvider(true);
        NilaiResiko6.setName("NilaiResiko6"); // NOI18N
        FormInput.add(NilaiResiko6);
        NilaiResiko6.setBounds(810, 1510, 40, 23);

        jLabel249.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel249.setText("7. Penggunaan Obat");
        jLabel249.setName("jLabel249"); // NOI18N
        FormInput.add(jLabel249);
        jLabel249.setBounds(40, 1540, 210, 23);

        SkalaResiko7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bermacam-macam Obat Yang Digunakan : Obat Sedative (Kecuali Pasien ICU Yang Menggunakan sedasi dan paralisis), Hipnotik, Barbiturat, Fenoti-Azin, Antidepresan, Laksans/Diuretika,Narkotik", "Salah Satu Dari Pengobatan Di Atas", "Pengobatan Lain" }));
        SkalaResiko7.setName("SkalaResiko7"); // NOI18N
        SkalaResiko7.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko7ItemStateChanged(evt);
            }
        });
        SkalaResiko7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko7KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko7);
        SkalaResiko7.setBounds(250, 1540, 550, 23);

        NilaiResiko7.setEditable(false);
        NilaiResiko7.setFocusTraversalPolicyProvider(true);
        NilaiResiko7.setName("NilaiResiko7"); // NOI18N
        FormInput.add(NilaiResiko7);
        NilaiResiko7.setBounds(810, 1540, 40, 23);

        TingkatResiko1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TingkatResiko1.setText("Tingkat Resiko");
        TingkatResiko1.setToolTipText("");
        TingkatResiko1.setName("TingkatResiko1"); // NOI18N
        FormInput.add(TingkatResiko1);
        TingkatResiko1.setBounds(40, 1570, 90, 23);

        jLabel256.setText("Total :");
        jLabel256.setName("jLabel256"); // NOI18N
        FormInput.add(jLabel256);
        jLabel256.setBounds(730, 1570, 70, 23);

        NilaiResikoTotal.setEditable(false);
        NilaiResikoTotal.setFocusTraversalPolicyProvider(true);
        NilaiResikoTotal.setName("NilaiResikoTotal"); // NOI18N
        FormInput.add(NilaiResikoTotal);
        NilaiResikoTotal.setBounds(810, 1570, 40, 23);

        KeteranganTingkatRisiko.setEditable(false);
        KeteranganTingkatRisiko.setFocusTraversalPolicyProvider(true);
        KeteranganTingkatRisiko.setName("KeteranganTingkatRisiko"); // NOI18N
        FormInput.add(KeteranganTingkatRisiko);
        KeteranganTingkatRisiko.setBounds(250, 1570, 500, 23);

        jLabel267.setText(":");
        jLabel267.setName("jLabel267"); // NOI18N
        FormInput.add(jLabel267);
        jLabel267.setBounds(0, 1570, 122, 23);

        jSeparator16.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator16.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator16.setName("jSeparator16"); // NOI18N
        FormInput.add(jSeparator16);
        jSeparator16.setBounds(0, 2050, 880, 1);

        jLabel272.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel272.setText("VIII. PERENCANAAN PULANG (DISCHARGE PLANNING)");
        jLabel272.setName("jLabel272"); // NOI18N
        FormInput.add(jLabel272);
        jLabel272.setBounds(20, 2050, 380, 23);

        InformasiPerencanaanPulang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        InformasiPerencanaanPulang.setName("InformasiPerencanaanPulang"); // NOI18N
        InformasiPerencanaanPulang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                InformasiPerencanaanPulangKeyPressed(evt);
            }
        });
        FormInput.add(InformasiPerencanaanPulang);
        InformasiPerencanaanPulang.setBounds(350, 2070, 80, 23);

        jLabel250.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel250.setText("Ibu Bayi & Keluarga Diberikan Informasi Perencanaan Pulang");
        jLabel250.setName("jLabel250"); // NOI18N
        FormInput.add(jLabel250);
        jLabel250.setBounds(40, 2070, 330, 23);

        jLabel257.setText("?");
        jLabel257.setName("jLabel257"); // NOI18N
        FormInput.add(jLabel257);
        jLabel257.setBounds(0, 2070, 347, 23);

        jLabel260.setText(":");
        jLabel260.setName("jLabel260"); // NOI18N
        FormInput.add(jLabel260);
        jLabel260.setBounds(0, 2100, 176, 23);

        KondisiPulang.setFocusTraversalPolicyProvider(true);
        KondisiPulang.setName("KondisiPulang"); // NOI18N
        KondisiPulang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KondisiPulangKeyPressed(evt);
            }
        });
        FormInput.add(KondisiPulang);
        KondisiPulang.setBounds(180, 2100, 674, 23);

        jLabel259.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel259.setText("Kondisi Klinis Saat Pulang");
        jLabel259.setName("jLabel259"); // NOI18N
        FormInput.add(jLabel259);
        jLabel259.setBounds(40, 2100, 180, 23);

        label29.setText("Perencanaan Pulang :");
        label29.setName("label29"); // NOI18N
        label29.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label29);
        label29.setBounds(630, 2070, 130, 23);

        TanggalPulang.setForeground(new java.awt.Color(50, 70, 50));
        TanggalPulang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "06-04-2026" }));
        TanggalPulang.setDisplayFormat("dd-MM-yyyy");
        TanggalPulang.setName("TanggalPulang"); // NOI18N
        TanggalPulang.setOpaque(false);
        TanggalPulang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalPulangKeyPressed(evt);
            }
        });
        FormInput.add(TanggalPulang);
        TanggalPulang.setBounds(760, 2070, 90, 23);

        jLabel261.setText("Lama Rawat Rata-rata :");
        jLabel261.setName("jLabel261"); // NOI18N
        FormInput.add(jLabel261);
        jLabel261.setBounds(440, 2070, 130, 23);

        LamaRatarata.setFocusTraversalPolicyProvider(true);
        LamaRatarata.setName("LamaRatarata"); // NOI18N
        LamaRatarata.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LamaRatarataKeyPressed(evt);
            }
        });
        FormInput.add(LamaRatarata);
        LamaRatarata.setBounds(570, 2070, 55, 23);

        jLabel262.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel262.setText("Perawatan Lanjutan Yang Diberikan Di Rumah");
        jLabel262.setName("jLabel262"); // NOI18N
        FormInput.add(jLabel262);
        jLabel262.setBounds(30, 2130, 240, 23);

        scrollPane7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane7.setName("scrollPane7"); // NOI18N

        PerawatanLanjutan.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        PerawatanLanjutan.setColumns(20);
        PerawatanLanjutan.setRows(5);
        PerawatanLanjutan.setName("PerawatanLanjutan"); // NOI18N
        PerawatanLanjutan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PerawatanLanjutanKeyPressed(evt);
            }
        });
        scrollPane7.setViewportView(PerawatanLanjutan);

        FormInput.add(scrollPane7);
        scrollPane7.setBounds(270, 2130, 576, 43);

        jLabel263.setText(":");
        jLabel263.setName("jLabel263"); // NOI18N
        FormInput.add(jLabel263);
        jLabel263.setBounds(0, 2130, 260, 20);

        CaraTransportasiPulang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mandiri", "Dibantu Sebagian", "Dibantu Keseluruhan", "Menggunakan Rostul", "Menggunakan Brankar", "Berjalan" }));
        CaraTransportasiPulang.setName("CaraTransportasiPulang"); // NOI18N
        CaraTransportasiPulang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CaraTransportasiPulangKeyPressed(evt);
            }
        });
        FormInput.add(CaraTransportasiPulang);
        CaraTransportasiPulang.setBounds(170, 2180, 160, 23);

        jLabel264.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel264.setText("Cara Transportasi Pulang");
        jLabel264.setName("jLabel264"); // NOI18N
        FormInput.add(jLabel264);
        jLabel264.setBounds(40, 2180, 140, 23);

        jLabel265.setText(":");
        jLabel265.setName("jLabel265"); // NOI18N
        FormInput.add(jLabel265);
        jLabel265.setBounds(0, 2180, 174, 23);

        jLabel266.setText("Transportasi Yang Digunakan :");
        jLabel266.setName("jLabel266"); // NOI18N
        FormInput.add(jLabel266);
        jLabel266.setBounds(340, 2180, 167, 23);

        TransportasiYangDigunakan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kendaraan Pribadi", "Mobil Ambulance", "Kendaraan Umum" }));
        TransportasiYangDigunakan.setName("TransportasiYangDigunakan"); // NOI18N
        TransportasiYangDigunakan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TransportasiYangDigunakanKeyPressed(evt);
            }
        });
        FormInput.add(TransportasiYangDigunakan);
        TransportasiYangDigunakan.setBounds(510, 2180, 140, 23);

        scrollInput.setViewportView(FormInput);

        internalFrame2.add(scrollInput, java.awt.BorderLayout.CENTER);

        TabRawat.addTab("Input Pengkajian", internalFrame2);

        internalFrame3.setBorder(null);
        internalFrame3.setName("internalFrame3"); // NOI18N
        internalFrame3.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);
        Scroll.setPreferredSize(new java.awt.Dimension(452, 200));

        tbObat.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbObat.setName("tbObat"); // NOI18N
        tbObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObatMouseClicked(evt);
            }
        });
        tbObat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbObatKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbObat);

        internalFrame3.add(Scroll, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel19.setText("Tgl.Asuhan :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "06-04-2026" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(DTPCari1);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("s.d.");
        jLabel21.setName("jLabel21"); // NOI18N
        jLabel21.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass9.add(jLabel21);

        DTPCari2.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "06-04-2026" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(DTPCari2);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass9.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(195, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass9.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('3');
        BtnCari.setToolTipText("Alt+3");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariActionPerformed(evt);
            }
        });
        BtnCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnCari);

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass9.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(LCount);

        internalFrame3.add(panelGlass9, java.awt.BorderLayout.PAGE_END);

        PanelAccor.setBackground(new java.awt.Color(255, 255, 255));
        PanelAccor.setName("PanelAccor"); // NOI18N
        PanelAccor.setPreferredSize(new java.awt.Dimension(470, 43));
        PanelAccor.setLayout(new java.awt.BorderLayout(1, 1));

        ChkAccor.setBackground(new java.awt.Color(255, 250, 250));
        ChkAccor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kiri.png"))); // NOI18N
        ChkAccor.setSelected(true);
        ChkAccor.setFocusable(false);
        ChkAccor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkAccor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkAccor.setName("ChkAccor"); // NOI18N
        ChkAccor.setPreferredSize(new java.awt.Dimension(15, 20));
        ChkAccor.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kiri.png"))); // NOI18N
        ChkAccor.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kanan.png"))); // NOI18N
        ChkAccor.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kanan.png"))); // NOI18N
        ChkAccor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkAccorActionPerformed(evt);
            }
        });
        PanelAccor.add(ChkAccor, java.awt.BorderLayout.WEST);

        FormMenu.setBackground(new java.awt.Color(255, 255, 255));
        FormMenu.setBorder(null);
        FormMenu.setName("FormMenu"); // NOI18N
        FormMenu.setPreferredSize(new java.awt.Dimension(115, 43));
        FormMenu.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        jLabel34.setText("Pasien :");
        jLabel34.setName("jLabel34"); // NOI18N
        jLabel34.setPreferredSize(new java.awt.Dimension(55, 23));
        FormMenu.add(jLabel34);

        TNoRM1.setEditable(false);
        TNoRM1.setHighlighter(null);
        TNoRM1.setName("TNoRM1"); // NOI18N
        TNoRM1.setPreferredSize(new java.awt.Dimension(100, 23));
        FormMenu.add(TNoRM1);

        TPasien1.setEditable(false);
        TPasien1.setBackground(new java.awt.Color(245, 250, 240));
        TPasien1.setHighlighter(null);
        TPasien1.setName("TPasien1"); // NOI18N
        TPasien1.setPreferredSize(new java.awt.Dimension(250, 23));
        FormMenu.add(TPasien1);

        BtnPrint1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item (copy).png"))); // NOI18N
        BtnPrint1.setMnemonic('T');
        BtnPrint1.setToolTipText("Alt+T");
        BtnPrint1.setName("BtnPrint1"); // NOI18N
        BtnPrint1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPrint1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrint1ActionPerformed(evt);
            }
        });
        FormMenu.add(BtnPrint1);

        PanelAccor.add(FormMenu, java.awt.BorderLayout.NORTH);

        TabRawat1.setBackground(new java.awt.Color(254, 255, 254));
        TabRawat1.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRawat1.setName("TabRawat1"); // NOI18N

        FormMasalahRencana.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)));
        FormMasalahRencana.setName("FormMasalahRencana"); // NOI18N
        FormMasalahRencana.setLayout(new java.awt.GridLayout(4, 0, 1, 1));

        scrollPane9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)), "Riwayat Imunisasi :", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        scrollPane9.setName("scrollPane9"); // NOI18N

        tbImunisasi2.setName("tbImunisasi2"); // NOI18N
        scrollPane9.setViewportView(tbImunisasi2);

        FormMasalahRencana.add(scrollPane9);

        Scroll7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)));
        Scroll7.setName("Scroll7"); // NOI18N
        Scroll7.setOpaque(true);

        tbMasalahDetailMasalah.setName("tbMasalahDetailMasalah"); // NOI18N
        Scroll7.setViewportView(tbMasalahDetailMasalah);

        FormMasalahRencana.add(Scroll7);

        Scroll10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)));
        Scroll10.setName("Scroll10"); // NOI18N
        Scroll10.setOpaque(true);

        tbRencanaDetail.setName("tbRencanaDetail"); // NOI18N
        Scroll10.setViewportView(tbRencanaDetail);

        FormMasalahRencana.add(Scroll10);

        scrollPane6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)), "Rencana Keperawatan Lainnya :", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        scrollPane6.setName("scrollPane6"); // NOI18N

        DetailRencana.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        DetailRencana.setColumns(20);
        DetailRencana.setRows(5);
        DetailRencana.setName("DetailRencana"); // NOI18N
        scrollPane6.setViewportView(DetailRencana);

        FormMasalahRencana.add(scrollPane6);

        TabRawat1.addTab("Masalah/Rencana Keperawatan", FormMasalahRencana);

        FormKebutuhanRencana.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)));
        FormKebutuhanRencana.setName("FormKebutuhanRencana"); // NOI18N
        FormKebutuhanRencana.setLayout(new java.awt.GridLayout(3, 0, 1, 1));

        Scroll15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)));
        Scroll15.setName("Scroll15"); // NOI18N
        Scroll15.setOpaque(true);

        tbDetailKebutuhanEdukasi.setName("tbDetailKebutuhanEdukasi"); // NOI18N
        Scroll15.setViewportView(tbDetailKebutuhanEdukasi);

        FormKebutuhanRencana.add(Scroll15);

        Scroll16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)));
        Scroll16.setName("Scroll16"); // NOI18N
        Scroll16.setOpaque(true);

        tbDetailRencanaEdukasi.setName("tbDetailRencanaEdukasi"); // NOI18N
        Scroll16.setViewportView(tbDetailRencanaEdukasi);

        FormKebutuhanRencana.add(Scroll16);

        TabRawat1.addTab("Kebutuhan/Rencana Edukasi", FormKebutuhanRencana);

        PanelAccor.add(TabRawat1, java.awt.BorderLayout.CENTER);

        internalFrame3.add(PanelAccor, java.awt.BorderLayout.EAST);

        TabRawat.addTab("Data Pengkajian", internalFrame3);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            isRawat();
        } else {
            Valid.pindah(evt, TCari, BtnPetugas);
        }
}//GEN-LAST:event_TNoRwKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if (TNoRM.getText().trim().equals("")) {
            Valid.textKosong(TNoRw, "Nama Pasien");
        } else if (KeluhanUtama.getText().trim().equals("")) {
            Valid.textKosong(KeluhanUtama, "Keluhan Utama");
        } else if (RPD.getText().trim().equals("")) {
            Valid.textKosong(RPD, "Riwayat Penyakit Dahulu");
        } else if (RPO.getText().trim().equals("")) {
            Valid.textKosong(RPO, "Riwayat Penggunaan Obat");
        } else if (NmPetugas.getText().trim().equals("")) {
            Valid.textKosong(BtnPetugas, "Petugas");
        } else {
            if (akses.getkode().equals("Admin Utama")) {
                simpan();
            } else {
                if (TanggalRegistrasi.getText().equals("")) {
                    TanggalRegistrasi.setText(Sequel.cariIsi("select concat(reg_periksa.tgl_registrasi,' ',reg_periksa.jam_reg) from reg_periksa where reg_periksa.no_rawat=?", TNoRw.getText()));
                }
                if (Sequel.cekTanggalRegistrasi(TanggalRegistrasi.getText(), Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19)) == true) {
                    simpan();
                }
            }
        }

}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnSimpanActionPerformed(null);
        } else {
            Valid.pindah(evt, Rencana, BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        emptTeks();
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            emptTeks();
        } else {
            Valid.pindah(evt, BtnSimpan, BtnHapus);
        }
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if (tbObat.getSelectedRow() > -1) {
            if (akses.getkode().equals("Admin Utama")) {
                hapus();
            } else {
                if (KdPetugas.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(), 127).toString())) {
                    if (Sequel.cekTanggal48jam(tbObat.getValueAt(tbObat.getSelectedRow(), 8).toString(), Sequel.ambiltanggalsekarang()) == true) {
                        hapus();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Hanya bisa dihapus oleh petugas yang bersangkutan..!!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Silahkan anda pilih data terlebih dahulu..!!");
        }

}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnHapusActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnBatal, BtnEdit);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        if (TNoRM.getText().trim().equals("")) {
            Valid.textKosong(TNoRw, "Nama Pasien");
        } else if (KeluhanUtama.getText().trim().equals("")) {
            Valid.textKosong(KeluhanUtama, "Keluhan Utama");
        } else if (RPD.getText().trim().equals("")) {
            Valid.textKosong(RPD, "Riwayat Penyakit Dahulu");
        } else if (RPO.getText().trim().equals("")) {
            Valid.textKosong(RPO, "Riwayat Penggunaan Obat");
        } else if (NmPetugas.getText().trim().equals("")) {
            Valid.textKosong(BtnPetugas, "Petugas");
        } else {
            if (tbObat.getSelectedRow() > -1) {
                if (akses.getkode().equals("Admin Utama")) {
                    ganti();
                } else {
                    if (KdPetugas.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(), 127).toString())) {
                        if (Sequel.cekTanggal48jam(tbObat.getValueAt(tbObat.getSelectedRow(), 8).toString(), Sequel.ambiltanggalsekarang()) == true) {
                            if (TanggalRegistrasi.getText().equals("")) {
                                TanggalRegistrasi.setText(Sequel.cariIsi("select concat(reg_periksa.tgl_registrasi,' ',reg_periksa.jam_reg) from reg_periksa where reg_periksa.no_rawat=?", TNoRw.getText()));
                            }
                            if (Sequel.cekTanggalRegistrasi(TanggalRegistrasi.getText(), Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19)) == true) {
                                ganti();
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Hanya bisa diganti oleh petugas yang bersangkutan..!!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Silahkan anda pilih data terlebih dahulu..!!");
            }
        }
}//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnEditActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnHapus, BtnPrint);
        }
}//GEN-LAST:event_BtnEditKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnKeluarActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnEdit, TCari);
        }
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (tabMode.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            BtnBatal.requestFocus();
        } else if (tabMode.getRowCount() != 0) {
            try {
                if (TCari.getText().equals("")) {
                    ps = koneksi.prepareStatement(
                            "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir,pasien.agama,bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,penilaian_awal_keperawatan_ralan_bayi.tanggal,"
                            + "penilaian_awal_keperawatan_ralan_bayi.informasi,penilaian_awal_keperawatan_ralan_bayi.td,penilaian_awal_keperawatan_ralan_bayi.nadi,penilaian_awal_keperawatan_ralan_bayi.rr,penilaian_awal_keperawatan_ralan_bayi.suhu,penilaian_awal_keperawatan_ralan_bayi.gcs,"
                            + "penilaian_awal_keperawatan_ralan_bayi.bb,penilaian_awal_keperawatan_ralan_bayi.tb,penilaian_awal_keperawatan_ralan_bayi.lp,penilaian_awal_keperawatan_ralan_bayi.lk,penilaian_awal_keperawatan_ralan_bayi.ld,penilaian_awal_keperawatan_ralan_bayi.keluhan_utama,"
                            + "penilaian_awal_keperawatan_ralan_bayi.rpd,penilaian_awal_keperawatan_ralan_bayi.rpk,penilaian_awal_keperawatan_ralan_bayi.rpo,penilaian_awal_keperawatan_ralan_bayi.alergi,penilaian_awal_keperawatan_ralan_bayi.anakke,penilaian_awal_keperawatan_ralan_bayi.darisaudara,"
                            + "penilaian_awal_keperawatan_ralan_bayi.caralahir,penilaian_awal_keperawatan_ralan_bayi.ket_caralahir,penilaian_awal_keperawatan_ralan_bayi.umurkelahiran,penilaian_awal_keperawatan_ralan_bayi.kelainanbawaan,penilaian_awal_keperawatan_ralan_bayi.ket_kelainan_bawaan,"
                            + "penilaian_awal_keperawatan_ralan_bayi.usiatengkurap,penilaian_awal_keperawatan_ralan_bayi.usiaduduk,penilaian_awal_keperawatan_ralan_bayi.usiaberdiri,penilaian_awal_keperawatan_ralan_bayi.usiagigipertama,penilaian_awal_keperawatan_ralan_bayi.usiaberjalan,"
                            + "penilaian_awal_keperawatan_ralan_bayi.usiabicara,penilaian_awal_keperawatan_ralan_bayi.usiamembaca,penilaian_awal_keperawatan_ralan_bayi.usiamenulis,penilaian_awal_keperawatan_ralan_bayi.gangguanemosi,penilaian_awal_keperawatan_ralan_bayi.alat_bantu,"
                            + "penilaian_awal_keperawatan_ralan_bayi.ket_bantu,penilaian_awal_keperawatan_ralan_bayi.prothesa,penilaian_awal_keperawatan_ralan_bayi.ket_pro,penilaian_awal_keperawatan_ralan_bayi.adl,penilaian_awal_keperawatan_ralan_bayi.status_psiko,"
                            + "penilaian_awal_keperawatan_ralan_bayi.ket_psiko,penilaian_awal_keperawatan_ralan_bayi.hub_keluarga,penilaian_awal_keperawatan_ralan_bayi.pengasuh,penilaian_awal_keperawatan_ralan_bayi.ket_pengasuh,penilaian_awal_keperawatan_ralan_bayi.ekonomi,"
                            + "penilaian_awal_keperawatan_ralan_bayi.budaya,penilaian_awal_keperawatan_ralan_bayi.ket_budaya,penilaian_awal_keperawatan_ralan_bayi.edukasi,penilaian_awal_keperawatan_ralan_bayi.ket_edukasi,penilaian_awal_keperawatan_ralan_bayi.berjalan_a,"
                            + "penilaian_awal_keperawatan_ralan_bayi.berjalan_b,penilaian_awal_keperawatan_ralan_bayi.berjalan_c,penilaian_awal_keperawatan_ralan_bayi.hasil,penilaian_awal_keperawatan_ralan_bayi.lapor,penilaian_awal_keperawatan_ralan_bayi.ket_lapor,"
                            + "penilaian_awal_keperawatan_ralan_bayi.sg1,penilaian_awal_keperawatan_ralan_bayi.nilai1,penilaian_awal_keperawatan_ralan_bayi.sg2,penilaian_awal_keperawatan_ralan_bayi.nilai2,penilaian_awal_keperawatan_ralan_bayi.sg3,penilaian_awal_keperawatan_ralan_bayi.nilai3,"
                            + "penilaian_awal_keperawatan_ralan_bayi.sg4,penilaian_awal_keperawatan_ralan_bayi.nilai4,penilaian_awal_keperawatan_ralan_bayi.total_hasil,penilaian_awal_keperawatan_ralan_bayi.wajah,penilaian_awal_keperawatan_ralan_bayi.nilaiwajah,penilaian_awal_keperawatan_ralan_bayi.kaki,"
                            + "penilaian_awal_keperawatan_ralan_bayi.nilaikaki,penilaian_awal_keperawatan_ralan_bayi.aktifitas,penilaian_awal_keperawatan_ralan_bayi.nilaiaktifitas,penilaian_awal_keperawatan_ralan_bayi.menangis,penilaian_awal_keperawatan_ralan_bayi.nilaimenangis,"
                            + "penilaian_awal_keperawatan_ralan_bayi.bersuara,penilaian_awal_keperawatan_ralan_bayi.nilaibersuara,penilaian_awal_keperawatan_ralan_bayi.hasilnyeri,penilaian_awal_keperawatan_ralan_bayi.nyeri,penilaian_awal_keperawatan_ralan_bayi.lokasi,"
                            + "penilaian_awal_keperawatan_ralan_bayi.durasi,penilaian_awal_keperawatan_ralan_bayi.frekuensi,penilaian_awal_keperawatan_ralan_bayi.nyeri_hilang,penilaian_awal_keperawatan_ralan_bayi.ket_nyeri,penilaian_awal_keperawatan_ralan_bayi.pada_dokter,"
                            + "penilaian_awal_keperawatan_ralan_bayi.ket_dokter,penilaian_awal_keperawatan_ralan_bayi.rencana,penilaian_awal_keperawatan_ralan_bayi.nip,petugas.nama "
                            + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                            + "inner join penilaian_awal_keperawatan_ralan_bayi on reg_periksa.no_rawat=penilaian_awal_keperawatan_ralan_bayi.no_rawat "
                            + "inner join petugas on penilaian_awal_keperawatan_ralan_bayi.nip=petugas.nip "
                            + "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "
                            + "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik where "
                            + "penilaian_awal_keperawatan_ralan_bayi.tanggal between ? and ? order by penilaian_awal_keperawatan_ralan_bayi.tanggal");
                } else {
                    ps = koneksi.prepareStatement(
                            "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir,pasien.agama,bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,penilaian_awal_keperawatan_ralan_bayi.tanggal,"
                            + "penilaian_awal_keperawatan_ralan_bayi.informasi,penilaian_awal_keperawatan_ralan_bayi.td,penilaian_awal_keperawatan_ralan_bayi.nadi,penilaian_awal_keperawatan_ralan_bayi.rr,penilaian_awal_keperawatan_ralan_bayi.suhu,penilaian_awal_keperawatan_ralan_bayi.gcs,"
                            + "penilaian_awal_keperawatan_ralan_bayi.bb,penilaian_awal_keperawatan_ralan_bayi.tb,penilaian_awal_keperawatan_ralan_bayi.lp,penilaian_awal_keperawatan_ralan_bayi.lk,penilaian_awal_keperawatan_ralan_bayi.ld,penilaian_awal_keperawatan_ralan_bayi.keluhan_utama,"
                            + "penilaian_awal_keperawatan_ralan_bayi.rpd,penilaian_awal_keperawatan_ralan_bayi.rpk,penilaian_awal_keperawatan_ralan_bayi.rpo,penilaian_awal_keperawatan_ralan_bayi.alergi,penilaian_awal_keperawatan_ralan_bayi.anakke,penilaian_awal_keperawatan_ralan_bayi.darisaudara,"
                            + "penilaian_awal_keperawatan_ralan_bayi.caralahir,penilaian_awal_keperawatan_ralan_bayi.ket_caralahir,penilaian_awal_keperawatan_ralan_bayi.umurkelahiran,penilaian_awal_keperawatan_ralan_bayi.kelainanbawaan,penilaian_awal_keperawatan_ralan_bayi.ket_kelainan_bawaan,"
                            + "penilaian_awal_keperawatan_ralan_bayi.usiatengkurap,penilaian_awal_keperawatan_ralan_bayi.usiaduduk,penilaian_awal_keperawatan_ralan_bayi.usiaberdiri,penilaian_awal_keperawatan_ralan_bayi.usiagigipertama,penilaian_awal_keperawatan_ralan_bayi.usiaberjalan,"
                            + "penilaian_awal_keperawatan_ralan_bayi.usiabicara,penilaian_awal_keperawatan_ralan_bayi.usiamembaca,penilaian_awal_keperawatan_ralan_bayi.usiamenulis,penilaian_awal_keperawatan_ralan_bayi.gangguanemosi,penilaian_awal_keperawatan_ralan_bayi.alat_bantu,"
                            + "penilaian_awal_keperawatan_ralan_bayi.ket_bantu,penilaian_awal_keperawatan_ralan_bayi.prothesa,penilaian_awal_keperawatan_ralan_bayi.ket_pro,penilaian_awal_keperawatan_ralan_bayi.adl,penilaian_awal_keperawatan_ralan_bayi.status_psiko,"
                            + "penilaian_awal_keperawatan_ralan_bayi.ket_psiko,penilaian_awal_keperawatan_ralan_bayi.hub_keluarga,penilaian_awal_keperawatan_ralan_bayi.pengasuh,penilaian_awal_keperawatan_ralan_bayi.ket_pengasuh,penilaian_awal_keperawatan_ralan_bayi.ekonomi,"
                            + "penilaian_awal_keperawatan_ralan_bayi.budaya,penilaian_awal_keperawatan_ralan_bayi.ket_budaya,penilaian_awal_keperawatan_ralan_bayi.edukasi,penilaian_awal_keperawatan_ralan_bayi.ket_edukasi,penilaian_awal_keperawatan_ralan_bayi.berjalan_a,"
                            + "penilaian_awal_keperawatan_ralan_bayi.berjalan_b,penilaian_awal_keperawatan_ralan_bayi.berjalan_c,penilaian_awal_keperawatan_ralan_bayi.hasil,penilaian_awal_keperawatan_ralan_bayi.lapor,penilaian_awal_keperawatan_ralan_bayi.ket_lapor,"
                            + "penilaian_awal_keperawatan_ralan_bayi.sg1,penilaian_awal_keperawatan_ralan_bayi.nilai1,penilaian_awal_keperawatan_ralan_bayi.sg2,penilaian_awal_keperawatan_ralan_bayi.nilai2,penilaian_awal_keperawatan_ralan_bayi.sg3,penilaian_awal_keperawatan_ralan_bayi.nilai3,"
                            + "penilaian_awal_keperawatan_ralan_bayi.sg4,penilaian_awal_keperawatan_ralan_bayi.nilai4,penilaian_awal_keperawatan_ralan_bayi.total_hasil,penilaian_awal_keperawatan_ralan_bayi.wajah,penilaian_awal_keperawatan_ralan_bayi.nilaiwajah,penilaian_awal_keperawatan_ralan_bayi.kaki,"
                            + "penilaian_awal_keperawatan_ralan_bayi.nilaikaki,penilaian_awal_keperawatan_ralan_bayi.aktifitas,penilaian_awal_keperawatan_ralan_bayi.nilaiaktifitas,penilaian_awal_keperawatan_ralan_bayi.menangis,penilaian_awal_keperawatan_ralan_bayi.nilaimenangis,"
                            + "penilaian_awal_keperawatan_ralan_bayi.bersuara,penilaian_awal_keperawatan_ralan_bayi.nilaibersuara,penilaian_awal_keperawatan_ralan_bayi.hasilnyeri,penilaian_awal_keperawatan_ralan_bayi.nyeri,penilaian_awal_keperawatan_ralan_bayi.lokasi,"
                            + "penilaian_awal_keperawatan_ralan_bayi.durasi,penilaian_awal_keperawatan_ralan_bayi.frekuensi,penilaian_awal_keperawatan_ralan_bayi.nyeri_hilang,penilaian_awal_keperawatan_ralan_bayi.ket_nyeri,penilaian_awal_keperawatan_ralan_bayi.pada_dokter,"
                            + "penilaian_awal_keperawatan_ralan_bayi.ket_dokter,penilaian_awal_keperawatan_ralan_bayi.rencana,penilaian_awal_keperawatan_ralan_bayi.nip,petugas.nama "
                            + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                            + "inner join penilaian_awal_keperawatan_ralan_bayi on reg_periksa.no_rawat=penilaian_awal_keperawatan_ralan_bayi.no_rawat "
                            + "inner join petugas on penilaian_awal_keperawatan_ralan_bayi.nip=petugas.nip "
                            + "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "
                            + "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik where "
                            + "penilaian_awal_keperawatan_ralan_bayi.tanggal between ? and ? and reg_periksa.no_rawat like ? or "
                            + "penilaian_awal_keperawatan_ralan_bayi.tanggal between ? and ? and pasien.no_rkm_medis like ? or "
                            + "penilaian_awal_keperawatan_ralan_bayi.tanggal between ? and ? and pasien.nm_pasien like ? or "
                            + "penilaian_awal_keperawatan_ralan_bayi.tanggal between ? and ? and penilaian_awal_keperawatan_ralan_bayi.nip like ? or "
                            + "penilaian_awal_keperawatan_ralan_bayi.tanggal between ? and ? and petugas.nama like ? order by penilaian_awal_keperawatan_ralan_bayi.tanggal");
                }

                try {
                    if (TCari.getText().equals("")) {
                        ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00");
                        ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59");
                    } else {
                        ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00");
                        ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59");
                        ps.setString(3, "%" + TCari.getText() + "%");
                        ps.setString(4, Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00");
                        ps.setString(5, Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59");
                        ps.setString(6, "%" + TCari.getText() + "%");
                        ps.setString(7, Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00");
                        ps.setString(8, Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59");
                        ps.setString(9, "%" + TCari.getText() + "%");
                        ps.setString(10, Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00");
                        ps.setString(11, Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59");
                        ps.setString(12, "%" + TCari.getText() + "%");
                        ps.setString(13, Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00");
                        ps.setString(14, Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59");
                        ps.setString(15, "%" + TCari.getText() + "%");
                    }
                    rs = ps.executeQuery();
                    StringBuilder htmlContent = new StringBuilder();
                    htmlContent.append(
                            "<tr class='isi'>").append(
                                    "<td valign='middle' bgcolor='#FFFAFA' align='center' width='13%'><b>PASIEN & PETUGAS</b></td>").append(
                                    "<td valign='middle' bgcolor='#FFFAFA' align='center' width='15%'><b>II. RIWAYAT KESEHATAN DAHULU</b></td>").append(
                                    "<td valign='middle' bgcolor='#FFFAFA' align='center' width='13%'><b>V. RIWAYAT TUMBUH KEMBANG ANAK</b></td>").append(
                                    "<td valign='middle' bgcolor='#FFFAFA' align='center' width='15%'><b>VII. RIWAYAT PSIKO-SOSIAL, SPIRITUAL DAN BUDAYA</b></td>").append(
                                    "<td valign='middle' bgcolor='#FFFAFA' align='center' width='15%'><b>VIII. PENGKAJIAN RESIKO JATUH</b></td>").append(
                                    "<td valign='middle' bgcolor='#FFFAFA' align='center' width='13%'><b>IX. SKRINING GIZI (Strong kid)</b></td>").append(
                                    "<td valign='middle' bgcolor='#FFFAFA' align='center' width='15%'><b>X. PENGKAJIAN TINGKAT NYERI</b></td>").append(
                                    "</tr>"
                            );
                    while (rs.next()) {
                        masalahkeperawatan = "";
                        ps2 = koneksi.prepareStatement(
                                "select master_masalah_keperawatan_anak.kode_masalah,master_masalah_keperawatan_anak.nama_masalah from master_masalah_keperawatan_anak "
                                + "inner join penilaian_awal_keperawatan_ralan_bayi_masalah on penilaian_awal_keperawatan_ralan_bayi_masalah.kode_masalah=master_masalah_keperawatan_anak.kode_masalah "
                                + "where penilaian_awal_keperawatan_ralan_bayi_masalah.no_rawat=? order by kode_masalah");
                        try {
                            ps2.setString(1, rs.getString("no_rawat"));
                            rs2 = ps2.executeQuery();
                            while (rs2.next()) {
                                masalahkeperawatan = rs2.getString("nama_masalah") + ", " + masalahkeperawatan;
                            }
                        } catch (Exception e) {
                            System.out.println("Notif : " + e);
                        } finally {
                            if (rs2 != null) {
                                rs2.close();
                            }
                            if (ps2 != null) {
                                ps2.close();
                            }
                        }
                        htmlContent.append(
                                "<tr class='isi'>").append(
                                        "<td valign='top' cellpadding='0' cellspacing='0'>").append(
                                        "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='32%' valign='top' align='justify'>No.Rawat</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>").append(rs.getString("no_rawat")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='32%' valign='top' align='justify'>No.R.M.</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>").append(rs.getString("no_rkm_medis")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='32%' valign='top' align='justify'>Nama Pasien</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>").append(rs.getString("nm_pasien")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='32%' valign='top' align='justify'>J.K.</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>").append(rs.getString("jk")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='32%' valign='top' align='justify'>Agama</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>").append(rs.getString("agama")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='32%' valign='top' align='justify'>Bahasa</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>").append(rs.getString("nama_bahasa")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='32%' valign='top' align='justify'>Tgl.Lahir</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>").append(rs.getString("nama_cacat")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='32%' valign='top' align='justify'>Cacat Fisik</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>").append(rs.getString("tgl_lahir")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='32%' valign='top' align='justify'>Petugas</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>").append(rs.getString("nip")).append(" ").append(rs.getString("nama")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='32%' valign='top' align='justify'>Tgl.Asuhan</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>").append(rs.getString("tanggal")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='32%' valign='top' align='justify'>Informasi</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>").append(rs.getString("informasi")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'><td valign='middle' bgcolor='#FFFAFA' align='center' colspan='3' width='100%'><b>I. KEADAAN UMUM</b></td></tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='34%' valign='top' align='justify'>TD</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>").append(rs.getString("td")).append("mmHg</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='34%' valign='top' align='justify'>Nadi</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>").append(rs.getString("nadi")).append("x/menit</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='34%' valign='top' align='justify'>RR</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>").append(rs.getString("rr")).append("x/menit</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='34%' valign='top' align='justify'>Suhu</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>").append(rs.getString("suhu")).append("°C</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='34%' valign='top' align='justify'>GCS(E,V,M)</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>").append(rs.getString("gcs")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='34%' valign='top' align='justify'>BB</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>").append(rs.getString("bb")).append("Kg</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='34%' valign='top' align='justify'>TB</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>").append(rs.getString("tb")).append("cm</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='34%' valign='top' align='justify'>LP</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>").append(rs.getString("lp")).append("cm</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='34%' valign='top' align='justify'>LK</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>").append(rs.getString("lk")).append("cm</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='34%' valign='top' align='justify'>LD</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>").append(rs.getString("ld")).append("cm</td>").append(
                                "</tr>").append(
                                        "</table>").append(
                                        "</td>").append(
                                        "<td valign='top' cellpadding='0' cellspacing='0'>").append(
                                        "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='32%' valign='top' align='justify'>Keluhan Utama</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>").append(rs.getString("keluhan_utama")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='32%' valign='top' align='justify'>RPD</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>").append(rs.getString("rpd")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='32%' valign='top' align='justify'>RPK</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>").append(rs.getString("rpk")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='32%' valign='top' align='justify'>RPO</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>").append(rs.getString("rpo")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='32%' valign='top' align='justify'>Alergi</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>").append(rs.getString("alergi")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'><td valign='middle' bgcolor='#FFFAFA' align='center' colspan='3' width='100%'><b>III. RIWAYAT TUMBUH KEMBANG DAN PERINATAL CARE</b></td></tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='40%' valign='top' align='justify'>Riwayat Kelahiran</td><td valign='top'>:&nbsp;</td><td width='59%' valign='top'> Anak ke ").append(rs.getString("anakke")).append(" dari ").append(rs.getString("darisaudara")).append(" saudara</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='40%' valign='top' align='justify'>Umur Kelahiran</td><td valign='top'>:&nbsp;</td><td width='59%' valign='top'>").append(rs.getString("umurkelahiran")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='40%' valign='top' align='justify'>Cara kelahiran</td><td valign='top'>:&nbsp;</td><td width='59%' valign='top'>").append(rs.getString("caralahir")).append("<br>").append(rs.getString("ket_caralahir")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='40%' valign='top' align='justify'>Kelainan Bawaan</td><td valign='top'>:&nbsp;</td><td width='59%' valign='top'>").append(rs.getString("kelainanbawaan")).append("<br>").append(rs.getString("ket_kelainan_bawaan")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td valign='top' cellpadding='0' cellspacing='0' colspan='3'>").append(
                                        "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>").append(
                                        "<tr class='isi2'>").append(
                                        "<td valign='middle' bgcolor='#FFFAFA' align='center' colspan='7'><b>IV. RIWAYAT IMUNISASI</b></td>").append(
                                        "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='70%' valign='top'>Nama Imunisasi</td><td width='5%' valign='top'>Ke 1</td><td width='5%' valign='top'>Ke 2</td><td width='5%' valign='top'>Ke 3</td><td width='5%' valign='top'>Ke 4</td><td width='5%' valign='top'>Ke 5</td><td width='5%' valign='top'>Ke 6</td>");
                        ps2 = koneksi.prepareStatement(
                                "select master_imunisasi.kode_imunisasi,master_imunisasi.nama_imunisasi from master_imunisasi inner join riwayat_imunisasi on riwayat_imunisasi.kode_imunisasi=master_imunisasi.kode_imunisasi "
                                + "where riwayat_imunisasi.no_rkm_medis=? group by master_imunisasi.kode_imunisasi order by master_imunisasi.kode_imunisasi  ");
                        try {
                            ps2.setString(1, rs.getString("no_rkm_medis"));
                            rs2 = ps2.executeQuery();
                            while (rs2.next()) {
                                htmlke1 = "&nbsp;";
                                htmlke2 = "&nbsp;";
                                htmlke3 = "&nbsp;";
                                htmlke4 = "&nbsp;";
                                htmlke5 = "&nbsp;";
                                htmlke6 = "&nbsp;";
                                ps3 = koneksi.prepareStatement("select * from riwayat_imunisasi where no_rkm_medis=? and kode_imunisasi=?");
                                try {
                                    ps3.setString(1, rs.getString("no_rkm_medis"));
                                    ps3.setString(2, rs2.getString(1));
                                    rs3 = ps3.executeQuery();
                                    while (rs3.next()) {
                                        if (rs3.getInt("no_imunisasi") == 1) {
                                            htmlke1 = "V";
                                        }
                                        if (rs3.getInt("no_imunisasi") == 2) {
                                            htmlke2 = "V";
                                        }
                                        if (rs3.getInt("no_imunisasi") == 3) {
                                            htmlke3 = "V";
                                        }
                                        if (rs3.getInt("no_imunisasi") == 4) {
                                            htmlke4 = "V";
                                        }
                                        if (rs3.getInt("no_imunisasi") == 5) {
                                            htmlke5 = "V";
                                        }
                                        if (rs3.getInt("no_imunisasi") == 6) {
                                            htmlke6 = "V";
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("Notif : " + e);
                                } finally {
                                    if (rs3 != null) {
                                        rs3.close();
                                    }
                                    if (ps3 != null) {
                                        ps3.close();
                                    }
                                }

                                htmlContent.append("<tr class='isi2'>").append(
                                        "<td>").append(rs2.getString(2)).append("</td><td>").append(htmlke1).append("</td><td>").append(htmlke2).append("</td><td>").append(htmlke3).append("</td><td>").append(htmlke4).append("</td><td>").append(htmlke5).append("</td><td>").append(htmlke6).append("</td>").append(
                                        "</tr>");
                            }
                        } catch (Exception e) {
                            System.out.println("Notif : " + e);
                        } finally {
                            if (rs2 != null) {
                                rs2.close();
                            }
                            if (ps2 != null) {
                                ps2.close();
                            }
                        }
                        htmlContent.append("</table>").append(
                                "</td>").append(
                                        "</tr>").append(
                                        "</table>").append(
                                        "</td>").append(
                                        "<td valign='top' cellpadding='0' cellspacing='0'>").append(
                                        "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='80%' valign='top' align='justify'>a. Tengkurap, usia</td><td valign='top'>:&nbsp;</td><td width='19%' valign='top'> ").append(rs.getString("usiatengkurap")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='80%' valign='top' align='justify'>b. Duduk, usia</td><td valign='top'>:&nbsp;</td><td width='19%' valign='top'> ").append(rs.getString("usiaduduk")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='80%' valign='top' align='justify'>c. Berdiri, usia</td><td valign='top'>:&nbsp;</td><td width='19%' valign='top'> ").append(rs.getString("usiaberdiri")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='80%' valign='top' align='justify'>d. Gigi pertama, usia</td><td valign='top'>:&nbsp;</td><td width='19%' valign='top'> ").append(rs.getString("usiagigipertama")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='80%' valign='top' align='justify'>e. Berjalan, usia</td><td valign='top'>:&nbsp;</td><td width='19%' valign='top'> ").append(rs.getString("usiaberjalan")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='80%' valign='top' align='justify'>f. Bicara Usia, usia</td><td valign='top'>:&nbsp;</td><td width='19%' valign='top'> ").append(rs.getString("usiabicara")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='80%' valign='top' align='justify'>g. Mulai bisa membaca, usia</td><td valign='top'>:&nbsp;</td><td width='19%' valign='top'> ").append(rs.getString("usiamembaca")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='80%' valign='top' align='justify'>h. Mulai bisa menulis, usia</td><td valign='top'>:&nbsp;</td><td width='19%' valign='top'> ").append(rs.getString("usiamenulis")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='100%' valign='top' colspan='3' align='justify'>Gangguan perkembangan mental / emosi : ").append(rs.getString("gangguanemosi")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'><td valign='middle' bgcolor='#FFFAFA' align='center' colspan='3'><b>VI. FUNGSIONAL</b></td></tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='30%' valign='top' align='justify'>Alat Bantu</td><td valign='top'>:&nbsp;</td><td width='69%' valign='top'>").append(rs.getString("alat_bantu")).append("<br>").append(rs.getString("ket_bantu")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='30%' valign='top' align='justify'>Prothesa</td><td valign='top'>:&nbsp;</td><td width='69%' valign='top'>").append(rs.getString("prothesa")).append("<br>").append(rs.getString("ket_pro")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='30%' valign='top' align='justify'>Cacat Fisik</td><td valign='top'>:&nbsp;</td><td width='69%' valign='top'>").append(rs.getString("nama_cacat")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='30%' valign='top' align='justify'>ADL</td><td valign='top'>:&nbsp;</td><td width='69%' valign='top'>").append(rs.getString("adl")).append("</td>").append(
                                "</tr>").append(
                                        "</table>").append(
                                        "</td>").append(
                                        "<td valign='top' cellpadding='0' cellspacing='0'>").append(
                                        "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='50%' valign='top' align='justify'>Status Psikologis</td><td valign='top'>:&nbsp;</td><td width='49%' valign='top'>").append(rs.getString("status_psiko")).append("<br>").append(rs.getString("ket_psiko")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='50%' valign='top' align='justify'>Bahasa yang digunakan sehari-hari</td><td valign='top'>:&nbsp;</td><td width='49%' valign='top'>").append(rs.getString("nama_bahasa")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='50%' valign='top' colspan='3' align='justify'>Status Sosial dan ekonomi :</td>").append(
                                        "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='50%' valign='top' align='justify'>a. Hubungan dengan anggota keluarga</td><td valign='top'>:&nbsp;</td><td width='49%' valign='top'>").append(rs.getString("hub_keluarga")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='50%' valign='top' align='justify'>b. Pengasuh</td><td valign='top'>:&nbsp;</td><td width='49%' valign='top'>").append(rs.getString("pengasuh")).append(" ").append(rs.getString("ket_pengasuh")).append(" </td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='50%' valign='top' align='justify'>c. Ekonomi (Ortu)</td><td valign='top'>:&nbsp;</td><td width='49%' valign='top'>").append(rs.getString("ekonomi")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='50%' valign='top' align='justify'>Kepercayaan / Budaya / Nilai-nilai khusus yang perlu diperhatikan</td><td valign='top'>:&nbsp;</td><td width='49%' valign='top'>").append(rs.getString("budaya")).append("<br>").append(rs.getString("ket_budaya")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='50%' valign='top' align='justify'>Agama</td><td valign='top'>:&nbsp;</td><td width='49%' valign='top'>").append(rs.getString("agama")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='50%' valign='top' align='justify'>Edukasi diberikan kepada</td><td valign='top'>:&nbsp;</td><td width='49%' valign='top'>").append(rs.getString("edukasi")).append("<br>").append(rs.getString("ket_edukasi")).append("</td>").append(
                                "</tr>").append(
                                        "</table>").append(
                                        "</td>").append(
                                        "<td valign='top' cellpadding='0' cellspacing='0'>").append(
                                        "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='100%' valign='top' colspan='3' align='justify'>a. Cara berjalan pasien (salah satu atau lebih) :</td>").append(
                                        "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='50%' valign='top' align='justify'>1. Tidak seimbang/ Sempoyongan/ Limbung</td><td valign='top'>:&nbsp;</td><td width='49%' valign='top'>").append(rs.getString("berjalan_a")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='50%' valign='top' align='justify'>2. Jalan dengan menggunakan alat bantu (kruk, tripot, kursi roda, orang lain)</td><td valign='top'>:&nbsp;</td><td width='49%' valign='top'>").append(rs.getString("berjalan_b")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='50%' valign='top' align='justify'>b. Duduk di kursi tanpa menggunakan tangan sebagai penopang (tampak memegang kursi atau meja/ benda lain)</td><td valign='top'>:&nbsp;</td><td width='49%' valign='top'>").append(rs.getString("berjalan_c")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='50%' valign='top' align='justify'>Hasil</td><td valign='top'>:&nbsp;</td><td width='49%' valign='top'>").append(rs.getString("hasil")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='50%' valign='top' align='justify'>Dilaporkan kepada dokter</td><td valign='top'>:&nbsp;</td><td width='49%' valign='top'>").append(rs.getString("lapor")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='50%' valign='top' align='justify'>Jam dilaporkan</td><td valign='top'>:&nbsp;</td><td width='49%' valign='top'>").append(rs.getString("ket_lapor")).append("</td>").append(
                                "</tr>").append(
                                        "</table>").append(
                                        "</td>").append(
                                        "<td valign='top' cellpadding='0' cellspacing='0'>").append(
                                        "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='85%' valign='top' align='justify'>1. Apakah pasien tampak kurus</td><td valign='top'>:&nbsp;</td><td width='9%' valign='top'>").append(rs.getString("sg1")).append("</td><td width='5%' valign='top' align='right'>").append(rs.getString("nilai1")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='85%' valign='top' align='justify'>2. Apakah terdapat penurunan berat badan selama satu bulan terakhir? (berdasarkan penilaian objektif data berat badan bila ada atau untuk bayi < 1 tahun ; berat badan tidak naik selama 3 bulan terakhir)</td><td valign='top'>:&nbsp;</td><td width='9%' valign='top'>").append(rs.getString("sg2")).append("</td><td width='5%' valign='top' align='right'>").append(rs.getString("nilai2")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='85%' valign='top' align='justify'>3. Apakah terdapat salah satu dari kondisi tersebut? Diare > 5 kali/hari dan/muntah > 3 kali/hari salam seminggu terakhir;Asupan makanan berkurang selama 1 minggu terakhir</td><td valign='top'>:&nbsp;</td><td width='9%' valign='top'>").append(rs.getString("sg3")).append("</td><td width='5%' valign='top' align='right'>").append(rs.getString("nilai3")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='85%' valign='top' align='justify'>4. Apakah terdapat penyakit atau keadaan yang menyebabkan pasien beresiko mengalami malnutrisi?</td><td valign='top'>:&nbsp;</td><td width='9%' valign='top'>").append(rs.getString("sg4")).append("</td><td width='5%' valign='top' align='right'>").append(rs.getString("nilai4")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='85%' valign='top' align='justify'>Total Skor</td><td valign='top'>:&nbsp;</td><td width='9%' valign='top'></td><td width='5%' valign='top' align='right'>").append(rs.getString("total_hasil")).append("</td>").append(
                                "</tr>").append(
                                        "</table>").append(
                                        "</td>").append(
                                        "<td valign='top' cellpadding='0' cellspacing='0'>").append(
                                        "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='25%' valign='top' align='justify'>Wajah</td><td valign='top'>:&nbsp;</td><td width='69%' valign='top'>").append(rs.getString("wajah")).append("</td><td width='5%' valign='top' align='right'>").append(rs.getString("nilaiwajah")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='25%' valign='top' align='justify'>Kaki</td><td valign='top'>:&nbsp;</td><td width='69%' valign='top'>").append(rs.getString("kaki")).append("</td><td width='5%' valign='top' align='right'>").append(rs.getString("nilaikaki")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='25%' valign='top' align='justify'>Aktifitas</td><td valign='top'>:&nbsp;</td><td width='69%' valign='top'>").append(rs.getString("aktifitas")).append("</td><td width='5%' valign='top' align='right'>").append(rs.getString("nilaiaktifitas")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='25%' valign='top' align='justify'>Menangis</td><td valign='top'>:&nbsp;</td><td width='69%' valign='top'>").append(rs.getString("menangis")).append("</td><td width='5%' valign='top' align='right'>").append(rs.getString("nilaimenangis")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='25%' valign='top' align='justify'>Bersuara</td><td valign='top'>:&nbsp;</td><td width='69%' valign='top'>").append(rs.getString("bersuara")).append("</td><td width='5%' valign='top' align='right'>").append(rs.getString("nilaibersuara")).append("</td>").append(
                                "</tr>").append(
                                        "<tr class='isi2'>").append(
                                        "<td width='25%' valign='top' align='justify'>Skala nyeri</td><td valign='top'>:&nbsp;</td><td width='69%' valign='top'></td><td width='5%' valign='top' align='right'>").append(rs.getString("hasilnyeri")).append("</td>").append(
                                "</tr>").append(
                                        "</table>").append(
                                        "<br><center><b>MASALAH & RENCANA KEPERAWATAN</b></center><br>").append(
                                        "Masalah Keperawatan : ").append(masalahkeperawatan).append("<br><br>").append(
                                "Rencana Keperawatan : ").append(rs.getString("rencana")).append(
                                "</td>").append(
                                        "</tr>"
                                );
                    }
                    LoadHTML.setText(
                            "<html>"
                            + "<table width='1800px' border='0' align='center' cellpadding='1px' cellspacing='0' class='tbl_form'>"
                            + htmlContent.toString()
                            + "</table>"
                            + "</html>"
                    );
                    htmlContent = null;

                    File g = new File("file2.css");
                    BufferedWriter bg = new BufferedWriter(new FileWriter(g));
                    bg.write(
                            ".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"
                            + ".isi2 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#323232;}"
                            + ".isi3 td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"
                            + ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"
                            + ".isi5 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#AA0000;}"
                            + ".isi6 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#FF0000;}"
                            + ".isi7 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#C8C800;}"
                            + ".isi8 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#00AA00;}"
                            + ".isi9 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#969696;}"
                    );
                    bg.close();

                    File f = new File("DataPenilaianAwalKeperawatanRalanBayiAnak.html");
                    BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                    bw.write(LoadHTML.getText().replaceAll("<head>", "<head>"
                            + "<link href=\"file2.css\" rel=\"stylesheet\" type=\"text/css\" />"
                            + "<table width='1800px' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                            + "<tr class='isi2'>"
                            + "<td valign='top' align='center'>"
                            + "<font size='4' face='Tahoma'>" + akses.getnamars() + "</font><br>"
                            + akses.getalamatrs() + ", " + akses.getkabupatenrs() + ", " + akses.getpropinsirs() + "<br>"
                            + akses.getkontakrs() + ", E-mail : " + akses.getemailrs() + "<br><br>"
                            + "<font size='2' face='Tahoma'>DATA PENGKAJIAN AWAL KEPERAWATAN RAWAT JALAN BAYI/ANAK<br><br></font>"
                            + "</td>"
                            + "</tr>"
                            + "</table>")
                    );
                    bw.close();
                    Desktop.getDesktop().browse(f.toURI());
                } catch (Exception e) {
                    System.out.println("Notif : " + e);
                } finally {
                    if (rs != null) {
                        rs.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                }

            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            }
        }
        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnPrintActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnEdit, BtnKeluar);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            BtnCariActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            BtnCari.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            BtnKeluar.requestFocus();
        }
}//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        runBackground(() -> tampil());
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnCariActionPerformed(null);
        } else {
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        runBackground(() -> tampil());
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            TCari.setText("");
            runBackground(() -> tampil());
        } else {
            Valid.pindah(evt, BtnCari, TPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if (tabMode.getRowCount() != 0) {
            try {
                ChkAccor.setSelected(true);
                isMenu();
                getMasalah();
                getImunisasi();
                getEdukasi();
            } catch (java.lang.NullPointerException e) {
            }
            if ((evt.getClickCount() == 2) && (tbObat.getSelectedColumn() == 0)) {
                TabRawat.setSelectedIndex(0);
            }
        }
}//GEN-LAST:event_tbObatMouseClicked

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if (tabMode.getRowCount() != 0) {
            if ((evt.getKeyCode() == KeyEvent.VK_ENTER) || (evt.getKeyCode() == KeyEvent.VK_UP) || (evt.getKeyCode() == KeyEvent.VK_DOWN)) {
                try {
                    ChkAccor.setSelected(true);
                    isMenu();
                    getMasalah();
                    getImunisasi();
                    getEdukasi();
                } catch (java.lang.NullPointerException e) {
                }
            } else if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
                try {
                    getData();
                    TabRawat.setSelectedIndex(0);
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbObatKeyPressed

    private void BtnPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPetugasActionPerformed
        if (petugas == null || !petugas.isDisplayable()) {
            petugas = new DlgCariPetugas(null, false);
            petugas.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            petugas.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    if (petugas.getTable().getSelectedRow() != -1) {
                        KdPetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(), 0).toString());
                        NmPetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(), 1).toString());
                    }
                    BtnPetugas.requestFocus();
                    petugas = null;
                }
            });

            petugas.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
            petugas.setLocationRelativeTo(internalFrame1);
        }
        if (petugas == null) {
            return;
        }
        if (!petugas.isVisible()) {
            petugas.isCek();
            petugas.emptTeks();
        }

        if (petugas.isVisible()) {
            petugas.toFront();
            return;
        }
        petugas.setVisible(true);
    }//GEN-LAST:event_BtnPetugasActionPerformed

    private void BtnPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPetugasKeyPressed
        Valid.pindah(evt, TCari, Informasi);
    }//GEN-LAST:event_BtnPetugasKeyPressed

    private void NadiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NadiKeyPressed
        Valid.pindah(evt, TD, RR);
    }//GEN-LAST:event_NadiKeyPressed

    private void SuhuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SuhuKeyPressed
        Valid.pindah(evt, RR, GCS);
    }//GEN-LAST:event_SuhuKeyPressed

    private void TDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TDKeyPressed
        Valid.pindah(evt, Informasi, Nadi);
    }//GEN-LAST:event_TDKeyPressed

    private void RRKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RRKeyPressed
        Valid.pindah(evt, Nadi, Suhu);
    }//GEN-LAST:event_RRKeyPressed

    private void InformasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_InformasiKeyPressed
        Valid.pindah(evt, TglAsuhan, TD);
    }//GEN-LAST:event_InformasiKeyPressed

    private void TglAsuhanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TglAsuhanKeyPressed
        //Valid.pindah(evt,Rencana,Informasi);
    }//GEN-LAST:event_TglAsuhanKeyPressed

    private void GCSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GCSKeyPressed
        Valid.pindah(evt, Suhu, BB);
    }//GEN-LAST:event_GCSKeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        try {
            if (Valid.daysOld("./cache/masalahkeperawatanbayi.iyem") < 30) {
                runBackground(() -> tampilMasalah2());
            } else {
                runBackground(() -> tampilMasalah());
            }
        } catch (Exception e) {
        }

        if (koneksiDB.CARICEPAT().equals("aktif")) {
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if (TCari.getText().length() > 2) {
                        runBackground(() -> tampil());
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    if (TCari.getText().length() > 2) {
                        runBackground(() -> tampil());
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    if (TCari.getText().length() > 2) {
                        runBackground(() -> tampil());
                    }
                }
            });

            TCariMasalah.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if (TCariMasalah.getText().length() > 2) {
                        runBackground(() -> tampilMasalah2());
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    if (TCariMasalah.getText().length() > 2) {
                        runBackground(() -> tampilMasalah2());
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    if (TCariMasalah.getText().length() > 2) {
                        runBackground(() -> tampilMasalah2());
                    }
                }
            });
        }
    }//GEN-LAST:event_formWindowOpened

    private void ChkAccorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkAccorActionPerformed
        if (tbObat.getSelectedRow() != -1) {
            isMenu();
        } else {
            ChkAccor.setSelected(false);
            JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data yang mau ditampilkan...!!!!");
        }
    }//GEN-LAST:event_ChkAccorActionPerformed

    private void BtnPrint1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrint1ActionPerformed
        if (tbObat.getSelectedRow() > -1) {
            Map<String, Object> param = new HashMap<>();
            param.put("namars", akses.getnamars());
            param.put("alamatrs", akses.getalamatrs());
            param.put("kotars", akses.getkabupatenrs());
            param.put("propinsirs", akses.getpropinsirs());
            param.put("kontakrs", akses.getkontakrs());
            param.put("emailrs", akses.getemailrs());
            param.put("logo", Sequel.cariGambar("select setting.logo from setting"));
            param.put("nyeri", Sequel.cariGambar("select gambar.nyeri from gambar"));
            finger = Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?", tbObat.getValueAt(tbObat.getSelectedRow(), 91).toString());
            param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + tbObat.getValueAt(tbObat.getSelectedRow(), 92).toString() + "\nID " + (finger.equals("") ? tbObat.getValueAt(tbObat.getSelectedRow(), 91).toString() : finger) + "\n" + Valid.SetTgl3(tbObat.getValueAt(tbObat.getSelectedRow(), 8).toString()));
            try {
                masalahkeperawatan = "";
                ps2 = koneksi.prepareStatement(
                        "select master_masalah_keperawatan_anak.kode_masalah,master_masalah_keperawatan_anak.nama_masalah from master_masalah_keperawatan_anak "
                        + "inner join penilaian_awal_keperawatan_ralan_bayi_masalah on penilaian_awal_keperawatan_ralan_bayi_masalah.kode_masalah=master_masalah_keperawatan_anak.kode_masalah "
                        + "where penilaian_awal_keperawatan_ralan_bayi_masalah.no_rawat=? order by penilaian_awal_keperawatan_ralan_bayi_masalah.kode_masalah");
                try {
                    ps2.setString(1, tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
                    rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        masalahkeperawatan = rs2.getString("nama_masalah") + ", " + masalahkeperawatan;
                    }
                } catch (Exception e) {
                    System.out.println("Notif : " + e);
                } finally {
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (ps2 != null) {
                        ps2.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            }
            param.put("masalah", masalahkeperawatan);
            try {
                masalahkeperawatan = "";
                ps2 = koneksi.prepareStatement(
                        "select master_rencana_keperawatan_anak.kode_rencana,master_rencana_keperawatan_anak.rencana_keperawatan from master_rencana_keperawatan_anak "
                        + "inner join penilaian_awal_keperawatan_ralan_rencana_anak on penilaian_awal_keperawatan_ralan_rencana_anak.kode_rencana=master_rencana_keperawatan_anak.kode_rencana "
                        + "where penilaian_awal_keperawatan_ralan_rencana_anak.no_rawat=? order by penilaian_awal_keperawatan_ralan_rencana_anak.kode_rencana");
                try {
                    ps2.setString(1, tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
                    rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        masalahkeperawatan = rs2.getString("rencana_keperawatan") + ", " + masalahkeperawatan;
                    }
                } catch (Exception e) {
                    System.out.println("Notif : " + e);
                } finally {
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (ps2 != null) {
                        ps2.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            }
            param.put("rencana", masalahkeperawatan);
            try {
                masalahkeperawatan = "";
                ps2 = koneksi.prepareStatement(
                        "select master_imunisasi.kode_imunisasi,master_imunisasi.nama_imunisasi,riwayat_imunisasi.no_imunisasi from master_imunisasi inner join riwayat_imunisasi on riwayat_imunisasi.kode_imunisasi=master_imunisasi.kode_imunisasi "
                        + "where riwayat_imunisasi.no_rkm_medis=? group by riwayat_imunisasi.no_imunisasi order by riwayat_imunisasi.no_imunisasi desc ");
                try {
                    ps2.setString(1, TNoRM1.getText());
                    rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        masalahkeperawatan = rs2.getString("nama_imunisasi") + " Ke " + rs2.getString("no_imunisasi") + ", " + masalahkeperawatan;
                    }
                } catch (Exception e) {
                    System.out.println("Notif : " + e);
                } finally {
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (ps2 != null) {
                        ps2.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            }
            param.put("imunisasi", masalahkeperawatan);
            Valid.MyReportqry("rptCetakPenilaianAwalKeperawatanRalanAnak.jasper", "report", "::[ Laporan Pengkajian Awal Keperawatan Ralan Bayi/Anak ]::",
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir,pasien.agama,bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,penilaian_awal_keperawatan_ralan_bayi.tanggal,"
                    + "penilaian_awal_keperawatan_ralan_bayi.informasi,penilaian_awal_keperawatan_ralan_bayi.td,penilaian_awal_keperawatan_ralan_bayi.nadi,penilaian_awal_keperawatan_ralan_bayi.rr,penilaian_awal_keperawatan_ralan_bayi.suhu,penilaian_awal_keperawatan_ralan_bayi.gcs,"
                    + "penilaian_awal_keperawatan_ralan_bayi.bb,penilaian_awal_keperawatan_ralan_bayi.tb,penilaian_awal_keperawatan_ralan_bayi.lp,penilaian_awal_keperawatan_ralan_bayi.lk,penilaian_awal_keperawatan_ralan_bayi.ld,penilaian_awal_keperawatan_ralan_bayi.keluhan_utama,"
                    + "penilaian_awal_keperawatan_ralan_bayi.rpd,penilaian_awal_keperawatan_ralan_bayi.rpk,penilaian_awal_keperawatan_ralan_bayi.rpo,penilaian_awal_keperawatan_ralan_bayi.alergi,penilaian_awal_keperawatan_ralan_bayi.anakke,penilaian_awal_keperawatan_ralan_bayi.darisaudara,"
                    + "penilaian_awal_keperawatan_ralan_bayi.caralahir,penilaian_awal_keperawatan_ralan_bayi.ket_caralahir,penilaian_awal_keperawatan_ralan_bayi.umurkelahiran,penilaian_awal_keperawatan_ralan_bayi.kelainanbawaan,penilaian_awal_keperawatan_ralan_bayi.ket_kelainan_bawaan,"
                    + "penilaian_awal_keperawatan_ralan_bayi.usiatengkurap,penilaian_awal_keperawatan_ralan_bayi.usiaduduk,penilaian_awal_keperawatan_ralan_bayi.usiaberdiri,penilaian_awal_keperawatan_ralan_bayi.usiagigipertama,penilaian_awal_keperawatan_ralan_bayi.usiaberjalan,"
                    + "penilaian_awal_keperawatan_ralan_bayi.usiabicara,penilaian_awal_keperawatan_ralan_bayi.usiamembaca,penilaian_awal_keperawatan_ralan_bayi.usiamenulis,penilaian_awal_keperawatan_ralan_bayi.gangguanemosi,penilaian_awal_keperawatan_ralan_bayi.alat_bantu,"
                    + "penilaian_awal_keperawatan_ralan_bayi.ket_bantu,penilaian_awal_keperawatan_ralan_bayi.prothesa,penilaian_awal_keperawatan_ralan_bayi.ket_pro,penilaian_awal_keperawatan_ralan_bayi.adl,penilaian_awal_keperawatan_ralan_bayi.status_psiko,"
                    + "penilaian_awal_keperawatan_ralan_bayi.ket_psiko,penilaian_awal_keperawatan_ralan_bayi.hub_keluarga,penilaian_awal_keperawatan_ralan_bayi.pengasuh,penilaian_awal_keperawatan_ralan_bayi.ket_pengasuh,penilaian_awal_keperawatan_ralan_bayi.ekonomi,"
                    + "penilaian_awal_keperawatan_ralan_bayi.budaya,penilaian_awal_keperawatan_ralan_bayi.ket_budaya,penilaian_awal_keperawatan_ralan_bayi.edukasi,penilaian_awal_keperawatan_ralan_bayi.ket_edukasi,penilaian_awal_keperawatan_ralan_bayi.berjalan_a,"
                    + "penilaian_awal_keperawatan_ralan_bayi.berjalan_b,penilaian_awal_keperawatan_ralan_bayi.berjalan_c,penilaian_awal_keperawatan_ralan_bayi.hasil,penilaian_awal_keperawatan_ralan_bayi.lapor,penilaian_awal_keperawatan_ralan_bayi.ket_lapor,"
                    + "penilaian_awal_keperawatan_ralan_bayi.sg1,penilaian_awal_keperawatan_ralan_bayi.nilai1,penilaian_awal_keperawatan_ralan_bayi.sg2,penilaian_awal_keperawatan_ralan_bayi.nilai2,penilaian_awal_keperawatan_ralan_bayi.sg3,penilaian_awal_keperawatan_ralan_bayi.nilai3,"
                    + "penilaian_awal_keperawatan_ralan_bayi.sg4,penilaian_awal_keperawatan_ralan_bayi.nilai4,penilaian_awal_keperawatan_ralan_bayi.total_hasil,penilaian_awal_keperawatan_ralan_bayi.wajah,penilaian_awal_keperawatan_ralan_bayi.nilaiwajah,penilaian_awal_keperawatan_ralan_bayi.kaki,"
                    + "penilaian_awal_keperawatan_ralan_bayi.nilaikaki,penilaian_awal_keperawatan_ralan_bayi.aktifitas,penilaian_awal_keperawatan_ralan_bayi.nilaiaktifitas,penilaian_awal_keperawatan_ralan_bayi.menangis,penilaian_awal_keperawatan_ralan_bayi.nilaimenangis,"
                    + "penilaian_awal_keperawatan_ralan_bayi.bersuara,penilaian_awal_keperawatan_ralan_bayi.nilaibersuara,penilaian_awal_keperawatan_ralan_bayi.hasilnyeri,penilaian_awal_keperawatan_ralan_bayi.nyeri,penilaian_awal_keperawatan_ralan_bayi.lokasi,"
                    + "penilaian_awal_keperawatan_ralan_bayi.durasi,penilaian_awal_keperawatan_ralan_bayi.frekuensi,penilaian_awal_keperawatan_ralan_bayi.nyeri_hilang,penilaian_awal_keperawatan_ralan_bayi.ket_nyeri,penilaian_awal_keperawatan_ralan_bayi.pada_dokter,"
                    + "penilaian_awal_keperawatan_ralan_bayi.ket_dokter,penilaian_awal_keperawatan_ralan_bayi.rencana,penilaian_awal_keperawatan_ralan_bayi.nip,petugas.nama "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                    + "inner join penilaian_awal_keperawatan_ralan_bayi on reg_periksa.no_rawat=penilaian_awal_keperawatan_ralan_bayi.no_rawat "
                    + "inner join petugas on penilaian_awal_keperawatan_ralan_bayi.nip=petugas.nip "
                    + "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "
                    + "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik where reg_periksa.no_rawat='" + tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString() + "'", param);
        } else {
            JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data terlebih dahulu..!!!!");
        }
    }//GEN-LAST:event_BtnPrint1ActionPerformed

    private void TBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TBKeyPressed
        Valid.pindah(evt, BB, LP);
    }//GEN-LAST:event_TBKeyPressed

    private void LKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LKKeyPressed
        Valid.pindah(evt, LP, LD);
    }//GEN-LAST:event_LKKeyPressed

    private void BBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BBKeyPressed
        Valid.pindah(evt, GCS, TB);
    }//GEN-LAST:event_BBKeyPressed

    private void LPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LPKeyPressed
        Valid.pindah(evt, TB, LK);
    }//GEN-LAST:event_LPKeyPressed

    private void LDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LDKeyPressed
        Valid.pindah(evt, LK, KeluhanUtama);
    }//GEN-LAST:event_LDKeyPressed

    private void AlergiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AlergiKeyPressed
        Valid.pindah(evt, RPO, Anakke);
    }//GEN-LAST:event_AlergiKeyPressed

    private void KeluhanUtamaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeluhanUtamaKeyPressed
        Valid.pindah2(evt, LD, RPK);
    }//GEN-LAST:event_KeluhanUtamaKeyPressed

    private void RPDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RPDKeyPressed
        Valid.pindah2(evt, RPK, RPO);
    }//GEN-LAST:event_RPDKeyPressed

    private void RPKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RPKKeyPressed
        Valid.pindah2(evt, KeluhanUtama, RPD);
    }//GEN-LAST:event_RPKKeyPressed

    private void RPOKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RPOKeyPressed
        Valid.pindah2(evt, RPD, Alergi);
    }//GEN-LAST:event_RPOKeyPressed

    private void AnakkeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AnakkeKeyPressed
        Valid.pindah(evt, AlatBantu, DariSaudara);
    }//GEN-LAST:event_AnakkeKeyPressed

    private void DariSaudaraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DariSaudaraKeyPressed
        Valid.pindah(evt, Anakke, UmurKelahiran);
    }//GEN-LAST:event_DariSaudaraKeyPressed

    private void CaraKelahiranKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CaraKelahiranKeyPressed
        Valid.pindah(evt, UmurKelahiran, KetCaraKelahiran);
    }//GEN-LAST:event_CaraKelahiranKeyPressed

    private void KetCaraKelahiranKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetCaraKelahiranKeyPressed
        Valid.pindah(evt, CaraKelahiran, KelainanBawaan);
    }//GEN-LAST:event_KetCaraKelahiranKeyPressed

    private void KelainanBawaanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KelainanBawaanKeyPressed
        Valid.pindah(evt, KetCaraKelahiran, KetKelainanBawaan);
    }//GEN-LAST:event_KelainanBawaanKeyPressed

    private void KetKelainanBawaanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetKelainanBawaanKeyPressed
        Valid.pindah(evt, KelainanBawaan, UsiaTengkurap);
    }//GEN-LAST:event_KetKelainanBawaanKeyPressed

    private void UmurKelahiranKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UmurKelahiranKeyPressed
        Valid.pindah(evt, DariSaudara, CaraKelahiran);
    }//GEN-LAST:event_UmurKelahiranKeyPressed

    private void BtnTambahImunisasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahImunisasiActionPerformed
        if (TNoRM.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Pilih terlebih dahulu pasien yang mau dimasukkan data riwayat imunisasinya...");
            Informasi.requestFocus();
        } else {
            KdImunisasi.setText("");
            NmImunisasi.setText("");
            ImunisasiKe.setSelectedIndex(0);
            BtnImunisasi.setEnabled(true);
            BtnSimpanImunisasi.setVisible(true);
            BtnHapusImunisasi.setVisible(false);
            DlgRiwayatImunisasi.setLocationRelativeTo(internalFrame1);
            DlgRiwayatImunisasi.setVisible(true);
        }
    }//GEN-LAST:event_BtnTambahImunisasiActionPerformed

    private void UsiaTengkurapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UsiaTengkurapKeyPressed
        Valid.pindah(evt, KetKelainanBawaan, UsiaDuduk);
    }//GEN-LAST:event_UsiaTengkurapKeyPressed

    private void UsiaDudukKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UsiaDudukKeyPressed
        Valid.pindah(evt, UsiaTengkurap, UsiaBerdiri);
    }//GEN-LAST:event_UsiaDudukKeyPressed

    private void UsiaBerdiriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UsiaBerdiriKeyPressed
        Valid.pindah(evt, UsiaDuduk, UsiaGigi);
    }//GEN-LAST:event_UsiaBerdiriKeyPressed

    private void UsiaGigiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UsiaGigiKeyPressed
        Valid.pindah(evt, UsiaBerdiri, UsiaBerjalan);
    }//GEN-LAST:event_UsiaGigiKeyPressed

    private void UsiaBerjalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UsiaBerjalanKeyPressed
        Valid.pindah(evt, UsiaGigi, UsiaBicara);
    }//GEN-LAST:event_UsiaBerjalanKeyPressed

    private void UsiaBicaraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UsiaBicaraKeyPressed
        Valid.pindah(evt, UsiaBerjalan, UsiaMembaca);
    }//GEN-LAST:event_UsiaBicaraKeyPressed

    private void UsiaMembacaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UsiaMembacaKeyPressed
        Valid.pindah(evt, UsiaBicara, UsiaMenulis);
    }//GEN-LAST:event_UsiaMembacaKeyPressed

    private void UsiaMenulisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UsiaMenulisKeyPressed
        Valid.pindah(evt, UsiaMembaca, GangguanEmosi);
    }//GEN-LAST:event_UsiaMenulisKeyPressed

    private void GangguanEmosiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GangguanEmosiKeyPressed
        Valid.pindah(evt, UsiaMenulis, AlatBantu);
    }//GEN-LAST:event_GangguanEmosiKeyPressed

    private void AlatBantuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AlatBantuKeyPressed
        Valid.pindah(evt, GangguanEmosi, KetBantu);
    }//GEN-LAST:event_AlatBantuKeyPressed

    private void KetBantuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetBantuKeyPressed
        Valid.pindah(evt, CaraKelahiran, Prothesa);
    }//GEN-LAST:event_KetBantuKeyPressed

    private void ProthesaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProthesaKeyPressed
        Valid.pindah(evt, KetCaraKelahiran, KetProthesa);
    }//GEN-LAST:event_ProthesaKeyPressed

    private void KetProthesaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetProthesaKeyPressed
        Valid.pindah(evt, Prothesa, ADL);
    }//GEN-LAST:event_KetProthesaKeyPressed

    private void ADLKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ADLKeyPressed
        Valid.pindah(evt, KetProthesa, StatusPsiko);
    }//GEN-LAST:event_ADLKeyPressed

    private void StatusPsikoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StatusPsikoKeyPressed
        Valid.pindah(evt, ADL, KetPsiko);
    }//GEN-LAST:event_StatusPsikoKeyPressed

    private void KetPsikoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetPsikoKeyPressed
        Valid.pindah(evt, StatusPsiko, HubunganKeluarga);
    }//GEN-LAST:event_KetPsikoKeyPressed

    private void HubunganKeluargaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HubunganKeluargaKeyPressed
        Valid.pindah(evt, KetPsiko, Pengasuh);
    }//GEN-LAST:event_HubunganKeluargaKeyPressed

    private void PengasuhKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PengasuhKeyPressed
        Valid.pindah(evt, HubunganKeluarga, KetPengasuh);
    }//GEN-LAST:event_PengasuhKeyPressed

    private void KetPengasuhKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetPengasuhKeyPressed
        Valid.pindah(evt, Pengasuh, Ekonomi);
    }//GEN-LAST:event_KetPengasuhKeyPressed

    private void EkonomiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EkonomiKeyPressed
        Valid.pindah(evt, KetPengasuh, StatusBudaya);
    }//GEN-LAST:event_EkonomiKeyPressed

    private void StatusBudayaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StatusBudayaKeyPressed
        Valid.pindah(evt, Ekonomi, KetBudaya);
    }//GEN-LAST:event_StatusBudayaKeyPressed

    private void KetBudayaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetBudayaKeyPressed
        Valid.pindah(evt, StatusBudaya, Edukasi);
    }//GEN-LAST:event_KetBudayaKeyPressed

    private void EdukasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EdukasiKeyPressed
        Valid.pindah(evt, KetBudaya, KetEdukasi);
    }//GEN-LAST:event_EdukasiKeyPressed

    private void KetEdukasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetEdukasiKeyPressed
//        Valid.pindah(evt,Edukasi,RJa1);
    }//GEN-LAST:event_KetEdukasiKeyPressed

    private void SG1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SG1ItemStateChanged
        NilaiGizi1.setText(SG1.getSelectedIndex() + "");
        TotalNilaiGizi.setText("" + (Integer.parseInt(NilaiGizi1.getText()) + Integer.parseInt(NilaiGizi2.getText()) + Integer.parseInt(NilaiGizi3.getText()) + Integer.parseInt(NilaiGizi4.getText())));
    }//GEN-LAST:event_SG1ItemStateChanged

    private void SG1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SG1KeyPressed
//        Valid.pindah(evt,KetLapor,SG2);
    }//GEN-LAST:event_SG1KeyPressed

    private void SG2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SG2ItemStateChanged
        NilaiGizi2.setText(SG2.getSelectedIndex() + "");
        TotalNilaiGizi.setText("" + (Integer.parseInt(NilaiGizi1.getText()) + Integer.parseInt(NilaiGizi2.getText()) + Integer.parseInt(NilaiGizi3.getText()) + Integer.parseInt(NilaiGizi4.getText())));
    }//GEN-LAST:event_SG2ItemStateChanged

    private void SG2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SG2KeyPressed
        Valid.pindah(evt, SG1, SG3);
    }//GEN-LAST:event_SG2KeyPressed

    private void SG3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SG3ItemStateChanged
        NilaiGizi3.setText(SG3.getSelectedIndex() + "");
        TotalNilaiGizi.setText("" + (Integer.parseInt(NilaiGizi1.getText()) + Integer.parseInt(NilaiGizi2.getText()) + Integer.parseInt(NilaiGizi3.getText()) + Integer.parseInt(NilaiGizi4.getText())));
    }//GEN-LAST:event_SG3ItemStateChanged

    private void SG3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SG3KeyPressed
        Valid.pindah(evt, SG2, SG4);
    }//GEN-LAST:event_SG3KeyPressed

    private void SkalaWajahItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaWajahItemStateChanged
        NilaiWajah.setText(SkalaWajah.getSelectedIndex() + "");
        SkalaNyeri.setText("" + (Integer.parseInt(NilaiWajah.getText()) + Integer.parseInt(NilaiKaki.getText()) + Integer.parseInt(NilaiAktifitas.getText()) + Integer.parseInt(NilaiMenangis.getText()) + Integer.parseInt(NilaiBersuara.getText())));
    }//GEN-LAST:event_SkalaWajahItemStateChanged

    private void SkalaWajahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaWajahKeyPressed
        Valid.pindah(evt, SG4, SkalaKaki);
    }//GEN-LAST:event_SkalaWajahKeyPressed

    private void SG4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SG4ItemStateChanged
        NilaiGizi4.setText(SG4.getSelectedIndex() + "");
        TotalNilaiGizi.setText("" + (Integer.parseInt(NilaiGizi1.getText()) + Integer.parseInt(NilaiGizi2.getText()) + Integer.parseInt(NilaiGizi3.getText()) + Integer.parseInt(NilaiGizi4.getText())));
    }//GEN-LAST:event_SG4ItemStateChanged

    private void SG4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SG4KeyPressed
        Valid.pindah(evt, SG3, SkalaWajah);
    }//GEN-LAST:event_SG4KeyPressed

    private void NilaiWajahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NilaiWajahKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NilaiWajahKeyPressed

    private void SkalaKakiItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaKakiItemStateChanged
        NilaiKaki.setText(SkalaKaki.getSelectedIndex() + "");
        SkalaNyeri.setText("" + (Integer.parseInt(NilaiWajah.getText()) + Integer.parseInt(NilaiKaki.getText()) + Integer.parseInt(NilaiAktifitas.getText()) + Integer.parseInt(NilaiMenangis.getText()) + Integer.parseInt(NilaiBersuara.getText())));
    }//GEN-LAST:event_SkalaKakiItemStateChanged

    private void SkalaKakiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaKakiKeyPressed
        Valid.pindah(evt, SkalaWajah, SkalaAktifitas);
    }//GEN-LAST:event_SkalaKakiKeyPressed

    private void NilaiKakiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NilaiKakiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NilaiKakiKeyPressed

    private void SkalaAktifitasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaAktifitasItemStateChanged
        NilaiAktifitas.setText(SkalaAktifitas.getSelectedIndex() + "");
        SkalaNyeri.setText("" + (Integer.parseInt(NilaiWajah.getText()) + Integer.parseInt(NilaiKaki.getText()) + Integer.parseInt(NilaiAktifitas.getText()) + Integer.parseInt(NilaiMenangis.getText()) + Integer.parseInt(NilaiBersuara.getText())));
    }//GEN-LAST:event_SkalaAktifitasItemStateChanged

    private void SkalaAktifitasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaAktifitasKeyPressed
        Valid.pindah(evt, SkalaKaki, SkalaMenangis);
    }//GEN-LAST:event_SkalaAktifitasKeyPressed

    private void NilaiAktifitasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NilaiAktifitasKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NilaiAktifitasKeyPressed

    private void SkalaMenangisItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaMenangisItemStateChanged
        NilaiMenangis.setText(SkalaMenangis.getSelectedIndex() + "");
        SkalaNyeri.setText("" + (Integer.parseInt(NilaiWajah.getText()) + Integer.parseInt(NilaiKaki.getText()) + Integer.parseInt(NilaiAktifitas.getText()) + Integer.parseInt(NilaiMenangis.getText()) + Integer.parseInt(NilaiBersuara.getText())));
    }//GEN-LAST:event_SkalaMenangisItemStateChanged

    private void SkalaMenangisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaMenangisKeyPressed
        Valid.pindah(evt, SkalaAktifitas, SkalaBersuara);
    }//GEN-LAST:event_SkalaMenangisKeyPressed

    private void NilaiMenangisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NilaiMenangisKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NilaiMenangisKeyPressed

    private void SkalaBersuaraItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaBersuaraItemStateChanged
        NilaiBersuara.setText(SkalaBersuara.getSelectedIndex() + "");
        SkalaNyeri.setText("" + (Integer.parseInt(NilaiWajah.getText()) + Integer.parseInt(NilaiKaki.getText()) + Integer.parseInt(NilaiAktifitas.getText()) + Integer.parseInt(NilaiMenangis.getText()) + Integer.parseInt(NilaiBersuara.getText())));
    }//GEN-LAST:event_SkalaBersuaraItemStateChanged

    private void SkalaBersuaraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaBersuaraKeyPressed
        Valid.pindah(evt, SkalaMenangis, Nyeri);
    }//GEN-LAST:event_SkalaBersuaraKeyPressed

    private void NilaiBersuaraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NilaiBersuaraKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NilaiBersuaraKeyPressed

    private void SkalaNyeriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaNyeriKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaNyeriKeyPressed

    private void NyeriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NyeriKeyPressed
        Valid.pindah(evt, SkalaBersuara, Lokasi);
    }//GEN-LAST:event_NyeriKeyPressed

    private void FrekuensiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FrekuensiKeyPressed
        Valid.pindah(evt, Durasi, NyeriHilang);
    }//GEN-LAST:event_FrekuensiKeyPressed

    private void LokasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LokasiKeyPressed
        Valid.pindah(evt, Nyeri, Durasi);
    }//GEN-LAST:event_LokasiKeyPressed

    private void DurasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DurasiKeyPressed
        Valid.pindah(evt, Lokasi, Frekuensi);
    }//GEN-LAST:event_DurasiKeyPressed

    private void NyeriHilangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NyeriHilangKeyPressed
        Valid.pindah(evt, Frekuensi, KetNyeri);
    }//GEN-LAST:event_NyeriHilangKeyPressed

    private void KetNyeriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetNyeriKeyPressed
        Valid.pindah(evt, NyeriHilang, PadaDokter);
    }//GEN-LAST:event_KetNyeriKeyPressed

    private void PadaDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PadaDokterKeyPressed
        Valid.pindah(evt, KetNyeri, KetDokter);
    }//GEN-LAST:event_PadaDokterKeyPressed

    private void KetDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetDokterKeyPressed
        Valid.pindah(evt, PadaDokter, Rencana);
    }//GEN-LAST:event_KetDokterKeyPressed

    private void BtnKeluarImunisasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarImunisasiActionPerformed
        DlgRiwayatImunisasi.dispose();
    }//GEN-LAST:event_BtnKeluarImunisasiActionPerformed

    private void BtnSimpanImunisasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanImunisasiActionPerformed
        if (KdImunisasi.getText().trim().equals("") || NmImunisasi.getText().trim().equals("")) {
            Valid.textKosong(BtnImunisasi, "Imunisasi");
        } else {
            if (Sequel.menyimpantf("riwayat_imunisasi", "?,?,?", "Riwayat Imunisasi", 3, new String[]{
                TNoRM.getText(), KdImunisasi.getText(), ImunisasiKe.getSelectedItem().toString()
            }) == true) {
                KdImunisasi.setText("");
                NmImunisasi.setText("");
                ImunisasiKe.setSelectedIndex(0);
                runBackground(() -> tampilImunisasi());
            }
        }
    }//GEN-LAST:event_BtnSimpanImunisasiActionPerformed

    private void BtnImunisasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnImunisasiActionPerformed
        MasterCariImunisasi imunisasi = new MasterCariImunisasi(null, false);
        imunisasi.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (imunisasi.getTable().getSelectedRow() != -1) {
                    KdImunisasi.setText(imunisasi.getTable().getValueAt(imunisasi.getTable().getSelectedRow(), 0).toString());
                    NmImunisasi.setText(imunisasi.getTable().getValueAt(imunisasi.getTable().getSelectedRow(), 1).toString());
                }
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        imunisasi.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    imunisasi.dispose();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        imunisasi.isCek();
        imunisasi.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        imunisasi.setLocationRelativeTo(internalFrame1);
        imunisasi.setVisible(true);
    }//GEN-LAST:event_BtnImunisasiActionPerformed

    private void BtnImunisasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnImunisasiKeyPressed
        //Valid.pindah(evt,Monitoring,BtnSimpan);
    }//GEN-LAST:event_BtnImunisasiKeyPressed

    private void KdImunisasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdImunisasiKeyPressed

    }//GEN-LAST:event_KdImunisasiKeyPressed

    private void ImunisasiKeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ImunisasiKeKeyPressed
        Valid.pindah(evt, TglAsuhan, TD);
    }//GEN-LAST:event_ImunisasiKeKeyPressed

    private void BtnHapusImunisasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusImunisasiActionPerformed
        Sequel.queryu2("delete from riwayat_imunisasi where no_rkm_medis=? and kode_imunisasi=? and no_imunisasi=?", 3, new String[]{TNoRM.getText(), KdImunisasi.getText(), ImunisasiKe.getSelectedItem().toString()});
        DlgRiwayatImunisasi.dispose();
        runBackground(() -> tampilImunisasi());
    }//GEN-LAST:event_BtnHapusImunisasiActionPerformed

    private void BtnHapusImunisasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusImunisasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnHapusImunisasiKeyPressed

    private void BtnPanggilHapusImunisasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPanggilHapusImunisasiActionPerformed
        if (tbImunisasi.getSelectedRow() > -1) {
            if (TNoRM.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Pilih terlebih dahulu pasien yang mau dihapus data riwayat imunisasinya...");
                Informasi.requestFocus();
            } else {
                BtnImunisasi.setEnabled(false);
                KdImunisasi.setText(tbImunisasi.getValueAt(tbImunisasi.getSelectedRow(), 0).toString());
                NmImunisasi.setText(tbImunisasi.getValueAt(tbImunisasi.getSelectedRow(), 1).toString());
                BtnSimpanImunisasi.setVisible(false);
                BtnHapusImunisasi.setVisible(true);
                DlgRiwayatImunisasi.setLocationRelativeTo(internalFrame1);
                DlgRiwayatImunisasi.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Silahkan anda pilih data terlebih dahulu..!!");
        }
    }//GEN-LAST:event_BtnPanggilHapusImunisasiActionPerformed

    private void RencanaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RencanaKeyPressed
        Valid.pindah2(evt, TCariMasalah, BtnSimpan);
    }//GEN-LAST:event_RencanaKeyPressed

    private void TCariRencanaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariRencanaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            runBackground(() -> tampilRencana2());
        } else if ((evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) || (evt.getKeyCode() == KeyEvent.VK_TAB)) {
            BtnCariRencana.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            TCariMasalah.requestFocus();
        }
    }//GEN-LAST:event_TCariRencanaKeyPressed

    private void BtnCariRencanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariRencanaActionPerformed
        runBackground(() -> tampilRencana2());
    }//GEN-LAST:event_BtnCariRencanaActionPerformed

    private void BtnCariRencanaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariRencanaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            runBackground(() -> tampilRencana2());
        } else if ((evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) || (evt.getKeyCode() == KeyEvent.VK_TAB)) {
            BtnSimpan.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            TCariRencana.requestFocus();
        }
    }//GEN-LAST:event_BtnCariRencanaKeyPressed

    private void BtnAllRencanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllRencanaActionPerformed
        TCariRencana.setText("");
        runBackground(() -> LoadRencana());
    }//GEN-LAST:event_BtnAllRencanaActionPerformed

    private void BtnAllRencanaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllRencanaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnAllRencanaActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnCariRencana, TCariRencana);
        }
    }//GEN-LAST:event_BtnAllRencanaKeyPressed

    private void BtnTambahRencanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahRencanaActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        MasterRencanaKeperawatanAnak form = new MasterRencanaKeperawatanAnak(null, false);
        form.isCek();
        form.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        form.setLocationRelativeTo(internalFrame1);
        form.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnTambahRencanaActionPerformed

    private void TCariMasalahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariMasalahKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            runBackground(() -> tampilMasalah2());
        } else if ((evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) || (evt.getKeyCode() == KeyEvent.VK_TAB)) {
            Rencana.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            KetDokter.requestFocus();
        }
    }//GEN-LAST:event_TCariMasalahKeyPressed

    private void BtnCariMasalahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariMasalahActionPerformed
        runBackground(() -> tampilMasalah2());
    }//GEN-LAST:event_BtnCariMasalahActionPerformed

    private void BtnCariMasalahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariMasalahKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            runBackground(() -> tampilMasalah2());
        } else if ((evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) || (evt.getKeyCode() == KeyEvent.VK_TAB)) {
            Rencana.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            KetDokter.requestFocus();
        }
    }//GEN-LAST:event_BtnCariMasalahKeyPressed

    private void BtnAllMasalahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllMasalahActionPerformed
        TCari.setText("");
        runBackground(() -> tampilMasalah());
    }//GEN-LAST:event_BtnAllMasalahActionPerformed

    private void BtnAllMasalahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllMasalahKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnAllMasalahActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnCariMasalah, TCariMasalah);
        }
    }//GEN-LAST:event_BtnAllMasalahKeyPressed

    private void BtnTambahMasalahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahMasalahActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        MasterMasalahKeperawatanAnak form = new MasterMasalahKeperawatanAnak(null, false);
        form.isCek();
        form.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        form.setLocationRelativeTo(internalFrame1);
        form.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnTambahMasalahActionPerformed

    private void tbMasalahKeperawatanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbMasalahKeperawatanMouseClicked
        if (tabModeMasalah.getRowCount() != 0) {
            try {
                runBackground(() -> tampilRencana2());
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbMasalahKeperawatanMouseClicked

    private void tbMasalahKeperawatanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbMasalahKeperawatanKeyPressed
        if (tabModeMasalah.getRowCount() != 0) {
            if (evt.getKeyCode() == KeyEvent.VK_SHIFT) {
                TCariMasalah.setText("");
                TCariMasalah.requestFocus();
            }
        }
    }//GEN-LAST:event_tbMasalahKeperawatanKeyPressed

    private void tbMasalahKeperawatanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbMasalahKeperawatanKeyReleased
        if (tabModeMasalah.getRowCount() != 0) {
            if ((evt.getKeyCode() == KeyEvent.VK_ENTER) || (evt.getKeyCode() == KeyEvent.VK_UP) || (evt.getKeyCode() == KeyEvent.VK_DOWN)) {
                try {
                    runBackground(() -> tampilRencana2());
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbMasalahKeperawatanKeyReleased

    private void KemampuanBacaTulisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KemampuanBacaTulisKeyPressed
//        Valid.pindah(evt, BahasaSehari, ButuhPenerjemah);
    }//GEN-LAST:event_KemampuanBacaTulisKeyPressed

    private void ButuhPenerjemahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ButuhPenerjemahKeyPressed
        Valid.pindah(evt, KemampuanBacaTulis, KeteranganButuhPenerjemah);
    }//GEN-LAST:event_ButuhPenerjemahKeyPressed

    private void KeteranganButuhPenerjemahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeteranganButuhPenerjemahKeyPressed
        Valid.pindah(evt, ButuhPenerjemah, TerdapatHambatanBelajar);
    }//GEN-LAST:event_KeteranganButuhPenerjemahKeyPressed

    private void TerdapatHambatanBelajarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TerdapatHambatanBelajarKeyPressed
        Valid.pindah(evt, KeteranganButuhPenerjemah, HambatanBelajar);
    }//GEN-LAST:event_TerdapatHambatanBelajarKeyPressed

    private void HambatanBelajarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HambatanBelajarKeyPressed
        Valid.pindah(evt, TerdapatHambatanBelajar, KeteranganHambatanBelajar);
    }//GEN-LAST:event_HambatanBelajarKeyPressed

    private void KeteranganHambatanBelajarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeteranganHambatanBelajarKeyPressed
        Valid.pindah(evt, HambatanBelajar, HambatanCaraBicara);
    }//GEN-LAST:event_KeteranganHambatanBelajarKeyPressed

    private void HambatanCaraBicaraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HambatanCaraBicaraKeyPressed
        Valid.pindah(evt, KeteranganHambatanBelajar, HambatanBahasaIsyarat);
    }//GEN-LAST:event_HambatanCaraBicaraKeyPressed

    private void HambatanBahasaIsyaratKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HambatanBahasaIsyaratKeyPressed
        Valid.pindah(evt, HambatanCaraBicara, CaraBelajarDisukai);
    }//GEN-LAST:event_HambatanBahasaIsyaratKeyPressed

    private void CaraBelajarDisukaiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CaraBelajarDisukaiKeyPressed
        Valid.pindah(evt, HambatanBahasaIsyarat, KesediaanMenerimaInformasi);
    }//GEN-LAST:event_CaraBelajarDisukaiKeyPressed

    private void KesediaanMenerimaInformasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KesediaanMenerimaInformasiKeyPressed
        Valid.pindah(evt, CaraBelajarDisukai, KeteranganKesediaanMenerimaInformasi);
    }//GEN-LAST:event_KesediaanMenerimaInformasiKeyPressed

    private void KeteranganKesediaanMenerimaInformasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeteranganKesediaanMenerimaInformasiKeyPressed
        Valid.pindah(evt, KesediaanMenerimaInformasi, PemahamanNutrisi);
    }//GEN-LAST:event_KeteranganKesediaanMenerimaInformasiKeyPressed

    private void PemahamanNutrisiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PemahamanNutrisiKeyPressed
        Valid.pindah(evt, KeteranganKesediaanMenerimaInformasi, PemahamanPenyakit);
    }//GEN-LAST:event_PemahamanNutrisiKeyPressed

    private void PemahamanPenyakitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PemahamanPenyakitKeyPressed
        Valid.pindah(evt, PemahamanNutrisi, PemahamanPengobatan);
    }//GEN-LAST:event_PemahamanPenyakitKeyPressed

    private void PemahamanPerawatanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PemahamanPerawatanKeyPressed
        Valid.pindah(evt, PemahamanPengobatan, SG1);
    }//GEN-LAST:event_PemahamanPerawatanKeyPressed

    private void PemahamanPengobatanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PemahamanPengobatanKeyPressed
        Valid.pindah(evt, PemahamanPenyakit, PemahamanPerawatan);
    }//GEN-LAST:event_PemahamanPengobatanKeyPressed

    private void KeyakinanNilaiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeyakinanNilaiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KeyakinanNilaiKeyPressed

    private void HambatanEmosionalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HambatanEmosionalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_HambatanEmosionalKeyPressed

    private void KeterbatasanFisikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeterbatasanFisikKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KeterbatasanFisikKeyPressed

    private void MotivasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MotivasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_MotivasiKeyPressed

    private void tbKebutuhanEdukasiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbKebutuhanEdukasiMouseClicked
        if (tabModeKebutuhanEdukasi.getRowCount() != 0) {
            try {
                runBackground(() -> tampilRencanaEdukasi2());
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbKebutuhanEdukasiMouseClicked

    private void tbKebutuhanEdukasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbKebutuhanEdukasiKeyPressed
        if (tabModeKebutuhanEdukasi.getRowCount() != 0) {
            if (evt.getKeyCode() == KeyEvent.VK_SHIFT) {
                TCariMasalah.setText("");
                TCariMasalah.requestFocus();
            }
        }
    }//GEN-LAST:event_tbKebutuhanEdukasiKeyPressed

    private void tbKebutuhanEdukasiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbKebutuhanEdukasiKeyReleased
        if (tabModeKebutuhanEdukasi.getRowCount() != 0) {
            if ((evt.getKeyCode() == KeyEvent.VK_ENTER) || (evt.getKeyCode() == KeyEvent.VK_UP) || (evt.getKeyCode() == KeyEvent.VK_DOWN)) {
                try {
                    runBackground(() -> tampilRencanaEdukasi2());
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbKebutuhanEdukasiKeyReleased

    private void BtnTambahKebutuhanEdukasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahKebutuhanEdukasiActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        MasterKebutuhanEdukasi form = new MasterKebutuhanEdukasi(null, false);
        form.isCek();
        form.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        form.setLocationRelativeTo(internalFrame1);
        form.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnTambahKebutuhanEdukasiActionPerformed

    private void BtnAllKebutuhanEdukasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllKebutuhanEdukasiActionPerformed
        TCari.setText("");
        runBackground(() -> tampilKebutuhanEdukasi());
    }//GEN-LAST:event_BtnAllKebutuhanEdukasiActionPerformed

    private void BtnAllKebutuhanEdukasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKebutuhanEdukasiKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnAllKebutuhanEdukasiActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnCariMasalah, TCariMasalah);
        }
    }//GEN-LAST:event_BtnAllKebutuhanEdukasiKeyPressed

    private void BtnCariKebutuhanEdukasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariKebutuhanEdukasiActionPerformed
        runBackground(() -> tampilKebutuhanEdukasi2());
    }//GEN-LAST:event_BtnCariKebutuhanEdukasiActionPerformed

    private void BtnCariKebutuhanEdukasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKebutuhanEdukasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCariKebutuhanEdukasiKeyPressed

    private void TCariMasalah2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariMasalah2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TCariMasalah2KeyPressed

    private void BtnTambahRencanaEdukasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahRencanaEdukasiActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        MasterRencanaEdukasi form = new MasterRencanaEdukasi(null, false);
        form.isCek();
        form.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        form.setLocationRelativeTo(internalFrame1);
        form.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnTambahRencanaEdukasiActionPerformed

    private void BtnAllRencanaEdukasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllRencanaEdukasiActionPerformed
        TCariRencana.setText("");
        runBackground(() -> LoadRencanaEdukasi());
    }//GEN-LAST:event_BtnAllRencanaEdukasiActionPerformed

    private void BtnAllRencanaEdukasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllRencanaEdukasiKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnAllRencanaEdukasiActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnCariRencana, TCariRencana);
        }
    }//GEN-LAST:event_BtnAllRencanaEdukasiKeyPressed

    private void BtnCariRencanaEdukasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariRencanaEdukasiActionPerformed
        runBackground(() -> tampilRencanaEdukasi2());
    }//GEN-LAST:event_BtnCariRencanaEdukasiActionPerformed

    private void BtnCariRencanaEdukasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariRencanaEdukasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCariRencanaEdukasiKeyPressed

    private void TCariRencana2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariRencana2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TCariRencana2KeyPressed

    private void SkalaResiko1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko1ItemStateChanged
        if (SkalaResiko1.getSelectedIndex() == 0) {
            NilaiResiko1.setText("4");
        } else if (SkalaResiko1.getSelectedIndex() == 1) {
            NilaiResiko1.setText("3");
        } else if (SkalaResiko1.getSelectedIndex() == 2) {
            NilaiResiko1.setText("2");
        } else {
            NilaiResiko1.setText("1");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko1ItemStateChanged

    private void SkalaResiko1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko1KeyPressed
//        Valid.pindah(evt, KeteranganSkriningGizi, SkalaResiko2);
    }//GEN-LAST:event_SkalaResiko1KeyPressed

    private void SkalaResiko2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko2ItemStateChanged
        if (SkalaResiko2.getSelectedIndex() == 0) {
            NilaiResiko2.setText("2");
        } else {
            NilaiResiko2.setText("1");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko2ItemStateChanged

    private void SkalaResiko2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko2KeyPressed
        Valid.pindah(evt, SkalaResiko1, SkalaResiko3);
    }//GEN-LAST:event_SkalaResiko2KeyPressed

    private void SkalaResiko3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko3ItemStateChanged
        if (SkalaResiko3.getSelectedIndex() == 0) {
            NilaiResiko3.setText("4");
        } else if (SkalaResiko3.getSelectedIndex() == 1) {
            NilaiResiko3.setText("3");
        } else if (SkalaResiko3.getSelectedIndex() == 2) {
            NilaiResiko3.setText("2");
        } else {
            NilaiResiko3.setText("1");
        }
//        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko3ItemStateChanged

    private void SkalaResiko3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko3KeyPressed
        Valid.pindah(evt, SkalaResiko2, SkalaResiko4);
    }//GEN-LAST:event_SkalaResiko3KeyPressed

    private void SkalaResiko4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko4ItemStateChanged
        if (SkalaResiko4.getSelectedIndex() == 0) {
            NilaiResiko4.setText("3");
        } else if (SkalaResiko4.getSelectedIndex() == 1) {
            NilaiResiko4.setText("2");
        } else {
            NilaiResiko4.setText("1");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko4ItemStateChanged

    private void SkalaResiko4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko4KeyPressed
        Valid.pindah(evt, SkalaResiko3, SkalaResiko5);
    }//GEN-LAST:event_SkalaResiko4KeyPressed

    private void SkalaResiko5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko5ItemStateChanged
        if (SkalaResiko5.getSelectedIndex() == 0) {
            NilaiResiko5.setText("4");
        } else if (SkalaResiko5.getSelectedIndex() == 1) {
            NilaiResiko5.setText("3");
        } else if (SkalaResiko5.getSelectedIndex() == 2) {
            NilaiResiko5.setText("2");
        } else {
            NilaiResiko5.setText("1");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko5ItemStateChanged

    private void SkalaResiko5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko5KeyPressed
        Valid.pindah(evt, SkalaResiko4, SkalaResiko6);
    }//GEN-LAST:event_SkalaResiko5KeyPressed

    private void SkalaResiko6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko6ItemStateChanged
        if (SkalaResiko6.getSelectedIndex() == 0) {
            NilaiResiko6.setText("3");
        } else if (SkalaResiko6.getSelectedIndex() == 1) {
            NilaiResiko6.setText("2");
        } else {
            NilaiResiko6.setText("1");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko6ItemStateChanged

    private void SkalaResiko6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko6KeyPressed
        Valid.pindah(evt, SkalaResiko5, SkalaResiko7);
    }//GEN-LAST:event_SkalaResiko6KeyPressed

    private void SkalaResiko7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko7ItemStateChanged
        if (SkalaResiko7.getSelectedIndex() == 0) {
            NilaiResiko7.setText("3");
        } else if (SkalaResiko7.getSelectedIndex() == 1) {
            NilaiResiko7.setText("2");
        } else {
            NilaiResiko7.setText("1");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko7ItemStateChanged

    private void SkalaResiko7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko7KeyPressed
//        Valid.pindah(evt, SkalaResiko6, SkalaNIPS1);
    }//GEN-LAST:event_SkalaResiko7KeyPressed

    private void InformasiPerencanaanPulangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_InformasiPerencanaanPulangKeyPressed
//        Valid.pindah(evt, SkalaNIPS5, LamaRatarata);
    }//GEN-LAST:event_InformasiPerencanaanPulangKeyPressed

    private void KondisiPulangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KondisiPulangKeyPressed
        Valid.pindah(evt, TanggalPulang, PerawatanLanjutan);
    }//GEN-LAST:event_KondisiPulangKeyPressed

    private void TanggalPulangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalPulangKeyPressed
        Valid.pindah(evt, LamaRatarata, KondisiPulang);
    }//GEN-LAST:event_TanggalPulangKeyPressed

    private void LamaRatarataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LamaRatarataKeyPressed
        Valid.pindah(evt, InformasiPerencanaanPulang, TanggalPulang);
    }//GEN-LAST:event_LamaRatarataKeyPressed

    private void PerawatanLanjutanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PerawatanLanjutanKeyPressed
        Valid.pindah2(evt, KondisiPulang, CaraTransportasiPulang);
    }//GEN-LAST:event_PerawatanLanjutanKeyPressed

    private void CaraTransportasiPulangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CaraTransportasiPulangKeyPressed
        Valid.pindah(evt, PerawatanLanjutan, TransportasiYangDigunakan);
    }//GEN-LAST:event_CaraTransportasiPulangKeyPressed

    private void TransportasiYangDigunakanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TransportasiYangDigunakanKeyPressed
        Valid.pindah(evt, CaraTransportasiPulang, TCariMasalah);
    }//GEN-LAST:event_TransportasiYangDigunakanKeyPressed

    private void SkalaResiko1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SkalaResiko1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMPenilaianAwalKeperawatanBayiAnak dialog = new RMPenilaianAwalKeperawatanBayiAnak(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.ComboBox ADL;
    private widget.TextBox Agama;
    private widget.ComboBox AlatBantu;
    private widget.TextBox Alergi;
    private widget.TextBox Anakke;
    private widget.TextBox BB;
    private widget.TextBox Bahasa;
    private widget.Button BtnAll;
    private widget.Button BtnAllKebutuhanEdukasi;
    private widget.Button BtnAllMasalah;
    private widget.Button BtnAllRencana;
    private widget.Button BtnAllRencanaEdukasi;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnCariKebutuhanEdukasi;
    private widget.Button BtnCariMasalah;
    private widget.Button BtnCariRencana;
    private widget.Button BtnCariRencanaEdukasi;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnHapusImunisasi;
    private widget.Button BtnImunisasi;
    private widget.Button BtnKeluar;
    private widget.Button BtnKeluarImunisasi;
    private widget.Button BtnPanggilHapusImunisasi;
    private widget.Button BtnPetugas;
    private widget.Button BtnPrint;
    private widget.Button BtnPrint1;
    private widget.Button BtnSimpan;
    private widget.Button BtnSimpanImunisasi;
    private widget.Button BtnTambahImunisasi;
    private widget.Button BtnTambahKebutuhanEdukasi;
    private widget.Button BtnTambahMasalah;
    private widget.Button BtnTambahRencana;
    private widget.Button BtnTambahRencanaEdukasi;
    private widget.ComboBox ButuhPenerjemah;
    private widget.TextBox CacatFisik;
    private widget.ComboBox CaraBelajarDisukai;
    private widget.ComboBox CaraKelahiran;
    private widget.ComboBox CaraTransportasiPulang;
    private widget.CekBox ChkAccor;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.TextBox DariSaudara;
    private widget.TextArea DetailRencana;
    private javax.swing.JDialog DlgRiwayatImunisasi;
    private widget.TextBox Durasi;
    private widget.ComboBox Edukasi;
    private widget.ComboBox Ekonomi;
    private widget.PanelBiasa FormInput;
    private widget.PanelBiasa FormKebutuhanRencana;
    private widget.PanelBiasa FormMasalahRencana;
    private widget.PanelBiasa FormMenu;
    private widget.TextBox Frekuensi;
    private widget.TextBox GCS;
    private widget.TextBox GangguanEmosi;
    private widget.ComboBox HambatanBahasaIsyarat;
    private widget.ComboBox HambatanBelajar;
    private widget.ComboBox HambatanCaraBicara;
    private widget.ComboBox HambatanEmosional;
    private widget.ComboBox HubunganKeluarga;
    private widget.ComboBox ImunisasiKe;
    private widget.ComboBox Informasi;
    private widget.ComboBox InformasiPerencanaanPulang;
    private widget.TextBox Jk;
    private widget.TextBox KdImunisasi;
    private widget.TextBox KdPetugas;
    private widget.ComboBox KelainanBawaan;
    private widget.TextArea KeluhanUtama;
    private widget.ComboBox KemampuanBacaTulis;
    private widget.ComboBox KesediaanMenerimaInformasi;
    private widget.TextBox KetBantu;
    private widget.TextBox KetBudaya;
    private widget.TextBox KetCaraKelahiran;
    private widget.TextBox KetDokter;
    private widget.TextBox KetEdukasi;
    private widget.TextBox KetKelainanBawaan;
    private widget.TextBox KetNyeri;
    private widget.TextBox KetPengasuh;
    private widget.TextBox KetProthesa;
    private widget.TextBox KetPsiko;
    private widget.TextBox KeteranganButuhPenerjemah;
    private widget.TextBox KeteranganHambatanBelajar;
    private widget.TextBox KeteranganKesediaanMenerimaInformasi;
    private widget.TextBox KeteranganTingkatRisiko;
    private widget.ComboBox KeterbatasanFisik;
    private widget.ComboBox KeyakinanNilai;
    private widget.TextBox KondisiPulang;
    private widget.Label LCount;
    private widget.TextBox LD;
    private widget.TextBox LK;
    private widget.TextBox LP;
    private widget.TextBox LamaRatarata;
    private widget.editorpane LoadHTML;
    private widget.TextBox Lokasi;
    private widget.ComboBox Motivasi;
    private widget.TextBox Nadi;
    private widget.TextBox NilaiAktifitas;
    private widget.TextBox NilaiBersuara;
    private widget.TextBox NilaiGizi1;
    private widget.TextBox NilaiGizi2;
    private widget.TextBox NilaiGizi3;
    private widget.TextBox NilaiGizi4;
    private widget.TextBox NilaiKaki;
    private widget.TextBox NilaiMenangis;
    private widget.TextBox NilaiResiko1;
    private widget.TextBox NilaiResiko2;
    private widget.TextBox NilaiResiko3;
    private widget.TextBox NilaiResiko4;
    private widget.TextBox NilaiResiko5;
    private widget.TextBox NilaiResiko6;
    private widget.TextBox NilaiResiko7;
    private widget.TextBox NilaiResikoTotal;
    private widget.TextBox NilaiWajah;
    private widget.TextBox NmImunisasi;
    private widget.TextBox NmPetugas;
    private widget.ComboBox Nyeri;
    private widget.ComboBox NyeriHilang;
    private widget.ComboBox PadaDokter;
    private widget.PanelBiasa PanelAccor;
    private usu.widget.glass.PanelGlass PanelWall;
    private widget.ComboBox PemahamanNutrisi;
    private widget.ComboBox PemahamanPengobatan;
    private widget.ComboBox PemahamanPenyakit;
    private widget.ComboBox PemahamanPerawatan;
    private widget.ComboBox Pengasuh;
    private widget.TextArea PerawatanLanjutan;
    private widget.ComboBox Prothesa;
    private widget.TextArea RPD;
    private widget.TextArea RPK;
    private widget.TextArea RPO;
    private widget.TextBox RR;
    private widget.TextArea Rencana;
    private widget.ComboBox SG1;
    private widget.ComboBox SG2;
    private widget.ComboBox SG3;
    private widget.ComboBox SG4;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll10;
    private widget.ScrollPane Scroll12;
    private widget.ScrollPane Scroll13;
    private widget.ScrollPane Scroll15;
    private widget.ScrollPane Scroll16;
    private widget.ScrollPane Scroll6;
    private widget.ScrollPane Scroll7;
    private widget.ScrollPane Scroll8;
    private widget.ScrollPane Scroll9;
    private widget.ComboBox SkalaAktifitas;
    private widget.ComboBox SkalaBersuara;
    private widget.ComboBox SkalaKaki;
    private widget.ComboBox SkalaMenangis;
    private widget.TextBox SkalaNyeri;
    private widget.ComboBox SkalaResiko1;
    private widget.ComboBox SkalaResiko2;
    private widget.ComboBox SkalaResiko3;
    private widget.ComboBox SkalaResiko4;
    private widget.ComboBox SkalaResiko5;
    private widget.ComboBox SkalaResiko6;
    private widget.ComboBox SkalaResiko7;
    private widget.ComboBox SkalaWajah;
    private widget.ComboBox StatusBudaya;
    private widget.ComboBox StatusPsiko;
    private widget.TextBox Suhu;
    private widget.TextBox TB;
    private widget.TextBox TCari;
    private widget.TextBox TCariMasalah;
    private widget.TextBox TCariMasalah2;
    private widget.TextBox TCariRencana;
    private widget.TextBox TCariRencana2;
    private widget.TextBox TD;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRM1;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.TextBox TPasien1;
    private javax.swing.JTabbedPane TabRawat;
    private javax.swing.JTabbedPane TabRawat1;
    private javax.swing.JTabbedPane TabRencanaKeperawatan;
    private javax.swing.JTabbedPane TabRencanaKeperawatan2;
    private widget.Tanggal TanggalPulang;
    private widget.TextBox TanggalRegistrasi;
    private widget.ComboBox TerdapatHambatanBelajar;
    private widget.Tanggal TglAsuhan;
    private widget.TextBox TglLahir;
    private widget.Label TingkatResiko1;
    private widget.TextBox TotalNilaiGizi;
    private widget.ComboBox TransportasiYangDigunakan;
    private widget.ComboBox UmurKelahiran;
    private widget.TextBox UsiaBerdiri;
    private widget.TextBox UsiaBerjalan;
    private widget.TextBox UsiaBicara;
    private widget.TextBox UsiaDuduk;
    private widget.TextBox UsiaGigi;
    private widget.TextBox UsiaMembaca;
    private widget.TextBox UsiaMenulis;
    private widget.TextBox UsiaTengkurap;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame3;
    private widget.InternalFrame internalFrame4;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel125;
    private widget.Label jLabel127;
    private widget.Label jLabel128;
    private widget.Label jLabel129;
    private widget.Label jLabel130;
    private widget.Label jLabel131;
    private widget.Label jLabel132;
    private widget.Label jLabel133;
    private widget.Label jLabel134;
    private widget.Label jLabel135;
    private widget.Label jLabel136;
    private widget.Label jLabel137;
    private widget.Label jLabel138;
    private widget.Label jLabel139;
    private widget.Label jLabel140;
    private widget.Label jLabel141;
    private widget.Label jLabel149;
    private widget.Label jLabel150;
    private widget.Label jLabel152;
    private widget.Label jLabel154;
    private widget.Label jLabel155;
    private widget.Label jLabel156;
    private widget.Label jLabel158;
    private widget.Label jLabel159;
    private widget.Label jLabel16;
    private widget.Label jLabel160;
    private widget.Label jLabel162;
    private widget.Label jLabel163;
    private widget.Label jLabel164;
    private widget.Label jLabel165;
    private widget.Label jLabel166;
    private widget.Label jLabel167;
    private widget.Label jLabel168;
    private widget.Label jLabel169;
    private widget.Label jLabel17;
    private widget.Label jLabel170;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel20;
    private widget.Label jLabel21;
    private widget.Label jLabel214;
    private widget.Label jLabel217;
    private widget.Label jLabel218;
    private widget.Label jLabel219;
    private widget.Label jLabel22;
    private widget.Label jLabel220;
    private widget.Label jLabel221;
    private widget.Label jLabel222;
    private widget.Label jLabel223;
    private widget.Label jLabel224;
    private widget.Label jLabel225;
    private widget.Label jLabel226;
    private widget.Label jLabel227;
    private widget.Label jLabel228;
    private widget.Label jLabel229;
    private widget.Label jLabel23;
    private widget.Label jLabel230;
    private widget.Label jLabel231;
    private widget.Label jLabel232;
    private widget.Label jLabel233;
    private widget.Label jLabel234;
    private widget.Label jLabel235;
    private widget.Label jLabel24;
    private widget.Label jLabel243;
    private widget.Label jLabel244;
    private widget.Label jLabel245;
    private widget.Label jLabel246;
    private widget.Label jLabel247;
    private widget.Label jLabel248;
    private widget.Label jLabel249;
    private widget.Label jLabel25;
    private widget.Label jLabel250;
    private widget.Label jLabel256;
    private widget.Label jLabel257;
    private widget.Label jLabel259;
    private widget.Label jLabel26;
    private widget.Label jLabel260;
    private widget.Label jLabel261;
    private widget.Label jLabel262;
    private widget.Label jLabel263;
    private widget.Label jLabel264;
    private widget.Label jLabel265;
    private widget.Label jLabel266;
    private widget.Label jLabel267;
    private widget.Label jLabel27;
    private widget.Label jLabel272;
    private widget.Label jLabel273;
    private widget.Label jLabel274;
    private widget.Label jLabel275;
    private widget.Label jLabel276;
    private widget.Label jLabel277;
    private widget.Label jLabel28;
    private widget.Label jLabel29;
    private widget.Label jLabel30;
    private widget.Label jLabel31;
    private widget.Label jLabel32;
    private widget.Label jLabel33;
    private widget.Label jLabel34;
    private widget.Label jLabel35;
    private widget.Label jLabel36;
    private widget.Label jLabel37;
    private widget.Label jLabel38;
    private widget.Label jLabel39;
    private widget.Label jLabel40;
    private widget.Label jLabel41;
    private widget.Label jLabel42;
    private widget.Label jLabel43;
    private widget.Label jLabel44;
    private widget.Label jLabel45;
    private widget.Label jLabel46;
    private widget.Label jLabel53;
    private widget.Label jLabel55;
    private widget.Label jLabel56;
    private widget.Label jLabel57;
    private widget.Label jLabel58;
    private widget.Label jLabel59;
    private widget.Label jLabel6;
    private widget.Label jLabel60;
    private widget.Label jLabel61;
    private widget.Label jLabel62;
    private widget.Label jLabel63;
    private widget.Label jLabel64;
    private widget.Label jLabel65;
    private widget.Label jLabel66;
    private widget.Label jLabel67;
    private widget.Label jLabel68;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private widget.Label jLabel83;
    private widget.Label jLabel86;
    private widget.Label jLabel87;
    private widget.Label jLabel88;
    private widget.Label jLabel89;
    private widget.Label jLabel9;
    private widget.Label jLabel94;
    private widget.Label jLabel95;
    private widget.Label jLabel96;
    private widget.Label jLabel97;
    private widget.Label jLabel99;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private widget.Label label11;
    private widget.Label label12;
    private widget.Label label13;
    private widget.Label label14;
    private widget.Label label29;
    private widget.Label label32;
    private widget.Label label33;
    private widget.PanelBiasa panelBiasa1;
    private widget.PanelBiasa panelBiasa2;
    private widget.PanelBiasa panelBiasa3;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ScrollPane scrollInput;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollPane2;
    private widget.ScrollPane scrollPane3;
    private widget.ScrollPane scrollPane4;
    private widget.ScrollPane scrollPane5;
    private widget.ScrollPane scrollPane6;
    private widget.ScrollPane scrollPane7;
    private widget.ScrollPane scrollPane9;
    private widget.Table tbDetailKebutuhanEdukasi;
    private widget.Table tbDetailRencanaEdukasi;
    private widget.Table tbImunisasi;
    private widget.Table tbImunisasi2;
    private widget.Table tbKebutuhanEdukasi;
    private widget.Table tbMasalahDetailMasalah;
    private widget.Table tbMasalahKeperawatan;
    private widget.Table tbObat;
    private widget.Table tbRencanaDetail;
    private widget.Table tbRencanaEdukasi;
    private widget.Table tbRencanaKeperawatan;
    // End of variables declaration//GEN-END:variables

    private void tampil() {
        Valid.tabelKosong(tabMode);
        try {
            if (TCari.getText().equals("")) {
                ps = koneksi.prepareStatement(
                        "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir,pasien.agama,bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,penilaian_awal_keperawatan_ralan_bayi.tanggal,"
                        + "penilaian_awal_keperawatan_ralan_bayi.informasi,penilaian_awal_keperawatan_ralan_bayi.td,penilaian_awal_keperawatan_ralan_bayi.nadi,penilaian_awal_keperawatan_ralan_bayi.rr,penilaian_awal_keperawatan_ralan_bayi.suhu,penilaian_awal_keperawatan_ralan_bayi.gcs,"
                        + "penilaian_awal_keperawatan_ralan_bayi.bb,penilaian_awal_keperawatan_ralan_bayi.tb,penilaian_awal_keperawatan_ralan_bayi.lp,penilaian_awal_keperawatan_ralan_bayi.lk,penilaian_awal_keperawatan_ralan_bayi.ld,penilaian_awal_keperawatan_ralan_bayi.keluhan_utama,"
                        + "penilaian_awal_keperawatan_ralan_bayi.rpd,penilaian_awal_keperawatan_ralan_bayi.rpk,penilaian_awal_keperawatan_ralan_bayi.rpo,penilaian_awal_keperawatan_ralan_bayi.alergi,penilaian_awal_keperawatan_ralan_bayi.anakke,penilaian_awal_keperawatan_ralan_bayi.darisaudara,"
                        + "penilaian_awal_keperawatan_ralan_bayi.caralahir,penilaian_awal_keperawatan_ralan_bayi.ket_caralahir,penilaian_awal_keperawatan_ralan_bayi.umurkelahiran,penilaian_awal_keperawatan_ralan_bayi.kelainanbawaan,penilaian_awal_keperawatan_ralan_bayi.ket_kelainan_bawaan,"
                        + "penilaian_awal_keperawatan_ralan_bayi.usiatengkurap,penilaian_awal_keperawatan_ralan_bayi.usiaduduk,penilaian_awal_keperawatan_ralan_bayi.usiaberdiri,penilaian_awal_keperawatan_ralan_bayi.usiagigipertama,penilaian_awal_keperawatan_ralan_bayi.usiaberjalan,"
                        + "penilaian_awal_keperawatan_ralan_bayi.usiabicara,penilaian_awal_keperawatan_ralan_bayi.usiamembaca,penilaian_awal_keperawatan_ralan_bayi.usiamenulis,penilaian_awal_keperawatan_ralan_bayi.gangguanemosi,penilaian_awal_keperawatan_ralan_bayi.alat_bantu,"
                        + "penilaian_awal_keperawatan_ralan_bayi.ket_bantu,penilaian_awal_keperawatan_ralan_bayi.prothesa,penilaian_awal_keperawatan_ralan_bayi.ket_pro,penilaian_awal_keperawatan_ralan_bayi.adl,penilaian_awal_keperawatan_ralan_bayi.status_psiko,"
                        + "penilaian_awal_keperawatan_ralan_bayi.ket_psiko,penilaian_awal_keperawatan_ralan_bayi.hub_keluarga,penilaian_awal_keperawatan_ralan_bayi.pengasuh,penilaian_awal_keperawatan_ralan_bayi.ket_pengasuh,penilaian_awal_keperawatan_ralan_bayi.ekonomi,"
                        + "penilaian_awal_keperawatan_ralan_bayi.budaya,penilaian_awal_keperawatan_ralan_bayi.ket_budaya,penilaian_awal_keperawatan_ralan_bayi.edukasi,penilaian_awal_keperawatan_ralan_bayi.ket_edukasi,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_skala1,"
                        + "penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_nilai1,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_skala2,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_nilai2,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_skala3,"
                        + "penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_nilai3,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_skala4,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_nilai4,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_skala5,"
                        + "penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_nilai5,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_skala6,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_nilai6,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_skala7,"
                        + "penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_nilai7,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_totalnilai,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_hasil,penilaian_awal_keperawatan_ralan_bayi.kemampuan_baca_tulis,"
                        + "penilaian_awal_keperawatan_ralan_bayi.butuh_penerjemah,penilaian_awal_keperawatan_ralan_bayi.keterangan_butuh_penerjemah,penilaian_awal_keperawatan_ralan_bayi.terdapat_hambatan_belajar,penilaian_awal_keperawatan_ralan_bayi.hambatan_belajar,penilaian_awal_keperawatan_ralan_bayi.keterangan_hambatan_belajar,"
                        + "penilaian_awal_keperawatan_ralan_bayi.hambatan_cara_bicara,penilaian_awal_keperawatan_ralan_bayi.hambatan_bahasa_isyarat,penilaian_awal_keperawatan_ralan_bayi.cara_belajar_disukai,penilaian_awal_keperawatan_ralan_bayi.kesediaan_menerima_informasi,penilaian_awal_keperawatan_ralan_bayi.ket_kesediaan_menerima_informasi,"
                        + "penilaian_awal_keperawatan_ralan_bayi.pemahaman_nutrisi,penilaian_awal_keperawatan_ralan_bayi.pemahaman_penyakit,penilaian_awal_keperawatan_ralan_bayi.pemahaman_pengobatan,penilaian_awal_keperawatan_ralan_bayi.pemahaman_perawatan,penilaian_awal_keperawatan_ralan_bayi.keyakinan_nilai,"
                        + "penilaian_awal_keperawatan_ralan_bayi.keterbatasan_fisik,penilaian_awal_keperawatan_ralan_bayi.hambatan_emosional,penilaian_awal_keperawatan_ralan_bayi.Motivasi,"
                        + "penilaian_awal_keperawatan_ralan_bayi.sg1,penilaian_awal_keperawatan_ralan_bayi.nilai1,penilaian_awal_keperawatan_ralan_bayi.sg2,penilaian_awal_keperawatan_ralan_bayi.nilai2,penilaian_awal_keperawatan_ralan_bayi.sg3,penilaian_awal_keperawatan_ralan_bayi.nilai3,"
                        + "penilaian_awal_keperawatan_ralan_bayi.sg4,penilaian_awal_keperawatan_ralan_bayi.nilai4,penilaian_awal_keperawatan_ralan_bayi.total_hasil,penilaian_awal_keperawatan_ralan_bayi.wajah,penilaian_awal_keperawatan_ralan_bayi.nilaiwajah,penilaian_awal_keperawatan_ralan_bayi.kaki,"
                        + "penilaian_awal_keperawatan_ralan_bayi.nilaikaki,penilaian_awal_keperawatan_ralan_bayi.aktifitas,penilaian_awal_keperawatan_ralan_bayi.nilaiaktifitas,penilaian_awal_keperawatan_ralan_bayi.menangis,penilaian_awal_keperawatan_ralan_bayi.nilaimenangis,"
                        + "penilaian_awal_keperawatan_ralan_bayi.bersuara,penilaian_awal_keperawatan_ralan_bayi.nilaibersuara,penilaian_awal_keperawatan_ralan_bayi.hasilnyeri,penilaian_awal_keperawatan_ralan_bayi.nyeri,penilaian_awal_keperawatan_ralan_bayi.lokasi,"
                        + "penilaian_awal_keperawatan_ralan_bayi.durasi,penilaian_awal_keperawatan_ralan_bayi.frekuensi,penilaian_awal_keperawatan_ralan_bayi.nyeri_hilang,penilaian_awal_keperawatan_ralan_bayi.ket_nyeri,penilaian_awal_keperawatan_ralan_bayi.pada_dokter,"
                        + "penilaian_awal_keperawatan_ralan_bayi.ket_dokter,penilaian_awal_keperawatan_ralan_bayi.informasi_perencanaan_pulang,penilaian_awal_keperawatan_ralan_bayi.lama_ratarata,"
                        + "penilaian_awal_keperawatan_ralan_bayi.tanggal_pulang,penilaian_awal_keperawatan_ralan_bayi.kondisi_saat_pulang,penilaian_awal_keperawatan_ralan_bayi.perawatan_lanjutan,penilaian_awal_keperawatan_ralan_bayi.cara_transportasi,penilaian_awal_keperawatan_ralan_bayi.transportasi_digunakan,penilaian_awal_keperawatan_ralan_bayi.rencana,penilaian_awal_keperawatan_ralan_bayi.nip,petugas.nama "
                        + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                        + "inner join penilaian_awal_keperawatan_ralan_bayi on reg_periksa.no_rawat=penilaian_awal_keperawatan_ralan_bayi.no_rawat "
                        + "inner join petugas on penilaian_awal_keperawatan_ralan_bayi.nip=petugas.nip "
                        + "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "
                        + "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik where "
                        + "penilaian_awal_keperawatan_ralan_bayi.tanggal between ? and ? order by penilaian_awal_keperawatan_ralan_bayi.tanggal");
            } else {
                ps = koneksi.prepareStatement(
                        "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir,pasien.agama,bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,penilaian_awal_keperawatan_ralan_bayi.tanggal,"
                        + "penilaian_awal_keperawatan_ralan_bayi.informasi,penilaian_awal_keperawatan_ralan_bayi.td,penilaian_awal_keperawatan_ralan_bayi.nadi,penilaian_awal_keperawatan_ralan_bayi.rr,penilaian_awal_keperawatan_ralan_bayi.suhu,penilaian_awal_keperawatan_ralan_bayi.gcs,"
                        + "penilaian_awal_keperawatan_ralan_bayi.bb,penilaian_awal_keperawatan_ralan_bayi.tb,penilaian_awal_keperawatan_ralan_bayi.lp,penilaian_awal_keperawatan_ralan_bayi.lk,penilaian_awal_keperawatan_ralan_bayi.ld,penilaian_awal_keperawatan_ralan_bayi.keluhan_utama,"
                        + "penilaian_awal_keperawatan_ralan_bayi.rpd,penilaian_awal_keperawatan_ralan_bayi.rpk,penilaian_awal_keperawatan_ralan_bayi.rpo,penilaian_awal_keperawatan_ralan_bayi.alergi,penilaian_awal_keperawatan_ralan_bayi.anakke,penilaian_awal_keperawatan_ralan_bayi.darisaudara,"
                        + "penilaian_awal_keperawatan_ralan_bayi.caralahir,penilaian_awal_keperawatan_ralan_bayi.ket_caralahir,penilaian_awal_keperawatan_ralan_bayi.umurkelahiran,penilaian_awal_keperawatan_ralan_bayi.kelainanbawaan,penilaian_awal_keperawatan_ralan_bayi.ket_kelainan_bawaan,"
                        + "penilaian_awal_keperawatan_ralan_bayi.usiatengkurap,penilaian_awal_keperawatan_ralan_bayi.usiaduduk,penilaian_awal_keperawatan_ralan_bayi.usiaberdiri,penilaian_awal_keperawatan_ralan_bayi.usiagigipertama,penilaian_awal_keperawatan_ralan_bayi.usiaberjalan,"
                        + "penilaian_awal_keperawatan_ralan_bayi.usiabicara,penilaian_awal_keperawatan_ralan_bayi.usiamembaca,penilaian_awal_keperawatan_ralan_bayi.usiamenulis,penilaian_awal_keperawatan_ralan_bayi.gangguanemosi,penilaian_awal_keperawatan_ralan_bayi.alat_bantu,"
                        + "penilaian_awal_keperawatan_ralan_bayi.ket_bantu,penilaian_awal_keperawatan_ralan_bayi.prothesa,penilaian_awal_keperawatan_ralan_bayi.ket_pro,penilaian_awal_keperawatan_ralan_bayi.adl,penilaian_awal_keperawatan_ralan_bayi.status_psiko,"
                        + "penilaian_awal_keperawatan_ralan_bayi.ket_psiko,penilaian_awal_keperawatan_ralan_bayi.hub_keluarga,penilaian_awal_keperawatan_ralan_bayi.pengasuh,penilaian_awal_keperawatan_ralan_bayi.ket_pengasuh,penilaian_awal_keperawatan_ralan_bayi.ekonomi,"
                        + "penilaian_awal_keperawatan_ralan_bayi.budaya,penilaian_awal_keperawatan_ralan_bayi.ket_budaya,penilaian_awal_keperawatan_ralan_bayi.edukasi,penilaian_awal_keperawatan_ralan_bayi.ket_edukasi,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_skala1,"
                        + "penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_nilai1,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_skala2,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_nilai2,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_skala3,"
                        + "penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_nilai3,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_skala4,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_nilai4,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_skala5,"
                        + "penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_nilai5,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_skala6,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_nilai6,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_skala7,"
                        + "penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_nilai7,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_totalnilai,penilaian_awal_keperawatan_ralan_bayi.penilaian_humptydumpty_hasil,penilaian_awal_keperawatan_ralan_bayi.kemampuan_baca_tulis,"
                        + "penilaian_awal_keperawatan_ralan_bayi.butuh_penerjemah,penilaian_awal_keperawatan_ralan_bayi.keterangan_butuh_penerjemah,penilaian_awal_keperawatan_ralan_bayi.terdapat_hambatan_belajar,penilaian_awal_keperawatan_ralan_bayi.hambatan_belajar,penilaian_awal_keperawatan_ralan_bayi.keterangan_hambatan_belajar,"
                        + "penilaian_awal_keperawatan_ralan_bayi.hambatan_cara_bicara,penilaian_awal_keperawatan_ralan_bayi.hambatan_bahasa_isyarat,penilaian_awal_keperawatan_ralan_bayi.cara_belajar_disukai,penilaian_awal_keperawatan_ralan_bayi.kesediaan_menerima_informasi,penilaian_awal_keperawatan_ralan_bayi.ket_kesediaan_menerima_informasi,"
                        + "penilaian_awal_keperawatan_ralan_bayi.pemahaman_nutrisi,penilaian_awal_keperawatan_ralan_bayi.pemahaman_penyakit,penilaian_awal_keperawatan_ralan_bayi.pemahaman_pengobatan,penilaian_awal_keperawatan_ralan_bayi.pemahaman_perawatan,penilaian_awal_keperawatan_ralan_bayi.keyakinan_nilai,"
                        + "penilaian_awal_keperawatan_ralan_bayi.keterbatasan_fisik,penilaian_awal_keperawatan_ralan_bayi.hambatan_emosional,penilaian_awal_keperawatan_ralan_bayi.Motivasi,"
                        + "penilaian_awal_keperawatan_ralan_bayi.sg1,penilaian_awal_keperawatan_ralan_bayi.nilai1,penilaian_awal_keperawatan_ralan_bayi.sg2,penilaian_awal_keperawatan_ralan_bayi.nilai2,penilaian_awal_keperawatan_ralan_bayi.sg3,penilaian_awal_keperawatan_ralan_bayi.nilai3,"
                        + "penilaian_awal_keperawatan_ralan_bayi.sg4,penilaian_awal_keperawatan_ralan_bayi.nilai4,penilaian_awal_keperawatan_ralan_bayi.total_hasil,penilaian_awal_keperawatan_ralan_bayi.wajah,penilaian_awal_keperawatan_ralan_bayi.nilaiwajah,penilaian_awal_keperawatan_ralan_bayi.kaki,"
                        + "penilaian_awal_keperawatan_ralan_bayi.nilaikaki,penilaian_awal_keperawatan_ralan_bayi.aktifitas,penilaian_awal_keperawatan_ralan_bayi.nilaiaktifitas,penilaian_awal_keperawatan_ralan_bayi.menangis,penilaian_awal_keperawatan_ralan_bayi.nilaimenangis,"
                        + "penilaian_awal_keperawatan_ralan_bayi.bersuara,penilaian_awal_keperawatan_ralan_bayi.nilaibersuara,penilaian_awal_keperawatan_ralan_bayi.hasilnyeri,penilaian_awal_keperawatan_ralan_bayi.nyeri,penilaian_awal_keperawatan_ralan_bayi.lokasi,"
                        + "penilaian_awal_keperawatan_ralan_bayi.durasi,penilaian_awal_keperawatan_ralan_bayi.frekuensi,penilaian_awal_keperawatan_ralan_bayi.nyeri_hilang,penilaian_awal_keperawatan_ralan_bayi.ket_nyeri,penilaian_awal_keperawatan_ralan_bayi.pada_dokter,"
                        + "penilaian_awal_keperawatan_ralan_bayi.ket_dokter,penilaian_awal_keperawatan_ralan_bayi.informasi_perencanaan_pulang,penilaian_awal_keperawatan_ralan_bayi.lama_ratarata,"
                        + "penilaian_awal_keperawatan_ralan_bayi.tanggal_pulang,penilaian_awal_keperawatan_ralan_bayi.kondisi_saat_pulang,penilaian_awal_keperawatan_ralan_bayi.perawatan_lanjutan,penilaian_awal_keperawatan_ralan_bayi.cara_transportasi,penilaian_awal_keperawatan_ralan_bayi.transportasi_digunakan,penilaian_awal_keperawatan_ralan_bayi.rencana,penilaian_awal_keperawatan_ralan_bayi.nip,petugas.nama "
                        + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                        + "inner join penilaian_awal_keperawatan_ralan_bayi on reg_periksa.no_rawat=penilaian_awal_keperawatan_ralan_bayi.no_rawat "
                        + "inner join petugas on penilaian_awal_keperawatan_ralan_bayi.nip=petugas.nip "
                        + "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "
                        + "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik where "
                        + "penilaian_awal_keperawatan_ralan_bayi.tanggal between ? and ? and "
                        + "(reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or "
                        + "penilaian_awal_keperawatan_ralan_bayi.nip like ? or petugas.nama like ?) "
                        + "order by penilaian_awal_keperawatan_ralan_bayi.tanggal");
            }

            try {
                if (TCari.getText().equals("")) {
                    ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00");
                    ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59");
                } else {
                    ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00");
                    ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59");
                    ps.setString(3, "%" + TCari.getText() + "%");
                    ps.setString(4, "%" + TCari.getText() + "%");
                    ps.setString(5, "%" + TCari.getText() + "%");
                    ps.setString(6, "%" + TCari.getText() + "%");
                    ps.setString(7, "%" + TCari.getText() + "%");
                }
                rs = ps.executeQuery();
                while (rs.next()) {
                    tabMode.addRow(new Object[]{
                        rs.getString("no_rawat"), rs.getString("no_rkm_medis"), rs.getString("nm_pasien"), rs.getString("jk"), rs.getString("agama"), rs.getString("nama_bahasa"), rs.getString("nama_cacat"),
                        rs.getString("tgl_lahir"), rs.getString("tanggal"), rs.getString("informasi"), rs.getString("td"), rs.getString("nadi"), rs.getString("rr"), rs.getString("suhu"), rs.getString("gcs"),
                        rs.getString("bb"), rs.getString("tb"), rs.getString("lp"), rs.getString("lk"), rs.getString("ld"), rs.getString("keluhan_utama"), rs.getString("rpd"), rs.getString("rpk"),
                        rs.getString("rpo"), rs.getString("alergi"), rs.getString("anakke"), rs.getString("darisaudara"), rs.getString("caralahir"), rs.getString("ket_caralahir"), rs.getString("umurkelahiran"),
                        rs.getString("kelainanbawaan"), rs.getString("ket_kelainan_bawaan"), rs.getString("usiatengkurap"), rs.getString("usiaduduk"), rs.getString("usiaberdiri"), rs.getString("usiagigipertama"),
                        rs.getString("usiaberjalan"), rs.getString("usiabicara"), rs.getString("usiamembaca"), rs.getString("usiamenulis"), rs.getString("gangguanemosi"), rs.getString("alat_bantu"),
                        rs.getString("ket_bantu"), rs.getString("prothesa"), rs.getString("ket_pro"), rs.getString("adl"), rs.getString("status_psiko"), rs.getString("ket_psiko"), rs.getString("hub_keluarga"),
                        rs.getString("pengasuh"), rs.getString("ket_pengasuh"), rs.getString("ekonomi"), rs.getString("budaya"), rs.getString("ket_budaya"), rs.getString("edukasi"), rs.getString("ket_edukasi"),
                        rs.getString("kemampuan_baca_tulis"), rs.getString("butuh_penerjemah"), rs.getString("keterangan_butuh_penerjemah"), rs.getString("terdapat_hambatan_belajar"), rs.getString("hambatan_belajar"),
                        rs.getString("keterangan_hambatan_belajar"), rs.getString("hambatan_cara_bicara"), rs.getString("hambatan_bahasa_isyarat"), rs.getString("cara_belajar_disukai"), rs.getString("kesediaan_menerima_informasi"),
                        rs.getString("ket_kesediaan_menerima_informasi"), rs.getString("pemahaman_nutrisi"), rs.getString("pemahaman_penyakit"), rs.getString("pemahaman_pengobatan"), rs.getString("pemahaman_perawatan"),
                        rs.getString("keyakinan_nilai"), rs.getString("keterbatasan_fisik"), rs.getString("hambatan_emosional"), rs.getString("Motivasi"),
                        rs.getString("penilaian_humptydumpty_skala1"), rs.getString("penilaian_humptydumpty_nilai1"), rs.getString("penilaian_humptydumpty_skala2"), rs.getString("penilaian_humptydumpty_nilai2"),
                        rs.getString("penilaian_humptydumpty_skala3"), rs.getString("penilaian_humptydumpty_nilai3"), rs.getString("penilaian_humptydumpty_skala4"), rs.getString("penilaian_humptydumpty_nilai4"),
                        rs.getString("penilaian_humptydumpty_skala5"), rs.getString("penilaian_humptydumpty_nilai5"), rs.getString("penilaian_humptydumpty_skala6"), rs.getString("penilaian_humptydumpty_nilai6"),
                        rs.getString("penilaian_humptydumpty_skala7"), rs.getString("penilaian_humptydumpty_nilai7"), rs.getString("penilaian_humptydumpty_totalnilai"), rs.getString("penilaian_humptydumpty_hasil"), rs.getString("sg1"),
                        rs.getString("nilai1"), rs.getString("sg2"), rs.getString("nilai2"), rs.getString("sg3"), rs.getString("nilai3"), rs.getString("sg4"), rs.getString("nilai4"), rs.getString("total_hasil"),
                        rs.getString("wajah"), rs.getString("nilaiwajah"), rs.getString("kaki"), rs.getString("nilaikaki"), rs.getString("aktifitas"), rs.getString("nilaiaktifitas"), rs.getString("menangis"),
                        rs.getString("nilaimenangis"), rs.getString("bersuara"), rs.getString("nilaibersuara"), rs.getString("hasilnyeri"), rs.getString("nyeri"), rs.getString("lokasi"), rs.getString("durasi"),
                        rs.getString("frekuensi"), rs.getString("nyeri_hilang"), rs.getString("ket_nyeri"), rs.getString("pada_dokter"), rs.getString("ket_dokter"), rs.getString("informasi_perencanaan_pulang"),
                        rs.getString("lama_ratarata"), rs.getString("tanggal_pulang"), rs.getString("kondisi_saat_pulang"), rs.getString("perawatan_lanjutan"), rs.getString("cara_transportasi"), rs.getString("transportasi_digunakan"),
                        rs.getString("rencana"), rs.getString("nip"), rs.getString("nama")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
        LCount.setText("" + tabMode.getRowCount());
    }

    public void emptTeks() {
        TglAsuhan.setDate(new Date());
        Informasi.setSelectedIndex(0);
        TD.setText("");
        Nadi.setText("");
        RR.setText("");
        Suhu.setText("");
        GCS.setText("");
        BB.setText("");
        TB.setText("");
        LP.setText("");
        LK.setText("");
        LD.setText("");
        KeluhanUtama.setText("");
        RPD.setText("");
        RPK.setText("");
        RPO.setText("");
        Alergi.setText("");
        Anakke.setText("");
        DariSaudara.setText("");
        CaraKelahiran.setSelectedIndex(0);
        KetCaraKelahiran.setText("");
        UmurKelahiran.setSelectedIndex(0);
        KelainanBawaan.setSelectedIndex(0);
        KetKelainanBawaan.setText("");
        UsiaTengkurap.setText("");
        UsiaDuduk.setText("");
        UsiaBerdiri.setText("");
        UsiaGigi.setText("");
        UsiaBerjalan.setText("");
        UsiaBicara.setText("");
        UsiaMembaca.setText("");
        UsiaMenulis.setText("");
        GangguanEmosi.setText("");
        AlatBantu.setSelectedIndex(0);
        KetBantu.setText("");
        Prothesa.setSelectedIndex(0);
        KetProthesa.setText("");
        ADL.setSelectedIndex(0);
        StatusPsiko.setSelectedIndex(0);
        KetPsiko.setText("");
        HubunganKeluarga.setSelectedIndex(0);
        Pengasuh.setSelectedIndex(0);
        KetPengasuh.setText("");
        Ekonomi.setSelectedIndex(0);
        StatusBudaya.setSelectedIndex(0);
        KetBudaya.setText("");
        Edukasi.setSelectedIndex(0);
        KetEdukasi.setText("");
        SG1.setSelectedIndex(0);
        NilaiGizi1.setText("0");
        SG2.setSelectedIndex(0);
        NilaiGizi2.setText("0");
        SG3.setSelectedIndex(0);
        NilaiGizi3.setText("0");
        SG4.setSelectedIndex(0);
        NilaiGizi4.setText("0");
        TotalNilaiGizi.setText("0");
        SkalaWajah.setSelectedIndex(0);
        NilaiWajah.setText("0");
        SkalaKaki.setSelectedIndex(0);
        NilaiKaki.setText("0");
        SkalaAktifitas.setSelectedIndex(0);
        NilaiAktifitas.setText("0");
        SkalaMenangis.setSelectedIndex(0);
        NilaiMenangis.setText("0");
        SkalaBersuara.setSelectedIndex(0);
        NilaiBersuara.setText("0");
        SkalaNyeri.setText("0");
        Nyeri.setSelectedIndex(0);
        Lokasi.setText("");
        Durasi.setText("");
        Frekuensi.setText("");
        NyeriHilang.setSelectedIndex(0);
        KetNyeri.setText("");
        PadaDokter.setSelectedIndex(0);
        KetDokter.setText("");
        Rencana.setText("");
        for (i = 0; i < tabModeMasalah.getRowCount(); i++) {
            tabModeMasalah.setValueAt(false, i, 0);
        }
        Valid.tabelKosong(tabModeRencana);
        TabRawat.setSelectedIndex(0);
        Informasi.requestFocus();
    }

    private void getData() {
        if (tbObat.getSelectedRow() != -1) {
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 1).toString());
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 2).toString());
            Jk.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 3).toString());
            Agama.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 4).toString());
            Bahasa.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 5).toString());
            CacatFisik.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 6).toString());
            TglLahir.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 7).toString());
            Informasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 9).toString());
            TD.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 10).toString());
            Nadi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 11).toString());
            RR.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 12).toString());
            Suhu.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 13).toString());
            GCS.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 14).toString());
            BB.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 15).toString());
            TB.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 16).toString());
            LP.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 17).toString());
            LK.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 18).toString());
            LD.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 19).toString());
            KeluhanUtama.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 20).toString());
            RPD.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 21).toString());
            RPK.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 22).toString());
            RPO.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 23).toString());
            Alergi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 24).toString());
            Anakke.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 25).toString());
            DariSaudara.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 26).toString());
            CaraKelahiran.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 27).toString());
            KetCaraKelahiran.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 28).toString());
            UmurKelahiran.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 29).toString());
            KelainanBawaan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 30).toString());
            KetKelainanBawaan.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 31).toString());
            UsiaTengkurap.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 32).toString());
            UsiaDuduk.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 33).toString());
            UsiaBerdiri.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 34).toString());
            UsiaGigi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 35).toString());
            UsiaBerjalan.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 36).toString());
            UsiaBicara.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 37).toString());
            UsiaMembaca.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 38).toString());
            UsiaMenulis.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 39).toString());
            GangguanEmosi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 40).toString());
            AlatBantu.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 41).toString());
            KetBantu.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 42).toString());
            Prothesa.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 43).toString());
            KetProthesa.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 44).toString());
            ADL.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 45).toString());
            StatusPsiko.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 46).toString());
            KetPsiko.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 47).toString());
            HubunganKeluarga.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 48).toString());
            Pengasuh.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 49).toString());
            KetPengasuh.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 50).toString());
            Ekonomi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 51).toString());
            StatusBudaya.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 52).toString());
            KetBudaya.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 53).toString());
            Edukasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 54).toString());
            KetEdukasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 55).toString());
            KemampuanBacaTulis.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 56).toString());
            ButuhPenerjemah.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 57).toString());
            KeteranganButuhPenerjemah.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 58).toString());
            TerdapatHambatanBelajar.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 59).toString());
            HambatanBelajar.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 60).toString());
            KeteranganHambatanBelajar.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 61).toString());
            HambatanCaraBicara.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 62).toString());
            HambatanBahasaIsyarat.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 63).toString());
            CaraBelajarDisukai.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 64).toString());
            KesediaanMenerimaInformasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 65).toString());
            KeteranganKesediaanMenerimaInformasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 66).toString());
            PemahamanNutrisi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 67).toString());
            PemahamanPenyakit.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 68).toString());
            PemahamanPengobatan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 69).toString());
            PemahamanPerawatan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 70).toString());
            KeyakinanNilai.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 71).toString());
            KeterbatasanFisik.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 72).toString());
            HambatanEmosional.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 73).toString());
            Motivasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 74).toString());
            SkalaResiko1.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 75).toString());
            NilaiResiko1.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 76).toString());
            SkalaResiko2.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 77).toString());
            NilaiResiko2.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 78).toString());
            SkalaResiko3.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 79).toString());
            NilaiResiko3.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 80).toString());
            SkalaResiko4.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 81).toString());
            NilaiResiko4.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 82).toString());
            SkalaResiko5.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 83).toString());
            NilaiResiko5.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 84).toString());
            SkalaResiko6.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 85).toString());
            NilaiResiko6.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 86).toString());
            SkalaResiko7.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 87).toString());
            NilaiResiko7.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 88).toString());
            NilaiResikoTotal.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 89).toString());
            KeteranganTingkatRisiko.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 90).toString());
            SG1.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 91).toString());
            NilaiGizi1.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 92).toString());
            SG2.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 93).toString());
            NilaiGizi2.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 94).toString());
            SG3.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 95).toString());
            NilaiGizi3.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 96).toString());
            SG4.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 97).toString());
            NilaiGizi4.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 98).toString());
            TotalNilaiGizi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 99).toString());
            SkalaWajah.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 100).toString());
            NilaiWajah.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 101).toString());
            SkalaKaki.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 102).toString());
            NilaiKaki.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 103).toString());
            SkalaAktifitas.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 104).toString());
            NilaiAktifitas.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 105).toString());
            SkalaMenangis.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 106).toString());
            NilaiMenangis.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 107).toString());
            SkalaBersuara.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 108).toString());
            NilaiBersuara.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 109).toString());
            SkalaNyeri.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 110).toString());
            Nyeri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 111).toString());
            Lokasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 112).toString());
            Durasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 113).toString());
            Frekuensi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 114).toString());
            NyeriHilang.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 115).toString());
            KetNyeri.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 116).toString());
            PadaDokter.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 117).toString());
            KetDokter.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 118).toString());
            InformasiPerencanaanPulang.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 119).toString());
            LamaRatarata.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 120).toString());
            Valid.SetTgl2(TanggalPulang, tbObat.getValueAt(tbObat.getSelectedRow(), 121).toString());
            KondisiPulang.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 122).toString());
            PerawatanLanjutan.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 123).toString());
            CaraTransportasiPulang.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 124).toString());
            TransportasiYangDigunakan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 125).toString());
            Rencana.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 126).toString());
            Valid.tabelKosong(tabModeMasalah);
            Valid.tabelKosong(tabModeRencana);
            Valid.tabelKosong(tabModeImunisasi);
            Valid.tabelKosong(tabModeKebutuhanEdukasi);
            Valid.tabelKosong(tabModeRencanaEdukasi);
            for (i = 0; i < tbMasalahDetailMasalah.getRowCount(); i++) {
                tabModeMasalah.addRow(new Object[]{
                    true, tbMasalahDetailMasalah.getValueAt(i, 0).toString(), tbMasalahDetailMasalah.getValueAt(i, 1).toString()
                });
            }
            for (i = 0; i < tbRencanaDetail.getRowCount(); i++) {
                tabModeRencana.addRow(new Object[]{
                    true, tbRencanaDetail.getValueAt(i, 0).toString(), tbRencanaDetail.getValueAt(i, 1).toString()
                });
            }
            for (i = 0; i < tbImunisasi2.getRowCount(); i++) {
                tabModeImunisasi.addRow(new Object[]{
                    tbImunisasi2.getValueAt(i, 0).toString(), tbImunisasi2.getValueAt(i, 1).toString(), tbImunisasi2.getValueAt(i, 2),
                    tbImunisasi2.getValueAt(i, 3), tbImunisasi2.getValueAt(i, 4), tbImunisasi2.getValueAt(i, 5),
                    tbImunisasi2.getValueAt(i, 6), tbImunisasi2.getValueAt(i, 7)
                });
            }
            for (i = 0; i < tbDetailKebutuhanEdukasi.getRowCount(); i++) {
                tabModeKebutuhanEdukasi.addRow(new Object[]{
                    true, tbDetailKebutuhanEdukasi.getValueAt(i, 0).toString(), tbDetailKebutuhanEdukasi.getValueAt(i, 1).toString()
                });
            }
            for (i = 0; i < tbDetailRencanaEdukasi.getRowCount(); i++) {
                tabModeRencanaEdukasi.addRow(new Object[]{
                    true, tbDetailRencanaEdukasi.getValueAt(i, 0).toString(), tbDetailRencanaEdukasi.getValueAt(i, 1).toString()
                });
            }
            Valid.SetTgl2(TglAsuhan, tbObat.getValueAt(tbObat.getSelectedRow(), 8).toString());
        }
    }

    private void isRawat() {
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rkm_medis,pasien.nm_pasien, if(pasien.jk='L','Laki-Laki','Perempuan') as jk,"
                    + "pasien.tgl_lahir,pasien.agama,bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,reg_periksa.tgl_registrasi,"
                    + "reg_periksa.jam_reg from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                    + "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "
                    + "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik "
                    + "where reg_periksa.no_rawat=?");
            try {
                ps.setString(1, TNoRw.getText());
                rs = ps.executeQuery();
                if (rs.next()) {
                    TNoRM.setText(rs.getString("no_rkm_medis"));
                    TPasien.setText(rs.getString("nm_pasien"));
                    DTPCari1.setDate(rs.getDate("tgl_registrasi"));
                    Jk.setText(rs.getString("jk"));
                    TglLahir.setText(rs.getString("tgl_lahir"));
                    Agama.setText(rs.getString("agama"));
                    Bahasa.setText(rs.getString("nama_bahasa"));
                    CacatFisik.setText(rs.getString("nama_cacat"));
                    TanggalRegistrasi.setText(rs.getString("tgl_registrasi") + " " + rs.getString("jam_reg"));
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
            runBackground(() -> tampilImunisasi());
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
    }

    public void setNoRm(String norwt, Date tgl2) {
        TNoRw.setText(norwt);
        TCari.setText(norwt);
        DTPCari2.setDate(tgl2);
        isRawat();
    }

    public void isCek() {
        BtnSimpan.setEnabled(akses.getpenilaian_awal_keperawatan_anak());
        BtnHapus.setEnabled(akses.getpenilaian_awal_keperawatan_anak());
        BtnEdit.setEnabled(akses.getpenilaian_awal_keperawatan_anak());
        BtnTambahMasalah.setEnabled(akses.getmaster_masalah_keperawatan_anak());
        BtnTambahRencana.setEnabled(akses.getmaster_rencana_keperawatan_anak());
        if (akses.getjml2() >= 1) {
            KdPetugas.setEditable(false);
            BtnPetugas.setEnabled(false);
            KdPetugas.setText(akses.getkode());
            NmPetugas.setText(Sequel.CariPetugas(KdPetugas.getText()));
            if (NmPetugas.getText().equals("")) {
                KdPetugas.setText("");
                JOptionPane.showMessageDialog(null, "User login bukan petugas...!!");
            }
        }

        if (TANGGALMUNDUR.equals("no")) {
            if (!akses.getkode().equals("Admin Utama")) {
                TglAsuhan.setEditable(false);
                TglAsuhan.setEnabled(false);
            }
        }
    }

    public void setTampil() {
        TabRawat.setSelectedIndex(1);
    }

    private void tampilMasalah() {
        try {
            Valid.tabelKosong(tabModeMasalah);
            file = new File("./cache/masalahkeperawatanbayi.iyem");
            file.createNewFile();
            fileWriter = new FileWriter(file);
            StringBuilder iyembuilder = new StringBuilder();
            ps = koneksi.prepareStatement("select * from master_masalah_keperawatan_anak order by master_masalah_keperawatan_anak.kode_masalah");
            try {
                rs = ps.executeQuery();
                while (rs.next()) {
                    tabModeMasalah.addRow(new Object[]{false, rs.getString(1), rs.getString(2)});
                    iyembuilder.append("{\"KodeMasalah\":\"").append(rs.getString(1)).append("\",\"NamaMasalah\":\"").append(rs.getString(2)).append("\"},");
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
            if (iyembuilder.length() > 0) {
                iyembuilder.setLength(iyembuilder.length() - 1);
                fileWriter.write("{\"masalahkeperawatanbayi\":[" + iyembuilder + "]}");
                fileWriter.flush();
            }

            fileWriter.close();
            iyembuilder = null;
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void tampilMasalah2() {
        try {
            jml = 0;
            for (i = 0; i < tbMasalahKeperawatan.getRowCount(); i++) {
                if (tbMasalahKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                    jml++;
                }
            }

            pilih = new boolean[jml];
            kode = new String[jml];
            masalah = new String[jml];

            index = 0;
            for (i = 0; i < tbMasalahKeperawatan.getRowCount(); i++) {
                if (tbMasalahKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                    pilih[index] = true;
                    kode[index] = tbMasalahKeperawatan.getValueAt(i, 1).toString();
                    masalah[index] = tbMasalahKeperawatan.getValueAt(i, 2).toString();
                    index++;
                }
            }

            Valid.tabelKosong(tabModeMasalah);

            for (i = 0; i < jml; i++) {
                tabModeMasalah.addRow(new Object[]{
                    pilih[i], kode[i], masalah[i]
                });
            }

            pilih = null;
            kode = null;
            masalah = null;

            myObj = new FileReader("./cache/masalahkeperawatanbayi.iyem");
            root = mapper.readTree(myObj);
            response = root.path("masalahkeperawatanbayi");
            if (response.isArray()) {
                for (JsonNode list : response) {
                    if (list.path("KodeMasalah").asText().toLowerCase().contains(TCariMasalah.getText().toLowerCase()) || list.path("NamaMasalah").asText().toLowerCase().contains(TCariMasalah.getText().toLowerCase())) {
                        tabModeMasalah.addRow(new Object[]{
                            false, list.path("KodeMasalah").asText(), list.path("NamaMasalah").asText()
                        });
                    }
                }
            }
            myObj.close();
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void tampilRencana() {
        try {
            file = new File("./cache/rencanakeperawatanbayi.iyem");
            file.createNewFile();
            fileWriter = new FileWriter(file);
            StringBuilder iyembuilder = new StringBuilder();
            ps = koneksi.prepareStatement("select * from master_rencana_keperawatan_anak order by master_rencana_keperawatan_anak.kode_rencana");
            try {
                rs = ps.executeQuery();
                while (rs.next()) {
                    iyembuilder.append("{\"KodeMasalah\":\"" + rs.getString(1) + "\",\"KodeRencana\":\"" + rs.getString(2) + "\",\"NamaRencana\":\"" + rs.getString(3) + "\"},");
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

            if (iyembuilder.length() > 0) {
                iyembuilder.setLength(iyembuilder.length() - 1);
                fileWriter.write("{\"rencanakeperawatanbayi\":[" + iyembuilder + "]}");
                fileWriter.flush();
            }

            fileWriter.close();
            iyembuilder = null;
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void tampilRencana2() {
        try {
            jml = 0;
            for (i = 0; i < tbRencanaKeperawatan.getRowCount(); i++) {
                if (tbRencanaKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                    jml++;
                }
            }

            pilih = new boolean[jml];
            kode = new String[jml];
            masalah = new String[jml];

            index = 0;
            for (i = 0; i < tbRencanaKeperawatan.getRowCount(); i++) {
                if (tbRencanaKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                    pilih[index] = true;
                    kode[index] = tbRencanaKeperawatan.getValueAt(i, 1).toString();
                    masalah[index] = tbRencanaKeperawatan.getValueAt(i, 2).toString();
                    index++;
                }
            }

            Valid.tabelKosong(tabModeRencana);

            for (i = 0; i < jml; i++) {
                tabModeRencana.addRow(new Object[]{
                    pilih[i], kode[i], masalah[i]
                });
            }

            pilih = null;
            kode = null;
            masalah = null;

            myObj = new FileReader("./cache/rencanakeperawatanbayi.iyem");
            root = mapper.readTree(myObj);
            response = root.path("rencanakeperawatanbayi");
            if (response.isArray()) {
                for (i = 0; i < tbMasalahKeperawatan.getRowCount(); i++) {
                    if (tbMasalahKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                        for (JsonNode list : response) {
                            if (list.path("KodeMasalah").asText().toLowerCase().equals(tbMasalahKeperawatan.getValueAt(i, 1).toString())
                                    && list.path("NamaRencana").asText().toLowerCase().contains(TCariRencana.getText().toLowerCase())) {
                                tabModeRencana.addRow(new Object[]{
                                    false, list.path("KodeRencana").asText(), list.path("NamaRencana").asText()
                                });
                            }
                        }
                    }
                }
            }
            myObj.close();
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void tampilKebutuhanEdukasi() {
        try {
            Valid.tabelKosong(tabModeKebutuhanEdukasi);
            file = new File("./cache/kebutuhanedukasikomunikasi.iyem");
            file.createNewFile();
            fileWriter = new FileWriter(file);
            StringBuilder iyembuilder = new StringBuilder();
            ps = koneksi.prepareStatement("select * from master_kebutuhan_edukasi_komunikasi order by master_kebutuhan_edukasi_komunikasi.kd_kebutuhan_edukasi");
            try {
                rs = ps.executeQuery();
                while (rs.next()) {
                    tabModeKebutuhanEdukasi.addRow(new Object[]{false, rs.getString(1), rs.getString(2)});
                    iyembuilder.append("{\"KodeKebutuhan\":\"").append(rs.getString(1)).append("\",\"kebutuhanEdukasi\":\"").append(rs.getString(2)).append("\"},");
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
            if (iyembuilder.length() > 0) {
                iyembuilder.setLength(iyembuilder.length() - 1);
                fileWriter.write("{\"kebutuhanedukasikomunikasi\":[" + iyembuilder + "]}");
                fileWriter.flush();
            }

            fileWriter.close();
            iyembuilder = null;
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void tampilKebutuhanEdukasi2() {
        try {
            jml = 0;
            for (i = 0; i < tbKebutuhanEdukasi.getRowCount(); i++) {
                if (tbKebutuhanEdukasi.getValueAt(i, 0).toString().equals("true")) {
                    jml++;
                }
            }

            pilih = new boolean[jml];
            kode = new String[jml];
            masalah = new String[jml];

            index = 0;
            for (i = 0; i < tbKebutuhanEdukasi.getRowCount(); i++) {
                if (tbKebutuhanEdukasi.getValueAt(i, 0).toString().equals("true")) {
                    pilih[index] = true;
                    kode[index] = tbKebutuhanEdukasi.getValueAt(i, 1).toString();
                    masalah[index] = tbKebutuhanEdukasi.getValueAt(i, 2).toString();
                    index++;
                }
            }

            Valid.tabelKosong(tabModeKebutuhanEdukasi);

            for (i = 0; i < jml; i++) {
                tabModeKebutuhanEdukasi.addRow(new Object[]{
                    pilih[i], kode[i], masalah[i]
                });
            }

            pilih = null;
            kode = null;
            masalah = null;

            myObj = new FileReader("./cache/kebutuhanedukasikomunikasi.iyem");
            root = mapper.readTree(myObj);
            response = root.path("kebutuhanedukasikomunikasi");
            if (response.isArray()) {
                for (JsonNode list : response) {
                    if (list.path("KodeKebutuhan").asText().toLowerCase().contains(TCariMasalah.getText().toLowerCase()) || list.path("NamaMasalah").asText().toLowerCase().contains(TCariMasalah.getText().toLowerCase())) {
                        tabModeKebutuhanEdukasi.addRow(new Object[]{
                            false, list.path("KodeKebutuhan").asText(), list.path("kebutuhanEdukasi").asText()
                        });
                    }
                }
            }
            myObj.close();
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void tampilRencanaEdukasi() {
        try {
            file = new File("./cache/rencanaedukasikomunikasi.iyem");
            file.createNewFile();
            fileWriter = new FileWriter(file);
            StringBuilder iyembuilder = new StringBuilder();
            ps = koneksi.prepareStatement("select * from master_rencana_edukasi_komunikasi order by master_rencana_edukasi_komunikasi.kd_rencana_edukasi");
            try {
                rs = ps.executeQuery();
                while (rs.next()) {
                    iyembuilder.append("{\"KodeKebutuhan\":\"").append(rs.getString(1)).append("\",\"KodeRencana\":\"").append(rs.getString(2)).append("\",\"rencanaEdukasi\":\"").append(rs.getString(3)).append("\"},");
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
            if (iyembuilder.length() > 0) {
                iyembuilder.setLength(iyembuilder.length() - 1);
                fileWriter.write("{\"rencanaedukasikomunikasi\":[" + iyembuilder + "]}");
                fileWriter.flush();
            }

            fileWriter.close();
            iyembuilder = null;
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void tampilRencanaEdukasi2() {
        try {
            jml = 0;
            for (i = 0; i < tbRencanaEdukasi.getRowCount(); i++) {
                if (tbRencanaEdukasi.getValueAt(i, 0).toString().equals("true")) {
                    jml++;
                }
            }

            pilih = new boolean[jml];
            kode = new String[jml];
            masalah = new String[jml];

            index = 0;
            for (i = 0; i < tbRencanaEdukasi.getRowCount(); i++) {
                if (tbRencanaEdukasi.getValueAt(i, 0).toString().equals("true")) {
                    pilih[index] = true;
                    kode[index] = tbRencanaEdukasi.getValueAt(i, 1).toString();
                    masalah[index] = tbRencanaEdukasi.getValueAt(i, 2).toString();
                    index++;
                }
            }

            Valid.tabelKosong(tabModeRencanaEdukasi);

            for (i = 0; i < jml; i++) {
                tabModeRencanaEdukasi.addRow(new Object[]{
                    pilih[i], kode[i], masalah[i]
                });
            }

            pilih = null;
            kode = null;
            masalah = null;

            myObj = new FileReader("./cache/rencanaedukasikomunikasi.iyem");
            root = mapper.readTree(myObj);
            response = root.path("rencanaedukasikomunikasi");
            if (response.isArray()) {
                for (i = 0; i < tbKebutuhanEdukasi.getRowCount(); i++) {
                    if (tbKebutuhanEdukasi.getValueAt(i, 0).toString().equals("true")) {
                        for (JsonNode list : response) {
                            if (list.path("KodeKebutuhan").asText().toLowerCase().equals(tbKebutuhanEdukasi.getValueAt(i, 1).toString())
                                    && list.path("rencanaEdukasi").asText().toLowerCase().contains(TCariRencana.getText().toLowerCase())) {
                                tabModeRencanaEdukasi.addRow(new Object[]{
                                    false, list.path("KodeRencana").asText(), list.path("rencanaEdukasi").asText()
                                });
                            }
                        }
                    }
                }
            }
            myObj.close();
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void isTotalResikoJatuh() {
        try {
            NilaiResikoTotal.setText((Integer.parseInt(NilaiResiko1.getText()) + Integer.parseInt(NilaiResiko2.getText()) + Integer.parseInt(NilaiResiko3.getText()) + Integer.parseInt(NilaiResiko4.getText()) + Integer.parseInt(NilaiResiko5.getText()) + Integer.parseInt(NilaiResiko6.getText()) + Integer.parseInt(NilaiResiko7.getText())) + "");
            if (Integer.parseInt(NilaiResikoTotal.getText()) < 12) {
                KeteranganTingkatRisiko.setText("Risiko Rendah 7 - 11");
            } else if (Integer.parseInt(NilaiResikoTotal.getText()) >= 12) {
                KeteranganTingkatRisiko.setText("Risiko Tinggi >=12");
            }
        } catch (Exception e) {
            NilaiResikoTotal.setText("0");
            KeteranganTingkatRisiko.setText("Risiko Rendah 7 - 11");
        }
    }

    private void isMenu() {
        if (ChkAccor.isSelected() == true) {
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(470, HEIGHT));
            FormMenu.setVisible(true);
            FormMasalahRencana.setVisible(true);
            ChkAccor.setVisible(true);
        } else if (ChkAccor.isSelected() == false) {
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(15, HEIGHT));
            FormMenu.setVisible(false);
            FormMasalahRencana.setVisible(false);
            ChkAccor.setVisible(true);
        }
    }

    private void getMasalah() {
        if (tbObat.getSelectedRow() != -1) {
            TNoRM1.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 1).toString());
            TPasien1.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 2).toString());
            DetailRencana.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 126).toString());
            try {
                Valid.tabelKosong(tabModeDetailMasalah);
                ps = koneksi.prepareStatement(
                        "select master_masalah_keperawatan_anak.kode_masalah,master_masalah_keperawatan_anak.nama_masalah from master_masalah_keperawatan_anak "
                        + "inner join penilaian_awal_keperawatan_ralan_bayi_masalah on penilaian_awal_keperawatan_ralan_bayi_masalah.kode_masalah=master_masalah_keperawatan_anak.kode_masalah "
                        + "where penilaian_awal_keperawatan_ralan_bayi_masalah.no_rawat=? order by penilaian_awal_keperawatan_ralan_bayi_masalah.kode_masalah");
                try {
                    ps.setString(1, tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        tabModeDetailMasalah.addRow(new Object[]{rs.getString(1), rs.getString(2)});
                    }
                } catch (Exception e) {
                    System.out.println("Notif : " + e);
                } finally {
                    if (rs != null) {
                        rs.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            }

            try {
                Valid.tabelKosong(tabModeDetailRencana);
                ps = koneksi.prepareStatement(
                        "select master_rencana_keperawatan_anak.kode_rencana,master_rencana_keperawatan_anak.rencana_keperawatan from master_rencana_keperawatan_anak "
                        + "inner join penilaian_awal_keperawatan_ralan_rencana_anak on penilaian_awal_keperawatan_ralan_rencana_anak.kode_rencana=master_rencana_keperawatan_anak.kode_rencana "
                        + "where penilaian_awal_keperawatan_ralan_rencana_anak.no_rawat=? order by penilaian_awal_keperawatan_ralan_rencana_anak.kode_rencana");
                try {
                    ps.setString(1, tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        tabModeDetailRencana.addRow(new Object[]{rs.getString(1), rs.getString(2)});
                    }
                } catch (Exception e) {
                    System.out.println("Notif : " + e);
                } finally {
                    if (rs != null) {
                        rs.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            }
        }
    }

    private void getEdukasi() {
        if (tbObat.getSelectedRow() != -1) {
            TNoRM1.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 1).toString());
            TPasien1.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 2).toString());
            try {
                Valid.tabelKosong(tabModeDetailKebutuhanEdukasi);
                ps = koneksi.prepareStatement(
                        "select master_kebutuhan_edukasi_komunikasi.kd_kebutuhan_edukasi,master_kebutuhan_edukasi_komunikasi.kebutuhan_edukasi from master_kebutuhan_edukasi_komunikasi "
                        + "inner join penilaian_awal_keperawatan_ralan_bayi_kebutuhan_edukasi on penilaian_awal_keperawatan_ralan_bayi_kebutuhan_edukasi.kd_kebutuhan_edukasi=master_kebutuhan_edukasi_komunikasi.kd_kebutuhan_edukasi "
                        + "where penilaian_awal_keperawatan_ralan_bayi_kebutuhan_edukasi.no_rawat=? order by penilaian_awal_keperawatan_ralan_bayi_kebutuhan_edukasi.kd_kebutuhan_edukasi");
                try {
                    ps.setString(1, tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        tabModeDetailKebutuhanEdukasi.addRow(new Object[]{rs.getString(1), rs.getString(2)});
                    }
                } catch (Exception e) {
                    System.out.println("Notif : " + e);
                } finally {
                    if (rs != null) {
                        rs.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            }

            try {
                Valid.tabelKosong(tabModeDetailRencanaEdukasi);
                ps = koneksi.prepareStatement(
                        "select master_rencana_edukasi_komunikasi.kd_rencana_edukasi,master_rencana_edukasi_komunikasi.rencana_edukasi from master_rencana_edukasi_komunikasi "
                        + "inner join penilaian_awal_keperawatan_ralan_bayi_rencana_edukasi on penilaian_awal_keperawatan_ralan_bayi_rencana_edukasi.kd_rencana_edukasi=master_rencana_edukasi_komunikasi.kd_rencana_edukasi "
                        + "where penilaian_awal_keperawatan_ralan_bayi_rencana_edukasi.no_rawat=? order by penilaian_awal_keperawatan_ralan_bayi_rencana_edukasi.kd_rencana_edukasi");
                try {
                    ps.setString(1, tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        tabModeDetailRencanaEdukasi.addRow(new Object[]{rs.getString(1), rs.getString(2)});
                    }
                } catch (Exception e) {
                    System.out.println("Notif : " + e);
                } finally {
                    if (rs != null) {
                        rs.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            }
        }
    }

    private void getImunisasi() {
        try {
            Valid.tabelKosong(tabModeImunisasi2);
            ps = koneksi.prepareStatement(
                    "select master_imunisasi.kode_imunisasi,master_imunisasi.nama_imunisasi from master_imunisasi inner join riwayat_imunisasi on riwayat_imunisasi.kode_imunisasi=master_imunisasi.kode_imunisasi "
                    + "where riwayat_imunisasi.no_rkm_medis=? group by master_imunisasi.kode_imunisasi order by master_imunisasi.kode_imunisasi  ");
            try {
                ps.setString(1, TNoRM1.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    ke1 = false;
                    ke2 = false;
                    ke3 = false;
                    ke4 = false;
                    ke5 = false;
                    ke6 = false;
                    ps2 = koneksi.prepareStatement("select * from riwayat_imunisasi where riwayat_imunisasi.no_rkm_medis=? and riwayat_imunisasi.kode_imunisasi=?");
                    try {
                        ps2.setString(1, TNoRM1.getText());
                        ps2.setString(2, rs.getString(1));
                        rs2 = ps2.executeQuery();
                        while (rs2.next()) {
                            if (rs2.getInt("no_imunisasi") == 1) {
                                ke1 = true;
                            }
                            if (rs2.getInt("no_imunisasi") == 2) {
                                ke2 = true;
                            }
                            if (rs2.getInt("no_imunisasi") == 3) {
                                ke3 = true;
                            }
                            if (rs2.getInt("no_imunisasi") == 4) {
                                ke4 = true;
                            }
                            if (rs2.getInt("no_imunisasi") == 5) {
                                ke5 = true;
                            }
                            if (rs2.getInt("no_imunisasi") == 6) {
                                ke6 = true;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Notif : " + e);
                    } finally {
                        if (rs2 != null) {
                            rs2.close();
                        }
                        if (ps2 != null) {
                            ps2.close();
                        }
                    }

                    tabModeImunisasi2.addRow(new Object[]{rs.getString(1), rs.getString(2), ke1, ke2, ke3, ke4, ke5, ke6});
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
    }

    private void tampilImunisasi() {
        try {
            Valid.tabelKosong(tabModeImunisasi);
            ps = koneksi.prepareStatement(
                    "select master_imunisasi.kode_imunisasi,master_imunisasi.nama_imunisasi from master_imunisasi inner join riwayat_imunisasi on riwayat_imunisasi.kode_imunisasi=master_imunisasi.kode_imunisasi "
                    + "where riwayat_imunisasi.no_rkm_medis=? group by master_imunisasi.kode_imunisasi order by master_imunisasi.kode_imunisasi  ");
            try {
                ps.setString(1, TNoRM.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    ke1 = false;
                    ke2 = false;
                    ke3 = false;
                    ke4 = false;
                    ke5 = false;
                    ke6 = false;
                    ps2 = koneksi.prepareStatement("select * from riwayat_imunisasi where riwayat_imunisasi.no_rkm_medis=? and riwayat_imunisasi.kode_imunisasi=?");
                    try {
                        ps2.setString(1, TNoRM.getText());
                        ps2.setString(2, rs.getString(1));
                        rs2 = ps2.executeQuery();
                        while (rs2.next()) {
                            if (rs2.getInt("no_imunisasi") == 1) {
                                ke1 = true;
                            }
                            if (rs2.getInt("no_imunisasi") == 2) {
                                ke2 = true;
                            }
                            if (rs2.getInt("no_imunisasi") == 3) {
                                ke3 = true;
                            }
                            if (rs2.getInt("no_imunisasi") == 4) {
                                ke4 = true;
                            }
                            if (rs2.getInt("no_imunisasi") == 5) {
                                ke5 = true;
                            }
                            if (rs2.getInt("no_imunisasi") == 6) {
                                ke6 = true;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Notif : " + e);
                    } finally {
                        if (rs2 != null) {
                            rs2.close();
                        }
                        if (ps2 != null) {
                            ps2.close();
                        }
                    }

                    tabModeImunisasi.addRow(new Object[]{rs.getString(1), rs.getString(2), ke1, ke2, ke3, ke4, ke5, ke6});
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
    }

    private void hapus() {
        if (Sequel.queryu2tf("delete from penilaian_awal_keperawatan_ralan_bayi where no_rawat=?", 1, new String[]{
            tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString()
        }) == true) {
            TNoRM1.setText("");
            TPasien1.setText("");
            Valid.tabelKosong(tabModeDetailMasalah);
            Valid.tabelKosong(tabModeDetailRencana);
            ChkAccor.setSelected(false);
            isMenu();
            tabMode.removeRow(tbObat.getSelectedRow());
            LCount.setText("" + tabMode.getRowCount());
        } else {
            JOptionPane.showMessageDialog(null, "Gagal menghapus..!!");
        }
    }

    private void ganti() {
        if (Sequel.mengedittf("penilaian_awal_keperawatan_ralan_bayi", "no_rawat=?", "no_rawat=?,tanggal=?,informasi=?,td=?,nadi=?,rr=?,suhu=?,gcs=?,bb=?,tb=?,lp=?,lk=?,ld=?,keluhan_utama=?,rpd=?,rpk=?,rpo=?,alergi=?,anakke=?,darisaudara=?,caralahir=?,ket_caralahir=?,umurkelahiran=?,kelainanbawaan=?,ket_kelainan_bawaan=?,usiatengkurap=?,usiaduduk=?,usiaberdiri=?,usiagigipertama=?,usiaberjalan=?,usiabicara=?,usiamembaca=?,usiamenulis=?,gangguanemosi=?,alat_bantu=?,ket_bantu=?,prothesa=?,ket_pro=?,adl=?,status_psiko=?,ket_psiko=?,hub_keluarga=?,pengasuh=?,ket_pengasuh=?,ekonomi=?,budaya=?,ket_budaya=?,edukasi=?,ket_edukasi=?,penilaian_humptydumpty_skala1=?,penilaian_humptydumpty_nilai1=?,penilaian_humptydumpty_skala2=?,penilaian_humptydumpty_nilai2=?,penilaian_humptydumpty_skala3=?,penilaian_humptydumpty_nilai3=?,penilaian_humptydumpty_skala4=?,penilaian_humptydumpty_nilai4=?,penilaian_humptydumpty_skala5=?,penilaian_humptydumpty_nilai5=?,penilaian_humptydumpty_skala6=?,penilaian_humptydumpty_nilai6=?,penilaian_humptydumpty_skala7=?,penilaian_humptydumpty_nilai7=?,penilaian_humptydumpty_totalnilai=?,penilaian_humptydumpty_hasil=?,kemampuan_baca_tulis=?,butuh_penerjemah=?,keterangan_butuh_penerjemah=?,terdapat_hambatan_belajar=?,hambatan_belajar=?,keterangan_hambatan_belajar=?,hambatan_cara_bicara=?,hambatan_bahasa_isyarat=?,cara_belajar_disukai=?,kesediaan_menerima_informasi=?,ket_kesediaan_menerima_informasi=?,pemahaman_nutrisi=?,pemahaman_penyakit=?,pemahaman_pengobatan=?,pemahaman_perawatan=?,keyakinan_nilai=?,keterbatasan_fisik=?,hambatan_emosional=?,Motivasi=?,sg1=?,nilai1=?,sg2=?,nilai2=?,sg3=?,nilai3=?,sg4=?,nilai4=?,total_hasil=?,wajah=?,nilaiwajah=?,kaki=?,nilaikaki=?,aktifitas=?,nilaiaktifitas=?,menangis=?,nilaimenangis=?,bersuara=?,nilaibersuara=?,hasilnyeri=?,nyeri=?,lokasi=?,durasi=?,frekuensi=?,nyeri_hilang=?,ket_nyeri=?,pada_dokter=?,ket_dokter=?,informasi_perencanaan_pulang=?,lama_ratarata=?,tanggal_pulang=?,kondisi_saat_pulang=?,perawatan_lanjutan=?,cara_transportasi=?,transportasi_digunakan=?,rencana=?,nip=?", 122, new String[]{
            TNoRw.getText(), Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19), Informasi.getSelectedItem().toString(), TD.getText(), Nadi.getText(), RR.getText(),
            Suhu.getText(), GCS.getText(), BB.getText(), TB.getText(), LP.getText(), LK.getText(), LD.getText(), KeluhanUtama.getText(), RPD.getText(), RPK.getText(), RPO.getText(), Alergi.getText(), Anakke.getText(), DariSaudara.getText(),
            CaraKelahiran.getSelectedItem().toString(), KetCaraKelahiran.getText(), UmurKelahiran.getSelectedItem().toString(), KelainanBawaan.getSelectedItem().toString(), KetKelainanBawaan.getText(), UsiaTengkurap.getText(),
            UsiaDuduk.getText(), UsiaBerdiri.getText(), UsiaGigi.getText(), UsiaBerjalan.getText(), UsiaBicara.getText(), UsiaMembaca.getText(), UsiaMenulis.getText(), GangguanEmosi.getText(), AlatBantu.getSelectedItem().toString(),
            KetBantu.getText(), Prothesa.getSelectedItem().toString(), KetProthesa.getText(), ADL.getSelectedItem().toString(), StatusPsiko.getSelectedItem().toString(), KetPsiko.getText(), HubunganKeluarga.getSelectedItem().toString(),
            Pengasuh.getSelectedItem().toString(), KetPengasuh.getText(), Ekonomi.getSelectedItem().toString(), StatusBudaya.getSelectedItem().toString(), KetBudaya.getText(), Edukasi.getSelectedItem().toString(), KetEdukasi.getText(),
            SkalaResiko1.getSelectedItem().toString(), NilaiResiko1.getText(), SkalaResiko2.getSelectedItem().toString(), NilaiResiko2.getText(), SkalaResiko3.getSelectedItem().toString(), NilaiResiko3.getText(), SkalaResiko4.getSelectedItem().toString(),
            NilaiResiko4.getText(), SkalaResiko5.getSelectedItem().toString(), NilaiResiko5.getText(), SkalaResiko6.getSelectedItem().toString(), NilaiResiko6.getText(), SkalaResiko7.getSelectedItem().toString(), NilaiResiko7.getText(), NilaiResikoTotal.getText(), KeteranganTingkatRisiko.getText(),
            KemampuanBacaTulis.getSelectedItem().toString(), ButuhPenerjemah.getSelectedItem().toString(), KeteranganButuhPenerjemah.getText(), TerdapatHambatanBelajar.getSelectedItem().toString(), HambatanBelajar.getSelectedItem().toString(), KeteranganHambatanBelajar.getText(),
            HambatanCaraBicara.getSelectedItem().toString(), HambatanBahasaIsyarat.getSelectedItem().toString(), CaraBelajarDisukai.getSelectedItem().toString(), KesediaanMenerimaInformasi.getSelectedItem().toString(), KeteranganKesediaanMenerimaInformasi.getText(),
            PemahamanNutrisi.getSelectedItem().toString(), PemahamanPenyakit.getSelectedItem().toString(), PemahamanPengobatan.getSelectedItem().toString(), PemahamanPerawatan.getSelectedItem().toString(), KeyakinanNilai.getSelectedItem().toString(), KeterbatasanFisik.getSelectedItem().toString(),
            HambatanEmosional.getSelectedItem().toString(), Motivasi.getSelectedItem().toString(), SG1.getSelectedItem().toString(),
            NilaiGizi1.getText(), SG2.getSelectedItem().toString(), NilaiGizi2.getText(), SG3.getSelectedItem().toString(), NilaiGizi3.getText(), SG4.getSelectedItem().toString(), NilaiGizi4.getText(), TotalNilaiGizi.getText(),
            SkalaWajah.getSelectedItem().toString(), NilaiWajah.getText(), SkalaKaki.getSelectedItem().toString(), NilaiKaki.getText(), SkalaAktifitas.getSelectedItem().toString(), NilaiAktifitas.getText(), SkalaMenangis.getSelectedItem().toString(),
            NilaiMenangis.getText(), SkalaBersuara.getSelectedItem().toString(), NilaiBersuara.getText(), SkalaNyeri.getText(), Nyeri.getSelectedItem().toString(), Lokasi.getText(), Durasi.getText(), Frekuensi.getText(),
            NyeriHilang.getSelectedItem().toString(), KetNyeri.getText(), PadaDokter.getSelectedItem().toString(), KetDokter.getText(), InformasiPerencanaanPulang.getSelectedItem().toString(), LamaRatarata.getText(), Valid.SetTgl(TanggalPulang.getSelectedItem() + ""),
            KondisiPulang.getText(), PerawatanLanjutan.getText(), CaraTransportasiPulang.getSelectedItem().toString(), TransportasiYangDigunakan.getSelectedItem().toString(), Rencana.getText(), KdPetugas.getText(), tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString()
        }) == true) {
            tbObat.setValueAt(TNoRw.getText(), tbObat.getSelectedRow(), 0);
            tbObat.setValueAt(TNoRM.getText(), tbObat.getSelectedRow(), 1);
            tbObat.setValueAt(TPasien.getText(), tbObat.getSelectedRow(), 2);
            tbObat.setValueAt(Jk.getText(), tbObat.getSelectedRow(), 3);
            tbObat.setValueAt(Agama.getText(), tbObat.getSelectedRow(), 4);
            tbObat.setValueAt(Bahasa.getText(), tbObat.getSelectedRow(), 5);
            tbObat.setValueAt(CacatFisik.getText(), tbObat.getSelectedRow(), 6);
            tbObat.setValueAt(TglLahir.getText(), tbObat.getSelectedRow(), 7);
            tbObat.setValueAt(Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19), tbObat.getSelectedRow(), 8);
            tbObat.setValueAt(Informasi.getSelectedItem().toString(), tbObat.getSelectedRow(), 9);
            tbObat.setValueAt(TD.getText(), tbObat.getSelectedRow(), 10);
            tbObat.setValueAt(Nadi.getText(), tbObat.getSelectedRow(), 11);
            tbObat.setValueAt(RR.getText(), tbObat.getSelectedRow(), 12);
            tbObat.setValueAt(Suhu.getText(), tbObat.getSelectedRow(), 13);
            tbObat.setValueAt(GCS.getText(), tbObat.getSelectedRow(), 14);
            tbObat.setValueAt(BB.getText(), tbObat.getSelectedRow(), 15);
            tbObat.setValueAt(TB.getText(), tbObat.getSelectedRow(), 16);
            tbObat.setValueAt(LP.getText(), tbObat.getSelectedRow(), 17);
            tbObat.setValueAt(LK.getText(), tbObat.getSelectedRow(), 18);
            tbObat.setValueAt(LD.getText(), tbObat.getSelectedRow(), 19);
            tbObat.setValueAt(KeluhanUtama.getText(), tbObat.getSelectedRow(), 20);
            tbObat.setValueAt(RPD.getText(), tbObat.getSelectedRow(), 21);
            tbObat.setValueAt(RPK.getText(), tbObat.getSelectedRow(), 22);
            tbObat.setValueAt(RPO.getText(), tbObat.getSelectedRow(), 23);
            tbObat.setValueAt(Alergi.getText(), tbObat.getSelectedRow(), 24);
            tbObat.setValueAt(Anakke.getText(), tbObat.getSelectedRow(), 25);
            tbObat.setValueAt(DariSaudara.getText(), tbObat.getSelectedRow(), 26);
            tbObat.setValueAt(CaraKelahiran.getSelectedItem().toString(), tbObat.getSelectedRow(), 27);
            tbObat.setValueAt(KetCaraKelahiran.getText(), tbObat.getSelectedRow(), 28);
            tbObat.setValueAt(UmurKelahiran.getSelectedItem().toString(), tbObat.getSelectedRow(), 29);
            tbObat.setValueAt(KelainanBawaan.getSelectedItem().toString(), tbObat.getSelectedRow(), 30);
            tbObat.setValueAt(KetKelainanBawaan.getText(), tbObat.getSelectedRow(), 31);
            tbObat.setValueAt(UsiaTengkurap.getText(), tbObat.getSelectedRow(), 32);
            tbObat.setValueAt(UsiaDuduk.getText(), tbObat.getSelectedRow(), 33);
            tbObat.setValueAt(UsiaBerdiri.getText(), tbObat.getSelectedRow(), 34);
            tbObat.setValueAt(UsiaGigi.getText(), tbObat.getSelectedRow(), 35);
            tbObat.setValueAt(UsiaBerjalan.getText(), tbObat.getSelectedRow(), 36);
            tbObat.setValueAt(UsiaBicara.getText(), tbObat.getSelectedRow(), 37);
            tbObat.setValueAt(UsiaMembaca.getText(), tbObat.getSelectedRow(), 38);
            tbObat.setValueAt(UsiaMenulis.getText(), tbObat.getSelectedRow(), 39);
            tbObat.setValueAt(GangguanEmosi.getText(), tbObat.getSelectedRow(), 40);
            tbObat.setValueAt(AlatBantu.getSelectedItem().toString(), tbObat.getSelectedRow(), 41);
            tbObat.setValueAt(KetBantu.getText(), tbObat.getSelectedRow(), 42);
            tbObat.setValueAt(Prothesa.getSelectedItem().toString(), tbObat.getSelectedRow(), 43);
            tbObat.setValueAt(KetProthesa.getText(), tbObat.getSelectedRow(), 44);
            tbObat.setValueAt(ADL.getSelectedItem().toString(), tbObat.getSelectedRow(), 45);
            tbObat.setValueAt(StatusPsiko.getSelectedItem().toString(), tbObat.getSelectedRow(), 46);
            tbObat.setValueAt(KetPsiko.getText(), tbObat.getSelectedRow(), 47);
            tbObat.setValueAt(HubunganKeluarga.getSelectedItem().toString(), tbObat.getSelectedRow(), 48);
            tbObat.setValueAt(Pengasuh.getSelectedItem().toString(), tbObat.getSelectedRow(), 49);
            tbObat.setValueAt(KetPengasuh.getText(), tbObat.getSelectedRow(), 50);
            tbObat.setValueAt(Ekonomi.getSelectedItem().toString(), tbObat.getSelectedRow(), 51);
            tbObat.setValueAt(StatusBudaya.getSelectedItem().toString(), tbObat.getSelectedRow(), 52);
            tbObat.setValueAt(KetBudaya.getText(), tbObat.getSelectedRow(), 53);
            tbObat.setValueAt(Edukasi.getSelectedItem().toString(), tbObat.getSelectedRow(), 54);
            tbObat.setValueAt(KetEdukasi.getText(), tbObat.getSelectedRow(), 55);
            tbObat.setValueAt(KemampuanBacaTulis.getSelectedItem().toString(), tbObat.getSelectedRow(), 56);
            tbObat.setValueAt(ButuhPenerjemah.getSelectedItem().toString(), tbObat.getSelectedRow(), 57);
            tbObat.setValueAt(KeteranganButuhPenerjemah.getText(), tbObat.getSelectedRow(), 58);
            tbObat.setValueAt(TerdapatHambatanBelajar.getSelectedItem().toString(), tbObat.getSelectedRow(), 59);
            tbObat.setValueAt(HambatanBelajar.getSelectedItem().toString(), tbObat.getSelectedRow(), 60);
            tbObat.setValueAt(KeteranganHambatanBelajar.getText(), tbObat.getSelectedRow(), 61);
            tbObat.setValueAt(HambatanCaraBicara.getSelectedItem().toString(), tbObat.getSelectedRow(), 62);
            tbObat.setValueAt(HambatanBahasaIsyarat.getSelectedItem().toString(), tbObat.getSelectedRow(), 63);
            tbObat.setValueAt(CaraBelajarDisukai.getSelectedItem().toString(), tbObat.getSelectedRow(), 64);
            tbObat.setValueAt(KesediaanMenerimaInformasi.getSelectedItem().toString(), tbObat.getSelectedRow(), 65);
            tbObat.setValueAt(KeteranganKesediaanMenerimaInformasi.getText(), tbObat.getSelectedRow(), 66);
            tbObat.setValueAt(PemahamanNutrisi.getSelectedItem().toString(), tbObat.getSelectedRow(), 67);
            tbObat.setValueAt(PemahamanPenyakit.getSelectedItem().toString(), tbObat.getSelectedRow(), 68);
            tbObat.setValueAt(PemahamanPengobatan.getSelectedItem().toString(), tbObat.getSelectedRow(), 69);
            tbObat.setValueAt(PemahamanPerawatan.getSelectedItem().toString(), tbObat.getSelectedRow(), 70);
            tbObat.setValueAt(KeyakinanNilai.getSelectedItem().toString(), tbObat.getSelectedRow(), 71);
            tbObat.setValueAt(KeterbatasanFisik.getSelectedItem().toString(), tbObat.getSelectedRow(), 72);
            tbObat.setValueAt(HambatanEmosional.getSelectedItem().toString(), tbObat.getSelectedRow(), 73);
            tbObat.setValueAt(Motivasi.getSelectedItem().toString(), tbObat.getSelectedRow(), 74);
            tbObat.setValueAt(SkalaResiko1.getSelectedItem().toString(), tbObat.getSelectedRow(), 75);
            tbObat.setValueAt(NilaiResiko1.getText(), tbObat.getSelectedRow(), 76);
            tbObat.setValueAt(SkalaResiko2.getSelectedItem().toString(), tbObat.getSelectedRow(), 77);
            tbObat.setValueAt(NilaiResiko2.getText(), tbObat.getSelectedRow(), 78);
            tbObat.setValueAt(SkalaResiko3.getSelectedItem().toString(), tbObat.getSelectedRow(), 79);
            tbObat.setValueAt(NilaiResiko3.getText(), tbObat.getSelectedRow(), 80);
            tbObat.setValueAt(SkalaResiko4.getSelectedItem().toString(), tbObat.getSelectedRow(), 81);
            tbObat.setValueAt(NilaiResiko4.getText(), tbObat.getSelectedRow(), 82);
            tbObat.setValueAt(SkalaResiko5.getSelectedItem().toString(), tbObat.getSelectedRow(), 83);
            tbObat.setValueAt(NilaiResiko5.getText(), tbObat.getSelectedRow(), 84);
            tbObat.setValueAt(SkalaResiko6.getSelectedItem().toString(), tbObat.getSelectedRow(), 85);
            tbObat.setValueAt(NilaiResiko6.getText(), tbObat.getSelectedRow(), 86);
            tbObat.setValueAt(SkalaResiko7.getSelectedItem().toString(), tbObat.getSelectedRow(), 87);
            tbObat.setValueAt(NilaiResiko7.getText(), tbObat.getSelectedRow(), 88);
            tbObat.setValueAt(NilaiResikoTotal.getText(), tbObat.getSelectedRow(), 89);
            tbObat.setValueAt(KeteranganTingkatRisiko.getText(), tbObat.getSelectedRow(), 90);
            tbObat.setValueAt(SG1.getSelectedItem().toString(), tbObat.getSelectedRow(), 91);
            tbObat.setValueAt(NilaiGizi1.getText(), tbObat.getSelectedRow(), 92);
            tbObat.setValueAt(SG2.getSelectedItem().toString(), tbObat.getSelectedRow(), 93);
            tbObat.setValueAt(NilaiGizi2.getText(), tbObat.getSelectedRow(), 94);
            tbObat.setValueAt(SG3.getSelectedItem().toString(), tbObat.getSelectedRow(), 95);
            tbObat.setValueAt(NilaiGizi3.getText(), tbObat.getSelectedRow(), 96);
            tbObat.setValueAt(SG4.getSelectedItem().toString(), tbObat.getSelectedRow(), 97);
            tbObat.setValueAt(NilaiGizi4.getText(), tbObat.getSelectedRow(), 98);
            tbObat.setValueAt(TotalNilaiGizi.getText(), tbObat.getSelectedRow(), 99);
            tbObat.setValueAt(SkalaWajah.getSelectedItem().toString(), tbObat.getSelectedRow(), 100);
            tbObat.setValueAt(NilaiWajah.getText(), tbObat.getSelectedRow(), 101);
            tbObat.setValueAt(SkalaKaki.getSelectedItem().toString(), tbObat.getSelectedRow(), 102);
            tbObat.setValueAt(NilaiKaki.getText(), tbObat.getSelectedRow(), 103);
            tbObat.setValueAt(SkalaAktifitas.getSelectedItem().toString(), tbObat.getSelectedRow(), 104);
            tbObat.setValueAt(NilaiAktifitas.getText(), tbObat.getSelectedRow(), 105);
            tbObat.setValueAt(SkalaMenangis.getSelectedItem().toString(), tbObat.getSelectedRow(), 106);
            tbObat.setValueAt(NilaiMenangis.getText(), tbObat.getSelectedRow(), 107);
            tbObat.setValueAt(SkalaBersuara.getSelectedItem().toString(), tbObat.getSelectedRow(), 108);
            tbObat.setValueAt(NilaiBersuara.getText(), tbObat.getSelectedRow(), 109);
            tbObat.setValueAt(SkalaNyeri.getText(), tbObat.getSelectedRow(), 110);
            tbObat.setValueAt(Nyeri.getSelectedItem().toString(), tbObat.getSelectedRow(), 111); // ✅ perbaiki dari 112 → 111
            tbObat.setValueAt(Lokasi.getText(), tbObat.getSelectedRow(), 112);                   // ✅ sesuaikan
            tbObat.setValueAt(Durasi.getText(), tbObat.getSelectedRow(), 113);
            tbObat.setValueAt(Frekuensi.getText(), tbObat.getSelectedRow(), 114);
            tbObat.setValueAt(NyeriHilang.getSelectedItem().toString(), tbObat.getSelectedRow(), 115);
            tbObat.setValueAt(KetNyeri.getText(), tbObat.getSelectedRow(), 116);
            tbObat.setValueAt(PadaDokter.getSelectedItem().toString(), tbObat.getSelectedRow(), 117);
            tbObat.setValueAt(KetDokter.getText(), tbObat.getSelectedRow(), 118);
            tbObat.setValueAt(InformasiPerencanaanPulang.getSelectedItem().toString(), tbObat.getSelectedRow(), 119);
            tbObat.setValueAt(LamaRatarata.getText(), tbObat.getSelectedRow(), 120);
            tbObat.setValueAt(Valid.SetTgl(TanggalPulang.getSelectedItem() + ""), tbObat.getSelectedRow(), 121);
            tbObat.setValueAt(KondisiPulang.getText(), tbObat.getSelectedRow(), 122);
            tbObat.setValueAt(PerawatanLanjutan.getText(), tbObat.getSelectedRow(), 123);
            tbObat.setValueAt(CaraTransportasiPulang.getSelectedItem().toString(), tbObat.getSelectedRow(), 124);
            tbObat.setValueAt(TransportasiYangDigunakan.getSelectedItem().toString(), tbObat.getSelectedRow(), 125);
            tbObat.setValueAt(Rencana.getText(), tbObat.getSelectedRow(), 126);
            tbObat.setValueAt(KdPetugas.getText(), tbObat.getSelectedRow(), 127);
            tbObat.setValueAt(NmPetugas.getText(), tbObat.getSelectedRow(), 128);
            Sequel.meghapus("penilaian_awal_keperawatan_ralan_bayi_masalah", "no_rawat", tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
            Sequel.meghapus("penilaian_awal_keperawatan_ralan_rencana_anak", "no_rawat", tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
            Sequel.meghapus("penilaian_awal_keperawatan_ralan_bayi_kebutuhan_edukasi", "no_rawat", tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
            Sequel.meghapus("penilaian_awal_keperawatan_ralan_bayi_rencana_edukasi", "no_rawat", tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
            Valid.tabelKosong(tabModeDetailMasalah);
            Valid.tabelKosong(tabModeDetailRencana);
            Valid.tabelKosong(tabModeImunisasi2);
            for (i = 0; i < tbMasalahKeperawatan.getRowCount(); i++) {
                if (tbMasalahKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                    if (Sequel.menyimpantf2("penilaian_awal_keperawatan_ralan_bayi_masalah", "?,?", 2, new String[]{TNoRw.getText(), tbMasalahKeperawatan.getValueAt(i, 1).toString()}) == true) {
                        tabModeDetailMasalah.addRow(new Object[]{
                            tbMasalahKeperawatan.getValueAt(i, 1).toString(), tbMasalahKeperawatan.getValueAt(i, 2).toString()
                        });
                    }
                }
            }
            for (i = 0; i < tbRencanaKeperawatan.getRowCount(); i++) {
                if (tbRencanaKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                    if (Sequel.menyimpantf2("penilaian_awal_keperawatan_ralan_rencana_anak", "?,?", 2, new String[]{TNoRw.getText(), tbRencanaKeperawatan.getValueAt(i, 1).toString()}) == true) {
                        tabModeDetailRencana.addRow(new Object[]{
                            tbRencanaKeperawatan.getValueAt(i, 1).toString(), tbRencanaKeperawatan.getValueAt(i, 2).toString()
                        });
                    }
                }
            }
            for (i = 0; i < tbImunisasi.getRowCount(); i++) {
                tabModeImunisasi2.addRow(new Object[]{
                    tbImunisasi.getValueAt(i, 0).toString(), tbImunisasi.getValueAt(i, 1).toString(), tbImunisasi.getValueAt(i, 2),
                    tbImunisasi.getValueAt(i, 3), tbImunisasi.getValueAt(i, 4), tbImunisasi.getValueAt(i, 5),
                    tbImunisasi.getValueAt(i, 6), tbImunisasi.getValueAt(i, 7)
                });
            }
            for (i = 0; i < tbKebutuhanEdukasi.getRowCount(); i++) {
                if (tbKebutuhanEdukasi.getValueAt(i, 0).toString().equals("true")) {
                    if (Sequel.menyimpantf2("penilaian_awal_keperawatan_ralan_bayi_kebutuhan_edukasi", "?,?", 2, new String[]{TNoRw.getText(), tbKebutuhanEdukasi.getValueAt(i, 1).toString()}) == true) {
                        tabModeDetailKebutuhanEdukasi.addRow(new Object[]{
                            tbKebutuhanEdukasi.getValueAt(i, 1).toString(), tbKebutuhanEdukasi.getValueAt(i, 2).toString()
                        });
                    }
                }
            }
            for (i = 0; i < tbRencanaEdukasi.getRowCount(); i++) {
                if (tbRencanaEdukasi.getValueAt(i, 0).toString().equals("true")) {
                    if (Sequel.menyimpantf2("penilaian_awal_keperawatan_ralan_bayi_rencana_edukasi", "?,?", 2, new String[]{TNoRw.getText(), tbRencanaEdukasi.getValueAt(i, 1).toString()}) == true) {
                        tabModeDetailRencanaEdukasi.addRow(new Object[]{
                            tbRencanaEdukasi.getValueAt(i, 1).toString(), tbRencanaEdukasi.getValueAt(i, 2).toString()
                        });
                    }
                }
            }
            DetailRencana.setText(Rencana.getText());
            TNoRM1.setText(TNoRM.getText());
            TPasien1.setText(TPasien.getText());
            emptTeks();
            TabRawat.setSelectedIndex(1);
        }
    }

    private void simpan() {
        if (Sequel.menyimpantf("penilaian_awal_keperawatan_ralan_bayi", "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?", "No.Rawat", 121, new String[]{
            TNoRw.getText(), Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19), Informasi.getSelectedItem().toString(), TD.getText(), Nadi.getText(), RR.getText(),
            Suhu.getText(), GCS.getText(), BB.getText(), TB.getText(), LP.getText(), LK.getText(), LD.getText(), KeluhanUtama.getText(), RPD.getText(), RPK.getText(), RPO.getText(), Alergi.getText(), Anakke.getText(), DariSaudara.getText(),
            CaraKelahiran.getSelectedItem().toString(), KetCaraKelahiran.getText(), UmurKelahiran.getSelectedItem().toString(), KelainanBawaan.getSelectedItem().toString(), KetKelainanBawaan.getText(), UsiaTengkurap.getText(),
            UsiaDuduk.getText(), UsiaBerdiri.getText(), UsiaGigi.getText(), UsiaBerjalan.getText(), UsiaBicara.getText(), UsiaMembaca.getText(), UsiaMenulis.getText(), GangguanEmosi.getText(), AlatBantu.getSelectedItem().toString(),
            KetBantu.getText(), Prothesa.getSelectedItem().toString(), KetProthesa.getText(), ADL.getSelectedItem().toString(), StatusPsiko.getSelectedItem().toString(), KetPsiko.getText(), HubunganKeluarga.getSelectedItem().toString(),
            Pengasuh.getSelectedItem().toString(), KetPengasuh.getText(), Ekonomi.getSelectedItem().toString(), StatusBudaya.getSelectedItem().toString(), KetBudaya.getText(), Edukasi.getSelectedItem().toString(), KetEdukasi.getText(),
            SkalaResiko1.getSelectedItem().toString(), NilaiResiko1.getText(), SkalaResiko2.getSelectedItem().toString(), NilaiResiko2.getText(), SkalaResiko3.getSelectedItem().toString(), NilaiResiko3.getText(), SkalaResiko4.getSelectedItem().toString(),
            NilaiResiko4.getText(), SkalaResiko5.getSelectedItem().toString(), NilaiResiko5.getText(), SkalaResiko6.getSelectedItem().toString(), NilaiResiko6.getText(), SkalaResiko7.getSelectedItem().toString(), NilaiResiko7.getText(), NilaiResikoTotal.getText(), KeteranganTingkatRisiko.getText(),
            KemampuanBacaTulis.getSelectedItem().toString(), ButuhPenerjemah.getSelectedItem().toString(), KeteranganButuhPenerjemah.getText(), TerdapatHambatanBelajar.getSelectedItem().toString(), HambatanBelajar.getSelectedItem().toString(), KeteranganHambatanBelajar.getText(),
            HambatanCaraBicara.getSelectedItem().toString(), HambatanBahasaIsyarat.getSelectedItem().toString(), CaraBelajarDisukai.getSelectedItem().toString(), KesediaanMenerimaInformasi.getSelectedItem().toString(), KeteranganKesediaanMenerimaInformasi.getText(),
            PemahamanNutrisi.getSelectedItem().toString(), PemahamanPenyakit.getSelectedItem().toString(), PemahamanPengobatan.getSelectedItem().toString(), PemahamanPerawatan.getSelectedItem().toString(), KeyakinanNilai.getSelectedItem().toString(), KeterbatasanFisik.getSelectedItem().toString(),
            HambatanEmosional.getSelectedItem().toString(), Motivasi.getSelectedItem().toString(), SG1.getSelectedItem().toString(),
            NilaiGizi1.getText(), SG2.getSelectedItem().toString(), NilaiGizi2.getText(), SG3.getSelectedItem().toString(), NilaiGizi3.getText(), SG4.getSelectedItem().toString(), NilaiGizi4.getText(), TotalNilaiGizi.getText(),
            SkalaWajah.getSelectedItem().toString(), NilaiWajah.getText(), SkalaKaki.getSelectedItem().toString(), NilaiKaki.getText(), SkalaAktifitas.getSelectedItem().toString(), NilaiAktifitas.getText(), SkalaMenangis.getSelectedItem().toString(),
            NilaiMenangis.getText(), SkalaBersuara.getSelectedItem().toString(), NilaiBersuara.getText(), SkalaNyeri.getText(), Nyeri.getSelectedItem().toString(), Lokasi.getText(), Durasi.getText(), Frekuensi.getText(),
            NyeriHilang.getSelectedItem().toString(), KetNyeri.getText(), PadaDokter.getSelectedItem().toString(), KetDokter.getText(), InformasiPerencanaanPulang.getSelectedItem().toString(), LamaRatarata.getText(), Valid.SetTgl(TanggalPulang.getSelectedItem() + ""),
            KondisiPulang.getText(), PerawatanLanjutan.getText(), CaraTransportasiPulang.getSelectedItem().toString(), TransportasiYangDigunakan.getSelectedItem().toString(), Rencana.getText(), KdPetugas.getText()
        }) == true) {
            tabMode.addRow(new Object[]{
                TNoRw.getText(), TNoRM.getText(), TPasien.getText(), Jk.getText(), Agama.getText(), Bahasa.getText(), CacatFisik.getText(), TglLahir.getText(), Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19), Informasi.getSelectedItem().toString(),
                TD.getText(), Nadi.getText(), RR.getText(), Suhu.getText(), GCS.getText(), BB.getText(), TB.getText(), LP.getText(), LK.getText(), LD.getText(), KeluhanUtama.getText(), RPD.getText(), RPK.getText(), RPO.getText(), Alergi.getText(), Anakke.getText(), DariSaudara.getText(), CaraKelahiran.getSelectedItem().toString(),
                KetCaraKelahiran.getText(), UmurKelahiran.getSelectedItem().toString(), KelainanBawaan.getSelectedItem().toString(), KetKelainanBawaan.getText(), UsiaTengkurap.getText(), UsiaDuduk.getText(), UsiaBerdiri.getText(), UsiaGigi.getText(), UsiaBerjalan.getText(), UsiaBicara.getText(), UsiaMembaca.getText(),
                UsiaMenulis.getText(), GangguanEmosi.getText(), AlatBantu.getSelectedItem().toString(), KetBantu.getText(), Prothesa.getSelectedItem().toString(), KetProthesa.getText(), ADL.getSelectedItem().toString(), StatusPsiko.getSelectedItem().toString(), KetPsiko.getText(), HubunganKeluarga.getSelectedItem().toString(),
                Pengasuh.getSelectedItem().toString(), KetPengasuh.getText(), Ekonomi.getSelectedItem().toString(), StatusBudaya.getSelectedItem().toString(), KetBudaya.getText(), Edukasi.getSelectedItem().toString(), KetEdukasi.getText(), SkalaResiko1.getSelectedItem().toString(), NilaiResiko1.getText(), SkalaResiko2.getSelectedItem().toString(), NilaiResiko2.getText(), SkalaResiko3.getSelectedItem().toString(), NilaiResiko3.getText(), SkalaResiko4.getSelectedItem().toString(),
                NilaiResiko4.getText(), SkalaResiko5.getSelectedItem().toString(), NilaiResiko5.getText(), SkalaResiko6.getSelectedItem().toString(), NilaiResiko6.getText(), SkalaResiko7.getSelectedItem().toString(), NilaiResiko7.getText(), NilaiResikoTotal.getText(), KeteranganTingkatRisiko.getText(),
                KemampuanBacaTulis.getSelectedItem().toString(), ButuhPenerjemah.getSelectedItem().toString(), KeteranganButuhPenerjemah.getText(), TerdapatHambatanBelajar.getSelectedItem().toString(), HambatanBelajar.getSelectedItem().toString(), KeteranganHambatanBelajar.getText(),
                HambatanCaraBicara.getSelectedItem().toString(), HambatanBahasaIsyarat.getSelectedItem().toString(), CaraBelajarDisukai.getSelectedItem().toString(), KesediaanMenerimaInformasi.getSelectedItem().toString(), KeteranganKesediaanMenerimaInformasi.getText(),
                PemahamanNutrisi.getSelectedItem().toString(), PemahamanPenyakit.getSelectedItem().toString(), PemahamanPengobatan.getSelectedItem().toString(), PemahamanPerawatan.getSelectedItem().toString(), KeyakinanNilai.getSelectedItem().toString(), KeterbatasanFisik.getSelectedItem().toString(),
                HambatanEmosional.getSelectedItem().toString(), Motivasi.getSelectedItem().toString(),
                SG1.getSelectedItem().toString(), NilaiGizi1.getText(), SG2.getSelectedItem().toString(), NilaiGizi2.getText(), SG3.getSelectedItem().toString(), NilaiGizi3.getText(),
                SG4.getSelectedItem().toString(), NilaiGizi4.getText(), TotalNilaiGizi.getText(), SkalaWajah.getSelectedItem().toString(), NilaiWajah.getText(), SkalaKaki.getSelectedItem().toString(), NilaiKaki.getText(), SkalaAktifitas.getSelectedItem().toString(), NilaiAktifitas.getText(), SkalaMenangis.getSelectedItem().toString(),
                NilaiMenangis.getText(), SkalaBersuara.getSelectedItem().toString(), NilaiBersuara.getText(), SkalaNyeri.getText(), Nyeri.getSelectedItem().toString(), Lokasi.getText(), Durasi.getText(), Frekuensi.getText(), NyeriHilang.getSelectedItem().toString(), KetNyeri.getText(), PadaDokter.getSelectedItem().toString(),
                KetDokter.getText(), InformasiPerencanaanPulang.getSelectedItem().toString(), LamaRatarata.getText(), Valid.SetTgl(TanggalPulang.getSelectedItem() + ""),
                KondisiPulang.getText(), PerawatanLanjutan.getText(), CaraTransportasiPulang.getSelectedItem().toString(), TransportasiYangDigunakan.getSelectedItem().toString(), Rencana.getText(), KdPetugas.getText(), NmPetugas.getText()
            });
            LCount.setText("" + tabMode.getRowCount());
            Valid.tabelKosong(tabModeDetailMasalah);
            Valid.tabelKosong(tabModeDetailRencana);
            Valid.tabelKosong(tabModeDetailKebutuhanEdukasi);
            Valid.tabelKosong(tabModeDetailRencanaEdukasi);
            Valid.tabelKosong(tabModeImunisasi2);
            for (i = 0; i < tbMasalahKeperawatan.getRowCount(); i++) {
                if (tbMasalahKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                    if (Sequel.menyimpantf2("penilaian_awal_keperawatan_ralan_bayi_masalah", "?,?", 2, new String[]{TNoRw.getText(), tbMasalahKeperawatan.getValueAt(i, 1).toString()}) == true) {
                        tabModeDetailMasalah.addRow(new Object[]{
                            tbMasalahKeperawatan.getValueAt(i, 1).toString(), tbMasalahKeperawatan.getValueAt(i, 2).toString()
                        });
                    }
                }
            }
            for (i = 0; i < tbRencanaKeperawatan.getRowCount(); i++) {
                if (tbRencanaKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                    if (Sequel.menyimpantf2("penilaian_awal_keperawatan_ralan_rencana_anak", "?,?", 2, new String[]{TNoRw.getText(), tbRencanaKeperawatan.getValueAt(i, 1).toString()}) == true) {
                        tabModeDetailRencana.addRow(new Object[]{
                            tbRencanaKeperawatan.getValueAt(i, 1).toString(), tbRencanaKeperawatan.getValueAt(i, 2).toString()
                        });
                    }
                }
            }
            for (i = 0; i < tbImunisasi.getRowCount(); i++) {
                tabModeImunisasi2.addRow(new Object[]{
                    tbImunisasi.getValueAt(i, 0).toString(), tbImunisasi.getValueAt(i, 1).toString(), tbImunisasi.getValueAt(i, 2),
                    tbImunisasi.getValueAt(i, 3), tbImunisasi.getValueAt(i, 4), tbImunisasi.getValueAt(i, 5),
                    tbImunisasi.getValueAt(i, 6), tbImunisasi.getValueAt(i, 7)
                });
            }
            for (i = 0; i < tbKebutuhanEdukasi.getRowCount(); i++) {
                if (tbKebutuhanEdukasi.getValueAt(i, 0).toString().equals("true")) {
                    if (Sequel.menyimpantf2("penilaian_awal_keperawatan_ralan_bayi_kebutuhan_edukasi", "?,?", 2, new String[]{TNoRw.getText(), tbKebutuhanEdukasi.getValueAt(i, 1).toString()}) == true) {
                        tabModeDetailKebutuhanEdukasi.addRow(new Object[]{
                            tbKebutuhanEdukasi.getValueAt(i, 1).toString(), tbKebutuhanEdukasi.getValueAt(i, 2).toString()
                        });
                    }
                }
            }
            for (i = 0; i < tbRencanaEdukasi.getRowCount(); i++) {
                if (tbRencanaEdukasi.getValueAt(i, 0).toString().equals("true")) {
                    if (Sequel.menyimpantf2("penilaian_awal_keperawatan_ralan_bayi_rencana_edukasi", "?,?", 2, new String[]{TNoRw.getText(), tbRencanaEdukasi.getValueAt(i, 1).toString()}) == true) {
                        tabModeDetailRencanaEdukasi.addRow(new Object[]{
                            tbRencanaEdukasi.getValueAt(i, 1).toString(), tbRencanaEdukasi.getValueAt(i, 2).toString()
                        });
                    }
                }
            }
            DetailRencana.setText(Rencana.getText());
            TNoRM1.setText(TNoRM.getText());
            TPasien1.setText(TPasien.getText());
            emptTeks();
        }
    }

    private void LoadRencana() {
        tampilRencana();
        tampilRencana2();
    }

    private void LoadRencanaEdukasi() {
        tampilRencanaEdukasi();
        tampilRencanaEdukasi2();
    }

    private void runBackground(Runnable task) {
        if (ceksukses) {
            return;
        }
        if (executor.isShutdown() || executor.isTerminated()) {
            return;
        }
        if (!isDisplayable()) {
            return;
        }

        ceksukses = true;
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {
            executor.submit(() -> {
                try {
                    task.run();
                } finally {
                    ceksukses = false;
                    SwingUtilities.invokeLater(() -> {
                        if (isDisplayable()) {
                            setCursor(Cursor.getDefaultCursor());
                        }
                    });
                }
            });
        } catch (RejectedExecutionException ex) {
            ceksukses = false;
        }
    }

    @Override
    public void dispose() {
        executor.shutdownNow();
        super.dispose();
    }
}
