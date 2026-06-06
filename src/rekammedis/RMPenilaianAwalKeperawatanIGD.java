/*
 * Kontribusi dari Abdul Wahid, RSUD Cipayung Jakarta Timur
 */
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import kepegawaian.DlgCariDokter;
import kepegawaian.DlgCariPetugas;

/**
 *
 * @author perpustakaan
 */
public final class RMPenilaianAwalKeperawatanIGD extends javax.swing.JDialog {

    private final DefaultTableModel tabMode, tabModeMasalah, tabModeDetailMasalah, tabModeRencana, tabModeDetailRencana, tabModeKebutuhanEdukasi, tabModeRencanaEdukasi, tabModeDetailKebutuhanEdukasi, tabModeDetailRencanaEdukasi,tabModeDetailGiziAnak,tabModeDetailGiziDewasa;
    private Connection koneksi = koneksiDB.condb();
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private PreparedStatement ps, ps2;
    private ResultSet rs, rs2;
    private int i = 0, jml = 0, index = 0;
    private DlgCariPetugas petugas;
    private DlgCariDokter dokter;
    private MasterMasalahKeperawatanIGD masalahkeperawatan;
    private MasterRencanaKeperawatanIGD rencanakeperawatan;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean ceksukses = false;
    private boolean[] pilih;
    private String[] kode, masalah;
    private String masalahkeperawatanigd = "", finger = "",kebutuhanedukasi = "";
    private File file;
    private FileWriter fileWriter;
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNode root;
    private JsonNode response;
    private FileReader myObj;
    private String TANGGALMUNDUR = "yes";
    private String transportasi = "";

    /**
     * Creates new form DlgRujuk
     *
     * @param parent
     * @param modal
     */
    public RMPenilaianAwalKeperawatanIGD(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        tabMode = new DefaultTableModel(null, new Object[]{
            "No.Rawat", "No.RM", "Nama Pasien", "J.K.", "Agama", "Bahasa", "Cacat Fisik", "Tgl.Lahir", "Tgl.Asuhan", "Informasi", "Riwayat Penyakit Sekarang", "Riwayat Penyakit Dahulu", "Riwayat Penggunaan obat",
            "Status Hamil", "Gravida", "Para", "Abortus", "HPHT","Kesadaran","GCS","TD","RR","Suhu","Nadi" ,"Tekanan Intrakranial", "Pupil", "Neurosensorik/Muskuloskeletal", "Integumen", "Turgor Kulit", "Edema", "Mukosa Mulut", "Perdarahan",
            "Jml Perdarahan (cc)", "Warna Perdarahan", "Intoksikasi", "Frekuensi BAB", "x/", "Konsistensi BAB", "Warna BAB", "Frekuensi BAK", "x/", "Warna BAK", "Lain-lain BAK", "Kondisi Psikologis",
            "Gangguan Jiwa Di Masa Lalu", "Adakah Perilaku", "Dilaporkan Ke", "Sebutkan", "Hubungan Pasien Dengan Anggota Keluarga", "Status Pernikahan", "Tinggal Dengan", "Ket. Tinggal Dengan",
            "Pekerjaan", "Pembayaran", "Nilai-nilai Kebudayaan", "Ket. Nilai-nilai Kebudayaan", "Pendidikan Pasien", "Pendidikan PJ", "Ket. Pendidikan PJ",
            "Edukasi Diberikan Kepada", "Ket. Edukasi Diberikan Kepada", "Kemampuan Baca Tulis", "Butuh Penerjemah", "Ket.Butuh Penerjemah", "Terdapat Hambatan Belajar",
            "Hambatan Belajar", "Ket.Hambatan Belajar", "Hambatan Cara Bicara", "Hambatan Bahasa Isyarat", "Cara Belajar Disukai", "Kesediaan Menerima Informasi", "Ket.Kesediaan Menerima Informasi", "Pemahaman Nutrisi",
            "Pemahaman Penyakit", "Pemahaman Pengobatan", "Pemahaman Perawatan", "Keyakinan Nilai", "Keterbatasan Fisik", "Hambatan Emosional", "Motivasi", "Kemampuan Aktifitas Sehari-hari", "Aktifitas", "Alat bantu", "Ket. Alat bantu",
            "Tingkat Nyeri", "Provokes", "Ket. Provokes", "Kualitas", "Ket. Kualitas", "Lokasi", "Menyebar", "Skala Nyeri", "Durasi", "Nyeri Hilang", "Ket. Hilang Nyeri", "Lapor Ke Dokter",
            "Jam Lapor", "Cara Berjalan A", "Cara Berjalan B", "Cara Berjalan C", "Hasil Pengkajian Resiko Jatuh", "Lapor Dokter", "Ket. Lapor", "Informasi Perencanaan Pulang", "Lama Rata-Rata", "Tgl Pulang", "Kondisi Saat Pulang", "Perawatan Lanjutan", "Cara Transportasi", "Transportasi Digunakan", "Rencana","Kondisi Pasien","Cara Pulang","Tempat Rujuk","Indikasi Rujuk","Kamar Rawat Pasien","Kd DPJP","DPJP","Tanggal/Jam Keluar RS","Tgl/Jam Meninggal","Transportasi Pulang","Pendidikan Kesehatan", "NIP", "Nama Petugas"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tbObat.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 124; i++) {
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
                column.setPreferredWidth(200);
            } else if (i == 11) {
                column.setPreferredWidth(200);
            } else if (i == 12) {
                column.setPreferredWidth(200);
            } else if (i == 13) {
                column.setPreferredWidth(70);
            } else if (i == 14) {
                column.setPreferredWidth(50);
            } else if (i == 15) {
                column.setPreferredWidth(50);
            } else if (i == 16) {
                column.setPreferredWidth(50);
            } else if (i == 17) {
                column.setPreferredWidth(70);
            } else if (i == 18) {
                column.setPreferredWidth(110);
            } else if (i == 19) {
                column.setPreferredWidth(50);
            } else if (i == 20) {
                column.setPreferredWidth(160);
            } else if (i == 21) {
                column.setPreferredWidth(80);
            } else if (i == 22) {
                column.setPreferredWidth(67);
            } else if (i == 23) {
                column.setPreferredWidth(77);
            } else if (i == 24) {
                column.setPreferredWidth(77);
            } else if (i == 25) {
                column.setPreferredWidth(66);
            } else if (i == 26) {
                column.setPreferredWidth(107);
            } else if (i == 27) {
                column.setPreferredWidth(105);
            } else if (i == 28) {
                column.setPreferredWidth(90);
            } else if (i == 29) {
                column.setPreferredWidth(80);
            } else if (i == 30) {
                column.setPreferredWidth(65);
            } else if (i == 31) {
                column.setPreferredWidth(85);
            } else if (i == 32) {
                column.setPreferredWidth(80);
            } else if (i == 33) {
                column.setPreferredWidth(77);
            } else if (i == 34) {
                column.setPreferredWidth(65);
            } else if (i == 35) {
                column.setPreferredWidth(65);
            } else if (i == 36) {
                column.setPreferredWidth(80);
            } else if (i == 37) {
                column.setPreferredWidth(100);
            } else if (i == 38) {
                column.setPreferredWidth(147);
            } else if (i == 39) {
                column.setPreferredWidth(190);
            } else if (i == 40) {
                column.setPreferredWidth(90);
            } else if (i == 41) {
                column.setPreferredWidth(100);
            } else if (i == 42) {
                column.setPreferredWidth(220);
            } else if (i == 43) {
                column.setPreferredWidth(95);
            } else if (i == 44) {
                column.setPreferredWidth(85);
            } else if (i == 45) {
                column.setPreferredWidth(105);
            } else if (i == 46) {
                column.setPreferredWidth(100);
            } else if (i == 47) {
                column.setPreferredWidth(100);
            } else if (i == 48) {
                column.setPreferredWidth(120);
            } else if (i == 49) {
                column.setPreferredWidth(150);
            } else if (i == 50) {
                column.setPreferredWidth(97);
            } else if (i == 51) {
                column.setPreferredWidth(97);
            } else if (i == 52) {
                column.setPreferredWidth(110);
            } else if (i == 53) {
                column.setPreferredWidth(135);
            } else if (i == 54) {
                column.setPreferredWidth(155);
            } else if (i == 55) {
                column.setPreferredWidth(175);
            } else if (i == 56) {
                column.setPreferredWidth(65);
            } else if (i == 57) {
                column.setPreferredWidth(63);
            } else if (i == 58) {
                column.setPreferredWidth(97);
            } else if (i == 59) {
                column.setPreferredWidth(87);
            } else if (i == 60) {
                column.setPreferredWidth(85);
            } else if (i == 61) {
                column.setPreferredWidth(100);
            } else if (i == 62) {
                column.setPreferredWidth(90);
            } else if (i == 63) {
                column.setPreferredWidth(150);
            } else if (i == 64) {
                column.setPreferredWidth(80);
            } else if (i == 65) {
                column.setPreferredWidth(58);
            } else if (i == 66) {
                column.setPreferredWidth(65);
            } else if (i == 67) {
                column.setPreferredWidth(45);
            } else if (i == 68) {
                column.setPreferredWidth(85);
            } else if (i == 69) {
                column.setPreferredWidth(100);
            } else if (i == 70) {
                column.setPreferredWidth(85);
            } else if (i == 71) {
                column.setPreferredWidth(60);
            } else if (i == 72) {
                column.setPreferredWidth(85);
            } else if (i == 73) {
                column.setPreferredWidth(85);
            } else if (i == 74) {
                column.setPreferredWidth(85);
            } else if (i == 75) {
                column.setPreferredWidth(203);
            } else if (i == 76) {
                column.setPreferredWidth(70);
            } else if (i == 77) {
                column.setPreferredWidth(90);
            } else if (i == 78) {
                column.setPreferredWidth(210);
            } else if (i == 79) {
                column.setPreferredWidth(75);
            } else if (i == 80) {
                column.setPreferredWidth(150);
            } else if (i == 81) {
                column.setPreferredWidth(60);
            } else if (i == 82) {
                column.setPreferredWidth(85);
            } else if (i == 83) {
                column.setPreferredWidth(85);
            } else if (i == 84) {
                column.setPreferredWidth(85);
            } else if (i == 85) {
                column.setPreferredWidth(203);
            } else if (i == 86) {
                column.setPreferredWidth(70);
            } else if (i == 87) {
                column.setPreferredWidth(90);
            } else if (i == 88) {
                column.setPreferredWidth(210);
            } else if (i == 89) {
                column.setPreferredWidth(75);
            } else if (i == 90) {
                column.setPreferredWidth(150);
            } else if (i == 91) {
                column.setPreferredWidth(60);
            } else if (i == 92) {
                column.setPreferredWidth(85);
            } else if (i == 93) {
                column.setPreferredWidth(85);
            } else if (i == 94) {
                column.setPreferredWidth(85);
            } else if (i == 95) {
                column.setPreferredWidth(203);
            } else if (i == 96) {
                column.setPreferredWidth(70);
            } else if (i == 97) {
                column.setPreferredWidth(90);
            } else if (i == 98) {
                column.setPreferredWidth(210);
            } else if (i == 99) {
                column.setPreferredWidth(75);
            } else if (i == 100) {
                column.setPreferredWidth(150);
            } else if (i == 101) {
                column.setPreferredWidth(203);
            } else if (i == 102) {
                column.setPreferredWidth(70);
            } else if (i == 103) {
                column.setPreferredWidth(90);
            } else if (i == 104) {
                column.setPreferredWidth(210);
            } else if (i == 105) {
                column.setPreferredWidth(75);
            } else if (i == 106) {
                column.setPreferredWidth(150);
            }else if (i == 107) {
                column.setPreferredWidth(150);
            }else if (i == 108) {
                column.setPreferredWidth(150);
            }else if (i == 109) {
                column.setPreferredWidth(150);
            }else if (i == 110) {
                column.setPreferredWidth(150);
            }else if (i == 111) {
                column.setPreferredWidth(150);
            }else if (i == 112) {
                column.setPreferredWidth(150);
            }else if (i == 113) {
                column.setPreferredWidth(150);
            }else if (i == 114) {
                column.setPreferredWidth(150);
            }else if (i == 115) {
                column.setPreferredWidth(150);
            }else if (i == 116) {
                column.setPreferredWidth(150);
            }else if (i == 117) {
                column.setPreferredWidth(150);
            }else if (i == 118) {
                column.setPreferredWidth(150);
            }else if (i == 119) {
                column.setPreferredWidth(150);
            }else if (i == 120) {
                column.setPreferredWidth(150);
            }else if (i == 121) {
                column.setPreferredWidth(150);
            }else if (i == 122) {
                column.setPreferredWidth(150);
            }else if (i == 123) {
                column.setPreferredWidth(150);
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
        KeluhanUtama.setDocument(new batasInput((int) 150).getKata(KeluhanUtama));
        RPD.setDocument(new batasInput((int) 100).getKata(RPD));
        RPO.setDocument(new batasInput((int) 100).getKata(RPO));
        Gravida.setDocument(new batasInput((byte) 20).getKata(Gravida));
        Para.setDocument(new batasInput((byte) 20).getKata(Para));
        Abortus.setDocument(new batasInput((byte) 20).getKata(Abortus));
        HPHT.setDocument(new batasInput((byte) 20).getKata(HPHT));
        JumlahPerdarahan.setDocument(new batasInput((byte) 5).getKata(JumlahPerdarahan));
        WarnaPerdarahan.setDocument(new batasInput((int) 40).getKata(WarnaPerdarahan));
        BAB.setDocument(new batasInput((byte) 2).getKata(BAB));
        XBAB.setDocument(new batasInput((byte) 10).getKata(XBAB));
        KBAB.setDocument(new batasInput((int) 40).getKata(KBAB));
        WBAB.setDocument(new batasInput((int) 40).getKata(WBAB));
        BAK.setDocument(new batasInput((byte) 2).getKata(BAK));
        XBAK.setDocument(new batasInput((byte) 10).getKata(XBAK));
        WBAK.setDocument(new batasInput((int) 40).getKata(WBAK));
        LBAK.setDocument(new batasInput((int) 40).getKata(LBAK));
        Dilaporkan.setDocument(new batasInput((int) 50).getKata(Dilaporkan));
        Sebutkan.setDocument(new batasInput((int) 50).getKata(Sebutkan));
        KetTinggal.setDocument(new batasInput((int) 50).getKata(KetTinggal));
        KetBudaya.setDocument(new batasInput((int) 50).getKata(KetBudaya));
        KetPendidikanPJ.setDocument(new batasInput((int) 50).getKata(KetPendidikanPJ));
        KetEdukasi.setDocument(new batasInput((int) 50).getKata(KetEdukasi));
        KetAlatBantu.setDocument(new batasInput((int) 50).getKata(KetAlatBantu));
        KetProvokes.setDocument(new batasInput((int) 40).getKata(KetProvokes));
        KetQuality.setDocument(new batasInput((int) 50).getKata(KetQuality));
        Lokasi.setDocument(new batasInput((int) 50).getKata(Lokasi));
        Durasi.setDocument(new batasInput((int) 25).getKata(Durasi));
        KetNyeri.setDocument(new batasInput((int) 40).getKata(KetNyeri));
        KetDokter.setDocument(new batasInput((byte) 15).getKata(KetDokter));
        KetLapor.setDocument(new batasInput((int) 15).getKata(KetLapor));
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
        
        tabModeDetailGiziAnak = new DefaultTableModel(null, new Object[]{
            "Kode", "Masalah Keperawatan"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tbDetailGiziAnak.setModel(tabModeDetailGiziAnak);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbDetailGiziAnak.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbDetailGiziAnak.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 2; i++) {
            TableColumn column = tbDetailGiziAnak.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 1) {
                column.setPreferredWidth(420);
            }
        }
        tbMasalahDetailMasalah.setDefaultRenderer(Object.class, new WarnaTable());

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
        TanggalRegistrasi = new widget.TextBox();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        javax.swing.ButtonGroup buttonGroup3 = new javax.swing.ButtonGroup();
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
        jLabel9 = new widget.Label();
        Jk = new widget.TextBox();
        jLabel10 = new widget.Label();
        label11 = new widget.Label();
        jLabel11 = new widget.Label();
        jLabel50 = new widget.Label();
        jLabel52 = new widget.Label();
        scrollPane1 = new widget.ScrollPane();
        KeluhanUtama = new widget.TextArea();
        jLabel30 = new widget.Label();
        scrollPane2 = new widget.ScrollPane();
        RPD = new widget.TextArea();
        jLabel31 = new widget.Label();
        scrollPane4 = new widget.ScrollPane();
        RPO = new widget.TextArea();
        Aktifitas = new widget.ComboBox();
        AlatBantu = new widget.ComboBox();
        KetAlatBantu = new widget.TextBox();
        jLabel55 = new widget.Label();
        ADL = new widget.ComboBox();
        jLabel57 = new widget.Label();
        jLabel58 = new widget.Label();
        TinggalDengan = new widget.ComboBox();
        KetTinggal = new widget.TextBox();
        jLabel60 = new widget.Label();
        Edukasi = new widget.ComboBox();
        KetEdukasi = new widget.TextBox();
        jLabel64 = new widget.Label();
        jLabel65 = new widget.Label();
        jLabel66 = new widget.Label();
        Lapor = new widget.ComboBox();
        ATS = new widget.ComboBox();
        BJM = new widget.ComboBox();
        jLabel67 = new widget.Label();
        Hasil = new widget.ComboBox();
        jLabel68 = new widget.Label();
        KetLapor = new widget.TextBox();
        jLabel70 = new widget.Label();
        jLabel72 = new widget.Label();
        MSA = new widget.ComboBox();
        Nyeri = new widget.ComboBox();
        Provokes = new widget.ComboBox();
        KetProvokes = new widget.TextBox();
        jLabel80 = new widget.Label();
        Quality = new widget.ComboBox();
        KetQuality = new widget.TextBox();
        jLabel81 = new widget.Label();
        jLabel82 = new widget.Label();
        Lokasi = new widget.TextBox();
        jLabel83 = new widget.Label();
        Menyebar = new widget.ComboBox();
        jLabel84 = new widget.Label();
        jLabel85 = new widget.Label();
        SkalaNyeri = new widget.ComboBox();
        jLabel86 = new widget.Label();
        Durasi = new widget.TextBox();
        jLabel87 = new widget.Label();
        jLabel88 = new widget.Label();
        NyeriHilang = new widget.ComboBox();
        KetNyeri = new widget.TextBox();
        jLabel89 = new widget.Label();
        PadaDokter = new widget.ComboBox();
        KetDokter = new widget.TextBox();
        TglAsuhan = new widget.Tanggal();
        jLabel94 = new widget.Label();
        jLabel51 = new widget.Label();
        CacatFisik = new widget.TextBox();
        jLabel56 = new widget.Label();
        jLabel95 = new widget.Label();
        StatusBudaya = new widget.ComboBox();
        KetBudaya = new widget.TextBox();
        jLabel63 = new widget.Label();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        PanelWall = new usu.widget.glass.PanelGlass();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel71 = new widget.Label();
        jSeparator10 = new javax.swing.JSeparator();
        Bahasa = new widget.TextBox();
        jLabel76 = new widget.Label();
        jLabel77 = new widget.Label();
        Agama = new widget.TextBox();
        jLabel78 = new widget.Label();
        StatusKehamilan = new widget.ComboBox();
        jLabel29 = new widget.Label();
        Gravida = new widget.TextBox();
        jLabel32 = new widget.Label();
        Para = new widget.TextBox();
        jLabel33 = new widget.Label();
        Abortus = new widget.TextBox();
        jLabel35 = new widget.Label();
        HPHT = new widget.TextBox();
        jSeparator11 = new javax.swing.JSeparator();
        jLabel98 = new widget.Label();
        jLabel90 = new widget.Label();
        Tekanan = new widget.ComboBox();
        jLabel91 = new widget.Label();
        Pupil = new widget.ComboBox();
        Neurosensorik = new widget.ComboBox();
        jLabel100 = new widget.Label();
        jLabel101 = new widget.Label();
        Integumen = new widget.ComboBox();
        jLabel102 = new widget.Label();
        Turgor = new widget.ComboBox();
        jLabel103 = new widget.Label();
        Edema = new widget.ComboBox();
        jLabel104 = new widget.Label();
        Mukosa = new widget.ComboBox();
        jLabel105 = new widget.Label();
        Perdarahan = new widget.ComboBox();
        jLabel36 = new widget.Label();
        JumlahPerdarahan = new widget.TextBox();
        jLabel37 = new widget.Label();
        WarnaPerdarahan = new widget.TextBox();
        jLabel38 = new widget.Label();
        jLabel106 = new widget.Label();
        Intoksikasi = new widget.ComboBox();
        jLabel107 = new widget.Label();
        jLabel108 = new widget.Label();
        KBAB = new widget.TextBox();
        jLabel109 = new widget.Label();
        BAB = new widget.TextBox();
        jLabel110 = new widget.Label();
        XBAB = new widget.TextBox();
        jLabel111 = new widget.Label();
        WBAB = new widget.TextBox();
        jLabel112 = new widget.Label();
        BAK = new widget.TextBox();
        jLabel113 = new widget.Label();
        XBAK = new widget.TextBox();
        jLabel114 = new widget.Label();
        WBAK = new widget.TextBox();
        jLabel115 = new widget.Label();
        LBAK = new widget.TextBox();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel116 = new widget.Label();
        jLabel117 = new widget.Label();
        Psikologis = new widget.ComboBox();
        jLabel119 = new widget.Label();
        Jiwa = new widget.ComboBox();
        jLabel120 = new widget.Label();
        Perilaku = new widget.ComboBox();
        jLabel118 = new widget.Label();
        Dilaporkan = new widget.TextBox();
        jLabel121 = new widget.Label();
        Sebutkan = new widget.TextBox();
        jLabel122 = new widget.Label();
        Hubungan = new widget.ComboBox();
        jLabel123 = new widget.Label();
        StatusPernikahan = new widget.TextBox();
        jLabel124 = new widget.Label();
        Pekerjaan = new widget.TextBox();
        jLabel125 = new widget.Label();
        Pembayaran = new widget.TextBox();
        jLabel126 = new widget.Label();
        PendidikanPasien = new widget.TextBox();
        jLabel127 = new widget.Label();
        PendidikanPJ = new widget.ComboBox();
        KetPendidikanPJ = new widget.TextBox();
        jLabel39 = new widget.Label();
        Informasi = new widget.ComboBox();
        Scroll8 = new widget.ScrollPane();
        tbMasalahKeperawatan = new widget.Table();
        BtnTambahMasalah = new widget.Button();
        BtnAllMasalah = new widget.Button();
        BtnCariMasalah = new widget.Button();
        TCariMasalah = new widget.TextBox();
        label12 = new widget.Label();
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
        jSeparator7 = new javax.swing.JSeparator();
        jLabel99 = new widget.Label();
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
        jSeparator13 = new javax.swing.JSeparator();
        Scroll11 = new widget.ScrollPane();
        tbKebutuhanEdukasi = new widget.Table();
        label15 = new widget.Label();
        BtnTambahKebutuhanEdukasi = new widget.Button();
        BtnAllKebutuhanEdukasi = new widget.Button();
        BtnCariKebutuhanEdukasi = new widget.Button();
        TabRencanaKeperawatan1 = new javax.swing.JTabbedPane();
        panelBiasa2 = new widget.PanelBiasa();
        Scroll12 = new widget.ScrollPane();
        tbRencanaEdukasi = new widget.Table();
        TCariMasalah1 = new widget.TextBox();
        BtnTambahRencanaEdukasi = new widget.Button();
        BtnAllRencanaEdukasi = new widget.Button();
        BtnCariRencanaEdukasi = new widget.Button();
        label16 = new widget.Label();
        TCariRencana1 = new widget.TextBox();
        jSeparator15 = new javax.swing.JSeparator();
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
        scrollPane8 = new widget.ScrollPane();
        jLabel263 = new widget.Label();
        CaraTransportasiPulang = new widget.ComboBox();
        jLabel264 = new widget.Label();
        jLabel265 = new widget.Label();
        jLabel266 = new widget.Label();
        TransportasiYangDigunakan = new widget.ComboBox();
        GCS = new widget.TextBox();
        TD = new widget.TextBox();
        jLabel40 = new widget.Label();
        jLabel23 = new widget.Label();
        jLabel26 = new widget.Label();
        RR = new widget.TextBox();
        jLabel25 = new widget.Label();
        jLabel18 = new widget.Label();
        Suhu = new widget.TextBox();
        jLabel20 = new widget.Label();
        Nadi = new widget.TextBox();
        jLabel17 = new widget.Label();
        jLabel16 = new widget.Label();
        jLabel202 = new widget.Label();
        Kesadaran = new widget.ComboBox();
        jLabel22 = new widget.Label();
        jLabel92 = new widget.Label();
        jLabel278 = new widget.Label();
        jLabel73 = new widget.Label();
        jLabel41 = new widget.Label();
        SG1Anak = new widget.ComboBox();
        jLabel75 = new widget.Label();
        jLabel93 = new widget.Label();
        jLabel43 = new widget.Label();
        SG2Anak = new widget.ComboBox();
        jLabel74 = new widget.Label();
        jLabel79 = new widget.Label();
        TotalHasilAnak = new widget.TextBox();
        jLabel44 = new widget.Label();
        Nilai1Anak = new widget.TextBox();
        Nilai2Anak = new widget.TextBox();
        jLabel45 = new widget.Label();
        SG3Anak = new widget.ComboBox();
        jLabel96 = new widget.Label();
        Nilai3Anak = new widget.TextBox();
        jLabel138 = new widget.Label();
        Nilai4Anak = new widget.TextBox();
        SG4Anak = new widget.ComboBox();
        jLabel46 = new widget.Label();
        LabelSkrining = new widget.Label();
        jLabel158 = new widget.Label();
        jLabel154 = new widget.Label();
        jLabel139 = new widget.Label();
        jLabel155 = new widget.Label();
        jLabel159 = new widget.Label();
        jLabel160 = new widget.Label();
        jLabel42 = new widget.Label();
        SG5Dewasa = new widget.ComboBox();
        Nilai5Dewasa = new widget.ComboBox();
        jLabel140 = new widget.Label();
        jLabel141 = new widget.Label();
        jLabel47 = new widget.Label();
        SG6Dewasa = new widget.ComboBox();
        jLabel142 = new widget.Label();
        Nilai6Dewasa = new widget.ComboBox();
        jLabel143 = new widget.Label();
        TotalHasilDewasa = new widget.TextBox();
        jLabel48 = new widget.Label();
        jSeparator16 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel97 = new widget.Label();
        jLabel267 = new widget.Label();
        StatusRawat = new widget.ComboBox();
        jLabel128 = new widget.Label();
        jLabel268 = new widget.Label();
        StatusRawat1 = new widget.ComboBox();
        jLabel269 = new widget.Label();
        StatusRawat2 = new widget.ComboBox();
        jLabel270 = new widget.Label();
        KondisiPulang1 = new widget.TextBox();
        jLabel271 = new widget.Label();
        KdPetugas1 = new widget.TextBox();
        NmPetugas1 = new widget.TextBox();
        BtnPetugas1 = new widget.Button();
        PerawatanLanjutan = new widget.TextArea();
        jLabel279 = new widget.Label();
        StatusRawat3 = new widget.ComboBox();
        jLabel280 = new widget.Label();
        KondisiPulang2 = new widget.TextBox();
        jLabel281 = new widget.Label();
        KondisiPulang3 = new widget.TextBox();
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
        SkrinningGizi = new widget.PanelBiasa();
        Scroll17 = new widget.ScrollPane();
        tbDetailGiziAnak = new widget.Table();
        Scroll18 = new widget.ScrollPane();
        tbDetailGiziDewasa = new widget.Table();

        LoadHTML.setBorder(null);
        LoadHTML.setName("LoadHTML"); // NOI18N

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

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Pengkajian Awal Keperawatan IGD ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
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
        FormInput.setPreferredSize(new java.awt.Dimension(870, 2597));
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
        TPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TPasienActionPerformed(evt);
            }
        });
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
        KdPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdPetugasKeyPressed(evt);
            }
        });
        FormInput.add(KdPetugas);
        KdPetugas.setBounds(70, 40, 100, 23);

        NmPetugas.setEditable(false);
        NmPetugas.setName("NmPetugas"); // NOI18N
        NmPetugas.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(NmPetugas);
        NmPetugas.setBounds(170, 40, 180, 23);

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
        BtnPetugas.setBounds(350, 40, 28, 23);

        jLabel8.setText("Tgl.Lahir :");
        jLabel8.setName("jLabel8"); // NOI18N
        FormInput.add(jLabel8);
        jLabel8.setBounds(580, 10, 60, 23);

        TglLahir.setEditable(false);
        TglLahir.setHighlighter(null);
        TglLahir.setName("TglLahir"); // NOI18N
        FormInput.add(TglLahir);
        TglLahir.setBounds(644, 10, 80, 23);

        jLabel9.setText("Riwayat Penggunaan Obat :");
        jLabel9.setName("jLabel9"); // NOI18N
        FormInput.add(jLabel9);
        jLabel9.setBounds(440, 90, 150, 23);

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

        jLabel50.setText("d. Alat Bantu :");
        jLabel50.setName("jLabel50"); // NOI18N
        FormInput.add(jLabel50);
        jLabel50.setBounds(10, 1250, 112, 23);

        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel52.setText("V. SKALA NYERI");
        jLabel52.setName("jLabel52"); // NOI18N
        FormInput.add(jLabel52);
        jLabel52.setBounds(20, 1280, 380, 23);

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
        scrollPane1.setBounds(179, 90, 260, 53);

        jLabel30.setText("Riwayat Penyakit Sekarang :");
        jLabel30.setName("jLabel30"); // NOI18N
        FormInput.add(jLabel30);
        jLabel30.setBounds(0, 90, 175, 20);

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
        scrollPane2.setBounds(179, 150, 260, 53);

        jLabel31.setText("Riwayat Penyakit Dahulu :");
        jLabel31.setName("jLabel31"); // NOI18N
        FormInput.add(jLabel31);
        jLabel31.setBounds(0, 150, 175, 23);

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
        scrollPane4.setBounds(594, 90, 260, 53);

        Aktifitas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tirah Baring", "Duduk", "Berjalan" }));
        Aktifitas.setName("Aktifitas"); // NOI18N
        Aktifitas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AktifitasKeyPressed(evt);
            }
        });
        FormInput.add(Aktifitas);
        Aktifitas.setBounds(500, 1220, 112, 23);

        AlatBantu.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        AlatBantu.setName("AlatBantu"); // NOI18N
        AlatBantu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AlatBantuKeyPressed(evt);
            }
        });
        FormInput.add(AlatBantu);
        AlatBantu.setBounds(130, 1250, 80, 23);

        KetAlatBantu.setFocusTraversalPolicyProvider(true);
        KetAlatBantu.setName("KetAlatBantu"); // NOI18N
        KetAlatBantu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetAlatBantuKeyPressed(evt);
            }
        });
        FormInput.add(KetAlatBantu);
        KetAlatBantu.setBounds(210, 1250, 402, 23);

        jLabel55.setText("b. Aktifitas :");
        jLabel55.setName("jLabel55"); // NOI18N
        FormInput.add(jLabel55);
        jLabel55.setBounds(420, 1220, 80, 23);

        ADL.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mandiri", "Bantuan Minimal", "Bantuan Sebagian", "Ketergantungan Total" }));
        ADL.setSelectedIndex(3);
        ADL.setName("ADL"); // NOI18N
        ADL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ADLKeyPressed(evt);
            }
        });
        FormInput.add(ADL);
        ADL.setBounds(240, 1220, 160, 23);

        jLabel57.setText("a. Kemampuan Aktifitas Sehari-hari :");
        jLabel57.setName("jLabel57"); // NOI18N
        FormInput.add(jLabel57);
        jLabel57.setBounds(10, 1220, 223, 23);

        jLabel58.setText("n. Edukasi Diberikan Kepada :");
        jLabel58.setName("jLabel58"); // NOI18N
        FormInput.add(jLabel58);
        jLabel58.setBounds(450, 620, 160, 23);

        TinggalDengan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sendiri", "Orang Tua", "Suami / Istri", "Lainnya" }));
        TinggalDengan.setName("TinggalDengan"); // NOI18N
        TinggalDengan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TinggalDenganKeyPressed(evt);
            }
        });
        FormInput.add(TinggalDengan);
        TinggalDengan.setBounds(150, 560, 112, 23);

        KetTinggal.setFocusTraversalPolicyProvider(true);
        KetTinggal.setName("KetTinggal"); // NOI18N
        KetTinggal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetTinggalKeyPressed(evt);
            }
        });
        FormInput.add(KetTinggal);
        KetTinggal.setBounds(270, 560, 126, 23);

        jLabel60.setText("h. Tinggal Dengan :");
        jLabel60.setName("jLabel60"); // NOI18N
        FormInput.add(jLabel60);
        jLabel60.setBounds(10, 560, 138, 23);

        Edukasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pasien", "Keluarga" }));
        Edukasi.setName("Edukasi"); // NOI18N
        Edukasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EdukasiKeyPressed(evt);
            }
        });
        FormInput.add(Edukasi);
        Edukasi.setBounds(610, 620, 100, 23);

        KetEdukasi.setFocusTraversalPolicyProvider(true);
        KetEdukasi.setName("KetEdukasi"); // NOI18N
        KetEdukasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetEdukasiKeyPressed(evt);
            }
        });
        FormInput.add(KetEdukasi);
        KetEdukasi.setBounds(710, 620, 150, 23);

        jLabel64.setText("Jam  :");
        jLabel64.setName("jLabel64"); // NOI18N
        FormInput.add(jLabel64);
        jLabel64.setBounds(730, 1450, 50, 23);

        jLabel65.setText("1. Tidak seimbang / sempoyongan / limbung :");
        jLabel65.setName("jLabel65"); // NOI18N
        FormInput.add(jLabel65);
        jLabel65.setBounds(40, 1540, 250, 23);

        jLabel66.setText("2. Jalan dengan menggunakan alat bantu (kruk, tripot, kursi roda, orang lain) :");
        jLabel66.setName("jLabel66"); // NOI18N
        FormInput.add(jLabel66);
        jLabel66.setBounds(380, 1540, 400, 23);

        Lapor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        Lapor.setName("Lapor"); // NOI18N
        Lapor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LaporKeyPressed(evt);
            }
        });
        FormInput.add(Lapor);
        Lapor.setBounds(580, 1600, 80, 23);

        ATS.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        ATS.setName("ATS"); // NOI18N
        ATS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ATSKeyPressed(evt);
            }
        });
        FormInput.add(ATS);
        ATS.setBounds(300, 1540, 80, 23);

        BJM.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        BJM.setName("BJM"); // NOI18N
        BJM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BJMKeyPressed(evt);
            }
        });
        FormInput.add(BJM);
        BJM.setBounds(780, 1540, 80, 23);

        jLabel67.setText("Menyebar :");
        jLabel67.setName("jLabel67"); // NOI18N
        FormInput.add(jLabel67);
        jLabel67.setBounds(700, 1380, 79, 23);

        Hasil.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak beresiko (tidak ditemukan a dan b)", "Resiko rendah (ditemukan a/b)", "Resiko tinggi (ditemukan a dan b)" }));
        Hasil.setName("Hasil"); // NOI18N
        Hasil.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HasilKeyPressed(evt);
            }
        });
        FormInput.add(Hasil);
        Hasil.setBounds(80, 1600, 293, 23);

        jLabel68.setText("Hasil :");
        jLabel68.setName("jLabel68"); // NOI18N
        FormInput.add(jLabel68);
        jLabel68.setBounds(10, 1600, 72, 23);

        KetLapor.setFocusTraversalPolicyProvider(true);
        KetLapor.setName("KetLapor"); // NOI18N
        KetLapor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetLaporKeyPressed(evt);
            }
        });
        FormInput.add(KetLapor);
        KetLapor.setBounds(780, 1600, 80, 23);

        jLabel70.setText("b. Menopang saat akan duduk, tampak memegang pinggiran kursi atau meja / benda lain sebagai penopang :");
        jLabel70.setName("jLabel70"); // NOI18N
        FormInput.add(jLabel70);
        jLabel70.setBounds(10, 1570, 571, 23);

        jLabel72.setText("a. Cara Berjalan :");
        jLabel72.setName("jLabel72"); // NOI18N
        FormInput.add(jLabel72);
        jLabel72.setBounds(10, 1520, 126, 23);

        MSA.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        MSA.setName("MSA"); // NOI18N
        MSA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MSAKeyPressed(evt);
            }
        });
        FormInput.add(MSA);
        MSA.setBounds(580, 1570, 80, 23);

        Nyeri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada Nyeri", "Nyeri Akut", "Nyeri Kronis" }));
        Nyeri.setName("Nyeri"); // NOI18N
        Nyeri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NyeriKeyPressed(evt);
            }
        });
        FormInput.add(Nyeri);
        Nyeri.setBounds(380, 1300, 130, 23);

        Provokes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Proses Penyakit", "Benturan", "Lain-lain" }));
        Provokes.setName("Provokes"); // NOI18N
        Provokes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ProvokesKeyPressed(evt);
            }
        });
        FormInput.add(Provokes);
        Provokes.setBounds(580, 1300, 130, 23);

        KetProvokes.setFocusTraversalPolicyProvider(true);
        KetProvokes.setName("KetProvokes"); // NOI18N
        KetProvokes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetProvokesKeyPressed(evt);
            }
        });
        FormInput.add(KetProvokes);
        KetProvokes.setBounds(710, 1300, 146, 23);

        jLabel80.setText("Penyebab :");
        jLabel80.setName("jLabel80"); // NOI18N
        FormInput.add(jLabel80);
        jLabel80.setBounds(520, 1300, 60, 23);

        Quality.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seperti Tertusuk", "Berdenyut", "Teriris", "Tertindih", "Tertiban", "Lain-lain" }));
        Quality.setName("Quality"); // NOI18N
        Quality.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                QualityKeyPressed(evt);
            }
        });
        FormInput.add(Quality);
        Quality.setBounds(440, 1330, 140, 23);

        KetQuality.setFocusTraversalPolicyProvider(true);
        KetQuality.setName("KetQuality"); // NOI18N
        KetQuality.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetQualityKeyPressed(evt);
            }
        });
        FormInput.add(KetQuality);
        KetQuality.setBounds(580, 1330, 281, 23);

        jLabel81.setText("Kualitas :");
        jLabel81.setName("jLabel81"); // NOI18N
        FormInput.add(jLabel81);
        jLabel81.setBounds(380, 1330, 55, 23);

        jLabel82.setText("Wilayah :");
        jLabel82.setName("jLabel82"); // NOI18N
        FormInput.add(jLabel82);
        jLabel82.setBounds(380, 1360, 55, 23);

        Lokasi.setFocusTraversalPolicyProvider(true);
        Lokasi.setName("Lokasi"); // NOI18N
        Lokasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LokasiKeyPressed(evt);
            }
        });
        FormInput.add(Lokasi);
        Lokasi.setBounds(460, 1380, 220, 23);

        jLabel83.setText("Lokasi :");
        jLabel83.setName("jLabel83"); // NOI18N
        FormInput.add(jLabel83);
        jLabel83.setBounds(400, 1380, 60, 23);

        Menyebar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        Menyebar.setName("Menyebar"); // NOI18N
        Menyebar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MenyebarKeyPressed(evt);
            }
        });
        FormInput.add(Menyebar);
        Menyebar.setBounds(780, 1380, 80, 23);

        jLabel84.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel84.setText("Menit");
        jLabel84.setName("jLabel84"); // NOI18N
        FormInput.add(jLabel84);
        jLabel84.setBounds(820, 1410, 35, 23);

        jLabel85.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel85.setText("Skala Nyeri");
        jLabel85.setName("jLabel85"); // NOI18N
        FormInput.add(jLabel85);
        jLabel85.setBounds(430, 1410, 60, 23);

        SkalaNyeri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        SkalaNyeri.setName("SkalaNyeri"); // NOI18N
        SkalaNyeri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaNyeriKeyPressed(evt);
            }
        });
        FormInput.add(SkalaNyeri);
        SkalaNyeri.setBounds(500, 1410, 70, 23);

        jLabel86.setText("Diberitahukan pada dokter ?");
        jLabel86.setName("jLabel86"); // NOI18N
        FormInput.add(jLabel86);
        jLabel86.setBounds(490, 1450, 150, 23);

        Durasi.setFocusTraversalPolicyProvider(true);
        Durasi.setName("Durasi"); // NOI18N
        Durasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DurasiKeyPressed(evt);
            }
        });
        FormInput.add(Durasi);
        Durasi.setBounds(730, 1410, 90, 23);

        jLabel87.setText("Waktu / Durasi :");
        jLabel87.setName("jLabel87"); // NOI18N
        FormInput.add(jLabel87);
        jLabel87.setBounds(630, 1410, 90, 23);

        jLabel88.setText("Severity :");
        jLabel88.setName("jLabel88"); // NOI18N
        FormInput.add(jLabel88);
        jLabel88.setBounds(380, 1410, 55, 23);

        NyeriHilang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Istirahat", "Medengar Musik", "Minum Obat" }));
        NyeriHilang.setName("NyeriHilang"); // NOI18N
        NyeriHilang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NyeriHilangKeyPressed(evt);
            }
        });
        FormInput.add(NyeriHilang);
        NyeriHilang.setBounds(140, 1450, 130, 23);

        KetNyeri.setFocusTraversalPolicyProvider(true);
        KetNyeri.setName("KetNyeri"); // NOI18N
        KetNyeri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetNyeriKeyPressed(evt);
            }
        });
        FormInput.add(KetNyeri);
        KetNyeri.setBounds(280, 1450, 150, 23);

        jLabel89.setText("Nyeri hilang bila :");
        jLabel89.setName("jLabel89"); // NOI18N
        FormInput.add(jLabel89);
        jLabel89.setBounds(10, 1450, 130, 23);

        PadaDokter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PadaDokter.setName("PadaDokter"); // NOI18N
        PadaDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PadaDokterKeyPressed(evt);
            }
        });
        FormInput.add(PadaDokter);
        PadaDokter.setBounds(640, 1450, 80, 23);

        KetDokter.setFocusTraversalPolicyProvider(true);
        KetDokter.setName("KetDokter"); // NOI18N
        KetDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetDokterKeyPressed(evt);
            }
        });
        FormInput.add(KetDokter);
        KetDokter.setBounds(780, 1450, 80, 23);

        TglAsuhan.setForeground(new java.awt.Color(50, 70, 50));
        TglAsuhan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "06-06-2026 12:59:25" }));
        TglAsuhan.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        TglAsuhan.setName("TglAsuhan"); // NOI18N
        TglAsuhan.setOpaque(false);
        TglAsuhan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TglAsuhanKeyPressed(evt);
            }
        });
        FormInput.add(TglAsuhan);
        TglAsuhan.setBounds(460, 40, 130, 23);

        jLabel94.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel94.setText("III. RIWAYAT PSIKOLOGIS - SOSIAL - EKONOMI - BUDAYA - SPIRITUAL");
        jLabel94.setName("jLabel94"); // NOI18N
        FormInput.add(jLabel94);
        jLabel94.setBounds(20, 450, 360, 23);

        jLabel51.setText("c. Cacat Fisik :");
        jLabel51.setName("jLabel51"); // NOI18N
        FormInput.add(jLabel51);
        jLabel51.setBounds(630, 1220, 100, 23);

        CacatFisik.setEditable(false);
        CacatFisik.setFocusTraversalPolicyProvider(true);
        CacatFisik.setName("CacatFisik"); // NOI18N
        FormInput.add(CacatFisik);
        CacatFisik.setBounds(730, 1220, 135, 23);

        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel56.setText("IV. PENGKAJIAN FUNGSI");
        jLabel56.setName("jLabel56"); // NOI18N
        FormInput.add(jLabel56);
        jLabel56.setBounds(20, 1200, 230, 23);

        jLabel95.setText("l. Kepercayaan / Budaya / Nilai-nilai Khusus Yang Perlu Diperhatikan :");
        jLabel95.setName("jLabel95"); // NOI18N
        FormInput.add(jLabel95);
        jLabel95.setBounds(230, 590, 350, 23);

        StatusBudaya.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Ada" }));
        StatusBudaya.setName("StatusBudaya"); // NOI18N
        StatusBudaya.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                StatusBudayaKeyPressed(evt);
            }
        });
        FormInput.add(StatusBudaya);
        StatusBudaya.setBounds(580, 590, 100, 23);

        KetBudaya.setFocusTraversalPolicyProvider(true);
        KetBudaya.setName("KetBudaya"); // NOI18N
        KetBudaya.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetBudayaKeyPressed(evt);
            }
        });
        FormInput.add(KetBudaya);
        KetBudaya.setBounds(680, 590, 180, 23);

        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel63.setText("VI. SKRINING GIZI");
        jLabel63.setName("jLabel63"); // NOI18N
        FormInput.add(jLabel63);
        jLabel63.setBounds(10, 1640, 380, 23);

        jSeparator1.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator1.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator1.setName("jSeparator1"); // NOI18N
        FormInput.add(jSeparator1);
        jSeparator1.setBounds(0, 70, 880, 1);

        jSeparator4.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator4.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator4.setName("jSeparator4"); // NOI18N
        FormInput.add(jSeparator4);
        jSeparator4.setBounds(10, 1200, 880, 1);

        jSeparator6.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator6.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator6.setName("jSeparator6"); // NOI18N
        FormInput.add(jSeparator6);
        jSeparator6.setBounds(10, 1500, 880, 1);

        jSeparator8.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator8.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator8.setName("jSeparator8"); // NOI18N
        FormInput.add(jSeparator8);
        jSeparator8.setBounds(10, 1280, 880, 1);

        PanelWall.setBackground(new java.awt.Color(29, 29, 29));
        PanelWall.setBackgroundImage(new javax.swing.ImageIcon(getClass().getResource("/picture/nyeri.png"))); // NOI18N
        PanelWall.setBackgroundImageType(usu.widget.constan.BackgroundConstan.BACKGROUND_IMAGE_STRECT);
        PanelWall.setPreferredSize(new java.awt.Dimension(200, 200));
        PanelWall.setRound(false);
        PanelWall.setWarna(new java.awt.Color(110, 110, 110));
        PanelWall.setLayout(null);
        FormInput.add(PanelWall);
        PanelWall.setBounds(50, 1300, 320, 130);

        jSeparator9.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator9.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator9.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator9.setName("jSeparator9"); // NOI18N
        FormInput.add(jSeparator9);
        jSeparator9.setBounds(370, 1300, 1, 140);

        jLabel71.setText("Jam dilaporkan :");
        jLabel71.setName("jLabel71"); // NOI18N
        FormInput.add(jLabel71);
        jLabel71.setBounds(690, 1600, 90, 23);

        jSeparator10.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator10.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator10.setName("jSeparator10"); // NOI18N
        FormInput.add(jSeparator10);
        jSeparator10.setBounds(10, 1640, 880, 1);

        Bahasa.setEditable(false);
        Bahasa.setFocusTraversalPolicyProvider(true);
        Bahasa.setName("Bahasa"); // NOI18N
        FormInput.add(Bahasa);
        Bahasa.setBounds(530, 560, 120, 23);

        jLabel76.setText("i. Bahasa Sehari-hari :");
        jLabel76.setName("jLabel76"); // NOI18N
        FormInput.add(jLabel76);
        jLabel76.setBounds(400, 560, 125, 23);

        jLabel77.setText("k. Agama :");
        jLabel77.setName("jLabel77"); // NOI18N
        FormInput.add(jLabel77);
        jLabel77.setBounds(10, 590, 95, 23);

        Agama.setEditable(false);
        Agama.setFocusTraversalPolicyProvider(true);
        Agama.setName("Agama"); // NOI18N
        FormInput.add(Agama);
        Agama.setBounds(110, 590, 110, 23);

        jLabel78.setText("Status Kehamilan :");
        jLabel78.setName("jLabel78"); // NOI18N
        FormInput.add(jLabel78);
        jLabel78.setBounds(440, 150, 106, 23);

        StatusKehamilan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Hamil", "Hamil" }));
        StatusKehamilan.setName("StatusKehamilan"); // NOI18N
        StatusKehamilan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                StatusKehamilanKeyPressed(evt);
            }
        });
        FormInput.add(StatusKehamilan);
        StatusKehamilan.setBounds(550, 150, 110, 23);

        jLabel29.setText("Gravida :");
        jLabel29.setName("jLabel29"); // NOI18N
        FormInput.add(jLabel29);
        jLabel29.setBounds(715, 180, 50, 23);

        Gravida.setHighlighter(null);
        Gravida.setName("Gravida"); // NOI18N
        Gravida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                GravidaKeyPressed(evt);
            }
        });
        FormInput.add(Gravida);
        Gravida.setBounds(769, 180, 85, 23);

        jLabel32.setText("Para :");
        jLabel32.setName("jLabel32"); // NOI18N
        FormInput.add(jLabel32);
        jLabel32.setBounds(440, 180, 43, 23);

        Para.setHighlighter(null);
        Para.setName("Para"); // NOI18N
        Para.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ParaKeyPressed(evt);
            }
        });
        FormInput.add(Para);
        Para.setBounds(487, 180, 85, 23);

        jLabel33.setText("Abortus :");
        jLabel33.setName("jLabel33"); // NOI18N
        FormInput.add(jLabel33);
        jLabel33.setBounds(575, 180, 50, 23);

        Abortus.setHighlighter(null);
        Abortus.setName("Abortus"); // NOI18N
        Abortus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AbortusKeyPressed(evt);
            }
        });
        FormInput.add(Abortus);
        Abortus.setBounds(629, 180, 85, 23);

        jLabel35.setText("HPHT :");
        jLabel35.setName("jLabel35"); // NOI18N
        FormInput.add(jLabel35);
        jLabel35.setBounds(680, 150, 50, 23);

        HPHT.setHighlighter(null);
        HPHT.setName("HPHT"); // NOI18N
        HPHT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HPHTKeyPressed(evt);
            }
        });
        FormInput.add(HPHT);
        HPHT.setBounds(734, 150, 120, 23);

        jSeparator11.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator11.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator11.setName("jSeparator11"); // NOI18N
        FormInput.add(jSeparator11);
        jSeparator11.setBounds(0, 210, 880, 1);

        jLabel98.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel98.setText("I. RIWAYAT KESEHATAN PASIEN");
        jLabel98.setName("jLabel98"); // NOI18N
        FormInput.add(jLabel98);
        jLabel98.setBounds(10, 70, 180, 23);

        jLabel90.setText("Tekanan Intrakranial :");
        jLabel90.setName("jLabel90"); // NOI18N
        FormInput.add(jLabel90);
        jLabel90.setBounds(0, 270, 150, 23);

        Tekanan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TAK", "Sakit Kepala", "Muntah", "Pusing", "Bingung" }));
        Tekanan.setName("Tekanan"); // NOI18N
        Tekanan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TekananKeyPressed(evt);
            }
        });
        FormInput.add(Tekanan);
        Tekanan.setBounds(150, 270, 112, 23);

        jLabel91.setText("Pupil :");
        jLabel91.setName("jLabel91"); // NOI18N
        FormInput.add(jLabel91);
        jLabel91.setBounds(290, 270, 50, 23);

        Pupil.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Miosis", "Isokor", "Anisokor" }));
        Pupil.setName("Pupil"); // NOI18N
        Pupil.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PupilKeyPressed(evt);
            }
        });
        FormInput.add(Pupil);
        Pupil.setBounds(350, 270, 93, 23);

        Neurosensorik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TAK", "Spasme Otot", "Perubahan Sensorik", "Perubahan Motorik", "Perubahan Bentuk Ekstremitas", "Penurunan Tingkat Kesadaran", "Fraktur/Dislokasi", "Luksasio", "Kerusakan Jaringan/Luka" }));
        Neurosensorik.setName("Neurosensorik"); // NOI18N
        Neurosensorik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NeurosensorikKeyPressed(evt);
            }
        });
        FormInput.add(Neurosensorik);
        Neurosensorik.setBounds(650, 270, 200, 23);

        jLabel100.setText("Neurosensorik / Muskuloskeletal :");
        jLabel100.setName("jLabel100"); // NOI18N
        FormInput.add(jLabel100);
        jLabel100.setBounds(470, 270, 180, 23);

        jLabel101.setText("Integumen :");
        jLabel101.setName("jLabel101"); // NOI18N
        FormInput.add(jLabel101);
        jLabel101.setBounds(0, 300, 150, 23);

        Integumen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TAK", "Luka Bakar", "Luka Robek", "Lecet", "Luka Decubitus", "Luka Gangren" }));
        Integumen.setName("Integumen"); // NOI18N
        Integumen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IntegumenKeyPressed(evt);
            }
        });
        FormInput.add(Integumen);
        Integumen.setBounds(150, 300, 125, 23);

        jLabel102.setText("Turgor Kulit :");
        jLabel102.setName("jLabel102"); // NOI18N
        FormInput.add(jLabel102);
        jLabel102.setBounds(290, 300, 80, 23);

        Turgor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Baik", "Menurun" }));
        Turgor.setName("Turgor"); // NOI18N
        Turgor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TurgorKeyPressed(evt);
            }
        });
        FormInput.add(Turgor);
        Turgor.setBounds(370, 300, 93, 23);

        jLabel103.setText("Edema :");
        jLabel103.setName("jLabel103"); // NOI18N
        FormInput.add(jLabel103);
        jLabel103.setBounds(480, 300, 50, 23);

        Edema.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Ekstremitas", "Seluruh Tubuh", "Asites", "Palpebrae" }));
        Edema.setName("Edema"); // NOI18N
        Edema.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EdemaKeyPressed(evt);
            }
        });
        FormInput.add(Edema);
        Edema.setBounds(540, 300, 120, 23);

        jLabel104.setText("Mukosa Mulut :");
        jLabel104.setName("jLabel104"); // NOI18N
        FormInput.add(jLabel104);
        jLabel104.setBounds(670, 300, 90, 23);

        Mukosa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Lembab", "Kering" }));
        Mukosa.setName("Mukosa"); // NOI18N
        Mukosa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MukosaKeyPressed(evt);
            }
        });
        FormInput.add(Mukosa);
        Mukosa.setBounds(760, 300, 90, 23);

        jLabel105.setText("Perdarahan :");
        jLabel105.setName("jLabel105"); // NOI18N
        FormInput.add(jLabel105);
        jLabel105.setBounds(0, 330, 150, 23);

        Perdarahan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Ada" }));
        Perdarahan.setName("Perdarahan"); // NOI18N
        Perdarahan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PerdarahanKeyPressed(evt);
            }
        });
        FormInput.add(Perdarahan);
        Perdarahan.setBounds(150, 330, 100, 23);

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel36.setText("cc");
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput.add(jLabel36);
        jLabel36.setBounds(380, 330, 20, 23);

        JumlahPerdarahan.setHighlighter(null);
        JumlahPerdarahan.setName("JumlahPerdarahan"); // NOI18N
        JumlahPerdarahan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JumlahPerdarahanKeyPressed(evt);
            }
        });
        FormInput.add(JumlahPerdarahan);
        JumlahPerdarahan.setBounds(300, 330, 70, 23);

        jLabel37.setText(", Warna :");
        jLabel37.setName("jLabel37"); // NOI18N
        FormInput.add(jLabel37);
        jLabel37.setBounds(380, 330, 50, 23);

        WarnaPerdarahan.setHighlighter(null);
        WarnaPerdarahan.setName("WarnaPerdarahan"); // NOI18N
        WarnaPerdarahan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                WarnaPerdarahanKeyPressed(evt);
            }
        });
        FormInput.add(WarnaPerdarahan);
        WarnaPerdarahan.setBounds(440, 330, 170, 23);

        jLabel38.setText(", Jumlah :");
        jLabel38.setName("jLabel38"); // NOI18N
        FormInput.add(jLabel38);
        jLabel38.setBounds(250, 330, 50, 23);

        jLabel106.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel106.setText("X/");
        jLabel106.setName("jLabel106"); // NOI18N
        FormInput.add(jLabel106);
        jLabel106.setBounds(280, 380, 13, 23);

        Intoksikasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Ada", "Gigitan Binatang", "Zat Kimia", "Gas", "Obat" }));
        Intoksikasi.setSelectedIndex(2);
        Intoksikasi.setName("Intoksikasi"); // NOI18N
        Intoksikasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IntoksikasiKeyPressed(evt);
            }
        });
        FormInput.add(Intoksikasi);
        Intoksikasi.setBounds(720, 330, 135, 23);

        jLabel107.setText("Intoksikasi :");
        jLabel107.setName("jLabel107"); // NOI18N
        FormInput.add(jLabel107);
        jLabel107.setBounds(620, 330, 90, 23);

        jLabel108.setText("Eliminasi :");
        jLabel108.setName("jLabel108"); // NOI18N
        FormInput.add(jLabel108);
        jLabel108.setBounds(0, 360, 150, 23);

        KBAB.setHighlighter(null);
        KBAB.setName("KBAB"); // NOI18N
        KBAB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KBABKeyPressed(evt);
            }
        });
        FormInput.add(KBAB);
        KBAB.setBounds(440, 380, 175, 23);

        jLabel109.setText("Konsistensi :");
        jLabel109.setName("jLabel109"); // NOI18N
        FormInput.add(jLabel109);
        jLabel109.setBounds(370, 380, 70, 23);

        BAB.setHighlighter(null);
        BAB.setName("BAB"); // NOI18N
        BAB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BABKeyPressed(evt);
            }
        });
        FormInput.add(BAB);
        BAB.setBounds(230, 380, 50, 23);

        jLabel110.setText("BAB : Frekuensi :");
        jLabel110.setName("jLabel110"); // NOI18N
        FormInput.add(jLabel110);
        jLabel110.setBounds(130, 380, 90, 23);

        XBAB.setHighlighter(null);
        XBAB.setName("XBAB"); // NOI18N
        XBAB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                XBABKeyPressed(evt);
            }
        });
        FormInput.add(XBAB);
        XBAB.setBounds(300, 380, 70, 23);

        jLabel111.setText("Warna :");
        jLabel111.setName("jLabel111"); // NOI18N
        FormInput.add(jLabel111);
        jLabel111.setBounds(620, 380, 55, 23);

        WBAB.setHighlighter(null);
        WBAB.setName("WBAB"); // NOI18N
        WBAB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                WBABKeyPressed(evt);
            }
        });
        FormInput.add(WBAB);
        WBAB.setBounds(680, 380, 175, 23);

        jLabel112.setText("BAK : Frekuensi :");
        jLabel112.setName("jLabel112"); // NOI18N
        FormInput.add(jLabel112);
        jLabel112.setBounds(130, 410, 90, 23);

        BAK.setHighlighter(null);
        BAK.setName("BAK"); // NOI18N
        BAK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BAKKeyPressed(evt);
            }
        });
        FormInput.add(BAK);
        BAK.setBounds(230, 410, 50, 23);

        jLabel113.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel113.setText("X/");
        jLabel113.setName("jLabel113"); // NOI18N
        FormInput.add(jLabel113);
        jLabel113.setBounds(280, 410, 13, 23);

        XBAK.setHighlighter(null);
        XBAK.setName("XBAK"); // NOI18N
        XBAK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                XBAKKeyPressed(evt);
            }
        });
        FormInput.add(XBAK);
        XBAK.setBounds(300, 410, 70, 23);

        jLabel114.setText("Warna :");
        jLabel114.setName("jLabel114"); // NOI18N
        FormInput.add(jLabel114);
        jLabel114.setBounds(370, 410, 70, 23);

        WBAK.setHighlighter(null);
        WBAK.setName("WBAK"); // NOI18N
        WBAK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                WBAKKeyPressed(evt);
            }
        });
        FormInput.add(WBAK);
        WBAK.setBounds(440, 410, 175, 23);

        jLabel115.setText("Lain-lain :");
        jLabel115.setName("jLabel115"); // NOI18N
        FormInput.add(jLabel115);
        jLabel115.setBounds(620, 410, 55, 23);

        LBAK.setHighlighter(null);
        LBAK.setName("LBAK"); // NOI18N
        LBAK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LBAKKeyPressed(evt);
            }
        });
        FormInput.add(LBAK);
        LBAK.setBounds(680, 410, 175, 23);

        jSeparator12.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator12.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator12.setName("jSeparator12"); // NOI18N
        FormInput.add(jSeparator12);
        jSeparator12.setBounds(10, 450, 880, 1);

        jLabel116.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel116.setText("II. PEMERIKSAAN FISIK");
        jLabel116.setName("jLabel116"); // NOI18N
        FormInput.add(jLabel116);
        jLabel116.setBounds(10, 210, 180, 23);

        jLabel117.setText("a. Kondisi Psikologis :");
        jLabel117.setName("jLabel117"); // NOI18N
        FormInput.add(jLabel117);
        jLabel117.setBounds(10, 470, 148, 23);

        Psikologis.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada Masalah", "Marah", "Takut", "Depresi", "Cepat Lelah", "Cemas", "Gelisah", "Lain-lain" }));
        Psikologis.setName("Psikologis"); // NOI18N
        Psikologis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PsikologisKeyPressed(evt);
            }
        });
        FormInput.add(Psikologis);
        Psikologis.setBounds(160, 470, 160, 23);

        jLabel119.setText("b. Gangguan Jiwa Di Masa Lalu :");
        jLabel119.setName("jLabel119"); // NOI18N
        FormInput.add(jLabel119);
        jLabel119.setBounds(320, 470, 180, 23);

        Jiwa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        Jiwa.setName("Jiwa"); // NOI18N
        Jiwa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JiwaKeyPressed(evt);
            }
        });
        FormInput.add(Jiwa);
        Jiwa.setBounds(500, 470, 90, 23);

        jLabel120.setText("c. Status Pernikahan :");
        jLabel120.setName("jLabel120"); // NOI18N
        FormInput.add(jLabel120);
        jLabel120.setBounds(600, 470, 120, 23);

        Perilaku.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Perilaku Kekerasan", "Gangguan Efek", "Gangguan Memori", "Halusinasi", "Kecenderungan Percobaan Bunuh Diri", "Lainnya", "-" }));
        Perilaku.setSelectedIndex(6);
        Perilaku.setName("Perilaku"); // NOI18N
        Perilaku.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PerilakuKeyPressed(evt);
            }
        });
        FormInput.add(Perilaku);
        Perilaku.setBounds(150, 500, 235, 23);

        jLabel118.setText(", Dilaporkan Ke :");
        jLabel118.setName("jLabel118"); // NOI18N
        FormInput.add(jLabel118);
        jLabel118.setBounds(390, 500, 82, 23);

        Dilaporkan.setHighlighter(null);
        Dilaporkan.setName("Dilaporkan"); // NOI18N
        Dilaporkan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DilaporkanKeyPressed(evt);
            }
        });
        FormInput.add(Dilaporkan);
        Dilaporkan.setBounds(470, 500, 163, 23);
        Dilaporkan.getAccessibleContext().setAccessibleName("");

        jLabel121.setText(", Sebutkan :");
        jLabel121.setName("jLabel121"); // NOI18N
        FormInput.add(jLabel121);
        jLabel121.setBounds(630, 500, 62, 23);

        Sebutkan.setHighlighter(null);
        Sebutkan.setName("Sebutkan"); // NOI18N
        Sebutkan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SebutkanKeyPressed(evt);
            }
        });
        FormInput.add(Sebutkan);
        Sebutkan.setBounds(700, 500, 163, 23);
        Sebutkan.getAccessibleContext().setAccessibleName("");

        jLabel122.setText("e. Hubungan Pasien Dengan Anggota Keluarga :");
        jLabel122.setName("jLabel122"); // NOI18N
        FormInput.add(jLabel122);
        jLabel122.setBounds(10, 530, 276, 23);

        Hubungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Harmonis", "Kurang Harmonis", "Tidak Harmonis", "Konflik Besar" }));
        Hubungan.setName("Hubungan"); // NOI18N
        Hubungan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HubunganKeyPressed(evt);
            }
        });
        FormInput.add(Hubungan);
        Hubungan.setBounds(290, 530, 135, 23);

        jLabel123.setText("d. Adakah Perilaku :");
        jLabel123.setName("jLabel123"); // NOI18N
        FormInput.add(jLabel123);
        jLabel123.setBounds(10, 500, 140, 23);

        StatusPernikahan.setEditable(false);
        StatusPernikahan.setHighlighter(null);
        StatusPernikahan.setName("StatusPernikahan"); // NOI18N
        StatusPernikahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StatusPernikahanActionPerformed(evt);
            }
        });
        FormInput.add(StatusPernikahan);
        StatusPernikahan.setBounds(720, 470, 140, 23);

        jLabel124.setText("f. Pekerjaan :");
        jLabel124.setName("jLabel124"); // NOI18N
        FormInput.add(jLabel124);
        jLabel124.setBounds(440, 530, 70, 23);

        Pekerjaan.setEditable(false);
        Pekerjaan.setHighlighter(null);
        Pekerjaan.setName("Pekerjaan"); // NOI18N
        FormInput.add(Pekerjaan);
        Pekerjaan.setBounds(520, 530, 120, 23);

        jLabel125.setText("g. Pembayaran :");
        jLabel125.setName("jLabel125"); // NOI18N
        FormInput.add(jLabel125);
        jLabel125.setBounds(650, 530, 90, 23);

        Pembayaran.setEditable(false);
        Pembayaran.setHighlighter(null);
        Pembayaran.setName("Pembayaran"); // NOI18N
        FormInput.add(Pembayaran);
        Pembayaran.setBounds(740, 530, 120, 23);

        jLabel126.setText("j. Pendidikan :");
        jLabel126.setName("jLabel126"); // NOI18N
        FormInput.add(jLabel126);
        jLabel126.setBounds(660, 560, 80, 23);

        PendidikanPasien.setEditable(false);
        PendidikanPasien.setFocusTraversalPolicyProvider(true);
        PendidikanPasien.setName("PendidikanPasien"); // NOI18N
        FormInput.add(PendidikanPasien);
        PendidikanPasien.setBounds(740, 560, 120, 23);

        jLabel127.setText("m. Pendidikan P.J. :");
        jLabel127.setName("jLabel127"); // NOI18N
        FormInput.add(jLabel127);
        jLabel127.setBounds(10, 620, 138, 23);

        PendidikanPJ.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "TS", "TK", "SD", "SMP", "SMA", "SLTA/SEDERAJAT", "D1", "D2", "D3", "D4", "S1", "S2", "S3" }));
        PendidikanPJ.setName("PendidikanPJ"); // NOI18N
        PendidikanPJ.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PendidikanPJKeyPressed(evt);
            }
        });
        FormInput.add(PendidikanPJ);
        PendidikanPJ.setBounds(150, 620, 137, 23);

        KetPendidikanPJ.setFocusTraversalPolicyProvider(true);
        KetPendidikanPJ.setName("KetPendidikanPJ"); // NOI18N
        KetPendidikanPJ.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetPendidikanPJKeyPressed(evt);
            }
        });
        FormInput.add(KetPendidikanPJ);
        KetPendidikanPJ.setBounds(290, 620, 150, 23);

        jLabel39.setText("Informasi didapat dari :");
        jLabel39.setName("jLabel39"); // NOI18N
        FormInput.add(jLabel39);
        jLabel39.setBounds(592, 40, 130, 23);

        Informasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Autoanamnesis", "Alloanamnesis" }));
        Informasi.setName("Informasi"); // NOI18N
        Informasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                InformasiKeyPressed(evt);
            }
        });
        FormInput.add(Informasi);
        Informasi.setBounds(726, 40, 128, 23);

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
        Scroll8.setBounds(20, 1960, 400, 130);

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
        BtnTambahMasalah.setBounds(350, 2110, 28, 23);

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
        BtnAllMasalah.setBounds(320, 2110, 28, 23);

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
        BtnCariMasalah.setBounds(290, 2110, 28, 23);

        TCariMasalah.setToolTipText("Alt+C");
        TCariMasalah.setName("TCariMasalah"); // NOI18N
        TCariMasalah.setPreferredSize(new java.awt.Dimension(140, 23));
        TCariMasalah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariMasalahKeyPressed(evt);
            }
        });
        FormInput.add(TCariMasalah);
        TCariMasalah.setBounds(70, 2110, 215, 23);

        label12.setText("Key Word :");
        label12.setName("label12"); // NOI18N
        label12.setPreferredSize(new java.awt.Dimension(60, 23));
        FormInput.add(label12);
        label12.setBounds(10, 2110, 60, 23);

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
        TabRencanaKeperawatan.setBounds(440, 1960, 420, 143);

        label13.setText("Key Word :");
        label13.setName("label13"); // NOI18N
        label13.setPreferredSize(new java.awt.Dimension(60, 23));
        FormInput.add(label13);
        label13.setBounds(440, 2110, 60, 23);

        TCariRencana.setToolTipText("Alt+C");
        TCariRencana.setName("TCariRencana"); // NOI18N
        TCariRencana.setPreferredSize(new java.awt.Dimension(215, 23));
        TCariRencana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariRencanaKeyPressed(evt);
            }
        });
        FormInput.add(TCariRencana);
        TCariRencana.setBounds(500, 2110, 235, 23);

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
        BtnCariRencana.setBounds(740, 2110, 28, 23);

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
        BtnAllRencana.setBounds(770, 2110, 28, 23);

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
        BtnTambahRencana.setBounds(800, 2110, 28, 23);

        jSeparator7.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator7.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator7.setName("jSeparator7"); // NOI18N
        FormInput.add(jSeparator7);
        jSeparator7.setBounds(10, 2310, 880, 1);

        jLabel99.setText("Dilaporkan kepada dokter ?");
        jLabel99.setName("jLabel99"); // NOI18N
        FormInput.add(jLabel99);
        jLabel99.setBounds(390, 1600, 190, 23);

        jSeparator14.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator14.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator14.setName("jSeparator14"); // NOI18N
        FormInput.add(jSeparator14);
        jSeparator14.setBounds(10, 660, 880, 1);

        jLabel217.setText("Kemampuan Baca & Tulis :");
        jLabel217.setName("jLabel217"); // NOI18N
        FormInput.add(jLabel217);
        jLabel217.setBounds(60, 680, 140, 23);

        KemampuanBacaTulis.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Baik", "Kurang", "Tidak Bisa" }));
        KemampuanBacaTulis.setName("KemampuanBacaTulis"); // NOI18N
        KemampuanBacaTulis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KemampuanBacaTulisKeyPressed(evt);
            }
        });
        FormInput.add(KemampuanBacaTulis);
        KemampuanBacaTulis.setBounds(210, 680, 100, 23);

        jLabel218.setText("Butuh Penerjamah :");
        jLabel218.setName("jLabel218"); // NOI18N
        FormInput.add(jLabel218);
        jLabel218.setBounds(310, 680, 110, 23);

        ButuhPenerjemah.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        ButuhPenerjemah.setName("ButuhPenerjemah"); // NOI18N
        ButuhPenerjemah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ButuhPenerjemahKeyPressed(evt);
            }
        });
        FormInput.add(ButuhPenerjemah);
        ButuhPenerjemah.setBounds(420, 680, 80, 23);

        jLabel219.setText("Jika Ya :");
        jLabel219.setName("jLabel219"); // NOI18N
        FormInput.add(jLabel219);
        jLabel219.setBounds(500, 680, 50, 23);

        KeteranganButuhPenerjemah.setFocusTraversalPolicyProvider(true);
        KeteranganButuhPenerjemah.setName("KeteranganButuhPenerjemah"); // NOI18N
        KeteranganButuhPenerjemah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeteranganButuhPenerjemahKeyPressed(evt);
            }
        });
        FormInput.add(KeteranganButuhPenerjemah);
        KeteranganButuhPenerjemah.setBounds(550, 680, 310, 23);

        jLabel220.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel220.setText(",");
        jLabel220.setName("jLabel220"); // NOI18N
        FormInput.add(jLabel220);
        jLabel220.setBounds(360, 710, 20, 23);

        jLabel221.setText(":");
        jLabel221.setName("jLabel221"); // NOI18N
        FormInput.add(jLabel221);
        jLabel221.setBounds(10, 710, 252, 23);

        TerdapatHambatanBelajar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        TerdapatHambatanBelajar.setName("TerdapatHambatanBelajar"); // NOI18N
        TerdapatHambatanBelajar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TerdapatHambatanBelajarKeyPressed(evt);
            }
        });
        FormInput.add(TerdapatHambatanBelajar);
        TerdapatHambatanBelajar.setBounds(270, 710, 90, 23);

        jLabel222.setText("Jika Ya :");
        jLabel222.setName("jLabel222"); // NOI18N
        FormInput.add(jLabel222);
        jLabel222.setBounds(350, 710, 60, 23);

        HambatanBelajar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "Gangguan Pendengaran", "Gangguan Penglihatan", "Gangguan Kognitif", "Gangguan Fisik", "Gangguan Emosi", "Keterbatasan Bahasa", "Keterbatasan Budaya", "Keterbatasan Spiritual", "Agama", "Lainnya" }));
        HambatanBelajar.setName("HambatanBelajar"); // NOI18N
        HambatanBelajar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HambatanBelajarKeyPressed(evt);
            }
        });
        FormInput.add(HambatanBelajar);
        HambatanBelajar.setBounds(410, 710, 177, 23);

        KeteranganHambatanBelajar.setFocusTraversalPolicyProvider(true);
        KeteranganHambatanBelajar.setName("KeteranganHambatanBelajar"); // NOI18N
        KeteranganHambatanBelajar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeteranganHambatanBelajarKeyPressed(evt);
            }
        });
        FormInput.add(KeteranganHambatanBelajar);
        KeteranganHambatanBelajar.setBounds(590, 710, 274, 23);

        jLabel224.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel224.setText("Terdapat Hambatan Dalam Pembelajaran");
        jLabel224.setName("jLabel224"); // NOI18N
        FormInput.add(jLabel224);
        jLabel224.setBounds(60, 710, 240, 23);

        jLabel225.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel225.setText(",");
        jLabel225.setName("jLabel225"); // NOI18N
        FormInput.add(jLabel225);
        jLabel225.setBounds(500, 680, 30, 23);

        jLabel223.setText(":");
        jLabel223.setName("jLabel223"); // NOI18N
        FormInput.add(jLabel223);
        jLabel223.setBounds(10, 740, 160, 23);

        HambatanCaraBicara.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Gangguan Bicara" }));
        HambatanCaraBicara.setName("HambatanCaraBicara"); // NOI18N
        HambatanCaraBicara.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HambatanCaraBicaraKeyPressed(evt);
            }
        });
        FormInput.add(HambatanCaraBicara);
        HambatanCaraBicara.setBounds(180, 740, 150, 23);

        HambatanBahasaIsyarat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        HambatanBahasaIsyarat.setName("HambatanBahasaIsyarat"); // NOI18N
        HambatanBahasaIsyarat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HambatanBahasaIsyaratKeyPressed(evt);
            }
        });
        FormInput.add(HambatanBahasaIsyarat);
        HambatanBahasaIsyarat.setBounds(490, 740, 90, 23);

        jLabel226.setText("Hambatan Bahasa Isyarat :");
        jLabel226.setName("jLabel226"); // NOI18N
        FormInput.add(jLabel226);
        jLabel226.setBounds(330, 740, 150, 23);

        jLabel227.setText("Cara Belajar Yang Disukai :");
        jLabel227.setName("jLabel227"); // NOI18N
        FormInput.add(jLabel227);
        jLabel227.setBounds(580, 740, 150, 23);

        CaraBelajarDisukai.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Audio", "Lisan", "Visual", "Demonstrasi", "Tulisan" }));
        CaraBelajarDisukai.setName("CaraBelajarDisukai"); // NOI18N
        CaraBelajarDisukai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CaraBelajarDisukaiKeyPressed(evt);
            }
        });
        FormInput.add(CaraBelajarDisukai);
        CaraBelajarDisukai.setBounds(740, 740, 130, 23);

        jLabel228.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel228.setText("Hambatan Cara Bicara");
        jLabel228.setName("jLabel228"); // NOI18N
        FormInput.add(jLabel228);
        jLabel228.setBounds(60, 740, 140, 23);

        jLabel229.setText(":");
        jLabel229.setName("jLabel229"); // NOI18N
        FormInput.add(jLabel229);
        jLabel229.setBounds(10, 770, 202, 23);

        KesediaanMenerimaInformasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        KesediaanMenerimaInformasi.setName("KesediaanMenerimaInformasi"); // NOI18N
        KesediaanMenerimaInformasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KesediaanMenerimaInformasiKeyPressed(evt);
            }
        });
        FormInput.add(KesediaanMenerimaInformasi);
        KesediaanMenerimaInformasi.setBounds(220, 770, 90, 23);

        KeteranganKesediaanMenerimaInformasi.setFocusTraversalPolicyProvider(true);
        KeteranganKesediaanMenerimaInformasi.setName("KeteranganKesediaanMenerimaInformasi"); // NOI18N
        KeteranganKesediaanMenerimaInformasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeteranganKesediaanMenerimaInformasiKeyPressed(evt);
            }
        });
        FormInput.add(KeteranganKesediaanMenerimaInformasi);
        KeteranganKesediaanMenerimaInformasi.setBounds(310, 770, 273, 23);

        jLabel230.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel230.setText("Kesediaan Menerima Informasi");
        jLabel230.setName("jLabel230"); // NOI18N
        FormInput.add(jLabel230);
        jLabel230.setBounds(60, 770, 170, 23);

        jLabel231.setText("Pemahaman Tentang Nutrisi/Diet :");
        jLabel231.setName("jLabel231"); // NOI18N
        FormInput.add(jLabel231);
        jLabel231.setBounds(590, 770, 180, 23);

        PemahamanNutrisi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        PemahamanNutrisi.setName("PemahamanNutrisi"); // NOI18N
        PemahamanNutrisi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PemahamanNutrisiKeyPressed(evt);
            }
        });
        FormInput.add(PemahamanNutrisi);
        PemahamanNutrisi.setBounds(780, 770, 90, 23);

        PemahamanPenyakit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        PemahamanPenyakit.setName("PemahamanPenyakit"); // NOI18N
        PemahamanPenyakit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PemahamanPenyakitKeyPressed(evt);
            }
        });
        FormInput.add(PemahamanPenyakit);
        PemahamanPenyakit.setBounds(220, 800, 90, 23);

        jLabel232.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel232.setText("Pemahaman Tentang Penyakit");
        jLabel232.setName("jLabel232"); // NOI18N
        FormInput.add(jLabel232);
        jLabel232.setBounds(60, 800, 180, 23);

        jLabel233.setText(":");
        jLabel233.setName("jLabel233"); // NOI18N
        FormInput.add(jLabel233);
        jLabel233.setBounds(10, 800, 198, 23);

        jLabel234.setText("Pemahaman Tentang Perawatan :");
        jLabel234.setName("jLabel234"); // NOI18N
        FormInput.add(jLabel234);
        jLabel234.setBounds(590, 800, 180, 23);

        PemahamanPerawatan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        PemahamanPerawatan.setName("PemahamanPerawatan"); // NOI18N
        PemahamanPerawatan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PemahamanPerawatanKeyPressed(evt);
            }
        });
        FormInput.add(PemahamanPerawatan);
        PemahamanPerawatan.setBounds(780, 800, 90, 23);

        PemahamanPengobatan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        PemahamanPengobatan.setName("PemahamanPengobatan"); // NOI18N
        PemahamanPengobatan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PemahamanPengobatanKeyPressed(evt);
            }
        });
        FormInput.add(PemahamanPengobatan);
        PemahamanPengobatan.setBounds(500, 800, 90, 23);

        jLabel235.setText("Pemahaman Tentang Pengobatan :");
        jLabel235.setName("jLabel235"); // NOI18N
        FormInput.add(jLabel235);
        jLabel235.setBounds(320, 800, 180, 23);

        jLabel273.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel273.setText("Keterbatasan Fisik:");
        jLabel273.setName("jLabel273"); // NOI18N
        FormInput.add(jLabel273);
        jLabel273.setBounds(350, 830, 100, 23);

        jLabel274.setText("Hambatan Emosional :");
        jLabel274.setName("jLabel274"); // NOI18N
        FormInput.add(jLabel274);
        jLabel274.setBounds(630, 830, 110, 23);

        KeyakinanNilai.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Flexible", "Primitive", "Kaku", "Sulit Berubah", "Modern", "Mudah Berubah" }));
        KeyakinanNilai.setName("KeyakinanNilai"); // NOI18N
        KeyakinanNilai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeyakinanNilaiKeyPressed(evt);
            }
        });
        FormInput.add(KeyakinanNilai);
        KeyakinanNilai.setBounds(180, 830, 90, 23);

        jLabel275.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel275.setText("Motivasi :");
        jLabel275.setName("jLabel275"); // NOI18N
        FormInput.add(jLabel275);
        jLabel275.setBounds(120, 860, 60, 23);

        HambatanEmosional.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sabar", "Pemarah" }));
        HambatanEmosional.setName("HambatanEmosional"); // NOI18N
        HambatanEmosional.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HambatanEmosionalKeyPressed(evt);
            }
        });
        FormInput.add(HambatanEmosional);
        HambatanEmosional.setBounds(740, 830, 90, 23);

        KeterbatasanFisik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada Keterbatasan", "Gangguan Penglihatan", "Gangguan Pendengaran", "Sulit Mengerti Sesuatu", "Keterbatasan Fisik" }));
        KeterbatasanFisik.setName("KeterbatasanFisik"); // NOI18N
        KeterbatasanFisik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeterbatasanFisikKeyPressed(evt);
            }
        });
        FormInput.add(KeterbatasanFisik);
        KeterbatasanFisik.setBounds(450, 830, 150, 23);

        Motivasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aktif", "Apatis" }));
        Motivasi.setName("Motivasi"); // NOI18N
        Motivasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MotivasiKeyPressed(evt);
            }
        });
        FormInput.add(Motivasi);
        Motivasi.setBounds(180, 860, 90, 23);

        jLabel276.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel276.setText("Keyakinan & Nilai :");
        jLabel276.setName("jLabel276"); // NOI18N
        FormInput.add(jLabel276);
        jLabel276.setBounds(80, 830, 100, 23);

        jLabel277.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel277.setText("IV. PERENCANAAN KEBUTUHAN EDUKASI/KOMUNIKASI");
        jLabel277.setName("jLabel277"); // NOI18N
        FormInput.add(jLabel277);
        jLabel277.setBounds(10, 940, 400, 23);

        jSeparator13.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator13.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator13.setName("jSeparator13"); // NOI18N
        FormInput.add(jSeparator13);
        jSeparator13.setBounds(10, 970, 880, 1);

        Scroll11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 253)));
        Scroll11.setName("Scroll11"); // NOI18N
        Scroll11.setOpaque(true);

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
        Scroll11.setViewportView(tbKebutuhanEdukasi);

        FormInput.add(Scroll11);
        Scroll11.setBounds(20, 990, 400, 143);

        label15.setText("Key Word :");
        label15.setName("label15"); // NOI18N
        label15.setPreferredSize(new java.awt.Dimension(60, 23));
        FormInput.add(label15);
        label15.setBounds(30, 1150, 60, 23);

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
        BtnTambahKebutuhanEdukasi.setBounds(380, 1150, 28, 23);

        BtnAllKebutuhanEdukasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAllKebutuhanEdukasi.setMnemonic('2');
        BtnAllKebutuhanEdukasi.setToolTipText("2Alt+2");
        BtnAllKebutuhanEdukasi.setName("BtnAllKebutuhanEdukasi"); // NOI18N
        BtnAllKebutuhanEdukasi.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAllKebutuhanEdukasi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnAllKebutuhanEdukasiMouseClicked(evt);
            }
        });
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
        BtnAllKebutuhanEdukasi.setBounds(350, 1150, 28, 23);

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
        BtnCariKebutuhanEdukasi.setBounds(320, 1150, 28, 23);

        TabRencanaKeperawatan1.setBackground(new java.awt.Color(255, 255, 254));
        TabRencanaKeperawatan1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TabRencanaKeperawatan1.setForeground(new java.awt.Color(50, 50, 50));
        TabRencanaKeperawatan1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRencanaKeperawatan1.setName("TabRencanaKeperawatan1"); // NOI18N

        panelBiasa2.setName("panelBiasa2"); // NOI18N
        panelBiasa2.setLayout(new java.awt.BorderLayout());

        Scroll12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 253)));
        Scroll12.setName("Scroll12"); // NOI18N
        Scroll12.setOpaque(true);

        tbRencanaEdukasi.setName("tbRencanaEdukasi"); // NOI18N
        Scroll12.setViewportView(tbRencanaEdukasi);

        panelBiasa2.add(Scroll12, java.awt.BorderLayout.CENTER);

        TabRencanaKeperawatan1.addTab("Rencana Edukasi/Komunikasi", panelBiasa2);

        FormInput.add(TabRencanaKeperawatan1);
        TabRencanaKeperawatan1.setBounds(440, 980, 420, 143);

        TCariMasalah1.setToolTipText("Alt+C");
        TCariMasalah1.setName("TCariMasalah1"); // NOI18N
        TCariMasalah1.setPreferredSize(new java.awt.Dimension(140, 23));
        TCariMasalah1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariMasalah1KeyPressed(evt);
            }
        });
        FormInput.add(TCariMasalah1);
        TCariMasalah1.setBounds(100, 1150, 215, 23);

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
        BtnTambahRencanaEdukasi.setBounds(820, 1150, 28, 23);

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
        BtnAllRencanaEdukasi.setBounds(790, 1150, 28, 23);

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
        BtnCariRencanaEdukasi.setBounds(760, 1150, 28, 23);

        label16.setText("Key Word :");
        label16.setName("label16"); // NOI18N
        label16.setPreferredSize(new java.awt.Dimension(60, 23));
        FormInput.add(label16);
        label16.setBounds(460, 1150, 60, 23);

        TCariRencana1.setToolTipText("Alt+C");
        TCariRencana1.setName("TCariRencana1"); // NOI18N
        TCariRencana1.setPreferredSize(new java.awt.Dimension(215, 23));
        TCariRencana1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariRencana1KeyPressed(evt);
            }
        });
        FormInput.add(TCariRencana1);
        TCariRencana1.setBounds(520, 1150, 235, 23);

        jSeparator15.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator15.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator15.setName("jSeparator15"); // NOI18N
        FormInput.add(jSeparator15);
        jSeparator15.setBounds(10, 2140, 880, 1);

        jLabel272.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel272.setText("VIII. PERENCANAAN PULANG (DISCHARGE PLANNING)");
        jLabel272.setName("jLabel272"); // NOI18N
        FormInput.add(jLabel272);
        jLabel272.setBounds(10, 2140, 380, 23);

        InformasiPerencanaanPulang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        InformasiPerencanaanPulang.setName("InformasiPerencanaanPulang"); // NOI18N
        InformasiPerencanaanPulang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                InformasiPerencanaanPulangKeyPressed(evt);
            }
        });
        FormInput.add(InformasiPerencanaanPulang);
        InformasiPerencanaanPulang.setBounds(360, 2160, 80, 23);

        jLabel250.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel250.setText("Ibu Bayi & Keluarga Diberikan Informasi Perencanaan Pulang");
        jLabel250.setName("jLabel250"); // NOI18N
        FormInput.add(jLabel250);
        jLabel250.setBounds(50, 2160, 330, 23);

        jLabel257.setText("?");
        jLabel257.setName("jLabel257"); // NOI18N
        FormInput.add(jLabel257);
        jLabel257.setBounds(10, 2160, 347, 23);

        jLabel260.setText(":");
        jLabel260.setName("jLabel260"); // NOI18N
        FormInput.add(jLabel260);
        jLabel260.setBounds(10, 2190, 176, 23);

        KondisiPulang.setFocusTraversalPolicyProvider(true);
        KondisiPulang.setName("KondisiPulang"); // NOI18N
        KondisiPulang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KondisiPulangKeyPressed(evt);
            }
        });
        FormInput.add(KondisiPulang);
        KondisiPulang.setBounds(190, 2190, 674, 23);

        jLabel259.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel259.setText("Kondisi Klinis Saat Pulang");
        jLabel259.setName("jLabel259"); // NOI18N
        FormInput.add(jLabel259);
        jLabel259.setBounds(50, 2190, 180, 23);

        label29.setText("Perencanaan Pulang :");
        label29.setName("label29"); // NOI18N
        label29.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label29);
        label29.setBounds(640, 2160, 130, 23);

        TanggalPulang.setForeground(new java.awt.Color(50, 70, 50));
        TanggalPulang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "06-06-2026" }));
        TanggalPulang.setDisplayFormat("dd-MM-yyyy");
        TanggalPulang.setName("TanggalPulang"); // NOI18N
        TanggalPulang.setOpaque(false);
        TanggalPulang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TanggalPulangActionPerformed(evt);
            }
        });
        TanggalPulang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalPulangKeyPressed(evt);
            }
        });
        FormInput.add(TanggalPulang);
        TanggalPulang.setBounds(770, 2160, 90, 23);

        jLabel261.setText("Lama Rawat Rata-rata :");
        jLabel261.setName("jLabel261"); // NOI18N
        FormInput.add(jLabel261);
        jLabel261.setBounds(450, 2160, 130, 23);

        LamaRatarata.setFocusTraversalPolicyProvider(true);
        LamaRatarata.setName("LamaRatarata"); // NOI18N
        LamaRatarata.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LamaRatarataKeyPressed(evt);
            }
        });
        FormInput.add(LamaRatarata);
        LamaRatarata.setBounds(580, 2160, 55, 23);

        jLabel262.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel262.setText("Perawatan Lanjutan Yang Diberikan Di Rumah");
        jLabel262.setName("jLabel262"); // NOI18N
        FormInput.add(jLabel262);
        jLabel262.setBounds(50, 2220, 240, 23);

        scrollPane8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane8.setName("scrollPane8"); // NOI18N
        FormInput.add(scrollPane8);
        scrollPane8.setBounds(290, 2220, 576, 43);

        jLabel263.setText(":");
        jLabel263.setName("jLabel263"); // NOI18N
        FormInput.add(jLabel263);
        jLabel263.setBounds(10, 2220, 274, 23);

        CaraTransportasiPulang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mandiri", "Dibantu Sebagian", "Dibantu Keseluruhan", "Menggunakan Rostul", "Menggunakan Brankar", "Berjalan" }));
        CaraTransportasiPulang.setName("CaraTransportasiPulang"); // NOI18N
        CaraTransportasiPulang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CaraTransportasiPulangKeyPressed(evt);
            }
        });
        FormInput.add(CaraTransportasiPulang);
        CaraTransportasiPulang.setBounds(190, 2270, 160, 23);

        jLabel264.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel264.setText("Status Pelayanan :");
        jLabel264.setName("jLabel264"); // NOI18N
        FormInput.add(jLabel264);
        jLabel264.setBounds(340, 2360, 100, 23);

        jLabel265.setText(":");
        jLabel265.setName("jLabel265"); // NOI18N
        FormInput.add(jLabel265);
        jLabel265.setBounds(10, 2270, 174, 23);

        jLabel266.setText("Transportasi Yang Digunakan :");
        jLabel266.setName("jLabel266"); // NOI18N
        FormInput.add(jLabel266);
        jLabel266.setBounds(350, 2270, 167, 23);

        TransportasiYangDigunakan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kendaraan Pribadi", "Mobil Ambulance", "Kendaraan Umum" }));
        TransportasiYangDigunakan.setName("TransportasiYangDigunakan"); // NOI18N
        TransportasiYangDigunakan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TransportasiYangDigunakanKeyPressed(evt);
            }
        });
        FormInput.add(TransportasiYangDigunakan);
        TransportasiYangDigunakan.setBounds(520, 2270, 140, 23);

        GCS.setFocusTraversalPolicyProvider(true);
        GCS.setName("GCS"); // NOI18N
        GCS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                GCSKeyPressed(evt);
            }
        });
        FormInput.add(GCS);
        GCS.setBounds(310, 240, 60, 23);

        TD.setFocusTraversalPolicyProvider(true);
        TD.setName("TD"); // NOI18N
        TD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TDKeyPressed(evt);
            }
        });
        FormInput.add(TD);
        TD.setBounds(400, 240, 60, 23);

        jLabel40.setText("GCS(E,V,M) :");
        jLabel40.setName("jLabel40"); // NOI18N
        FormInput.add(jLabel40);
        jLabel40.setBounds(220, 240, 90, 23);

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel23.setText("mmHg");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput.add(jLabel23);
        jLabel23.setBounds(460, 240, 50, 23);

        jLabel26.setText("RR :");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput.add(jLabel26);
        jLabel26.setBounds(480, 240, 40, 20);

        RR.setFocusTraversalPolicyProvider(true);
        RR.setName("RR"); // NOI18N
        RR.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RRKeyPressed(evt);
            }
        });
        FormInput.add(RR);
        RR.setBounds(520, 240, 45, 20);

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel25.setText("x/menit");
        jLabel25.setName("jLabel25"); // NOI18N
        FormInput.add(jLabel25);
        jLabel25.setBounds(570, 240, 50, 20);

        jLabel18.setText("Suhu :");
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput.add(jLabel18);
        jLabel18.setBounds(610, 240, 40, 23);

        Suhu.setFocusTraversalPolicyProvider(true);
        Suhu.setName("Suhu"); // NOI18N
        Suhu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SuhuKeyPressed(evt);
            }
        });
        FormInput.add(Suhu);
        Suhu.setBounds(660, 240, 45, 23);

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel20.setText("°C");
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput.add(jLabel20);
        jLabel20.setBounds(710, 240, 30, 23);

        Nadi.setFocusTraversalPolicyProvider(true);
        Nadi.setName("Nadi"); // NOI18N
        Nadi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NadiKeyPressed(evt);
            }
        });
        FormInput.add(Nadi);
        Nadi.setBounds(760, 240, 45, 23);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel17.setText("Nadi");
        jLabel17.setName("jLabel17"); // NOI18N
        FormInput.add(jLabel17);
        jLabel17.setBounds(730, 240, 40, 23);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel16.setText("x/menit");
        jLabel16.setName("jLabel16"); // NOI18N
        FormInput.add(jLabel16);
        jLabel16.setBounds(810, 240, 50, 23);

        jLabel202.setText(":");
        jLabel202.setName("jLabel202"); // NOI18N
        FormInput.add(jLabel202);
        jLabel202.setBounds(100, 240, 10, 23);

        Kesadaran.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Compos Mentis", "Apatis", "Somnolen", "Sopor", "Soporcoma", "Coma" }));
        Kesadaran.setName("Kesadaran"); // NOI18N
        Kesadaran.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KesadaranKeyPressed(evt);
            }
        });
        FormInput.add(Kesadaran);
        Kesadaran.setBounds(110, 240, 125, 23);

        jLabel22.setText("TD :");
        jLabel22.setName("jLabel22"); // NOI18N
        FormInput.add(jLabel22);
        jLabel22.setBounds(360, 240, 40, 23);

        jLabel92.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel92.setText("Kesadaran");
        jLabel92.setName("jLabel92"); // NOI18N
        FormInput.add(jLabel92);
        jLabel92.setBounds(50, 240, 70, 23);

        jLabel278.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel278.setText("IV. KEBUTUHAN KOMUNIKASI DAN BELAJAR/EDUKASI (ORANGTUA)");
        jLabel278.setName("jLabel278"); // NOI18N
        FormInput.add(jLabel278);
        jLabel278.setBounds(20, 660, 400, 23);

        jLabel73.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel73.setText("VI. PENGKAJIAN RESIKO JATUH (GET UP AND GO)");
        jLabel73.setName("jLabel73"); // NOI18N
        FormInput.add(jLabel73);
        jLabel73.setBounds(20, 1500, 380, 23);

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel41.setText("A. Skrining Gizi Awal Dengan Strong-kids :");
        jLabel41.setName("jLabel41"); // NOI18N
        FormInput.add(jLabel41);
        jLabel41.setBounds(20, 1660, 460, 23);

        SG1Anak.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        SG1Anak.setName("SG1Anak"); // NOI18N
        SG1Anak.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SG1AnakItemStateChanged(evt);
            }
        });
        SG1Anak.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SG1AnakKeyPressed(evt);
            }
        });
        FormInput.add(SG1Anak);
        SG1Anak.setBounds(600, 1680, 80, 23);

        jLabel75.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel75.setText("1.");
        jLabel75.setName("jLabel75"); // NOI18N
        FormInput.add(jLabel75);
        jLabel75.setBounds(40, 1680, 20, 23);

        jLabel93.setText("Nilai :");
        jLabel93.setName("jLabel93"); // NOI18N
        FormInput.add(jLabel93);
        jLabel93.setBounds(680, 1680, 50, 23);

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel43.setText("2.");
        jLabel43.setName("jLabel43"); // NOI18N
        FormInput.add(jLabel43);
        jLabel43.setBounds(40, 1710, 20, 23);

        SG2Anak.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        SG2Anak.setName("SG2Anak"); // NOI18N
        SG2Anak.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SG2AnakItemStateChanged(evt);
            }
        });
        SG2Anak.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SG2AnakKeyPressed(evt);
            }
        });
        FormInput.add(SG2Anak);
        SG2Anak.setBounds(600, 1710, 80, 23);

        jLabel74.setText("Nilai :");
        jLabel74.setName("jLabel74"); // NOI18N
        FormInput.add(jLabel74);
        jLabel74.setBounds(680, 1710, 50, 23);

        jLabel79.setText("Total Skor :");
        jLabel79.setName("jLabel79"); // NOI18N
        FormInput.add(jLabel79);
        jLabel79.setBounds(660, 1820, 70, 23);

        TotalHasilAnak.setEditable(false);
        TotalHasilAnak.setText("0");
        TotalHasilAnak.setFocusTraversalPolicyProvider(true);
        TotalHasilAnak.setName("TotalHasilAnak"); // NOI18N
        FormInput.add(TotalHasilAnak);
        TotalHasilAnak.setBounds(730, 1820, 60, 23);

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel44.setText("Hasil Skrining :");
        jLabel44.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel44.setName("jLabel44"); // NOI18N
        FormInput.add(jLabel44);
        jLabel44.setBounds(40, 1820, 100, 23);

        Nilai1Anak.setEditable(false);
        Nilai1Anak.setText("0");
        Nilai1Anak.setFocusTraversalPolicyProvider(true);
        Nilai1Anak.setName("Nilai1Anak"); // NOI18N
        FormInput.add(Nilai1Anak);
        Nilai1Anak.setBounds(730, 1680, 60, 23);

        Nilai2Anak.setEditable(false);
        Nilai2Anak.setText("0");
        Nilai2Anak.setFocusTraversalPolicyProvider(true);
        Nilai2Anak.setName("Nilai2Anak"); // NOI18N
        FormInput.add(Nilai2Anak);
        Nilai2Anak.setBounds(730, 1710, 60, 23);

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel45.setText("3.");
        jLabel45.setName("jLabel45"); // NOI18N
        FormInput.add(jLabel45);
        jLabel45.setBounds(40, 1750, 20, 23);

        SG3Anak.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        SG3Anak.setName("SG3Anak"); // NOI18N
        SG3Anak.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SG3AnakItemStateChanged(evt);
            }
        });
        SG3Anak.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SG3AnakKeyPressed(evt);
            }
        });
        FormInput.add(SG3Anak);
        SG3Anak.setBounds(600, 1750, 80, 23);

        jLabel96.setText("Nilai :");
        jLabel96.setName("jLabel96"); // NOI18N
        FormInput.add(jLabel96);
        jLabel96.setBounds(680, 1750, 50, 23);

        Nilai3Anak.setEditable(false);
        Nilai3Anak.setText("0");
        Nilai3Anak.setFocusTraversalPolicyProvider(true);
        Nilai3Anak.setName("Nilai3Anak"); // NOI18N
        FormInput.add(Nilai3Anak);
        Nilai3Anak.setBounds(730, 1750, 60, 23);

        jLabel138.setText("Nilai :");
        jLabel138.setName("jLabel138"); // NOI18N
        FormInput.add(jLabel138);
        jLabel138.setBounds(680, 1790, 50, 23);

        Nilai4Anak.setEditable(false);
        Nilai4Anak.setText("0");
        Nilai4Anak.setFocusTraversalPolicyProvider(true);
        Nilai4Anak.setName("Nilai4Anak"); // NOI18N
        FormInput.add(Nilai4Anak);
        Nilai4Anak.setBounds(730, 1790, 60, 23);

        SG4Anak.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        SG4Anak.setName("SG4Anak"); // NOI18N
        SG4Anak.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SG4AnakItemStateChanged(evt);
            }
        });
        SG4Anak.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SG4AnakKeyPressed(evt);
            }
        });
        FormInput.add(SG4Anak);
        SG4Anak.setBounds(600, 1790, 80, 23);

        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel46.setText("4.");
        jLabel46.setName("jLabel46"); // NOI18N
        FormInput.add(jLabel46);
        jLabel46.setBounds(40, 1790, 20, 23);

        LabelSkrining.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LabelSkrining.setText("Risiko Rendah");
        LabelSkrining.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        LabelSkrining.setName("LabelSkrining"); // NOI18N
        FormInput.add(LabelSkrining);
        LabelSkrining.setBounds(120, 1820, 380, 23);

        jLabel158.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel158.setText("data berat badan bila ada atau untuk bayi < 1 tahun ; berat badan tidak naik selama 3 bulan terakhir)");
        jLabel158.setName("jLabel158"); // NOI18N
        FormInput.add(jLabel158);
        jLabel158.setBounds(50, 1720, 540, 23);

        jLabel154.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel154.setText("Apakah terdapat penurunan berat badan selama satu bulan terakhir? (berdasarkan penilaian objektif ");
        jLabel154.setName("jLabel154"); // NOI18N
        FormInput.add(jLabel154);
        jLabel154.setBounds(50, 1710, 540, 23);

        jLabel139.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel139.setText("Apakah pasien tampak kurus?");
        jLabel139.setName("jLabel139"); // NOI18N
        FormInput.add(jLabel139);
        jLabel139.setBounds(50, 1680, 530, 23);

        jLabel155.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel155.setText("Apakah terdapat salah satu dari kondisi tersebut? Diare > 5 kali/hari dan/muntah > 3 kali/hari dalam ");
        jLabel155.setName("jLabel155"); // NOI18N
        FormInput.add(jLabel155);
        jLabel155.setBounds(50, 1740, 540, 23);

        jLabel159.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel159.setText("seminggu terakhir; Asupan makanan berkurang selama 1 minggu terakhir");
        jLabel159.setName("jLabel159"); // NOI18N
        FormInput.add(jLabel159);
        jLabel159.setBounds(50, 1760, 540, 23);

        jLabel160.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel160.setText("Apakah terdapat penyakit atau keadaan yang menyebabkan pasien beresiko mengalami malnutrisi?");
        jLabel160.setName("jLabel160"); // NOI18N
        FormInput.add(jLabel160);
        jLabel160.setBounds(50, 1790, 540, 23);

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel42.setText("B. Skrining Gizi Awal Dengan MST (Malnutrition Screening Tool) Bagi Perawat (Dewasa) :");
        jLabel42.setName("jLabel42"); // NOI18N
        FormInput.add(jLabel42);
        jLabel42.setBounds(20, 1840, 460, 23);

        SG5Dewasa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Tidak Yakin", "Ya, 1-5 Kg", "Ya, 6-10 Kg", "Ya, 11-15 Kg", "Ya, >15 Kg" }));
        SG5Dewasa.setName("SG5Dewasa"); // NOI18N
        SG5Dewasa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SG5DewasaItemStateChanged(evt);
            }
        });
        SG5Dewasa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SG5DewasaKeyPressed(evt);
            }
        });
        FormInput.add(SG5Dewasa);
        SG5Dewasa.setBounds(530, 1860, 130, 23);

        Nilai5Dewasa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "2", "1", "2", "3", "4" }));
        Nilai5Dewasa.setName("Nilai5Dewasa"); // NOI18N
        Nilai5Dewasa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                Nilai5DewasaItemStateChanged(evt);
            }
        });
        Nilai5Dewasa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Nilai5DewasaKeyPressed(evt);
            }
        });
        FormInput.add(Nilai5Dewasa);
        Nilai5Dewasa.setBounds(730, 1860, 60, 23);

        jLabel140.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel140.setText("1. Apakah Ada Penurunan Berat Badan Yang Tidak Diinginkan Selama 6 Bulan Terakhir ?");
        jLabel140.setName("jLabel140"); // NOI18N
        FormInput.add(jLabel140);
        jLabel140.setBounds(70, 1860, 440, 23);

        jLabel141.setText("Nilai :");
        jLabel141.setName("jLabel141"); // NOI18N
        FormInput.add(jLabel141);
        jLabel141.setBounds(660, 1860, 70, 23);

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel47.setText("2. Apakah Nafsu Makan Berkurang Karena Tidak Nafsu Makan ?");
        jLabel47.setName("jLabel47"); // NOI18N
        FormInput.add(jLabel47);
        jLabel47.setBounds(70, 1890, 440, 23);

        SG6Dewasa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        SG6Dewasa.setName("SG6Dewasa"); // NOI18N
        SG6Dewasa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SG6DewasaItemStateChanged(evt);
            }
        });
        SG6Dewasa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SG6DewasaKeyPressed(evt);
            }
        });
        FormInput.add(SG6Dewasa);
        SG6Dewasa.setBounds(530, 1890, 130, 23);

        jLabel142.setText("Nilai :");
        jLabel142.setName("jLabel142"); // NOI18N
        FormInput.add(jLabel142);
        jLabel142.setBounds(660, 1890, 70, 23);

        Nilai6Dewasa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1" }));
        Nilai6Dewasa.setName("Nilai6Dewasa"); // NOI18N
        Nilai6Dewasa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Nilai6DewasaKeyPressed(evt);
            }
        });
        FormInput.add(Nilai6Dewasa);
        Nilai6Dewasa.setBounds(730, 1890, 60, 23);

        jLabel143.setText("Total Skor :");
        jLabel143.setName("jLabel143"); // NOI18N
        FormInput.add(jLabel143);
        jLabel143.setBounds(660, 1920, 70, 23);

        TotalHasilDewasa.setEditable(false);
        TotalHasilDewasa.setText("0");
        TotalHasilDewasa.setFocusTraversalPolicyProvider(true);
        TotalHasilDewasa.setName("TotalHasilDewasa"); // NOI18N
        FormInput.add(TotalHasilDewasa);
        TotalHasilDewasa.setBounds(730, 1920, 60, 23);

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel48.setText("Bila Skor >= 2, Pasien Beresiko Malnutrisi, Konsul Ke Ahli Gizi");
        jLabel48.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel48.setName("jLabel48"); // NOI18N
        FormInput.add(jLabel48);
        jLabel48.setBounds(70, 1920, 440, 23);

        jSeparator16.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator16.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator16.setName("jSeparator16"); // NOI18N
        FormInput.add(jSeparator16);
        jSeparator16.setBounds(10, 1640, 880, 1);

        jSeparator2.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator2.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator2.setName("jSeparator2"); // NOI18N
        FormInput.add(jSeparator2);
        jSeparator2.setBounds(0, 1953, 900, 3);

        jLabel97.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel97.setText("VI. KESIMPULAN");
        jLabel97.setName("jLabel97"); // NOI18N
        FormInput.add(jLabel97);
        jLabel97.setBounds(10, 2320, 90, 23);

        jLabel267.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel267.setText("Cara Transportasi Pulang");
        jLabel267.setName("jLabel267"); // NOI18N
        FormInput.add(jLabel267);
        jLabel267.setBounds(50, 2270, 140, 23);

        StatusRawat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Preventif", "Kuratif", "Rehabilitatif", "Paliatif", "Pelayanan Khusus/Spesifik", "Pelayanan Intensif" }));
        StatusRawat.setName("StatusRawat"); // NOI18N
        StatusRawat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                StatusRawatKeyPressed(evt);
            }
        });
        FormInput.add(StatusRawat);
        StatusRawat.setBounds(430, 2360, 160, 20);

        jLabel128.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel128.setText("TINDAK LANJUT");
        jLabel128.setName("jLabel128"); // NOI18N
        FormInput.add(jLabel128);
        jLabel128.setBounds(30, 2340, 110, 23);

        jLabel268.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel268.setText("DPJP :");
        jLabel268.setName("jLabel268"); // NOI18N
        FormInput.add(jLabel268);
        jLabel268.setBounds(370, 2390, 40, 23);

        StatusRawat1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Rawat Jalan", "Rawat Inap", "Dirujuk" }));
        StatusRawat1.setName("StatusRawat1"); // NOI18N
        StatusRawat1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                StatusRawat1KeyPressed(evt);
            }
        });
        FormInput.add(StatusRawat1);
        StatusRawat1.setBounds(120, 2360, 160, 20);

        jLabel269.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel269.setText("Status Pulang/Keluar :");
        jLabel269.setName("jLabel269"); // NOI18N
        FormInput.add(jLabel269);
        jLabel269.setBounds(630, 2360, 110, 23);

        StatusRawat2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sembuh", "Meninggal", "APS", "DOA" }));
        StatusRawat2.setName("StatusRawat2"); // NOI18N
        StatusRawat2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                StatusRawat2KeyPressed(evt);
            }
        });
        FormInput.add(StatusRawat2);
        StatusRawat2.setBounds(740, 2360, 160, 20);

        jLabel270.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel270.setText("Status Rawat :");
        jLabel270.setName("jLabel270"); // NOI18N
        FormInput.add(jLabel270);
        jLabel270.setBounds(40, 2360, 80, 23);

        KondisiPulang1.setFocusTraversalPolicyProvider(true);
        KondisiPulang1.setName("KondisiPulang1"); // NOI18N
        KondisiPulang1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KondisiPulang1ActionPerformed(evt);
            }
        });
        KondisiPulang1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KondisiPulang1KeyPressed(evt);
            }
        });
        FormInput.add(KondisiPulang1);
        KondisiPulang1.setBounds(120, 2420, 160, 24);

        jLabel271.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel271.setText("Meninggal Jam :");
        jLabel271.setName("jLabel271"); // NOI18N
        FormInput.add(jLabel271);
        jLabel271.setBounds(40, 2420, 80, 23);

        KdPetugas1.setEditable(false);
        KdPetugas1.setName("KdPetugas1"); // NOI18N
        KdPetugas1.setPreferredSize(new java.awt.Dimension(80, 23));
        KdPetugas1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KdPetugas1ActionPerformed(evt);
            }
        });
        KdPetugas1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdPetugas1KeyPressed(evt);
            }
        });
        FormInput.add(KdPetugas1);
        KdPetugas1.setBounds(400, 2390, 60, 23);

        NmPetugas1.setEditable(false);
        NmPetugas1.setName("NmPetugas1"); // NOI18N
        NmPetugas1.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(NmPetugas1);
        NmPetugas1.setBounds(460, 2390, 130, 23);

        BtnPetugas1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPetugas1.setMnemonic('2');
        BtnPetugas1.setToolTipText("Alt+2");
        BtnPetugas1.setName("BtnPetugas1"); // NOI18N
        BtnPetugas1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPetugas1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPetugas1ActionPerformed(evt);
            }
        });
        BtnPetugas1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPetugas1KeyPressed(evt);
            }
        });
        FormInput.add(BtnPetugas1);
        BtnPetugas1.setBounds(590, 2390, 28, 23);

        PerawatanLanjutan.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        PerawatanLanjutan.setColumns(20);
        PerawatanLanjutan.setRows(5);
        PerawatanLanjutan.setName("PerawatanLanjutan"); // NOI18N
        PerawatanLanjutan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PerawatanLanjutanKeyPressed(evt);
            }
        });
        FormInput.add(PerawatanLanjutan);
        PerawatanLanjutan.setBounds(290, 2220, 162, 72);

        jLabel279.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel279.setText("Transportasi Pulang :");
        jLabel279.setName("jLabel279"); // NOI18N
        FormInput.add(jLabel279);
        jLabel279.setBounds(630, 2390, 110, 23);

        StatusRawat3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kendaraan Pribadi", "Mobil Ambulance", "Kendaraan Umum" }));
        StatusRawat3.setName("StatusRawat3"); // NOI18N
        StatusRawat3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                StatusRawat3KeyPressed(evt);
            }
        });
        FormInput.add(StatusRawat3);
        StatusRawat3.setBounds(740, 2390, 160, 20);

        jLabel280.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel280.setText("Indikasi Rujuk :");
        jLabel280.setName("jLabel280"); // NOI18N
        FormInput.add(jLabel280);
        jLabel280.setBounds(40, 2390, 80, 23);

        KondisiPulang2.setFocusTraversalPolicyProvider(true);
        KondisiPulang2.setName("KondisiPulang2"); // NOI18N
        KondisiPulang2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KondisiPulang2ActionPerformed(evt);
            }
        });
        KondisiPulang2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KondisiPulang2KeyPressed(evt);
            }
        });
        FormInput.add(KondisiPulang2);
        KondisiPulang2.setBounds(120, 2390, 160, 24);

        jLabel281.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel281.setText("Pendidikan Kesehatan Pasien Pulang :");
        jLabel281.setName("jLabel281"); // NOI18N
        FormInput.add(jLabel281);
        jLabel281.setBounds(310, 2420, 190, 23);

        KondisiPulang3.setFocusTraversalPolicyProvider(true);
        KondisiPulang3.setName("KondisiPulang3"); // NOI18N
        KondisiPulang3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KondisiPulang3ActionPerformed(evt);
            }
        });
        KondisiPulang3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KondisiPulang3KeyPressed(evt);
            }
        });
        FormInput.add(KondisiPulang3);
        KondisiPulang3.setBounds(500, 2420, 400, 24);

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
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "06-06-2026" }));
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
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "06-06-2026" }));
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
        FormMasalahRencana.setLayout(new java.awt.GridLayout(3, 0, 1, 1));

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

        SkrinningGizi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)));
        SkrinningGizi.setName("SkrinningGizi"); // NOI18N
        SkrinningGizi.setLayout(new java.awt.GridLayout(4, 0, 1, 1));

        Scroll17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)));
        Scroll17.setName("Scroll17"); // NOI18N
        Scroll17.setOpaque(true);

        tbDetailGiziAnak.setName("tbDetailGiziAnak"); // NOI18N
        Scroll17.setViewportView(tbDetailGiziAnak);

        SkrinningGizi.add(Scroll17);

        Scroll18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)));
        Scroll18.setName("Scroll18"); // NOI18N
        Scroll18.setOpaque(true);

        tbDetailGiziDewasa.setName("tbDetailGiziDewasa"); // NOI18N
        Scroll18.setViewportView(tbDetailGiziDewasa);

        SkrinningGizi.add(Scroll18);

        TabRawat1.addTab("Skrinning Gizi", SkrinningGizi);

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
            Valid.textKosong(KeluhanUtama, "Riwayat Penyakit Sekarang");
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
                if (KdPetugas.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(), 79).toString())) {
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
            Valid.textKosong(KeluhanUtama, "Riwayat Penyakit Sekarang");
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
                    if (KdPetugas.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(), 79).toString())) {
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
                            "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir,pasien.agama,bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,penilaian_awal_keperawatan_igd.tanggal,penilaian_awal_keperawatan_igd.informasi,"
                            + "penilaian_awal_keperawatan_igd.keluhan_utama,penilaian_awal_keperawatan_igd.rpd,penilaian_awal_keperawatan_igd.rpo,penilaian_awal_keperawatan_igd.status_kehamilan,penilaian_awal_keperawatan_igd.gravida,penilaian_awal_keperawatan_igd.para,"
                            + "penilaian_awal_keperawatan_igd.abortus,penilaian_awal_keperawatan_igd.hpht,penilaian_awal_keperawatan_igd.tekanan,penilaian_awal_keperawatan_igd.pupil,penilaian_awal_keperawatan_igd.neurosensorik,penilaian_awal_keperawatan_igd.integumen,penilaian_awal_keperawatan_igd.turgor,"
                            + "penilaian_awal_keperawatan_igd.edema,penilaian_awal_keperawatan_igd.mukosa,penilaian_awal_keperawatan_igd.perdarahan,penilaian_awal_keperawatan_igd.jumlah_perdarahan,penilaian_awal_keperawatan_igd.warna_perdarahan,penilaian_awal_keperawatan_igd.intoksikasi,"
                            + "penilaian_awal_keperawatan_igd.bab,penilaian_awal_keperawatan_igd.xbab,penilaian_awal_keperawatan_igd.kbab,penilaian_awal_keperawatan_igd.wbab,penilaian_awal_keperawatan_igd.bak,penilaian_awal_keperawatan_igd.xbak,penilaian_awal_keperawatan_igd.wbak,"
                            + "penilaian_awal_keperawatan_igd.lbak,penilaian_awal_keperawatan_igd.psikologis,penilaian_awal_keperawatan_igd.jiwa,penilaian_awal_keperawatan_igd.perilaku,penilaian_awal_keperawatan_igd.dilaporkan,penilaian_awal_keperawatan_igd.sebutkan,penilaian_awal_keperawatan_igd.hubungan,pasien.stts_nikah,"
                            + "penilaian_awal_keperawatan_igd.tinggal_dengan,penilaian_awal_keperawatan_igd.ket_tinggal,pasien.pekerjaan,penjab.png_jawab,penilaian_awal_keperawatan_igd.budaya,penilaian_awal_keperawatan_igd.ket_budaya,pasien.pnd,penilaian_awal_keperawatan_igd.pendidikan_pj,penilaian_awal_keperawatan_igd.ket_pendidikan_pj,"
                            + "penilaian_awal_keperawatan_igd.edukasi,penilaian_awal_keperawatan_igd.ket_edukasi,penilaian_awal_keperawatan_igd.kemampuan,penilaian_awal_keperawatan_igd.aktifitas,penilaian_awal_keperawatan_igd.alat_bantu,penilaian_awal_keperawatan_igd.ket_bantu,"
                            + "penilaian_awal_keperawatan_igd.nyeri,penilaian_awal_keperawatan_igd.provokes,penilaian_awal_keperawatan_igd.ket_provokes,penilaian_awal_keperawatan_igd.quality,penilaian_awal_keperawatan_igd.ket_quality,penilaian_awal_keperawatan_igd.lokasi,penilaian_awal_keperawatan_igd.menyebar,"
                            + "penilaian_awal_keperawatan_igd.skala_nyeri,penilaian_awal_keperawatan_igd.durasi,penilaian_awal_keperawatan_igd.nyeri_hilang,penilaian_awal_keperawatan_igd.ket_nyeri,penilaian_awal_keperawatan_igd.pada_dokter,penilaian_awal_keperawatan_igd.ket_dokter,"
                            + "penilaian_awal_keperawatan_igd.berjalan_a,penilaian_awal_keperawatan_igd.berjalan_b,penilaian_awal_keperawatan_igd.berjalan_c,penilaian_awal_keperawatan_igd.hasil,penilaian_awal_keperawatan_igd.lapor,penilaian_awal_keperawatan_igd.ket_lapor,"
                            + "penilaian_awal_keperawatan_igd.rencana,penilaian_awal_keperawatan_igd.nip,petugas.nama "
                            + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                            + "inner join penilaian_awal_keperawatan_igd on reg_periksa.no_rawat=penilaian_awal_keperawatan_igd.no_rawat "
                            + "inner join petugas on penilaian_awal_keperawatan_igd.nip=petugas.nip "
                            + "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "
                            + "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj "
                            + "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik where "
                            + "penilaian_awal_keperawatan_igd.tanggal between ? and ? order by penilaian_awal_keperawatan_igd.tanggal");
                } else {
                    ps = koneksi.prepareStatement(
                            "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir,pasien.agama,bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,penilaian_awal_keperawatan_igd.tanggal,penilaian_awal_keperawatan_igd.informasi,"
                            + "penilaian_awal_keperawatan_igd.keluhan_utama,penilaian_awal_keperawatan_igd.rpd,penilaian_awal_keperawatan_igd.rpo,penilaian_awal_keperawatan_igd.status_kehamilan,penilaian_awal_keperawatan_igd.gravida,penilaian_awal_keperawatan_igd.para,"
                            + "penilaian_awal_keperawatan_igd.abortus,penilaian_awal_keperawatan_igd.hpht,penilaian_awal_keperawatan_igd.tekanan,penilaian_awal_keperawatan_igd.pupil,penilaian_awal_keperawatan_igd.neurosensorik,penilaian_awal_keperawatan_igd.integumen,penilaian_awal_keperawatan_igd.turgor,"
                            + "penilaian_awal_keperawatan_igd.edema,penilaian_awal_keperawatan_igd.mukosa,penilaian_awal_keperawatan_igd.perdarahan,penilaian_awal_keperawatan_igd.jumlah_perdarahan,penilaian_awal_keperawatan_igd.warna_perdarahan,penilaian_awal_keperawatan_igd.intoksikasi,"
                            + "penilaian_awal_keperawatan_igd.bab,penilaian_awal_keperawatan_igd.xbab,penilaian_awal_keperawatan_igd.kbab,penilaian_awal_keperawatan_igd.wbab,penilaian_awal_keperawatan_igd.bak,penilaian_awal_keperawatan_igd.xbak,penilaian_awal_keperawatan_igd.wbak,"
                            + "penilaian_awal_keperawatan_igd.lbak,penilaian_awal_keperawatan_igd.psikologis,penilaian_awal_keperawatan_igd.jiwa,penilaian_awal_keperawatan_igd.perilaku,penilaian_awal_keperawatan_igd.dilaporkan,penilaian_awal_keperawatan_igd.sebutkan,penilaian_awal_keperawatan_igd.hubungan,pasien.stts_nikah,"
                            + "penilaian_awal_keperawatan_igd.tinggal_dengan,penilaian_awal_keperawatan_igd.ket_tinggal,pasien.pekerjaan,penjab.png_jawab,penilaian_awal_keperawatan_igd.budaya,penilaian_awal_keperawatan_igd.ket_budaya,pasien.pnd,penilaian_awal_keperawatan_igd.pendidikan_pj,penilaian_awal_keperawatan_igd.ket_pendidikan_pj,"
                            + "penilaian_awal_keperawatan_igd.edukasi,penilaian_awal_keperawatan_igd.ket_edukasi,penilaian_awal_keperawatan_igd.kemampuan,penilaian_awal_keperawatan_igd.aktifitas,penilaian_awal_keperawatan_igd.alat_bantu,penilaian_awal_keperawatan_igd.ket_bantu,"
                            + "penilaian_awal_keperawatan_igd.nyeri,penilaian_awal_keperawatan_igd.provokes,penilaian_awal_keperawatan_igd.ket_provokes,penilaian_awal_keperawatan_igd.quality,penilaian_awal_keperawatan_igd.ket_quality,penilaian_awal_keperawatan_igd.lokasi,penilaian_awal_keperawatan_igd.menyebar,"
                            + "penilaian_awal_keperawatan_igd.skala_nyeri,penilaian_awal_keperawatan_igd.durasi,penilaian_awal_keperawatan_igd.nyeri_hilang,penilaian_awal_keperawatan_igd.ket_nyeri,penilaian_awal_keperawatan_igd.pada_dokter,penilaian_awal_keperawatan_igd.ket_dokter,"
                            + "penilaian_awal_keperawatan_igd.berjalan_a,penilaian_awal_keperawatan_igd.berjalan_b,penilaian_awal_keperawatan_igd.berjalan_c,penilaian_awal_keperawatan_igd.hasil,penilaian_awal_keperawatan_igd.lapor,penilaian_awal_keperawatan_igd.ket_lapor,"
                            + "penilaian_awal_keperawatan_igd.rencana,penilaian_awal_keperawatan_igd.nip,petugas.nama "
                            + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                            + "inner join penilaian_awal_keperawatan_igd on reg_periksa.no_rawat=penilaian_awal_keperawatan_igd.no_rawat "
                            + "inner join petugas on penilaian_awal_keperawatan_igd.nip=petugas.nip "
                            + "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "
                            + "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj "
                            + "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik where "
                            + "penilaian_awal_keperawatan_igd.tanggal between ? and ? and "
                            + "(reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or "
                            + "penilaian_awal_keperawatan_igd.nip like ? or petugas.nama like ?) "
                            + "order by penilaian_awal_keperawatan_igd.tanggal");
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
                    StringBuilder htmlContent = new StringBuilder();
                    htmlContent.append(
                            "<tr class='isi'>"
                            + "<td valign='middle' bgcolor='#FFFAFA' align='center' width='9%'><b>PASIEN & PETUGAS</b></td>"
                            + "<td valign='middle' bgcolor='#FFFAFA' align='center' width='10%'><b>I. RIWAYAT KESEHATAN PASIEN</b></td>"
                            + "<td valign='middle' bgcolor='#FFFAFA' align='center' width='15%'><b>II. PEMERIKSAAN FISIK</b></td>"
                            + "<td valign='middle' bgcolor='#FFFAFA' align='center' width='14%'><b>III. RIWAYAT PSIKOLOGIS - SOSIAL - EKONOMI - BUDAYA - SPIRITUAL</b></td>"
                            + "<td valign='middle' bgcolor='#FFFAFA' align='center' width='8%'><b>IV. PENGKAJIAN FUNGSI</b></td>"
                            + "<td valign='middle' bgcolor='#FFFAFA' align='center' width='12%'><b>V. SKALA NYERI</b></td>"
                            + "<td valign='middle' bgcolor='#FFFAFA' align='center' width='16%'><b>VI. PENGKAJIAN RESIKO JATUH (GET UP AND GO)</b></td>"
                            + "<td valign='middle' bgcolor='#FFFAFA' align='center' width='11%'><b>MASALAH & RENCANA KEPERAWATAN</b></td>"
                            + "</tr>"
                    );
                    while (rs.next()) {
                        masalahkeperawatanigd = "";
                        ps2 = koneksi.prepareStatement(
                                "select master_masalah_keperawatan_igd.kode_masalah,master_masalah_keperawatan_igd.nama_masalah from master_masalah_keperawatan_igd "
                                + "inner join penilaian_awal_keperawatan_igd_masalah on penilaian_awal_keperawatan_igd_masalah.kode_masalah=master_masalah_keperawatan_igd.kode_masalah "
                                + "where penilaian_awal_keperawatan_igd_masalah.no_rawat=? order by penilaian_awal_keperawatan_igd_masalah.kode_masalah");
                        try {
                            ps2.setString(1, rs.getString("no_rawat"));
                            rs2 = ps2.executeQuery();
                            while (rs2.next()) {
                                masalahkeperawatanigd = rs2.getString("nama_masalah") + ", " + masalahkeperawatanigd;
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
                                "<tr class='isi'>"
                                + "<td valign='top' cellpadding='0' cellspacing='0'>"
                                + "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>No.Rawat</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("no_rawat") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>No.R.M.</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("no_rkm_medis") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Nama Pasien</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("nm_pasien") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>J.K.</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("jk") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Tgl.Lahir</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("tgl_lahir") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Agama</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("agama") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Bahasa</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("nama_bahasa") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Pekerjaan</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("pekerjaan") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Pembayaran</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("png_jawab") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Pendidikan</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("pnd") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Stts.Nikah</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("stts_nikah") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Cacat Fisik</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("nama_cacat") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Petugas</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("nip") + " " + rs.getString("nama") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Tgl.Asuhan</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("tanggal") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Informasi</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("informasi") + "</td>"
                                + "</tr>"
                                + "</table>"
                                + "</td>"
                                + "<td valign='top' cellpadding='0' cellspacing='0'>"
                                + "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>RPS</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("keluhan_utama") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>RPD</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("rpd") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>RPO</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("rpo") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Stts.Hami</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("status_kehamilan") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>HPHT</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("hpht") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Para</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("para") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Abortus</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("abortus") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Gravida</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("gravida") + "</td>"
                                + "</tr>"
                                + "</table>"
                                + "</td>"
                                + "<td valign='top' cellpadding='0' cellspacing='0'>"
                                + "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>Tekanan Intrakranial</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>" + rs.getString("tekanan") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>Pupil</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>" + rs.getString("pupil") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>Neurosensorik / Muskuloskeletal</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>" + rs.getString("neurosensorik") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>Integumen</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>" + rs.getString("integumen") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>Turgor kulit</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>" + rs.getString("turgor") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>Edema</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>" + rs.getString("edema") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>Mukosa Mulut</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>" + rs.getString("mukosa") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>Perdarahan</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>" + rs.getString("perdarahan") + ", Jumlah : " + rs.getString("jumlah_perdarahan") + ", Warna : " + rs.getString("warna_perdarahan") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>Intoksikasi</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>" + rs.getString("intoksikasi") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>Eliminasi</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>"
                                + "BAB : -Frekuensi : " + rs.getString("bab") + " x / " + rs.getString("xbab") + " -Konsistensi : " + rs.getString("kbab") + " -Warna : " + rs.getString("wbab") + "<br>"
                                + "BAK : -Frekuensi : " + rs.getString("bak") + " x / " + rs.getString("xbak") + " -Warna : " + rs.getString("wbak") + " -Lain-lain : " + rs.getString("lbak") + "<br>"
                                + "</td>"
                                + "</tr>"
                                + "</table>"
                                + "</td>"
                                + "<td valign='top' cellpadding='0' cellspacing='0'>"
                                + "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Kondisi Psikologis</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("psikologis") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Gangguan Jiwa Di Masa Lalu</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("jiwa") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Adakah perilaku</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("perilaku") + ", Dilaporkan Ke : " + rs.getString("dilaporkan") + ", Sebutkan : " + rs.getString("sebutkan") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Hubungan Pasien Dengan Anggota Keluarga</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("hubungan") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Tinggal Dengan</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("tinggal_dengan") + (rs.getString("ket_tinggal").equals("") ? "" : ", " + rs.getString("ket_tinggal")) + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Kepercayaan / Budaya / Nilai-nilai Khusus</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("budaya") + (rs.getString("ket_budaya").equals("") ? "" : ", " + rs.getString("ket_budaya")) + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Pendidikan P.J.</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("pendidikan_pj") + (rs.getString("ket_pendidikan_pj").equals("") ? "" : ", " + rs.getString("ket_pendidikan_pj")) + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Edukasi Diberikan Kepada</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("edukasi") + (rs.getString("ket_edukasi").equals("") ? "" : ", " + rs.getString("ket_edukasi")) + "</td>"
                                + "</tr>"
                                + "</table>"
                                + "</td>"
                                + "<td valign='top' cellpadding='0' cellspacing='0'>"
                                + "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>Kemampuan Aktifitas Sehari-hari</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>" + rs.getString("kemampuan") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>Aktifitas</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>" + rs.getString("aktifitas") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>Alat Bantu</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>" + rs.getString("alat_bantu") + (rs.getString("ket_bantu").equals("") ? "" : ", " + rs.getString("ket_bantu")) + "</td>"
                                + "</tr>"
                                + "</table>"
                                + "</td>"
                                + "<td valign='top' cellpadding='0' cellspacing='0'>"
                                + "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Tingkat Nyeri</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("nyeri") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Provokes</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("provokes") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Ket. Provokes</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("ket_provokes") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Kualitas</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("quality") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Ket. Kualitas</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("ket_quality") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Lokas</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("lokasi") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Menyebar</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("menyebar") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Skala Nyeri</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("skala_nyeri") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Durasi</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("durasi") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Nyeri Hilang</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("nyeri_hilang") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Ket. Hilang Nyeri</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("ket_nyeri") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Lapor Ke Dokter</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("pada_dokter") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Jam Lapor</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("ket_dokter") + "</td>"
                                + "</tr>"
                                + "</table>"
                                + "</td>"
                                + "<td valign='top' cellpadding='0' cellspacing='0'>"
                                + "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Tidak seimbang/sempoyongan/limbung</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("berjalan_a") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Jalan dengan menggunakan alat bantu (kruk, tripot, kursi roda, orang lain)</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("berjalan_b") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Menopang saat akan duduk, tampak memegang pinggiran kursi atau meja/benda lain sebagai penopang</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("berjalan_c") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Hasil</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("hasil") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Dilaporan ke dokter?</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("lapor") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Jam Lapor</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("ket_lapor") + "</td>"
                                + "</tr>"
                                + "</table>"
                                + "</td>"
                                + "<td valign='top' cellpadding='0' cellspacing='0'>"
                                + "Masalah Keperawatan : " + masalahkeperawatanigd + "<br><br>"
                                + "Rencana Keperawatan : " + rs.getString("rencana")
                                + "</td>"
                                + "</tr>"
                        );
                    }
                    LoadHTML.setText(
                            "<html>"
                            + "<table width='100%' border='0' align='center' cellpadding='1px' cellspacing='0' class='tbl_form'>"
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

                    File f = new File("DataPenilaianAwalKeperawatanIGD.html");
                    BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                    bw.write(LoadHTML.getText().replaceAll("<head>", "<head>"
                            + "<link href=\"file2.css\" rel=\"stylesheet\" type=\"text/css\" />"
                            + "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                            + "<tr class='isi2'>"
                            + "<td valign='top' align='center'>"
                            + "<font size='4' face='Tahoma'>" + akses.getnamars() + "</font><br>"
                            + akses.getalamatrs() + ", " + akses.getkabupatenrs() + ", " + akses.getpropinsirs() + "<br>"
                            + akses.getkontakrs() + ", E-mail : " + akses.getemailrs() + "<br><br>"
                            + "<font size='2' face='Tahoma'>DATA PENGKAJIAN AWAL KEPERAWATAN IGD<br><br></font>"
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

    private void KdPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdPetugasKeyPressed

    }//GEN-LAST:event_KdPetugasKeyPressed

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
        //Valid.pindah(evt,Monitoring,BtnSimpan);
    }//GEN-LAST:event_BtnPetugasKeyPressed

    private void KeluhanUtamaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeluhanUtamaKeyPressed
        Valid.pindah2(evt, Informasi, RPD);
    }//GEN-LAST:event_KeluhanUtamaKeyPressed

    private void RPDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RPDKeyPressed
        Valid.pindah2(evt, KeluhanUtama, RPO);
    }//GEN-LAST:event_RPDKeyPressed

    private void RPOKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RPOKeyPressed
        Valid.pindah2(evt, RPD, StatusKehamilan);
    }//GEN-LAST:event_RPOKeyPressed

    private void AktifitasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AktifitasKeyPressed
        Valid.pindah(evt, ADL, AlatBantu);
    }//GEN-LAST:event_AktifitasKeyPressed

    private void AlatBantuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AlatBantuKeyPressed
        Valid.pindah(evt, Aktifitas, KetAlatBantu);
    }//GEN-LAST:event_AlatBantuKeyPressed

    private void KetAlatBantuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetAlatBantuKeyPressed
        Valid.pindah(evt, AlatBantu, Nyeri);
    }//GEN-LAST:event_KetAlatBantuKeyPressed

    private void ADLKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ADLKeyPressed
        Valid.pindah(evt, KetEdukasi, Aktifitas);
    }//GEN-LAST:event_ADLKeyPressed

    private void TinggalDenganKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TinggalDenganKeyPressed
        Valid.pindah(evt, Hubungan, KetTinggal);
    }//GEN-LAST:event_TinggalDenganKeyPressed

    private void KetTinggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetTinggalKeyPressed
        Valid.pindah(evt, TinggalDengan, StatusBudaya);
    }//GEN-LAST:event_KetTinggalKeyPressed

    private void EdukasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EdukasiKeyPressed
        Valid.pindah(evt, KetPendidikanPJ, KetEdukasi);
    }//GEN-LAST:event_EdukasiKeyPressed

    private void KetEdukasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetEdukasiKeyPressed
        Valid.pindah(evt, Edukasi, ADL);
    }//GEN-LAST:event_KetEdukasiKeyPressed

    private void LaporKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LaporKeyPressed
        Valid.pindah(evt, Hasil, KetLapor);
    }//GEN-LAST:event_LaporKeyPressed

    private void ATSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ATSKeyPressed
        Valid.pindah(evt, KetDokter, BJM);
    }//GEN-LAST:event_ATSKeyPressed

    private void BJMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BJMKeyPressed
        Valid.pindah(evt, ATS, MSA);
    }//GEN-LAST:event_BJMKeyPressed

    private void HasilKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HasilKeyPressed
        Valid.pindah(evt, MSA, Lapor);
    }//GEN-LAST:event_HasilKeyPressed

    private void KetLaporKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetLaporKeyPressed
        Valid.pindah(evt, Lapor, Rencana);
    }//GEN-LAST:event_KetLaporKeyPressed

    private void MSAKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MSAKeyPressed
        Valid.pindah(evt, BJM, Hasil);
    }//GEN-LAST:event_MSAKeyPressed

    private void NyeriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NyeriKeyPressed
        Valid.pindah(evt, KetAlatBantu, Provokes);
    }//GEN-LAST:event_NyeriKeyPressed

    private void ProvokesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProvokesKeyPressed
        Valid.pindah(evt, Nyeri, KetProvokes);
    }//GEN-LAST:event_ProvokesKeyPressed

    private void KetProvokesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetProvokesKeyPressed
        Valid.pindah(evt, Provokes, Quality);
    }//GEN-LAST:event_KetProvokesKeyPressed

    private void QualityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_QualityKeyPressed
        Valid.pindah(evt, KetProvokes, KetQuality);
    }//GEN-LAST:event_QualityKeyPressed

    private void KetQualityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetQualityKeyPressed
        Valid.pindah(evt, Quality, Lokasi);
    }//GEN-LAST:event_KetQualityKeyPressed

    private void LokasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LokasiKeyPressed
        Valid.pindah(evt, KetQuality, Menyebar);
    }//GEN-LAST:event_LokasiKeyPressed

    private void MenyebarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MenyebarKeyPressed
        Valid.pindah(evt, Lokasi, SkalaNyeri);
    }//GEN-LAST:event_MenyebarKeyPressed

    private void SkalaNyeriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaNyeriKeyPressed
        Valid.pindah(evt, Menyebar, Durasi);
    }//GEN-LAST:event_SkalaNyeriKeyPressed

    private void DurasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DurasiKeyPressed
        Valid.pindah(evt, SkalaNyeri, NyeriHilang);
    }//GEN-LAST:event_DurasiKeyPressed

    private void NyeriHilangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NyeriHilangKeyPressed
        Valid.pindah(evt, Durasi, KetNyeri);
    }//GEN-LAST:event_NyeriHilangKeyPressed

    private void KetNyeriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetNyeriKeyPressed
        Valid.pindah(evt, NyeriHilang, PadaDokter);
    }//GEN-LAST:event_KetNyeriKeyPressed

    private void PadaDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PadaDokterKeyPressed
        Valid.pindah(evt, KetNyeri, KetDokter);
    }//GEN-LAST:event_PadaDokterKeyPressed

    private void KetDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetDokterKeyPressed
        Valid.pindah(evt, PadaDokter, ATS);
    }//GEN-LAST:event_KetDokterKeyPressed

    private void TglAsuhanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TglAsuhanKeyPressed
        Valid.pindah2(evt, Rencana, RPD);
    }//GEN-LAST:event_TglAsuhanKeyPressed

    private void StatusBudayaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StatusBudayaKeyPressed
        Valid.pindah(evt, KetTinggal, KetBudaya);
    }//GEN-LAST:event_StatusBudayaKeyPressed

    private void KetBudayaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetBudayaKeyPressed
        Valid.pindah(evt, StatusBudaya, PendidikanPJ);
    }//GEN-LAST:event_KetBudayaKeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        try {
            if (Valid.daysOld("./cache/masalahkeperawatanigd.iyem") < 30) {
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
            finger = Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?", tbObat.getValueAt(tbObat.getSelectedRow(), 79).toString());
            param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + tbObat.getValueAt(tbObat.getSelectedRow(), 80).toString() + "\nID " + (finger.equals("") ? tbObat.getValueAt(tbObat.getSelectedRow(), 79).toString() : finger) + "\n" + Valid.SetTgl3(tbObat.getValueAt(tbObat.getSelectedRow(), 8).toString()));
            try {
                masalahkeperawatanigd = "";
                ps2 = koneksi.prepareStatement(
                        "select master_masalah_keperawatan_igd.kode_masalah,master_masalah_keperawatan_igd.nama_masalah from master_masalah_keperawatan_igd "
                        + "inner join penilaian_awal_keperawatan_igd_masalah on penilaian_awal_keperawatan_igd_masalah.kode_masalah=master_masalah_keperawatan_igd.kode_masalah "
                        + "where penilaian_awal_keperawatan_igd_masalah.no_rawat=? order by penilaian_awal_keperawatan_igd_masalah.kode_masalah");
                try {
                    ps2.setString(1, tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
                    rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        masalahkeperawatanigd = rs2.getString("nama_masalah") + ", " + masalahkeperawatanigd;
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
            param.put("masalah", masalahkeperawatanigd);
            try {
                masalahkeperawatanigd = "";
                ps2 = koneksi.prepareStatement(
                        "select master_rencana_keperawatan_igd.kode_rencana,master_rencana_keperawatan_igd.rencana_keperawatan from master_rencana_keperawatan_igd "
                        + "inner join penilaian_awal_keperawatan_ralan_rencana_igd on penilaian_awal_keperawatan_ralan_rencana_igd.kode_rencana=master_rencana_keperawatan_igd.kode_rencana "
                        + "where penilaian_awal_keperawatan_ralan_rencana_igd.no_rawat=? order by penilaian_awal_keperawatan_ralan_rencana_igd.kode_rencana");
                try {
                    ps2.setString(1, tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
                    rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        masalahkeperawatanigd = rs2.getString("rencana_keperawatan") + ", " + masalahkeperawatanigd;
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
            param.put("rencana", masalahkeperawatanigd);
            try {
                kebutuhanedukasi = "";
                ps = koneksi.prepareStatement(
                        "select master_kebutuhan_edukasi_komunikasi.kd_kebutuhan_edukasi,master_kebutuhan_edukasi_komunikasi.kebutuhan_edukasi from master_kebutuhan_edukasi_komunikasi "
                        + "inner join penilaian_awal_keperawatan_igd_kebutuhan_edukasi on penilaian_awal_keperawatan_igd_kebutuhan_edukasi.kd_kebutuhan_edukasi=master_kebutuhan_edukasi_komunikasi.kd_kebutuhan_edukasi "
                        + "where penilaian_awal_keperawatan_igd_kebutuhan_edukasi.no_rawat=? order by penilaian_awal_keperawatan_igd_kebutuhan_edukasi.kd_kebutuhan_edukasi");
                try {
                    ps.setString(1, tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        kebutuhanedukasi = rs.getString("kebutuhan_edukasi") + ", " + kebutuhanedukasi;
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
            
            param.put("kebutuhan_edukasi", kebutuhanedukasi);
            try {
                kebutuhanedukasi = "";
                ps = koneksi.prepareStatement(
                        "select master_rencana_edukasi_komunikasi.kd_rencana_edukasi,master_rencana_edukasi_komunikasi.rencana_edukasi from master_rencana_edukasi_komunikasi "
                        + "inner join penilaian_awal_keperawatan_igd_rencana_edukasi on penilaian_awal_keperawatan_igd_rencana_edukasi.kd_rencana_edukasi=master_rencana_edukasi_komunikasi.kd_rencana_edukasi "
                        + "where penilaian_awal_keperawatan_igd_rencana_edukasi.no_rawat=? order by penilaian_awal_keperawatan_igd_rencana_edukasi.kd_rencana_edukasi");
                try {
                    ps.setString(1, tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        kebutuhanedukasi = rs.getString("rencana_edukasi") + ", " + kebutuhanedukasi;
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
            param.put("rencana_edukasi", kebutuhanedukasi);
            Valid.MyReportqry("rptCetakPenilaianAwalKeperawatanIGD.jasper", "report", "::[ Laporan Pengkajian Awal Keperawatan IGD ]::",
                        "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir,pasien.agama,bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,penilaian_awal_keperawatan_igd.tanggal,penilaian_awal_keperawatan_igd.informasi,"
                        + "penilaian_awal_keperawatan_igd.keluhan_utama,penilaian_awal_keperawatan_igd.rpd,penilaian_awal_keperawatan_igd.rpo,penilaian_awal_keperawatan_igd.status_kehamilan,penilaian_awal_keperawatan_igd.gravida,penilaian_awal_keperawatan_igd.para,"
                        + "penilaian_awal_keperawatan_igd.abortus,penilaian_awal_keperawatan_igd.hpht,penilaian_awal_keperawatan_igd.tekanan,penilaian_awal_keperawatan_igd.pupil,penilaian_awal_keperawatan_igd.neurosensorik,penilaian_awal_keperawatan_igd.integumen,penilaian_awal_keperawatan_igd.turgor,"
                        + "penilaian_awal_keperawatan_igd.edema,penilaian_awal_keperawatan_igd.mukosa,penilaian_awal_keperawatan_igd.perdarahan,penilaian_awal_keperawatan_igd.jumlah_perdarahan,penilaian_awal_keperawatan_igd.warna_perdarahan,penilaian_awal_keperawatan_igd.intoksikasi,"
                        + "penilaian_awal_keperawatan_igd.bab,penilaian_awal_keperawatan_igd.xbab,penilaian_awal_keperawatan_igd.kbab,penilaian_awal_keperawatan_igd.wbab,penilaian_awal_keperawatan_igd.bak,penilaian_awal_keperawatan_igd.xbak,penilaian_awal_keperawatan_igd.wbak,"
                        + "penilaian_awal_keperawatan_igd.lbak,penilaian_awal_keperawatan_igd.psikologis,penilaian_awal_keperawatan_igd.jiwa,penilaian_awal_keperawatan_igd.perilaku,penilaian_awal_keperawatan_igd.dilaporkan,penilaian_awal_keperawatan_igd.sebutkan,penilaian_awal_keperawatan_igd.hubungan,pasien.stts_nikah,"
                        + "penilaian_awal_keperawatan_igd.tinggal_dengan,penilaian_awal_keperawatan_igd.ket_tinggal,pasien.pekerjaan,penjab.png_jawab,penilaian_awal_keperawatan_igd.budaya,penilaian_awal_keperawatan_igd.ket_budaya,pasien.pnd,penilaian_awal_keperawatan_igd.pendidikan_pj,penilaian_awal_keperawatan_igd.ket_pendidikan_pj,"
                        + "penilaian_awal_keperawatan_igd.edukasi,penilaian_awal_keperawatan_igd.ket_edukasi,penilaian_awal_keperawatan_igd.kemampuan_baca_tulis,penilaian_awal_keperawatan_igd.butuh_penerjemah,penilaian_awal_keperawatan_igd.keterangan_butuh_penerjemah,"
                        + "penilaian_awal_keperawatan_igd.terdapat_hambatan_belajar,penilaian_awal_keperawatan_igd.hambatan_belajar,penilaian_awal_keperawatan_igd.keterangan_hambatan_belajar,penilaian_awal_keperawatan_igd.hambatan_cara_bicara,penilaian_awal_keperawatan_igd.hambatan_bahasa_isyarat,"
                        + "penilaian_awal_keperawatan_igd.cara_belajar_disukai,penilaian_awal_keperawatan_igd.kesediaan_menerima_informasi,penilaian_awal_keperawatan_igd.ket_kesediaan_menerima_informasi,penilaian_awal_keperawatan_igd.pemahaman_nutrisi,penilaian_awal_keperawatan_igd.pemahaman_penyakit,penilaian_awal_keperawatan_igd.pemahaman_pengobatan,penilaian_awal_keperawatan_igd.pemahaman_perawatan,"
                        + "penilaian_awal_keperawatan_igd.keyakinan_nilai,penilaian_awal_keperawatan_igd.keterbatasan_fisik,penilaian_awal_keperawatan_igd.hambatan_emosional,penilaian_awal_keperawatan_igd.motivasi,penilaian_awal_keperawatan_igd.kemampuan,penilaian_awal_keperawatan_igd.aktifitas,penilaian_awal_keperawatan_igd.alat_bantu,penilaian_awal_keperawatan_igd.ket_bantu,"
                        + "penilaian_awal_keperawatan_igd.nyeri,penilaian_awal_keperawatan_igd.provokes,penilaian_awal_keperawatan_igd.ket_provokes,penilaian_awal_keperawatan_igd.quality,penilaian_awal_keperawatan_igd.ket_quality,penilaian_awal_keperawatan_igd.lokasi,penilaian_awal_keperawatan_igd.menyebar,"
                        + "penilaian_awal_keperawatan_igd.skala_nyeri,penilaian_awal_keperawatan_igd.durasi,penilaian_awal_keperawatan_igd.nyeri_hilang,penilaian_awal_keperawatan_igd.ket_nyeri,penilaian_awal_keperawatan_igd.pada_dokter,penilaian_awal_keperawatan_igd.ket_dokter,"
                        + "penilaian_awal_keperawatan_igd.berjalan_a,penilaian_awal_keperawatan_igd.berjalan_b,penilaian_awal_keperawatan_igd.berjalan_c,penilaian_awal_keperawatan_igd.hasil,penilaian_awal_keperawatan_igd.lapor,penilaian_awal_keperawatan_igd.ket_lapor,penilaian_awal_keperawatan_igd.informasi_perencanaan_pulang,"
                        + "penilaian_awal_keperawatan_igd.tanggal_pulang,penilaian_awal_keperawatan_igd.kondisi_saat_pulang,penilaian_awal_keperawatan_igd.perawatan_lanjutan,penilaian_awal_keperawatan_igd.lama_ratarata,penilaian_awal_keperawatan_igd.tanggal_pulang,penilaian_awal_keperawatan_igd.kondisi_saat_pulang,penilaian_awal_keperawatan_igd.perawatan_lanjutan,"
                        + "penilaian_awal_keperawatan_igd.cara_transportasi,penilaian_awal_keperawatan_igd.transportasi_digunakan,"
                        + "penilaian_awal_keperawatan_igd.rencana,penilaian_awal_keperawatan_igd.kondisi_pasien,penilaian_awal_keperawatan_igd.cara_pulang,penilaian_awal_keperawatan_igd.dirujukke,penilaian_awal_keperawatan_igd.indikasi,penilaian_awal_keperawatan_igd.kamar_rawat,penilaian_awal_keperawatan_igd.dpjp,dokter.nm_dokter,penilaian_awal_keperawatan_igd.waktu_keluar,penilaian_awal_keperawatan_igd.waktu_meninggal,penilaian_awal_keperawatan_igd.transportasi_pulang,penilaian_awal_keperawatan_igd.pendidikan_kesehatan,penilaian_awal_keperawatan_igd.nip,petugas.nama "
                        + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                        + "inner join penilaian_awal_keperawatan_igd on reg_periksa.no_rawat=penilaian_awal_keperawatan_igd.no_rawat "
                        + "inner join petugas on penilaian_awal_keperawatan_igd.nip=petugas.nip "
                        + "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "
                        + "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj "
                        + "left join dokter on penilaian_awal_keperawatan_igd.dpjp=dokter.kd_dokter "
                        + "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik where reg_periksa.no_rawat='" + tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString() + "'", param);
        } else {
            JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data terlebih dahulu..!!!!");
        }
    }//GEN-LAST:event_BtnPrint1ActionPerformed

    private void TPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TPasienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TPasienActionPerformed

    private void StatusKehamilanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StatusKehamilanKeyPressed
        Valid.pindah(evt, RPO, HPHT);
    }//GEN-LAST:event_StatusKehamilanKeyPressed

    private void TekananKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TekananKeyPressed
        Valid.pindah(evt, Gravida, Pupil);
    }//GEN-LAST:event_TekananKeyPressed

    private void PupilKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PupilKeyPressed
        Valid.pindah(evt, Tekanan, Neurosensorik);
    }//GEN-LAST:event_PupilKeyPressed

    private void NeurosensorikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NeurosensorikKeyPressed
        Valid.pindah(evt, Pupil, Integumen);
    }//GEN-LAST:event_NeurosensorikKeyPressed

    private void IntegumenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IntegumenKeyPressed
        Valid.pindah(evt, Neurosensorik, Turgor);
    }//GEN-LAST:event_IntegumenKeyPressed

    private void TurgorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TurgorKeyPressed
        Valid.pindah(evt, Integumen, Edema);
    }//GEN-LAST:event_TurgorKeyPressed

    private void EdemaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EdemaKeyPressed
        Valid.pindah(evt, Turgor, Mukosa);
    }//GEN-LAST:event_EdemaKeyPressed

    private void MukosaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MukosaKeyPressed
        Valid.pindah(evt, Edema, Perdarahan);
    }//GEN-LAST:event_MukosaKeyPressed

    private void PerdarahanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PerdarahanKeyPressed
        Valid.pindah(evt, Mukosa, JumlahPerdarahan);
    }//GEN-LAST:event_PerdarahanKeyPressed

    private void IntoksikasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IntoksikasiKeyPressed
        Valid.pindah(evt, WarnaPerdarahan, BAB);
    }//GEN-LAST:event_IntoksikasiKeyPressed

    private void PsikologisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PsikologisKeyPressed
        Valid.pindah(evt, LBAK, Jiwa);
    }//GEN-LAST:event_PsikologisKeyPressed

    private void JiwaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JiwaKeyPressed
        Valid.pindah(evt, Psikologis, Perilaku);
    }//GEN-LAST:event_JiwaKeyPressed

    private void PerilakuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PerilakuKeyPressed
        Valid.pindah(evt, Jiwa, Dilaporkan);
    }//GEN-LAST:event_PerilakuKeyPressed

    private void HubunganKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HubunganKeyPressed
        Valid.pindah(evt, Sebutkan, TinggalDengan);
    }//GEN-LAST:event_HubunganKeyPressed

    private void StatusPernikahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StatusPernikahanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_StatusPernikahanActionPerformed

    private void KetPendidikanPJKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetPendidikanPJKeyPressed
        Valid.pindah(evt, PendidikanPJ, Edukasi);
    }//GEN-LAST:event_KetPendidikanPJKeyPressed

    private void PendidikanPJKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PendidikanPJKeyPressed
        Valid.pindah(evt, KetBudaya, KetPendidikanPJ);
    }//GEN-LAST:event_PendidikanPJKeyPressed

    private void InformasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_InformasiKeyPressed
        Valid.pindah(evt, TglAsuhan, KeluhanUtama);
    }//GEN-LAST:event_InformasiKeyPressed

    private void HPHTKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HPHTKeyPressed
        Valid.pindah(evt, StatusKehamilan, Para);
    }//GEN-LAST:event_HPHTKeyPressed

    private void ParaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ParaKeyPressed
        Valid.pindah(evt, HPHT, Abortus);
    }//GEN-LAST:event_ParaKeyPressed

    private void AbortusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AbortusKeyPressed
        Valid.pindah(evt, Para, Gravida);
    }//GEN-LAST:event_AbortusKeyPressed

    private void GravidaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GravidaKeyPressed
        Valid.pindah(evt, Abortus, Tekanan);
    }//GEN-LAST:event_GravidaKeyPressed

    private void JumlahPerdarahanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JumlahPerdarahanKeyPressed
        Valid.pindah(evt, Perdarahan, WarnaPerdarahan);
    }//GEN-LAST:event_JumlahPerdarahanKeyPressed

    private void WarnaPerdarahanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_WarnaPerdarahanKeyPressed
        Valid.pindah(evt, JumlahPerdarahan, Intoksikasi);
    }//GEN-LAST:event_WarnaPerdarahanKeyPressed

    private void BABKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BABKeyPressed
        Valid.pindah(evt, Intoksikasi, XBAB);
    }//GEN-LAST:event_BABKeyPressed

    private void XBABKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_XBABKeyPressed
        Valid.pindah(evt, BAB, KBAB);
    }//GEN-LAST:event_XBABKeyPressed

    private void KBABKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KBABKeyPressed
        Valid.pindah(evt, XBAB, WBAB);
    }//GEN-LAST:event_KBABKeyPressed

    private void WBABKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_WBABKeyPressed
        Valid.pindah(evt, KBAB, BAK);
    }//GEN-LAST:event_WBABKeyPressed

    private void BAKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BAKKeyPressed
        Valid.pindah(evt, WBAB, XBAK);
    }//GEN-LAST:event_BAKKeyPressed

    private void XBAKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_XBAKKeyPressed
        Valid.pindah(evt, BAK, WBAK);
    }//GEN-LAST:event_XBAKKeyPressed

    private void WBAKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_WBAKKeyPressed
        Valid.pindah(evt, XBAK, LBAK);
    }//GEN-LAST:event_WBAKKeyPressed

    private void LBAKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LBAKKeyPressed
        Valid.pindah(evt, WBAK, Psikologis);
    }//GEN-LAST:event_LBAKKeyPressed

    private void DilaporkanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DilaporkanKeyPressed
        Valid.pindah(evt, Perilaku, Sebutkan);
    }//GEN-LAST:event_DilaporkanKeyPressed

    private void SebutkanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SebutkanKeyPressed
        Valid.pindah(evt, Dilaporkan, Hubungan);
    }//GEN-LAST:event_SebutkanKeyPressed

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

    private void BtnTambahMasalahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahMasalahActionPerformed
        if (masalahkeperawatan == null || !masalahkeperawatan.isDisplayable()) {
            masalahkeperawatan = new MasterMasalahKeperawatanIGD(null, false);
            masalahkeperawatan.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            masalahkeperawatan.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    masalahkeperawatan = null;
                }
            });

            masalahkeperawatan.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
            masalahkeperawatan.setLocationRelativeTo(internalFrame1);
        }
        if (masalahkeperawatan == null) {
            return;
        }
        if (!masalahkeperawatan.isVisible()) {
            masalahkeperawatan.isCek();
        }

        if (masalahkeperawatan.isVisible()) {
            masalahkeperawatan.toFront();
            return;
        }
        masalahkeperawatan.setVisible(true);
    }//GEN-LAST:event_BtnTambahMasalahActionPerformed

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

    private void TCariMasalahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariMasalahKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            runBackground(() -> tampilMasalah2());
        } else if ((evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) || (evt.getKeyCode() == KeyEvent.VK_TAB)) {
            Rencana.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            KetDokter.requestFocus();
        }
    }//GEN-LAST:event_TCariMasalahKeyPressed

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
        if (rencanakeperawatan == null || !rencanakeperawatan.isDisplayable()) {
            rencanakeperawatan = new MasterRencanaKeperawatanIGD(null, false);
            rencanakeperawatan.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            rencanakeperawatan.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    rencanakeperawatan = null;
                }
            });

            rencanakeperawatan.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
            rencanakeperawatan.setLocationRelativeTo(internalFrame1);
        }
        if (rencanakeperawatan == null) {
            return;
        }
        if (!rencanakeperawatan.isVisible()) {
            rencanakeperawatan.isCek();
        }

        if (rencanakeperawatan.isVisible()) {
            rencanakeperawatan.toFront();
            return;
        }
        rencanakeperawatan.setVisible(true);
    }//GEN-LAST:event_BtnTambahRencanaActionPerformed

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
//        Valid.pindah(evt, PemahamanPengobatan, SG1);
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
        if(tabModeKebutuhanEdukasi.getRowCount()!=0){
            try {
                runBackground(() ->tampilRencanaEdukasi2());
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbKebutuhanEdukasiMouseClicked

    private void tbKebutuhanEdukasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbKebutuhanEdukasiKeyPressed
        if(tabModeKebutuhanEdukasi.getRowCount()!=0){
            if(evt.getKeyCode()==KeyEvent.VK_SHIFT){
                TCariMasalah.setText("");
                TCariMasalah.requestFocus();
            }
        }
    }//GEN-LAST:event_tbKebutuhanEdukasiKeyPressed

    private void tbKebutuhanEdukasiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbKebutuhanEdukasiKeyReleased
        if(tabModeKebutuhanEdukasi.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    runBackground(() ->tampilRencanaEdukasi2());
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbKebutuhanEdukasiKeyReleased

    private void BtnTambahKebutuhanEdukasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahKebutuhanEdukasiActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        MasterKebutuhanEdukasi form = new MasterKebutuhanEdukasi(null, false);
//        form.isCek();
        form.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        form.setLocationRelativeTo(internalFrame1);
        form.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnTambahKebutuhanEdukasiActionPerformed

    private void BtnAllKebutuhanEdukasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllKebutuhanEdukasiActionPerformed
        TCari.setText("");
        runBackground(() ->tampilKebutuhanEdukasi());
    }//GEN-LAST:event_BtnAllKebutuhanEdukasiActionPerformed

    private void BtnAllKebutuhanEdukasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKebutuhanEdukasiKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnAllKebutuhanEdukasiActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnCariMasalah, TCariMasalah);
        }
    }//GEN-LAST:event_BtnAllKebutuhanEdukasiKeyPressed

    private void BtnCariKebutuhanEdukasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariKebutuhanEdukasiActionPerformed
        runBackground(() ->tampilKebutuhanEdukasi2());
    }//GEN-LAST:event_BtnCariKebutuhanEdukasiActionPerformed

    private void BtnCariKebutuhanEdukasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKebutuhanEdukasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCariKebutuhanEdukasiKeyPressed

    private void TCariMasalah1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariMasalah1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TCariMasalah1KeyPressed

    private void BtnTambahRencanaEdukasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahRencanaEdukasiActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        MasterRencanaEdukasi form = new MasterRencanaEdukasi(null, false);
//        form.isCek();
        form.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        form.setLocationRelativeTo(internalFrame1);
        form.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnTambahRencanaEdukasiActionPerformed

    private void BtnAllRencanaEdukasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllRencanaEdukasiActionPerformed
        TCariRencana.setText("");
        runBackground(() ->LoadRencanaEdukasi());
    }//GEN-LAST:event_BtnAllRencanaEdukasiActionPerformed

    private void BtnAllRencanaEdukasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllRencanaEdukasiKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnAllRencanaEdukasiActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnCariRencana, TCariRencana);
        }
    }//GEN-LAST:event_BtnAllRencanaEdukasiKeyPressed

    private void BtnCariRencanaEdukasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariRencanaEdukasiActionPerformed
        runBackground(() ->tampilRencanaEdukasi2());
    }//GEN-LAST:event_BtnCariRencanaEdukasiActionPerformed

    private void BtnCariRencanaEdukasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariRencanaEdukasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCariRencanaEdukasiKeyPressed

    private void TCariRencana1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariRencana1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TCariRencana1KeyPressed

    private void InformasiPerencanaanPulangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_InformasiPerencanaanPulangKeyPressed
        //        Valid.pindah(evt,SkalaNIPS5,LamaRatarata);
    }//GEN-LAST:event_InformasiPerencanaanPulangKeyPressed

    private void KondisiPulangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KondisiPulangKeyPressed
        Valid.pindah(evt, TanggalPulang, PerawatanLanjutan);
    }//GEN-LAST:event_KondisiPulangKeyPressed

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

    private void TanggalPulangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalPulangKeyPressed
        Valid.pindah(evt, LamaRatarata, KondisiPulang);
    }//GEN-LAST:event_TanggalPulangKeyPressed

    private void TanggalPulangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TanggalPulangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TanggalPulangActionPerformed

    private void BtnAllKebutuhanEdukasiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnAllKebutuhanEdukasiMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnAllKebutuhanEdukasiMouseClicked

    private void GCSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GCSKeyPressed
        Valid.pindah(evt,Kesadaran,TD);
    }//GEN-LAST:event_GCSKeyPressed

    private void TDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TDKeyPressed
        Valid.pindah(evt,GCS,RR);
    }//GEN-LAST:event_TDKeyPressed

    private void RRKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RRKeyPressed
        Valid.pindah(evt,TD,Suhu);
    }//GEN-LAST:event_RRKeyPressed

    private void SuhuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SuhuKeyPressed
        Valid.pindah(evt,RR,Nadi);
    }//GEN-LAST:event_SuhuKeyPressed

    private void NadiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NadiKeyPressed
//        Valid.pindah(evt,Suhu,BB);
    }//GEN-LAST:event_NadiKeyPressed

    private void KesadaranKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KesadaranKeyPressed
//        Valid.pindah(evt,RiwayatPersalinanLainnya,GCS);
    }//GEN-LAST:event_KesadaranKeyPressed

    private void SG1AnakItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SG1AnakItemStateChanged
        Nilai1Anak.setText(SG1Anak.getSelectedIndex()+"");
        isTotal();
    }//GEN-LAST:event_SG1AnakItemStateChanged

    private void SG1AnakKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SG1AnakKeyPressed
//        Valid.pindah(evt,Alergi,SG2);
    }//GEN-LAST:event_SG1AnakKeyPressed

    private void SG2AnakItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SG2AnakItemStateChanged
        Nilai2Anak.setText(SG2Anak.getSelectedIndex()+"");
        isTotal();
    }//GEN-LAST:event_SG2AnakItemStateChanged

    private void SG2AnakKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SG2AnakKeyPressed
        Valid.pindah(evt,SG1Anak,SG3Anak);
    }//GEN-LAST:event_SG2AnakKeyPressed

    private void SG3AnakItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SG3AnakItemStateChanged
        Nilai3Anak.setText(SG3Anak.getSelectedIndex()+"");
        isTotal();
    }//GEN-LAST:event_SG3AnakItemStateChanged

    private void SG3AnakKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SG3AnakKeyPressed
        Valid.pindah(evt,SG2Anak,SG4Anak);
    }//GEN-LAST:event_SG3AnakKeyPressed

    private void SG4AnakItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SG4AnakItemStateChanged
        Nilai4Anak.setText(SG4Anak.getSelectedIndex()+"");
        isTotal();
    }//GEN-LAST:event_SG4AnakItemStateChanged

    private void SG4AnakKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SG4AnakKeyPressed
        //Valid.pindah(evt,SG3,SG5);
    }//GEN-LAST:event_SG4AnakKeyPressed

    private void SG5DewasaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SG5DewasaItemStateChanged
        Nilai5Dewasa.setSelectedIndex(SG5Dewasa.getSelectedIndex());
        TotalHasilDewasa.setText(""+(Integer.parseInt(Nilai5Dewasa.getSelectedItem().toString())+Integer.parseInt(Nilai6Dewasa.getSelectedItem().toString())));
    }//GEN-LAST:event_SG5DewasaItemStateChanged

    private void SG5DewasaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SG5DewasaKeyPressed
//        Valid.pindah(evt,Nilai5Dewasa);
    }//GEN-LAST:event_SG5DewasaKeyPressed

    private void Nilai5DewasaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_Nilai5DewasaItemStateChanged
        TotalHasilDewasa.setText(""+(Integer.parseInt(Nilai5Dewasa.getSelectedItem().toString())+Integer.parseInt(Nilai6Dewasa.getSelectedItem().toString())));
    }//GEN-LAST:event_Nilai5DewasaItemStateChanged

    private void Nilai5DewasaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Nilai5DewasaKeyPressed
        Valid.pindah(evt,SG1Anak,SG2Anak);
    }//GEN-LAST:event_Nilai5DewasaKeyPressed

    private void SG6DewasaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SG6DewasaItemStateChanged
        Nilai6Dewasa.setSelectedIndex(SG6Dewasa.getSelectedIndex());
        TotalHasilDewasa.setText(""+(Integer.parseInt(Nilai6Dewasa.getSelectedItem().toString())+Integer.parseInt(Nilai6Dewasa.getSelectedItem().toString())));
    }//GEN-LAST:event_SG6DewasaItemStateChanged

    private void SG6DewasaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SG6DewasaKeyPressed
        Valid.pindah(evt,Nilai1Anak,Nilai2Anak);
    }//GEN-LAST:event_SG6DewasaKeyPressed

    private void Nilai6DewasaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Nilai6DewasaKeyPressed
        Valid.pindah(evt,SG2Anak,BtnSimpan);
    }//GEN-LAST:event_Nilai6DewasaKeyPressed

    private void StatusRawatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StatusRawatKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_StatusRawatKeyPressed

    private void StatusRawat1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StatusRawat1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_StatusRawat1KeyPressed

    private void StatusRawat2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StatusRawat2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_StatusRawat2KeyPressed

    private void KondisiPulang1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KondisiPulang1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KondisiPulang1KeyPressed

    private void KondisiPulang1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KondisiPulang1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KondisiPulang1ActionPerformed

    private void KdPetugas1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdPetugas1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KdPetugas1KeyPressed

    private void BtnPetugas1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPetugas1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnPetugas1ActionPerformed

    private void BtnPetugas1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPetugas1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnPetugas1KeyPressed

    private void KdPetugas1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KdPetugas1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KdPetugas1ActionPerformed

    private void StatusRawat3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StatusRawat3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_StatusRawat3KeyPressed

    private void KondisiPulang2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KondisiPulang2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KondisiPulang2ActionPerformed

    private void KondisiPulang2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KondisiPulang2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KondisiPulang2KeyPressed

    private void KondisiPulang3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KondisiPulang3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KondisiPulang3ActionPerformed

    private void KondisiPulang3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KondisiPulang3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KondisiPulang3KeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMPenilaianAwalKeperawatanIGD dialog = new RMPenilaianAwalKeperawatanIGD(new javax.swing.JFrame(), true);
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
    private widget.ComboBox ATS;
    private widget.TextBox Abortus;
    private widget.TextBox Agama;
    private widget.ComboBox Aktifitas;
    private widget.ComboBox AlatBantu;
    private widget.TextBox BAB;
    private widget.TextBox BAK;
    private widget.ComboBox BJM;
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
    private widget.Button BtnKeluar;
    private widget.Button BtnPetugas;
    private widget.Button BtnPetugas1;
    private widget.Button BtnPrint;
    private widget.Button BtnPrint1;
    private widget.Button BtnSimpan;
    private widget.Button BtnTambahKebutuhanEdukasi;
    private widget.Button BtnTambahMasalah;
    private widget.Button BtnTambahRencana;
    private widget.Button BtnTambahRencanaEdukasi;
    private widget.ComboBox ButuhPenerjemah;
    private widget.TextBox CacatFisik;
    private widget.ComboBox CaraBelajarDisukai;
    private widget.ComboBox CaraTransportasiPulang;
    private widget.CekBox ChkAccor;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.TextArea DetailRencana;
    private widget.TextBox Dilaporkan;
    private widget.TextBox Durasi;
    private widget.ComboBox Edema;
    private widget.ComboBox Edukasi;
    private widget.PanelBiasa FormInput;
    private widget.PanelBiasa FormKebutuhanRencana;
    private widget.PanelBiasa FormMasalahRencana;
    private widget.PanelBiasa FormMenu;
    private widget.TextBox GCS;
    private widget.TextBox Gravida;
    private widget.TextBox HPHT;
    private widget.ComboBox HambatanBahasaIsyarat;
    private widget.ComboBox HambatanBelajar;
    private widget.ComboBox HambatanCaraBicara;
    private widget.ComboBox HambatanEmosional;
    private widget.ComboBox Hasil;
    private widget.ComboBox Hubungan;
    private widget.ComboBox Informasi;
    private widget.ComboBox InformasiPerencanaanPulang;
    private widget.ComboBox Integumen;
    private widget.ComboBox Intoksikasi;
    private widget.ComboBox Jiwa;
    private widget.TextBox Jk;
    private widget.TextBox JumlahPerdarahan;
    private widget.TextBox KBAB;
    private widget.TextBox KdPetugas;
    private widget.TextBox KdPetugas1;
    private widget.TextArea KeluhanUtama;
    private widget.ComboBox KemampuanBacaTulis;
    private widget.ComboBox Kesadaran;
    private widget.ComboBox KesediaanMenerimaInformasi;
    private widget.TextBox KetAlatBantu;
    private widget.TextBox KetBudaya;
    private widget.TextBox KetDokter;
    private widget.TextBox KetEdukasi;
    private widget.TextBox KetLapor;
    private widget.TextBox KetNyeri;
    private widget.TextBox KetPendidikanPJ;
    private widget.TextBox KetProvokes;
    private widget.TextBox KetQuality;
    private widget.TextBox KetTinggal;
    private widget.TextBox KeteranganButuhPenerjemah;
    private widget.TextBox KeteranganHambatanBelajar;
    private widget.TextBox KeteranganKesediaanMenerimaInformasi;
    private widget.ComboBox KeterbatasanFisik;
    private widget.ComboBox KeyakinanNilai;
    private widget.TextBox KondisiPulang;
    private widget.TextBox KondisiPulang1;
    private widget.TextBox KondisiPulang2;
    private widget.TextBox KondisiPulang3;
    private widget.TextBox LBAK;
    private widget.Label LCount;
    private widget.Label LabelSkrining;
    private widget.TextBox LamaRatarata;
    private widget.ComboBox Lapor;
    private widget.editorpane LoadHTML;
    private widget.TextBox Lokasi;
    private widget.ComboBox MSA;
    private widget.ComboBox Menyebar;
    private widget.ComboBox Motivasi;
    private widget.ComboBox Mukosa;
    private widget.TextBox Nadi;
    private widget.ComboBox Neurosensorik;
    private widget.TextBox Nilai1Anak;
    private widget.TextBox Nilai2Anak;
    private widget.TextBox Nilai3Anak;
    private widget.TextBox Nilai4Anak;
    private widget.ComboBox Nilai5Dewasa;
    private widget.ComboBox Nilai6Dewasa;
    private widget.TextBox NmPetugas;
    private widget.TextBox NmPetugas1;
    private widget.ComboBox Nyeri;
    private widget.ComboBox NyeriHilang;
    private widget.ComboBox PadaDokter;
    private widget.PanelBiasa PanelAccor;
    private usu.widget.glass.PanelGlass PanelWall;
    private widget.TextBox Para;
    private widget.TextBox Pekerjaan;
    private widget.ComboBox PemahamanNutrisi;
    private widget.ComboBox PemahamanPengobatan;
    private widget.ComboBox PemahamanPenyakit;
    private widget.ComboBox PemahamanPerawatan;
    private widget.TextBox Pembayaran;
    private widget.ComboBox PendidikanPJ;
    private widget.TextBox PendidikanPasien;
    private widget.TextArea PerawatanLanjutan;
    private widget.ComboBox Perdarahan;
    private widget.ComboBox Perilaku;
    private widget.ComboBox Provokes;
    private widget.ComboBox Psikologis;
    private widget.ComboBox Pupil;
    private widget.ComboBox Quality;
    private widget.TextArea RPD;
    private widget.TextArea RPO;
    private widget.TextBox RR;
    private widget.TextArea Rencana;
    private widget.ComboBox SG1Anak;
    private widget.ComboBox SG2Anak;
    private widget.ComboBox SG3Anak;
    private widget.ComboBox SG4Anak;
    private widget.ComboBox SG5Dewasa;
    private widget.ComboBox SG6Dewasa;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll10;
    private widget.ScrollPane Scroll11;
    private widget.ScrollPane Scroll12;
    private widget.ScrollPane Scroll15;
    private widget.ScrollPane Scroll16;
    private widget.ScrollPane Scroll17;
    private widget.ScrollPane Scroll18;
    private widget.ScrollPane Scroll7;
    private widget.ScrollPane Scroll8;
    private widget.ScrollPane Scroll9;
    private widget.TextBox Sebutkan;
    private widget.ComboBox SkalaNyeri;
    private widget.PanelBiasa SkrinningGizi;
    private widget.ComboBox StatusBudaya;
    private widget.ComboBox StatusKehamilan;
    private widget.TextBox StatusPernikahan;
    private widget.ComboBox StatusRawat;
    private widget.ComboBox StatusRawat1;
    private widget.ComboBox StatusRawat2;
    private widget.ComboBox StatusRawat3;
    private widget.TextBox Suhu;
    private widget.TextBox TCari;
    private widget.TextBox TCariMasalah;
    private widget.TextBox TCariMasalah1;
    private widget.TextBox TCariRencana;
    private widget.TextBox TCariRencana1;
    private widget.TextBox TD;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRM1;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.TextBox TPasien1;
    private javax.swing.JTabbedPane TabRawat;
    private javax.swing.JTabbedPane TabRawat1;
    private javax.swing.JTabbedPane TabRencanaKeperawatan;
    private javax.swing.JTabbedPane TabRencanaKeperawatan1;
    private widget.Tanggal TanggalPulang;
    private widget.TextBox TanggalRegistrasi;
    private widget.ComboBox Tekanan;
    private widget.ComboBox TerdapatHambatanBelajar;
    private widget.Tanggal TglAsuhan;
    private widget.TextBox TglLahir;
    private widget.ComboBox TinggalDengan;
    private widget.TextBox TotalHasilAnak;
    private widget.TextBox TotalHasilDewasa;
    private widget.ComboBox TransportasiYangDigunakan;
    private widget.ComboBox Turgor;
    private widget.TextBox WBAB;
    private widget.TextBox WBAK;
    private widget.TextBox WarnaPerdarahan;
    private widget.TextBox XBAB;
    private widget.TextBox XBAK;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame3;
    private widget.Label jLabel10;
    private widget.Label jLabel100;
    private widget.Label jLabel101;
    private widget.Label jLabel102;
    private widget.Label jLabel103;
    private widget.Label jLabel104;
    private widget.Label jLabel105;
    private widget.Label jLabel106;
    private widget.Label jLabel107;
    private widget.Label jLabel108;
    private widget.Label jLabel109;
    private widget.Label jLabel11;
    private widget.Label jLabel110;
    private widget.Label jLabel111;
    private widget.Label jLabel112;
    private widget.Label jLabel113;
    private widget.Label jLabel114;
    private widget.Label jLabel115;
    private widget.Label jLabel116;
    private widget.Label jLabel117;
    private widget.Label jLabel118;
    private widget.Label jLabel119;
    private widget.Label jLabel120;
    private widget.Label jLabel121;
    private widget.Label jLabel122;
    private widget.Label jLabel123;
    private widget.Label jLabel124;
    private widget.Label jLabel125;
    private widget.Label jLabel126;
    private widget.Label jLabel127;
    private widget.Label jLabel128;
    private widget.Label jLabel138;
    private widget.Label jLabel139;
    private widget.Label jLabel140;
    private widget.Label jLabel141;
    private widget.Label jLabel142;
    private widget.Label jLabel143;
    private widget.Label jLabel154;
    private widget.Label jLabel155;
    private widget.Label jLabel158;
    private widget.Label jLabel159;
    private widget.Label jLabel16;
    private widget.Label jLabel160;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel20;
    private widget.Label jLabel202;
    private widget.Label jLabel21;
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
    private widget.Label jLabel25;
    private widget.Label jLabel250;
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
    private widget.Label jLabel268;
    private widget.Label jLabel269;
    private widget.Label jLabel270;
    private widget.Label jLabel271;
    private widget.Label jLabel272;
    private widget.Label jLabel273;
    private widget.Label jLabel274;
    private widget.Label jLabel275;
    private widget.Label jLabel276;
    private widget.Label jLabel277;
    private widget.Label jLabel278;
    private widget.Label jLabel279;
    private widget.Label jLabel280;
    private widget.Label jLabel281;
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
    private widget.Label jLabel47;
    private widget.Label jLabel48;
    private widget.Label jLabel50;
    private widget.Label jLabel51;
    private widget.Label jLabel52;
    private widget.Label jLabel55;
    private widget.Label jLabel56;
    private widget.Label jLabel57;
    private widget.Label jLabel58;
    private widget.Label jLabel6;
    private widget.Label jLabel60;
    private widget.Label jLabel63;
    private widget.Label jLabel64;
    private widget.Label jLabel65;
    private widget.Label jLabel66;
    private widget.Label jLabel67;
    private widget.Label jLabel68;
    private widget.Label jLabel7;
    private widget.Label jLabel70;
    private widget.Label jLabel71;
    private widget.Label jLabel72;
    private widget.Label jLabel73;
    private widget.Label jLabel74;
    private widget.Label jLabel75;
    private widget.Label jLabel76;
    private widget.Label jLabel77;
    private widget.Label jLabel78;
    private widget.Label jLabel79;
    private widget.Label jLabel8;
    private widget.Label jLabel80;
    private widget.Label jLabel81;
    private widget.Label jLabel82;
    private widget.Label jLabel83;
    private widget.Label jLabel84;
    private widget.Label jLabel85;
    private widget.Label jLabel86;
    private widget.Label jLabel87;
    private widget.Label jLabel88;
    private widget.Label jLabel89;
    private widget.Label jLabel9;
    private widget.Label jLabel90;
    private widget.Label jLabel91;
    private widget.Label jLabel92;
    private widget.Label jLabel93;
    private widget.Label jLabel94;
    private widget.Label jLabel95;
    private widget.Label jLabel96;
    private widget.Label jLabel97;
    private widget.Label jLabel98;
    private widget.Label jLabel99;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private widget.Label label11;
    private widget.Label label12;
    private widget.Label label13;
    private widget.Label label14;
    private widget.Label label15;
    private widget.Label label16;
    private widget.Label label29;
    private widget.PanelBiasa panelBiasa1;
    private widget.PanelBiasa panelBiasa2;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ScrollPane scrollInput;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollPane2;
    private widget.ScrollPane scrollPane4;
    private widget.ScrollPane scrollPane5;
    private widget.ScrollPane scrollPane6;
    private widget.ScrollPane scrollPane8;
    private widget.Table tbDetailGiziAnak;
    private widget.Table tbDetailGiziDewasa;
    private widget.Table tbDetailKebutuhanEdukasi;
    private widget.Table tbDetailRencanaEdukasi;
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
                        "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir,pasien.agama,bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,penilaian_awal_keperawatan_igd.tanggal,penilaian_awal_keperawatan_igd.informasi,"
                        + "penilaian_awal_keperawatan_igd.keluhan_utama,penilaian_awal_keperawatan_igd.rpd,penilaian_awal_keperawatan_igd.rpo,penilaian_awal_keperawatan_igd.status_kehamilan,penilaian_awal_keperawatan_igd.gravida,penilaian_awal_keperawatan_igd.para,"
                        + "penilaian_awal_keperawatan_igd.abortus,penilaian_awal_keperawatan_igd.hpht,penilaian_awal_keperawatan_igd.kesadaran,penilaian_awal_keperawatan_igd.gcs,penilaian_awal_keperawatan_igd.td,penilaian_awal_keperawatan_igd.rr,penilaian_awal_keperawatan_igd.suhu,penilaian_awal_keperawatan_igd.nadi,penilaian_awal_keperawatan_igd.tekanan,penilaian_awal_keperawatan_igd.pupil,penilaian_awal_keperawatan_igd.neurosensorik,penilaian_awal_keperawatan_igd.integumen,penilaian_awal_keperawatan_igd.turgor,"
                        + "penilaian_awal_keperawatan_igd.edema,penilaian_awal_keperawatan_igd.mukosa,penilaian_awal_keperawatan_igd.perdarahan,penilaian_awal_keperawatan_igd.jumlah_perdarahan,penilaian_awal_keperawatan_igd.warna_perdarahan,penilaian_awal_keperawatan_igd.intoksikasi,"
                        + "penilaian_awal_keperawatan_igd.bab,penilaian_awal_keperawatan_igd.xbab,penilaian_awal_keperawatan_igd.kbab,penilaian_awal_keperawatan_igd.wbab,penilaian_awal_keperawatan_igd.bak,penilaian_awal_keperawatan_igd.xbak,penilaian_awal_keperawatan_igd.wbak,"
                        + "penilaian_awal_keperawatan_igd.lbak,penilaian_awal_keperawatan_igd.psikologis,penilaian_awal_keperawatan_igd.jiwa,penilaian_awal_keperawatan_igd.perilaku,penilaian_awal_keperawatan_igd.dilaporkan,penilaian_awal_keperawatan_igd.sebutkan,penilaian_awal_keperawatan_igd.hubungan,pasien.stts_nikah,"
                        + "penilaian_awal_keperawatan_igd.tinggal_dengan,penilaian_awal_keperawatan_igd.ket_tinggal,pasien.pekerjaan,penjab.png_jawab,penilaian_awal_keperawatan_igd.budaya,penilaian_awal_keperawatan_igd.ket_budaya,pasien.pnd,penilaian_awal_keperawatan_igd.pendidikan_pj,penilaian_awal_keperawatan_igd.ket_pendidikan_pj,"
                        + "penilaian_awal_keperawatan_igd.edukasi,penilaian_awal_keperawatan_igd.ket_edukasi,penilaian_awal_keperawatan_igd.kemampuan_baca_tulis,penilaian_awal_keperawatan_igd.butuh_penerjemah,penilaian_awal_keperawatan_igd.keterangan_butuh_penerjemah,"
                        + "penilaian_awal_keperawatan_igd.terdapat_hambatan_belajar,penilaian_awal_keperawatan_igd.hambatan_belajar,penilaian_awal_keperawatan_igd.keterangan_hambatan_belajar,penilaian_awal_keperawatan_igd.hambatan_cara_bicara,penilaian_awal_keperawatan_igd.hambatan_bahasa_isyarat,"
                        + "penilaian_awal_keperawatan_igd.cara_belajar_disukai,penilaian_awal_keperawatan_igd.kesediaan_menerima_informasi,penilaian_awal_keperawatan_igd.ket_kesediaan_menerima_informasi,penilaian_awal_keperawatan_igd.pemahaman_nutrisi,penilaian_awal_keperawatan_igd.pemahaman_penyakit,penilaian_awal_keperawatan_igd.pemahaman_pengobatan,penilaian_awal_keperawatan_igd.pemahaman_perawatan,"
                        + "penilaian_awal_keperawatan_igd.keyakinan_nilai,penilaian_awal_keperawatan_igd.keterbatasan_fisik,penilaian_awal_keperawatan_igd.hambatan_emosional,penilaian_awal_keperawatan_igd.motivasi,penilaian_awal_keperawatan_igd.kemampuan,penilaian_awal_keperawatan_igd.aktifitas,penilaian_awal_keperawatan_igd.alat_bantu,penilaian_awal_keperawatan_igd.ket_bantu,"
                        + "penilaian_awal_keperawatan_igd.nyeri,penilaian_awal_keperawatan_igd.provokes,penilaian_awal_keperawatan_igd.ket_provokes,penilaian_awal_keperawatan_igd.quality,penilaian_awal_keperawatan_igd.ket_quality,penilaian_awal_keperawatan_igd.lokasi,penilaian_awal_keperawatan_igd.menyebar,"
                        + "penilaian_awal_keperawatan_igd.skala_nyeri,penilaian_awal_keperawatan_igd.durasi,penilaian_awal_keperawatan_igd.nyeri_hilang,penilaian_awal_keperawatan_igd.ket_nyeri,penilaian_awal_keperawatan_igd.pada_dokter,penilaian_awal_keperawatan_igd.ket_dokter,"
                        + "penilaian_awal_keperawatan_igd.berjalan_a,penilaian_awal_keperawatan_igd.berjalan_b,penilaian_awal_keperawatan_igd.berjalan_c,penilaian_awal_keperawatan_igd.hasil,penilaian_awal_keperawatan_igd.lapor,penilaian_awal_keperawatan_igd.ket_lapor,penilaian_awal_keperawatan_igd.informasi_perencanaan_pulang,"
                        + "penilaian_awal_keperawatan_igd.tanggal_pulang,penilaian_awal_keperawatan_igd.kondisi_saat_pulang,penilaian_awal_keperawatan_igd.perawatan_lanjutan,penilaian_awal_keperawatan_igd.lama_ratarata,penilaian_awal_keperawatan_igd.tanggal_pulang,penilaian_awal_keperawatan_igd.kondisi_saat_pulang,penilaian_awal_keperawatan_igd.perawatan_lanjutan,"
                        + "penilaian_awal_keperawatan_igd.cara_transportasi,penilaian_awal_keperawatan_igd.transportasi_digunakan,"
                        + "penilaian_awal_keperawatan_igd.rencana,penilaian_awal_keperawatan_igd.kondisi_pasien,penilaian_awal_keperawatan_igd.cara_pulang,penilaian_awal_keperawatan_igd.dirujukke,penilaian_awal_keperawatan_igd.indikasi,penilaian_awal_keperawatan_igd.kamar_rawat,penilaian_awal_keperawatan_igd.dpjp,dokter.nm_dokter,penilaian_awal_keperawatan_igd.waktu_keluar,penilaian_awal_keperawatan_igd.waktu_meninggal,penilaian_awal_keperawatan_igd.transportasi_pulang,penilaian_awal_keperawatan_igd.pendidikan_kesehatan,penilaian_awal_keperawatan_igd.nip,petugas.nama "
                        + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                        + "inner join penilaian_awal_keperawatan_igd on reg_periksa.no_rawat=penilaian_awal_keperawatan_igd.no_rawat "
                        + "inner join petugas on penilaian_awal_keperawatan_igd.nip=petugas.nip "
                        + "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "
                        + "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj "
                        + "left join dokter on penilaian_awal_keperawatan_igd.dpjp=dokter.kd_dokter "
                        + "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik where "
                        + "penilaian_awal_keperawatan_igd.tanggal between ? and ? order by penilaian_awal_keperawatan_igd.tanggal");
            } else {
                ps = koneksi.prepareStatement(
                        "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir,pasien.agama,bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,penilaian_awal_keperawatan_igd.tanggal,penilaian_awal_keperawatan_igd.informasi,"
                        + "penilaian_awal_keperawatan_igd.keluhan_utama,penilaian_awal_keperawatan_igd.rpd,penilaian_awal_keperawatan_igd.rpo,penilaian_awal_keperawatan_igd.status_kehamilan,penilaian_awal_keperawatan_igd.gravida,penilaian_awal_keperawatan_igd.para,"
                        + "penilaian_awal_keperawatan_igd.abortus,penilaian_awal_keperawatan_igd.hpht,penilaian_awal_keperawatan_igd.kesadaran,penilaian_awal_keperawatan_igd.gcs,penilaian_awal_keperawatan_igd.td,penilaian_awal_keperawatan_igd.rr,penilaian_awal_keperawatan_igd.suhu,penilaian_awal_keperawatan_igd.nadi,penilaian_awal_keperawatan_igd.tekanan,penilaian_awal_keperawatan_igd.pupil,penilaian_awal_keperawatan_igd.neurosensorik,penilaian_awal_keperawatan_igd.integumen,penilaian_awal_keperawatan_igd.turgor,"
                        + "penilaian_awal_keperawatan_igd.edema,penilaian_awal_keperawatan_igd.mukosa,penilaian_awal_keperawatan_igd.perdarahan,penilaian_awal_keperawatan_igd.jumlah_perdarahan,penilaian_awal_keperawatan_igd.warna_perdarahan,penilaian_awal_keperawatan_igd.intoksikasi,"
                        + "penilaian_awal_keperawatan_igd.bab,penilaian_awal_keperawatan_igd.xbab,penilaian_awal_keperawatan_igd.kbab,penilaian_awal_keperawatan_igd.wbab,penilaian_awal_keperawatan_igd.bak,penilaian_awal_keperawatan_igd.xbak,penilaian_awal_keperawatan_igd.wbak,"
                        + "penilaian_awal_keperawatan_igd.lbak,penilaian_awal_keperawatan_igd.psikologis,penilaian_awal_keperawatan_igd.jiwa,penilaian_awal_keperawatan_igd.perilaku,penilaian_awal_keperawatan_igd.dilaporkan,penilaian_awal_keperawatan_igd.sebutkan,penilaian_awal_keperawatan_igd.hubungan,pasien.stts_nikah,"
                        + "penilaian_awal_keperawatan_igd.tinggal_dengan,penilaian_awal_keperawatan_igd.ket_tinggal,pasien.pekerjaan,penjab.png_jawab,penilaian_awal_keperawatan_igd.budaya,penilaian_awal_keperawatan_igd.ket_budaya,pasien.pnd,penilaian_awal_keperawatan_igd.pendidikan_pj,penilaian_awal_keperawatan_igd.ket_pendidikan_pj,"
                        + "penilaian_awal_keperawatan_igd.edukasi,penilaian_awal_keperawatan_igd.ket_edukasi,penilaian_awal_keperawatan_igd.kemampuan,penilaian_awal_keperawatan_igd.aktifitas,penilaian_awal_keperawatan_igd.alat_bantu,penilaian_awal_keperawatan_igd.ket_bantu,"
                        + "penilaian_awal_keperawatan_igd.nyeri,penilaian_awal_keperawatan_igd.provokes,penilaian_awal_keperawatan_igd.ket_provokes,penilaian_awal_keperawatan_igd.quality,penilaian_awal_keperawatan_igd.ket_quality,penilaian_awal_keperawatan_igd.lokasi,penilaian_awal_keperawatan_igd.menyebar,"
                        + "penilaian_awal_keperawatan_igd.skala_nyeri,penilaian_awal_keperawatan_igd.durasi,penilaian_awal_keperawatan_igd.nyeri_hilang,penilaian_awal_keperawatan_igd.ket_nyeri,penilaian_awal_keperawatan_igd.pada_dokter,penilaian_awal_keperawatan_igd.ket_dokter,"
                        + "penilaian_awal_keperawatan_igd.berjalan_a,penilaian_awal_keperawatan_igd.berjalan_b,penilaian_awal_keperawatan_igd.berjalan_c,penilaian_awal_keperawatan_igd.hasil,penilaian_awal_keperawatan_igd.lapor,penilaian_awal_keperawatan_igd.ket_lapor,"
                        + "penilaian_awal_keperawatan_igd.rencana,penilaian_awal_keperawatan_igd.kondisi_pasien,penilaian_awal_keperawatan_igd.cara_pulang,penilaian_awal_keperawatan_igd.dirujukke,penilaian_awal_keperawatan_igd.indikasi,penilaian_awal_keperawatan_igd.kamar_rawat,penilaian_awal_keperawatan_igd.dpjp,dokter.nm_dokter,penilaian_awal_keperawatan_igd.waktu_keluar,penilaian_awal_keperawatan_igd.waktu_meninggal,penilaian_awal_keperawatan_igd.transportasi_pulang,penilaian_awal_keperawatan_igd.pendidikan_kesehatan,penilaian_awal_keperawatan_igd.nip,petugas.nama "
                        + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                        + "inner join penilaian_awal_keperawatan_igd on reg_periksa.no_rawat=penilaian_awal_keperawatan_igd.no_rawat "
                        + "inner join petugas on penilaian_awal_keperawatan_igd.nip=petugas.nip "
                        + "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "
                        + "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj "
                        + "inner join dokter on penilaian_awal_keperawatan_igd.dpjp=dokter.kd_dokter "
                        + "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik where "
                        + "penilaian_awal_keperawatan_igd.tanggal between ? and ? and "
                        + "(reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or "
                        + "penilaian_awal_keperawatan_igd.nip like ? or petugas.nama like ?) "
                        + "order by penilaian_awal_keperawatan_igd.tanggal");
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
                        rs.getString("tgl_lahir"), rs.getString("tanggal"), rs.getString("informasi"), rs.getString("keluhan_utama"), rs.getString("rpd"), rs.getString("rpo"), rs.getString("status_kehamilan"), rs.getString("gravida"),
                        rs.getString("para"), rs.getString("abortus"), rs.getString("hpht"),rs.getString("kesadaran"),rs.getString("gcs"),rs.getString("td"),rs.getString("rr"),rs.getString("suhu"),rs.getString("nadi") ,rs.getString("tekanan"), rs.getString("pupil"), rs.getString("neurosensorik"), rs.getString("integumen"), rs.getString("turgor"),
                        rs.getString("edema"), rs.getString("mukosa"), rs.getString("perdarahan"), rs.getString("jumlah_perdarahan"), rs.getString("warna_perdarahan"), rs.getString("intoksikasi"), rs.getString("bab"), rs.getString("xbab"), rs.getString("kbab"),
                        rs.getString("wbab"), rs.getString("bak"), rs.getString("xbak"), rs.getString("wbak"), rs.getString("lbak"), rs.getString("psikologis"), rs.getString("jiwa"), rs.getString("perilaku"), rs.getString("dilaporkan"),
                        rs.getString("sebutkan"), rs.getString("hubungan"), rs.getString("stts_nikah"), rs.getString("tinggal_dengan"), rs.getString("ket_tinggal"), rs.getString("pekerjaan"), rs.getString("png_jawab"), rs.getString("budaya"), rs.getString("ket_budaya"), rs.getString("pnd"), rs.getString("pendidikan_pj"), rs.getString("ket_pendidikan_pj"), rs.getString("edukasi"),
                        rs.getString("ket_edukasi"), rs.getString("kemampuan_baca_tulis"),rs.getString("butuh_penerjemah"),rs.getString("keterangan_butuh_penerjemah"),
                        rs.getString("terdapat_hambatan_belajar"),rs.getString("hambatan_belajar"),rs.getString("keterangan_hambatan_belajar"),rs.getString("hambatan_cara_bicara"),rs.getString("hambatan_bahasa_isyarat"),
                        rs.getString("cara_belajar_disukai"),rs.getString("kesediaan_menerima_informasi"),rs.getString("ket_kesediaan_menerima_informasi"),rs.getString("pemahaman_nutrisi"),rs.getString("pemahaman_penyakit"),
                        rs.getString("pemahaman_pengobatan"),rs.getString("pemahaman_perawatan"),rs.getString("keyakinan_nilai"),rs.getString("keterbatasan_fisik"),rs.getString("hambatan_emosional"),rs.getString("motivasi"),rs.getString("kemampuan"), rs.getString("aktifitas"), rs.getString("alat_bantu"), rs.getString("ket_bantu"), rs.getString("nyeri"), rs.getString("provokes"), rs.getString("ket_provokes"), rs.getString("quality"), rs.getString("ket_quality"),
                        rs.getString("lokasi"), rs.getString("menyebar"), rs.getString("skala_nyeri"), rs.getString("durasi"), rs.getString("nyeri_hilang"), rs.getString("ket_nyeri"), rs.getString("pada_dokter"), rs.getString("ket_dokter"),
                        rs.getString("berjalan_a"), rs.getString("berjalan_b"), rs.getString("berjalan_c"), rs.getString("hasil"), rs.getString("lapor"), rs.getString("ket_lapor"), rs.getString("informasi_perencanaan_pulang"),rs.getString("lama_ratarata"),rs.getString("tanggal_pulang"),rs.getString("kondisi_saat_pulang"),
                        rs.getString("perawatan_lanjutan"),rs.getString("cara_transportasi"),rs.getString("transportasi_digunakan"),rs.getString("rencana"),rs.getString("kondisi_pasien"),rs.getString("cara_pulang"),rs.getString("dirujukke"),rs.getString("indikasi"),rs.getString("kamar_rawat"),rs.getString("dpjp"),rs.getString("nm_dokter"),rs.getString("waktu_keluar"),rs.getString("waktu_meninggal"),rs.getString("transportasi_pulang"),rs.getString("pendidikan_kesehatan") ,rs.getString("nip"), rs.getString("nama")
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
        KeluhanUtama.setText("");
        RPD.setText("");
        RPO.setText("");
        StatusKehamilan.setSelectedIndex(0);
        Gravida.setText("");
        Para.setText("");
        Abortus.setText("");
        HPHT.setText("");
        Kesadaran.setSelectedIndex(0);
        GCS.setText("");
        TD.setText("");
        RR.setText("");
        Suhu.setText("");
        Nadi.setText("");
        
        Tekanan.setSelectedIndex(0);
        Pupil.setSelectedIndex(0);
        Neurosensorik.setSelectedIndex(0);
        Integumen.setSelectedIndex(0);
        Turgor.setSelectedIndex(0);
        Edema.setSelectedIndex(0);
        Mukosa.setSelectedIndex(0);
        Perdarahan.setSelectedIndex(0);
        JumlahPerdarahan.setText("");
        WarnaPerdarahan.setText("");
        Intoksikasi.setSelectedIndex(0);
        BAB.setText("");
        XBAB.setText("");
        KBAB.setText("");
        WBAB.setText("");
        BAK.setText("");
        XBAK.setText("");
        WBAK.setText("");
        LBAK.setText("");
        Psikologis.setSelectedIndex(0);
        Jiwa.setSelectedIndex(0);
        Perilaku.setSelectedIndex(0);
        Dilaporkan.setText("");
        Sebutkan.setText("");
        Hubungan.setSelectedIndex(0);
        TinggalDengan.setSelectedIndex(0);
        KetTinggal.setText("");
        StatusBudaya.setSelectedIndex(0);
        KetBudaya.setText("");
        PendidikanPJ.setSelectedIndex(0);
        KetPendidikanPJ.setText("");
        Edukasi.setSelectedIndex(0);
        KetEdukasi.setText("");
        ADL.setSelectedIndex(0);
        Aktifitas.setSelectedIndex(0);
        AlatBantu.setSelectedIndex(0);
        KetAlatBantu.setText("");
        Nyeri.setSelectedIndex(0);
        Provokes.setSelectedIndex(0);
        KetProvokes.setText("");
        Quality.setSelectedIndex(0);
        KetQuality.setText("");
        Lokasi.setText("");
        Menyebar.setSelectedIndex(0);
        SkalaNyeri.setSelectedIndex(0);
        Durasi.setText("");
        NyeriHilang.setSelectedIndex(0);
        KetNyeri.setText("");
        PadaDokter.setSelectedIndex(0);
        KetDokter.setText("");
        ATS.setSelectedIndex(0);
        BJM.setSelectedIndex(0);
        MSA.setSelectedIndex(0);
        Hasil.setSelectedIndex(0);
        Lapor.setSelectedIndex(0);
        KetLapor.setText("");
        Rencana.setText("");
        for (i = 0; i < tabModeMasalah.getRowCount(); i++) {
            tabModeMasalah.setValueAt(false, i, 0);
        }
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
            KeluhanUtama.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 10).toString());
            RPD.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 11).toString());
            RPO.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 12).toString());
            StatusKehamilan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 13).toString());
            Gravida.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 14).toString());
            Para.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 15).toString());
            Abortus.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 16).toString());
            HPHT.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 17).toString());
            Kesadaran.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 18).toString());
            GCS.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 19).toString());
            TD.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 20).toString());
            RR.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 21).toString());
            Suhu.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 22).toString());
            Nadi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 23).toString());
            Tekanan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 24).toString());
            Pupil.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 25).toString());
            Neurosensorik.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 26).toString());
            Integumen.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 27).toString());
            Turgor.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 28).toString());
            Edema.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 29).toString());
            Mukosa.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 30).toString());
            Perdarahan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 31).toString());
            JumlahPerdarahan.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 32).toString());
            WarnaPerdarahan.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 33).toString());
            Intoksikasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 34).toString());
            BAB.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 35).toString());
            XBAB.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 36).toString());
            KBAB.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 37).toString());
            WBAB.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 38).toString());
            BAK.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 39).toString());
            XBAK.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 40).toString());
            WBAK.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 41).toString());
            LBAK.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 42).toString());
            Psikologis.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 43).toString());
            Jiwa.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 44).toString());
            Perilaku.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 45).toString());
            Dilaporkan.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 46).toString());
            Sebutkan.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 47).toString());
            Hubungan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 48).toString());
            StatusPernikahan.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 49).toString());
            TinggalDengan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 50).toString());
            KetTinggal.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 51).toString());
            Pekerjaan.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 52).toString());
            Pembayaran.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 53).toString());
            StatusBudaya.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 54).toString());
            KetBudaya.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 55).toString());
            PendidikanPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 56).toString());
            PendidikanPJ.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 57).toString());
            KetPendidikanPJ.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 58).toString());
            Edukasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 59).toString());
            KetEdukasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 60).toString());
            KemampuanBacaTulis.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 61).toString());
            ButuhPenerjemah.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 62).toString());
            KeteranganButuhPenerjemah.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 63).toString());
            TerdapatHambatanBelajar.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 64).toString());
            HambatanBelajar.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 65).toString());
            KeteranganHambatanBelajar.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 66).toString());
            HambatanCaraBicara.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 67).toString());
            HambatanBahasaIsyarat.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 68).toString());
            CaraBelajarDisukai.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 69).toString());
            KesediaanMenerimaInformasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 70).toString());
            KeteranganKesediaanMenerimaInformasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 71).toString());
            PemahamanNutrisi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 72).toString());
            PemahamanPenyakit.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 73).toString());
            PemahamanPengobatan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 74).toString());
            PemahamanPerawatan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 75).toString());
            KeyakinanNilai.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 76).toString());
            KeterbatasanFisik.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 77).toString());
            HambatanEmosional.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 78).toString());
            Motivasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 79).toString());
            ADL.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 80).toString());
            Aktifitas.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 81).toString());
            AlatBantu.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 82).toString());
            KetAlatBantu.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 83).toString());
            Nyeri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 84).toString());
            Provokes.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 85).toString());
            KetProvokes.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 86).toString());
            Quality.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 87).toString());
            KetQuality.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 88).toString());
            Lokasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 89).toString());
            Menyebar.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 90).toString());
            SkalaNyeri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 91).toString());
            Durasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 92).toString());
            NyeriHilang.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 93).toString());
            KetNyeri.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 94).toString());
            PadaDokter.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 95).toString());
            KetDokter.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 96).toString());
            ATS.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 97).toString());
            BJM.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 98).toString());
            MSA.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 99).toString());
            Hasil.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 100).toString());
            Lapor.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 101).toString());
            KetLapor.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 102).toString());
            InformasiPerencanaanPulang.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 103).toString());
            LamaRatarata.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 104).toString());
            Valid.SetTgl2(TanggalPulang, tbObat.getValueAt(tbObat.getSelectedRow(), 105).toString());
            KondisiPulang.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 106).toString());
            PerawatanLanjutan.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 107).toString());
            CaraTransportasiPulang.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 108).toString());
            TransportasiYangDigunakan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 109).toString());
            Rencana.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 110).toString());
//            KondisiPasien.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 111).toString());
//            StatusPulang.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 112).toString());
//            Dirujukke.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 113).toString());
//            Indikasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 114).toString());
//            RuangRanap.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 115).toString());
//            KdDPJP.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 116).toString());
//            NmDPJP.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 117).toString());
//            Valid.SetTgl2(WaktuKeluar, tbObat.getValueAt(tbObat.getSelectedRow(), 118).toString());
//            WaktuMeninggal.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 119).toString());
//            if(tbObat.getValueAt(tbObat.getSelectedRow(), 120).toString().equals("Kendaraan Pribadi")){
//                KendaraanPribadi.setSelected(true);
//            }else if (tbObat.getValueAt(tbObat.getSelectedRow(), 121).toString().equals("Kendaraan Pribadi")){
//                Ambulance.setSelected(true);
//            }else if (tbObat.getValueAt(tbObat.getSelectedRow(), 121).toString().equals("Mobil Jenazah")){
//                MobilJenazah.setSelected(true);
//            }
//            PendidikanKesehatan.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 122).toString());
            Valid.tabelKosong(tabModeMasalah);
            Valid.tabelKosong(tabModeRencana);
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
                    + "pasien.tgl_lahir,pasien.agama,bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,reg_periksa.tgl_registrasi, "
                    + "pasien.stts_nikah,pasien.pekerjaan,pasien.pnd,penjab.png_jawab,reg_periksa.jam_reg "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                    + "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "
                    + "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik "
                    + "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj "
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
                    StatusPernikahan.setText(rs.getString("stts_nikah"));
                    Pekerjaan.setText(rs.getString("pekerjaan"));
                    PendidikanPasien.setText(rs.getString("pnd"));
                    Pembayaran.setText(rs.getString("png_jawab"));
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
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
    }
    private void isTotal() {
        try {
            TotalHasilAnak.setText(""+(Integer.parseInt(Nilai1Anak.getText())+Integer.parseInt(Nilai2Anak.getText())+Integer.parseInt(Nilai3Anak.getText())+Integer.parseInt(Nilai4Anak.getText())));
            if((Integer.parseInt(TotalHasilAnak.getText())>=4)&&(Integer.parseInt(TotalHasilAnak.getText())<=5)){
                LabelSkrining.setText("Risikio Berat");
            }else if((Integer.parseInt(TotalHasilAnak.getText())>=1)&&(Integer.parseInt(TotalHasilAnak.getText())<=3)){
                LabelSkrining.setText("Risiko Sedang");
            }else if(Integer.parseInt(TotalHasilAnak.getText())==0){
                LabelSkrining.setText("Risiko Rendah");
            }
        } catch (Exception e) {
        }
    }

    public void setNoRm(String norwt, Date tgl2) {
        TNoRw.setText(norwt);
        TCari.setText(norwt);
        DTPCari2.setDate(tgl2);
        isRawat();
    }

    public void isCek() {
        BtnSimpan.setEnabled(akses.getpenilaian_awal_keperawatan_igd());
        BtnHapus.setEnabled(akses.getpenilaian_awal_keperawatan_igd());
        BtnEdit.setEnabled(akses.getpenilaian_awal_keperawatan_igd());
        BtnEdit.setEnabled(akses.getpenilaian_awal_keperawatan_igd());
        BtnTambahMasalah.setEnabled(akses.getmaster_masalah_keperawatan_igd());
        BtnTambahRencana.setEnabled(akses.getmaster_rencana_keperawatan_igd());
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
            file = new File("./cache/masalahkeperawatanigd.iyem");
            file.createNewFile();
            fileWriter = new FileWriter(file);
            StringBuilder iyembuilder = new StringBuilder();
            ps = koneksi.prepareStatement("select * from master_masalah_keperawatan_igd order by master_masalah_keperawatan_igd.kode_masalah");
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
                fileWriter.write("{\"masalahkeperawatanigd\":[" + iyembuilder + "]}");
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

            myObj = new FileReader("./cache/masalahkeperawatanigd.iyem");
            root = mapper.readTree(myObj);
            response = root.path("masalahkeperawatanigd");
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
            file = new File("./cache/rencanakeperawatanigd.iyem");
            file.createNewFile();
            fileWriter = new FileWriter(file);
            StringBuilder iyembuilder = new StringBuilder();
            ps = koneksi.prepareStatement("select * from master_rencana_keperawatan_igd order by master_rencana_keperawatan_igd.kode_rencana");
            try {
                rs = ps.executeQuery();
                while (rs.next()) {
                    iyembuilder.append("{\"KodeMasalah\":\"").append(rs.getString(1)).append("\",\"KodeRencana\":\"").append(rs.getString(2)).append("\",\"NamaRencana\":\"").append(rs.getString(3)).append("\"},");
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
                fileWriter.write("{\"rencanakeperawatanigd\":[" + iyembuilder + "]}");
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

            myObj = new FileReader("./cache/rencanakeperawatanigd.iyem");
            root = mapper.readTree(myObj);
            response = root.path("rencanakeperawatanigd");
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
            DetailRencana.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 104).toString());
            try {
                Valid.tabelKosong(tabModeDetailMasalah);
                ps = koneksi.prepareStatement(
                        "select master_masalah_keperawatan_igd.kode_masalah,master_masalah_keperawatan_igd.nama_masalah from master_masalah_keperawatan_igd "
                        + "inner join penilaian_awal_keperawatan_igd_masalah on penilaian_awal_keperawatan_igd_masalah.kode_masalah=master_masalah_keperawatan_igd.kode_masalah "
                        + "where penilaian_awal_keperawatan_igd_masalah.no_rawat=? order by penilaian_awal_keperawatan_igd_masalah.kode_masalah");
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
                        "select master_rencana_keperawatan_igd.kode_rencana,master_rencana_keperawatan_igd.rencana_keperawatan from master_rencana_keperawatan_igd "
                        + "inner join penilaian_awal_keperawatan_ralan_rencana_igd on penilaian_awal_keperawatan_ralan_rencana_igd.kode_rencana=master_rencana_keperawatan_igd.kode_rencana "
                        + "where penilaian_awal_keperawatan_ralan_rencana_igd.no_rawat=? order by penilaian_awal_keperawatan_ralan_rencana_igd.kode_rencana");
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
                        + "inner join penilaian_awal_keperawatan_igd_kebutuhan_edukasi on penilaian_awal_keperawatan_igd_kebutuhan_edukasi.kd_kebutuhan_edukasi=master_kebutuhan_edukasi_komunikasi.kd_kebutuhan_edukasi "
                        + "where penilaian_awal_keperawatan_igd_kebutuhan_edukasi.no_rawat=? order by penilaian_awal_keperawatan_igd_kebutuhan_edukasi.kd_kebutuhan_edukasi");
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
                        + "inner join penilaian_awal_keperawatan_igd_rencana_edukasi on penilaian_awal_keperawatan_igd_rencana_edukasi.kd_rencana_edukasi=master_rencana_edukasi_komunikasi.kd_rencana_edukasi "
                        + "where penilaian_awal_keperawatan_igd_rencana_edukasi.no_rawat=? order by penilaian_awal_keperawatan_igd_rencana_edukasi.kd_rencana_edukasi");
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

    private void ganti() {
//        if(KendaraanPribadi.isSelected()){
//            transportasi = "Kendaraan Pribadi";
//        }else if(Ambulance.isSelected()){
//            transportasi = "Ambulance";
//        }else if(MobilJenazah.isSelected()){
//            transportasi = "Mobil Jenazah";
//        }
        if (Sequel.mengedittf("penilaian_awal_keperawatan_igd", "no_rawat=?", "no_rawat=?,tanggal=?,keluhan_utama=?,rpd=?,rpo=?,status_kehamilan=?,gravida=?,"
                + "para=?,abortus=?,hpht=?,kesadaran=?,gcs=?,td=?,rr=?,suhu=?,nadi=?,tekanan=?,pupil=?,neurosensorik=?,integumen=?,turgor=?,edema=?,mukosa=?,perdarahan=?,jumlah_perdarahan=?,warna_perdarahan=?,"
                + "intoksikasi=?,bab=?,xbab=?,kbab=?,wbab=?,bak=?,xbak=?,wbak=?,lbak=?,psikologis=?,jiwa=?,perilaku=?,dilaporkan=?,sebutkan=?,hubungan=?,tinggal_dengan=?,"
                + "ket_tinggal=?,budaya=?,ket_budaya=?,pendidikan_pj=?,ket_pendidikan_pj=?,edukasi=?,ket_edukasi=?,kemampuan_baca_tulis=?,butuh_penerjemah=?,keterangan_butuh_penerjemah=?,"
                + "terdapat_hambatan_belajar=?,hambatan_belajar=?,keterangan_hambatan_belajar=?,hambatan_cara_bicara=?,hambatan_bahasa_isyarat=?,cara_belajar_disukai=?,kesediaan_menerima_informasi=?,ket_kesediaan_menerima_informasi=?,pemahaman_nutrisi=?,pemahaman_penyakit=?,pemahaman_pengobatan=?,pemahaman_perawatan=?,keyakinan_nilai=?,keterbatasan_fisik=?,hambatan_emosional=?,motivasi=?,kemampuan=?,aktifitas=?,alat_bantu=?,ket_bantu=?,nyeri=?,"
                + "provokes=?,ket_provokes=?,quality=?,ket_quality=?,lokasi=?,menyebar=?,skala_nyeri=?,durasi=?,nyeri_hilang=?,ket_nyeri=?,pada_dokter=?,ket_dokter=?,"
                + "berjalan_a=?,berjalan_b=?,berjalan_c=?,hasil=?,lapor=?,ket_lapor=?,informasi_perencanaan_pulang=?,lama_ratarata=?,tanggal_pulang=?,kondisi_saat_pulang=?,perawatan_lanjutan=?,cara_transportasi=?,transportasi_digunakan=?,rencana=?,kondisi_pasien=?,cara_pulang=?,dirujukke=?,indikasi=?,kamar_rawat=?,dpjp=?,waktu_keluar=?,waktu_meninggal=?,transportasi_pulang=?,pendidikan_kesehatan=?,nip=?,informasi=?", 112, new String[]{
                    TNoRw.getText(), Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19), KeluhanUtama.getText(), RPD.getText(), RPO.getText(), StatusKehamilan.getSelectedItem().toString(),
                    Gravida.getText(), Para.getText(), Abortus.getText(), HPHT.getText(),Kesadaran.getSelectedItem().toString(),GCS.getText(),TD.getText(),RR.getText(),Suhu.getText(),Nadi.getText() ,Tekanan.getSelectedItem().toString(), Pupil.getSelectedItem().toString(), Neurosensorik.getSelectedItem().toString(), Integumen.getSelectedItem().toString(),
                    Turgor.getSelectedItem().toString(), Edema.getSelectedItem().toString(), Mukosa.getSelectedItem().toString(), Perdarahan.getSelectedItem().toString(), JumlahPerdarahan.getText(),
                    WarnaPerdarahan.getText(), Intoksikasi.getSelectedItem().toString(), BAB.getText(), XBAB.getText(), KBAB.getText(), WBAB.getText(), BAK.getText(), XBAK.getText(), WBAK.getText(), LBAK.getText(), Psikologis.getSelectedItem().toString(),
                    Jiwa.getSelectedItem().toString(), Perilaku.getSelectedItem().toString(), Dilaporkan.getText(), Sebutkan.getText(), Hubungan.getSelectedItem().toString(), TinggalDengan.getSelectedItem().toString(), KetTinggal.getText(),
                    StatusBudaya.getSelectedItem().toString(), KetBudaya.getText(), PendidikanPJ.getSelectedItem().toString(), KetPendidikanPJ.getText(), Edukasi.getSelectedItem().toString(), KetEdukasi.getText(), KemampuanBacaTulis.getSelectedItem().toString(), ButuhPenerjemah.getSelectedItem().toString(), KeteranganButuhPenerjemah.getText(), TerdapatHambatanBelajar.getSelectedItem().toString(), HambatanBelajar.getSelectedItem().toString(),
                    KeteranganHambatanBelajar.getText(), HambatanCaraBicara.getSelectedItem().toString(), HambatanBahasaIsyarat.getSelectedItem().toString(), CaraBelajarDisukai.getSelectedItem().toString(), KesediaanMenerimaInformasi.getSelectedItem().toString(),
                    KeteranganKesediaanMenerimaInformasi.getText(), PemahamanNutrisi.getSelectedItem().toString(), PemahamanPenyakit.getSelectedItem().toString(), PemahamanPengobatan.getSelectedItem().toString(), PemahamanPerawatan.getSelectedItem().toString(),
                    KeyakinanNilai.getSelectedItem().toString(), KeterbatasanFisik.getSelectedItem().toString(), HambatanEmosional.getSelectedItem().toString(), Motivasi.getSelectedItem().toString(), ADL.getSelectedItem().toString(),
                    Aktifitas.getSelectedItem().toString(), AlatBantu.getSelectedItem().toString(), KetAlatBantu.getText(), Nyeri.getSelectedItem().toString(), Provokes.getSelectedItem().toString(), KetProvokes.getText(), Quality.getSelectedItem().toString(),
                    KetQuality.getText(), Lokasi.getText(), Menyebar.getSelectedItem().toString(), SkalaNyeri.getSelectedItem().toString(), Durasi.getText(), NyeriHilang.getSelectedItem().toString(), KetNyeri.getText(), PadaDokter.getSelectedItem().toString(),
                    KetDokter.getText(), ATS.getSelectedItem().toString(), BJM.getSelectedItem().toString(), MSA.getSelectedItem().toString(), Hasil.getSelectedItem().toString(), Lapor.getSelectedItem().toString(), KetLapor.getText(),InformasiPerencanaanPulang.getSelectedItem().toString(), LamaRatarata.getText(), Valid.SetTgl(TanggalPulang.getSelectedItem() + ""), KondisiPulang.getText(), PerawatanLanjutan.getText(),
                    CaraTransportasiPulang.getSelectedItem().toString(), TransportasiYangDigunakan.getSelectedItem().toString(),  Rencana.getText(),
//                    KondisiPasien.getSelectedItem().toString(),StatusPulang.getSelectedItem().toString(),Dirujukke.getText(),Indikasi.getText(),RuangRanap.getText(),KdDPJP.getText(),Valid.SetTgl(WaktuKeluar.getSelectedItem()+"")+" "+WaktuKeluar.getSelectedItem().toString().substring(11,19),WaktuMeninggal.getText(),transportasi,PendidikanKesehatan.getText(),KdPetugas.getText(), Informasi.getSelectedItem().toString(), tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString()
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
            tbObat.setValueAt(KeluhanUtama.getText(), tbObat.getSelectedRow(), 10);
            tbObat.setValueAt(RPD.getText(), tbObat.getSelectedRow(), 11);
            tbObat.setValueAt(RPO.getText(), tbObat.getSelectedRow(), 12);
            tbObat.setValueAt(StatusKehamilan.getSelectedItem().toString(), tbObat.getSelectedRow(), 13);
            tbObat.setValueAt(Gravida.getText(), tbObat.getSelectedRow(), 14);
            tbObat.setValueAt(Para.getText(), tbObat.getSelectedRow(), 15);
            tbObat.setValueAt(Abortus.getText(), tbObat.getSelectedRow(), 16);
            tbObat.setValueAt(HPHT.getText(), tbObat.getSelectedRow(), 17);
            tbObat.setValueAt(Kesadaran.getSelectedItem().toString(), tbObat.getSelectedRow(), 18);
            tbObat.setValueAt(GCS.getText(), tbObat.getSelectedRow(), 19);
            tbObat.setValueAt(TD.getText(), tbObat.getSelectedRow(), 20);
            tbObat.setValueAt(RR.getText(), tbObat.getSelectedRow(), 21);
            tbObat.setValueAt(Suhu.getText(), tbObat.getSelectedRow(), 22);
            tbObat.setValueAt(Nadi.getText(), tbObat.getSelectedRow(), 23);
            tbObat.setValueAt(Pupil.getSelectedItem().toString(), tbObat.getSelectedRow(), 24);
            tbObat.setValueAt(Neurosensorik.getSelectedItem().toString(), tbObat.getSelectedRow(), 25);
            tbObat.setValueAt(Integumen.getSelectedItem().toString(), tbObat.getSelectedRow(), 26);
            tbObat.setValueAt(Turgor.getSelectedItem().toString(), tbObat.getSelectedRow(), 27);
            tbObat.setValueAt(Edema.getSelectedItem().toString(), tbObat.getSelectedRow(), 28);
            tbObat.setValueAt(Mukosa.getSelectedItem().toString(), tbObat.getSelectedRow(), 29);
            tbObat.setValueAt(Perdarahan.getSelectedItem().toString(), tbObat.getSelectedRow(), 30);
            tbObat.setValueAt(JumlahPerdarahan.getText(), tbObat.getSelectedRow(), 31);
            tbObat.setValueAt(WarnaPerdarahan.getText(), tbObat.getSelectedRow(), 32);
            tbObat.setValueAt(Intoksikasi.getSelectedItem().toString(), tbObat.getSelectedRow(), 33);
            tbObat.setValueAt(BAB.getText(), tbObat.getSelectedRow(), 34);
            tbObat.setValueAt(XBAB.getText(), tbObat.getSelectedRow(), 35);
            tbObat.setValueAt(KBAB.getText(), tbObat.getSelectedRow(), 36);
            tbObat.setValueAt(WBAB.getText(), tbObat.getSelectedRow(), 37);
            tbObat.setValueAt(BAK.getText(), tbObat.getSelectedRow(), 38);
            tbObat.setValueAt(XBAK.getText(), tbObat.getSelectedRow(), 39);
            tbObat.setValueAt(WBAK.getText(), tbObat.getSelectedRow(), 40);
            tbObat.setValueAt(LBAK.getText(), tbObat.getSelectedRow(), 41);
            tbObat.setValueAt(Psikologis.getSelectedItem().toString(), tbObat.getSelectedRow(), 42);
            tbObat.setValueAt(Jiwa.getSelectedItem().toString(), tbObat.getSelectedRow(), 43);
            tbObat.setValueAt(Perilaku.getSelectedItem().toString(), tbObat.getSelectedRow(), 44);
            tbObat.setValueAt(Dilaporkan.getText(), tbObat.getSelectedRow(), 45);
            tbObat.setValueAt(Sebutkan.getText(), tbObat.getSelectedRow(), 46);
            tbObat.setValueAt(Hubungan.getSelectedItem().toString(), tbObat.getSelectedRow(), 47);
            tbObat.setValueAt(StatusPernikahan.getText(), tbObat.getSelectedRow(), 48);
            tbObat.setValueAt(TinggalDengan.getSelectedItem().toString(), tbObat.getSelectedRow(), 49);
            tbObat.setValueAt(KetTinggal.getText(), tbObat.getSelectedRow(), 50);
            tbObat.setValueAt(Pekerjaan.getText(), tbObat.getSelectedRow(), 51);
            tbObat.setValueAt(Pembayaran.getText(), tbObat.getSelectedRow(), 52);
            tbObat.setValueAt(StatusBudaya.getSelectedItem().toString(), tbObat.getSelectedRow(), 53);
            tbObat.setValueAt(KetBudaya.getText(), tbObat.getSelectedRow(), 54);
            tbObat.setValueAt(PendidikanPasien.getText(), tbObat.getSelectedRow(), 55);
            tbObat.setValueAt(PendidikanPJ.getSelectedItem().toString(), tbObat.getSelectedRow(), 56);
            tbObat.setValueAt(KetPendidikanPJ.getText(), tbObat.getSelectedRow(), 57);
            tbObat.setValueAt(Edukasi.getSelectedItem().toString(), tbObat.getSelectedRow(), 58);
            tbObat.setValueAt(KetEdukasi.getText(), tbObat.getSelectedRow(), 59);
            tbObat.setValueAt(KemampuanBacaTulis.getSelectedItem().toString(), tbObat.getSelectedRow(),60);
            tbObat.setValueAt(ButuhPenerjemah.getSelectedItem().toString(), tbObat.getSelectedRow(), 61);
            tbObat.setValueAt(KeteranganButuhPenerjemah.getText(), tbObat.getSelectedRow(), 62);
            tbObat.setValueAt(TerdapatHambatanBelajar.getSelectedItem().toString(), tbObat.getSelectedRow(), 63);
            tbObat.setValueAt(HambatanBelajar.getSelectedItem().toString(), tbObat.getSelectedRow(), 64);
            tbObat.setValueAt(KeteranganHambatanBelajar.getText(), tbObat.getSelectedRow(), 65);
            tbObat.setValueAt(HambatanCaraBicara.getSelectedItem().toString(), tbObat.getSelectedRow(), 66);
            tbObat.setValueAt(HambatanBahasaIsyarat.getSelectedItem().toString(), tbObat.getSelectedRow(), 67);
            tbObat.setValueAt(CaraBelajarDisukai.getSelectedItem().toString(), tbObat.getSelectedRow(), 68);
            tbObat.setValueAt(KesediaanMenerimaInformasi.getSelectedItem().toString(), tbObat.getSelectedRow(), 69);
            tbObat.setValueAt(KeteranganKesediaanMenerimaInformasi.getText(), tbObat.getSelectedRow(), 70);
            tbObat.setValueAt(PemahamanNutrisi.getSelectedItem().toString(), tbObat.getSelectedRow(), 71);
            tbObat.setValueAt(PemahamanPenyakit.getSelectedItem().toString(), tbObat.getSelectedRow(), 72);
            tbObat.setValueAt(PemahamanPengobatan.getSelectedItem().toString(), tbObat.getSelectedRow(), 73);
            tbObat.setValueAt(PemahamanPerawatan.getSelectedItem().toString(), tbObat.getSelectedRow(), 74);
            tbObat.setValueAt(KeyakinanNilai.getSelectedItem().toString(), tbObat.getSelectedRow(), 75);
            tbObat.setValueAt(KeterbatasanFisik.getSelectedItem().toString(), tbObat.getSelectedRow(), 76);
            tbObat.setValueAt(HambatanEmosional.getSelectedItem().toString(), tbObat.getSelectedRow(), 77);
            tbObat.setValueAt(Motivasi.getSelectedItem().toString(), tbObat.getSelectedRow(), 78);
            tbObat.setValueAt(ADL.getSelectedItem().toString(), tbObat.getSelectedRow(), 79);
            tbObat.setValueAt(Aktifitas.getSelectedItem().toString(), tbObat.getSelectedRow(), 80);
            tbObat.setValueAt(AlatBantu.getSelectedItem().toString(), tbObat.getSelectedRow(), 81);
            tbObat.setValueAt(KetAlatBantu.getText(), tbObat.getSelectedRow(), 82);
            tbObat.setValueAt(Nyeri.getSelectedItem().toString(), tbObat.getSelectedRow(), 83);
            tbObat.setValueAt(Provokes.getSelectedItem().toString(), tbObat.getSelectedRow(), 84);
            tbObat.setValueAt(KetProvokes.getText(), tbObat.getSelectedRow(),85);
            tbObat.setValueAt(Quality.getSelectedItem().toString(), tbObat.getSelectedRow(), 86);
            tbObat.setValueAt(KetQuality.getText(), tbObat.getSelectedRow(), 87);
            tbObat.setValueAt(Lokasi.getText(), tbObat.getSelectedRow(), 88);
            tbObat.setValueAt(Menyebar.getSelectedItem().toString(), tbObat.getSelectedRow(), 89);
            tbObat.setValueAt(SkalaNyeri.getSelectedItem().toString(), tbObat.getSelectedRow(),90);
            tbObat.setValueAt(Durasi.getText(), tbObat.getSelectedRow(), 91);
            tbObat.setValueAt(NyeriHilang.getSelectedItem().toString(), tbObat.getSelectedRow(), 92);
            tbObat.setValueAt(KetNyeri.getText(), tbObat.getSelectedRow(), 93);
            tbObat.setValueAt(PadaDokter.getSelectedItem().toString(), tbObat.getSelectedRow(), 94);
            tbObat.setValueAt(KetDokter.getText(), tbObat.getSelectedRow(), 95);
            tbObat.setValueAt(ATS.getSelectedItem().toString(), tbObat.getSelectedRow(), 96);
            tbObat.setValueAt(BJM.getSelectedItem().toString(), tbObat.getSelectedRow(), 97);
            tbObat.setValueAt(MSA.getSelectedItem().toString(), tbObat.getSelectedRow(), 98);
            tbObat.setValueAt(Hasil.getSelectedItem().toString(), tbObat.getSelectedRow(),99);
            tbObat.setValueAt(Lapor.getSelectedItem().toString(), tbObat.getSelectedRow(), 100);
            tbObat.setValueAt(KetLapor.getText(), tbObat.getSelectedRow(), 101);
            tbObat.setValueAt(InformasiPerencanaanPulang.getSelectedItem().toString(), tbObat.getSelectedRow(), 102);
            tbObat.setValueAt(LamaRatarata.getText(), tbObat.getSelectedRow(), 103);
            tbObat.setValueAt(Valid.SetTgl(TanggalPulang.getSelectedItem() + ""), tbObat.getSelectedRow(), 104);
            tbObat.setValueAt(KondisiPulang.getText(), tbObat.getSelectedRow(), 105);
            tbObat.setValueAt(PerawatanLanjutan.getText(), tbObat.getSelectedRow(), 106);
            tbObat.setValueAt(CaraTransportasiPulang.getSelectedItem().toString(), tbObat.getSelectedRow(), 107);
            tbObat.setValueAt(TransportasiYangDigunakan.getSelectedItem().toString(), tbObat.getSelectedRow(), 108);
            tbObat.setValueAt(Rencana.getText(), tbObat.getSelectedRow(), 109);
//            tbObat.setValueAt(KondisiPasien.getSelectedItem().toString() , tbObat.getSelectedRow(), 110);
//            tbObat.setValueAt(StatusPulang.getSelectedItem().toString() , tbObat.getSelectedRow(), 111);
//            tbObat.setValueAt(Dirujukke.getText(), tbObat.getSelectedRow(), 112);
//            tbObat.setValueAt(Indikasi.getText(), tbObat.getSelectedRow(), 113);
//            tbObat.setValueAt(RuangRanap.getText(), tbObat.getSelectedRow(), 114);
//            tbObat.setValueAt(KdDPJP.getText(), tbObat.getSelectedRow(), 115);
//            tbObat.setValueAt(NmDPJP.getText(), tbObat.getSelectedRow(), 116);
////            tbObat.setValueAt(Valid.SetTgl(WaktuKeluar.getSelectedItem() + "") + " " + WaktuKeluar.getSelectedItem().toString().substring(11, 19), tbObat.getSelectedRow(), 117);
//            tbObat.setValueAt(WaktuMeninggal.getText(), tbObat.getSelectedRow(), 118);
//            tbObat.setValueAt(transportasi, tbObat.getSelectedRow(), 119);
//            tbObat.setValueAt(PendidikanKesehatan.getText(), tbObat.getSelectedRow(), 120);
            tbObat.setValueAt(KdPetugas.getText(), tbObat.getSelectedRow(), 121);
            tbObat.setValueAt(NmPetugas.getText(), tbObat.getSelectedRow(), 122);
            Sequel.meghapus("penilaian_awal_keperawatan_igd_masalah", "no_rawat", tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
            Sequel.meghapus("penilaian_awal_keperawatan_ralan_rencana_igd", "no_rawat", tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
            Valid.tabelKosong(tabModeDetailMasalah);
            Valid.tabelKosong(tabModeDetailRencana);
            Valid.tabelKosong(tabModeDetailKebutuhanEdukasi);
            Valid.tabelKosong(tabModeDetailRencanaEdukasi);           
            for (i = 0; i < tbMasalahKeperawatan.getRowCount(); i++) {
                if (tbMasalahKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                    if (Sequel.menyimpantf2("penilaian_awal_keperawatan_igd_masalah", "?,?", 2, new String[]{TNoRw.getText(), tbMasalahKeperawatan.getValueAt(i, 1).toString()}) == true) {
                        tabModeDetailMasalah.addRow(new Object[]{
                            tbMasalahKeperawatan.getValueAt(i, 1).toString(), tbMasalahKeperawatan.getValueAt(i, 2).toString()
                        });
                    }
                }
            }
            for (i = 0; i < tbRencanaKeperawatan.getRowCount(); i++) {
                if (tbRencanaKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                    if (Sequel.menyimpantf2("penilaian_awal_keperawatan_ralan_rencana_igd", "?,?", 2, new String[]{TNoRw.getText(), tbRencanaKeperawatan.getValueAt(i, 1).toString()}) == true) {
                        tabModeDetailRencana.addRow(new Object[]{
                            tbRencanaKeperawatan.getValueAt(i, 1).toString(), tbRencanaKeperawatan.getValueAt(i, 2).toString()
                        });
                    }
                }
            }
            for (i = 0; i < tbKebutuhanEdukasi.getRowCount(); i++) {
                    if(tbKebutuhanEdukasi.getValueAt(i,0).toString().equals("true")){
                        if(Sequel.menyimpantf2("penilaian_awal_keperawatan_igd_kebutuhan_edukasi","?,?",2,new String[]{TNoRw.getText(),tbKebutuhanEdukasi.getValueAt(i,1).toString()})==true){
                            tabModeDetailKebutuhanEdukasi.addRow(new Object[]{
                                tbKebutuhanEdukasi.getValueAt(i,1).toString(),tbKebutuhanEdukasi.getValueAt(i,2).toString()
                            });
                        }
                    }
                }
                for (i = 0; i < tbRencanaEdukasi.getRowCount(); i++) {
                    if(tbRencanaEdukasi.getValueAt(i,0).toString().equals("true")){
                        if(Sequel.menyimpantf2("penilaian_awal_keperawatan_igd_rencana_edukasi","?,?",2,new String[]{TNoRw.getText(),tbRencanaEdukasi.getValueAt(i,1).toString()})==true){
                            tabModeDetailRencanaEdukasi.addRow(new Object[]{
                                tbRencanaEdukasi.getValueAt(i,1).toString(),tbRencanaEdukasi.getValueAt(i,2).toString()
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

    private void hapus() {
        if (Sequel.queryu2tf("delete from penilaian_awal_keperawatan_igd where no_rawat=?", 1, new String[]{
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
            emptTeks();
        } else {
            JOptionPane.showMessageDialog(null, "Gagal menghapus..!!");
        }
    }

    private void simpan() {
        
//        if(KendaraanPribadi.isSelected()){
//            transportasi = "Kendaraan Pribadi";
//        }else if(Ambulance.isSelected()){
//            transportasi = "Ambulance";
//        }else if(MobilJenazah.isSelected()){
//            transportasi = "Mobil Jenazah";
//        }
        if (Sequel.menyimpantf("penilaian_awal_keperawatan_igd", "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?", "No.Rawat", 111, new String[]{
            TNoRw.getText(), Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19), Informasi.getSelectedItem().toString(), KeluhanUtama.getText(), RPD.getText(), RPO.getText(), StatusKehamilan.getSelectedItem().toString(),
            Gravida.getText(), Para.getText(), Abortus.getText(), HPHT.getText(),Kesadaran.getSelectedItem().toString(),GCS.getText(),TD.getText(),RR.getText(),Suhu.getText(),Nadi.getText() ,Tekanan.getSelectedItem().toString(), Pupil.getSelectedItem().toString(), Neurosensorik.getSelectedItem().toString(), Integumen.getSelectedItem().toString(),
            Turgor.getSelectedItem().toString(), Edema.getSelectedItem().toString(), Mukosa.getSelectedItem().toString(), Perdarahan.getSelectedItem().toString(), JumlahPerdarahan.getText(),
            WarnaPerdarahan.getText(), Intoksikasi.getSelectedItem().toString(), BAB.getText(), XBAB.getText(), KBAB.getText(), WBAB.getText(), BAK.getText(), XBAK.getText(), WBAK.getText(), LBAK.getText(), Psikologis.getSelectedItem().toString(),
            Jiwa.getSelectedItem().toString(), Perilaku.getSelectedItem().toString(), Dilaporkan.getText(), Sebutkan.getText(), Hubungan.getSelectedItem().toString(), TinggalDengan.getSelectedItem().toString(), KetTinggal.getText(),
            StatusBudaya.getSelectedItem().toString(), KetBudaya.getText(), PendidikanPJ.getSelectedItem().toString(), KetPendidikanPJ.getText(), Edukasi.getSelectedItem().toString(), KetEdukasi.getText(), KemampuanBacaTulis.getSelectedItem().toString(), ButuhPenerjemah.getSelectedItem().toString(), KeteranganButuhPenerjemah.getText(), TerdapatHambatanBelajar.getSelectedItem().toString(),
            HambatanBelajar.getSelectedItem().toString(), KeteranganHambatanBelajar.getText(), HambatanCaraBicara.getSelectedItem().toString(), HambatanBahasaIsyarat.getSelectedItem().toString(), CaraBelajarDisukai.getSelectedItem().toString(),
            KesediaanMenerimaInformasi.getSelectedItem().toString(), KeteranganKesediaanMenerimaInformasi.getText(), PemahamanNutrisi.getSelectedItem().toString(), PemahamanPenyakit.getSelectedItem().toString(), PemahamanPengobatan.getSelectedItem().toString(), PemahamanPerawatan.getSelectedItem().toString(),
            KeyakinanNilai.getSelectedItem().toString(), KeterbatasanFisik.getSelectedItem().toString(), HambatanEmosional.getSelectedItem().toString(), Motivasi.getSelectedItem().toString(), ADL.getSelectedItem().toString(),
            Aktifitas.getSelectedItem().toString(), AlatBantu.getSelectedItem().toString(), KetAlatBantu.getText(), Nyeri.getSelectedItem().toString(), Provokes.getSelectedItem().toString(), KetProvokes.getText(), Quality.getSelectedItem().toString(),
            KetQuality.getText(), Lokasi.getText(), Menyebar.getSelectedItem().toString(), SkalaNyeri.getSelectedItem().toString(), Durasi.getText(), NyeriHilang.getSelectedItem().toString(), KetNyeri.getText(), PadaDokter.getSelectedItem().toString(),
            KetDokter.getText(), ATS.getSelectedItem().toString(), BJM.getSelectedItem().toString(), MSA.getSelectedItem().toString(), Hasil.getSelectedItem().toString(), Lapor.getSelectedItem().toString(), KetLapor.getText(), InformasiPerencanaanPulang.getSelectedItem().toString(), LamaRatarata.getText(), Valid.SetTgl(TanggalPulang.getSelectedItem() + ""), KondisiPulang.getText(), PerawatanLanjutan.getText(), CaraTransportasiPulang.getSelectedItem().toString(), TransportasiYangDigunakan.getSelectedItem().toString(), 
//            Rencana.getText(),KondisiPasien.getSelectedItem().toString(),StatusPulang.getSelectedItem().toString(),Dirujukke.getText(),Indikasi.getText(),RuangRanap.getText(),KdDPJP.getText(),Valid.SetTgl(WaktuKeluar.getSelectedItem()+"")+" "+WaktuKeluar.getSelectedItem().toString().substring(11,19),WaktuMeninggal.getText(),transportasi,PendidikanKesehatan.getText(), KdPetugas.getText()
        }) == true) {
            tabMode.addRow(new Object[]{
                TNoRw.getText(), TNoRM.getText(), TPasien.getText(), Jk.getText(), Agama.getText(), Bahasa.getText(), CacatFisik.getText(), TglLahir.getText(), Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19), Informasi.getSelectedItem().toString(),
                KeluhanUtama.getText(), RPD.getText(), RPO.getText(), StatusKehamilan.getSelectedItem().toString(), Gravida.getText(), Para.getText(), Abortus.getText(), HPHT.getText(), Kesadaran.getSelectedItem().toString(),GCS.getText(),TD.getText(),RR.getText(),Suhu.getText(),Nadi.getText() ,Tekanan.getSelectedItem().toString(), Pupil.getSelectedItem().toString(), Neurosensorik.getSelectedItem().toString(),
                Integumen.getSelectedItem().toString(), Turgor.getSelectedItem().toString(), Edema.getSelectedItem().toString(), Mukosa.getSelectedItem().toString(), Perdarahan.getSelectedItem().toString(), JumlahPerdarahan.getText(), WarnaPerdarahan.getText(), Intoksikasi.getSelectedItem().toString(),
                BAB.getText(), XBAB.getText(), KBAB.getText(), WBAB.getText(), BAK.getText(), XBAK.getText(), WBAK.getText(), LBAK.getText(), Psikologis.getSelectedItem().toString(), Jiwa.getSelectedItem().toString(), Perilaku.getSelectedItem().toString(), Dilaporkan.getText(), Sebutkan.getText(),
                Hubungan.getSelectedItem().toString(), StatusPernikahan.getText(), TinggalDengan.getSelectedItem().toString(), KetTinggal.getText(), Pekerjaan.getText(), Pembayaran.getText(), StatusBudaya.getSelectedItem().toString(), KetBudaya.getText(), PendidikanPasien.getText(),
                PendidikanPJ.getSelectedItem().toString(), KetPendidikanPJ.getText(), Edukasi.getSelectedItem().toString(), KetEdukasi.getText(), KemampuanBacaTulis.getSelectedItem().toString(), ButuhPenerjemah.getSelectedItem().toString(), KeteranganButuhPenerjemah.getText(), TerdapatHambatanBelajar.getSelectedItem().toString(),
                HambatanBelajar.getSelectedItem().toString(), KeteranganHambatanBelajar.getText(), HambatanCaraBicara.getSelectedItem().toString(), HambatanBahasaIsyarat.getSelectedItem().toString(), CaraBelajarDisukai.getSelectedItem().toString(),
                KesediaanMenerimaInformasi.getSelectedItem().toString(), KeteranganKesediaanMenerimaInformasi.getText(), PemahamanNutrisi.getSelectedItem().toString(), PemahamanPenyakit.getSelectedItem().toString(), PemahamanPengobatan.getSelectedItem().toString(), PemahamanPerawatan.getSelectedItem().toString(),
                KeyakinanNilai.getSelectedItem().toString(), KeterbatasanFisik.getSelectedItem().toString(), HambatanEmosional.getSelectedItem().toString(), Motivasi.getSelectedItem().toString(), ADL.getSelectedItem().toString(), Aktifitas.getSelectedItem().toString(), AlatBantu.getSelectedItem().toString(), KetAlatBantu.getText(),
                Nyeri.getSelectedItem().toString(), Provokes.getSelectedItem().toString(), KetProvokes.getText(), Quality.getSelectedItem().toString(), KetQuality.getText(), Lokasi.getText(), Menyebar.getSelectedItem().toString(), SkalaNyeri.getSelectedItem().toString(), Durasi.getText(),
                NyeriHilang.getSelectedItem().toString(), KetNyeri.getText(), PadaDokter.getSelectedItem().toString(), KetDokter.getText(), ATS.getSelectedItem().toString(), BJM.getSelectedItem().toString(), MSA.getSelectedItem().toString(), Hasil.getSelectedItem().toString(),
                Lapor.getSelectedItem().toString(), KetLapor.getText(), InformasiPerencanaanPulang.getSelectedItem().toString(), LamaRatarata.getText(), Valid.SetTgl(TanggalPulang.getSelectedItem() + ""), KondisiPulang.getText(), PerawatanLanjutan.getText(), CaraTransportasiPulang.getSelectedItem().toString(), TransportasiYangDigunakan.getSelectedItem().toString(), 
//                Rencana.getText(),KondisiPasien.getSelectedItem().toString(),StatusPulang.getSelectedItem().toString(),Dirujukke.getText(),Indikasi.getText(),RuangRanap.getText(),KdDPJP.getText(),Valid.SetTgl(WaktuKeluar.getSelectedItem()+"")+" "+WaktuKeluar.getSelectedItem().toString().substring(11,19),WaktuMeninggal.getText(),transportasi,PendidikanKesehatan.getText(), KdPetugas.getText(), NmPetugas.getText()
            });
            LCount.setText("" + tabMode.getRowCount());
            Valid.tabelKosong(tabModeDetailMasalah);
            Valid.tabelKosong(tabModeDetailRencana);
            Valid.tabelKosong(tabModeDetailKebutuhanEdukasi);
            Valid.tabelKosong(tabModeDetailRencanaEdukasi);
            for (i = 0; i < tbMasalahKeperawatan.getRowCount(); i++) {
                if (tbMasalahKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                    if (Sequel.menyimpantf2("penilaian_awal_keperawatan_igd_masalah", "?,?", 2, new String[]{TNoRw.getText(), tbMasalahKeperawatan.getValueAt(i, 1).toString()}) == true) {
                        tabModeDetailMasalah.addRow(new Object[]{
                            tbMasalahKeperawatan.getValueAt(i, 1).toString(), tbMasalahKeperawatan.getValueAt(i, 2).toString()
                        });
                    }
                }
            }

            for (i = 0; i < tbRencanaKeperawatan.getRowCount(); i++) {
                if (tbRencanaKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                    if (Sequel.menyimpantf2("penilaian_awal_keperawatan_ralan_rencana_igd", "?,?", 2, new String[]{TNoRw.getText(), tbRencanaKeperawatan.getValueAt(i, 1).toString()}) == true) {
                        tabModeDetailRencana.addRow(new Object[]{
                            tbRencanaKeperawatan.getValueAt(i, 1).toString(), tbRencanaKeperawatan.getValueAt(i, 2).toString()
                        });
                    }
                }
            }
            for (i = 0; i < tbKebutuhanEdukasi.getRowCount(); i++) {
                if (tbKebutuhanEdukasi.getValueAt(i, 0).toString().equals("true")) {
                    if (Sequel.menyimpantf2("penilaian_awal_keperawatan_igd_kebutuhan_edukasi", "?,?", 2, new String[]{TNoRw.getText(), tbKebutuhanEdukasi.getValueAt(i, 1).toString()}) == true) {
                        tabModeDetailKebutuhanEdukasi.addRow(new Object[]{
                            tbKebutuhanEdukasi.getValueAt(i, 1).toString(), tbKebutuhanEdukasi.getValueAt(i, 2).toString()
                        });
                    }
                }
            }
            for (i = 0; i < tbRencanaEdukasi.getRowCount(); i++) {
                if (tbRencanaEdukasi.getValueAt(i, 0).toString().equals("true")) {
                    if (Sequel.menyimpantf2("penilaian_awal_keperawatan_igd_rencana_edukasi", "?,?", 2, new String[]{TNoRw.getText(), tbRencanaEdukasi.getValueAt(i, 1).toString()}) == true) {
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
    private void LoadRencanaEdukasi(){
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
