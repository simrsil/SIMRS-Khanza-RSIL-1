/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * CUSTOMM RSI FULL
 */


package rekammedis;

import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.WindowConstants;
import java.awt.event.WindowAdapter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
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
public final class RMPenilaianPasienTerminal extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private int i=0;    
    private DlgCariPetugas petugas;
    private DlgCariDokter dokter;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean ceksukses = false;
    private String finger="";
    private StringBuilder htmlContent;
    /** Creates new form DlgRujuk
     * @param parent
     * @param modal */
    public RMPenilaianPasienTerminal(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(628,674);

        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","No.R.M.","Nama Pasien","Tgl.Lahir","JK","Tanggal","Kegawatan Pernafasan","Kehilangan Tanus Otot", 
            "Nyeri","Ket.Nyeri","Perlambatan Sirkulasi","Faktor Yang Meningkatkan Gejala Fisik","Masalah Keperawatan Pasien","Pelayanan Spiritual","Ket.Pelayanan Spiritual","Perlu Didoakan", 
            "Perlu bimbingan Rohani","Perlu Pendampingn Rohani","Orang Yang Ingin Dihubungi","Ket.Yang Ingin Dihubungi","Hubungan Dengan Pasien","Alamat Yang Dihubungi","No.HP Yang Dihubungi","Perawatan Selanjutnya",
            "Reaksi Pasien Atas Penyakitnya","Masalah Keperawatan","Reaksi Keluarga","Masalah Keperawatan","Dukungan/Kelonggaran Pelayanan","Kebutuhan Akan Alternatif/Tingkat Pelayanan","Faktor Resiko Bagi Keluarga","Masalah Keperawatan","NIP","Petugas","Kode Dokter","Dokter"
        }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 36; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(105);
            }else if(i==1){
                column.setPreferredWidth(65);
            }else if(i==2){
                column.setPreferredWidth(160);
            }else if(i==3){
                column.setPreferredWidth(65);
            }else if(i==4){
                column.setPreferredWidth(25);
            }else if(i==5){
                column.setPreferredWidth(124);
            }else if(i==6){
                column.setPreferredWidth(170);
            }else if(i==7){
                column.setPreferredWidth(200);
            }else if(i==8){
                column.setPreferredWidth(190);
            }else if(i==9){
                column.setPreferredWidth(85);
            }else if(i==10){
                column.setPreferredWidth(80);
            }else if(i==11){
                column.setPreferredWidth(60);
            }else if(i==12){
                column.setPreferredWidth(76);
            }else if(i==13){
                column.setPreferredWidth(51);
            }else if(i==14){
                column.setPreferredWidth(68);
            }else if(i==15){
                column.setPreferredWidth(55);
            }else if(i==16){
                column.setPreferredWidth(61);
            }else if(i==17){
                column.setPreferredWidth(115);
            }else if(i==18){
                column.setPreferredWidth(220);
            }else if(i==19){
                column.setPreferredWidth(190);
            }else if(i==20){
                column.setPreferredWidth(85);
            }else if(i==21){
                column.setPreferredWidth(150);
            }else if(i==22){
                column.setPreferredWidth(150);
            }else if(i==23){
                column.setPreferredWidth(150);
            }else if(i==24){
                column.setPreferredWidth(150);
            }else if(i==25){
                column.setPreferredWidth(150);
            }else if(i==26){
                column.setPreferredWidth(150);
            }else if(i==27){
                column.setPreferredWidth(150);
            }else if(i==28){
                column.setPreferredWidth(150);
            }else if(i==29){
                column.setPreferredWidth(150);
            }else if(i==30){
                column.setPreferredWidth(150);
            }else if(i==31){
                column.setPreferredWidth(150);
            }else if(i==32){
                column.setPreferredWidth(150);
            }else if(i==33){
                column.setPreferredWidth(150);
            }else if(i==34){
                column.setPreferredWidth(150);
            }else if(i==35){
                column.setPreferredWidth(150);
            }
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        KdPetugas.setDocument(new batasInput((byte)20).getKata(KdPetugas));
        KdDokter.setDocument(new batasInput((int)500).getKata(KdDokter));
        FaktorGejala.setDocument(new batasInput((int)500).getKata(FaktorGejala));
        PerawatanLanjutan.setDocument(new batasInput((int)500).getKata(PerawatanLanjutan));
        KetNyeri.setDocument(new batasInput((byte)8).getKata(KetNyeri));
        DukunganPelayanan.setDocument(new batasInput((byte)5).getKata(DukunganPelayanan));
        AlternatifPelayanan.setDocument(new batasInput((byte)5).getKata(AlternatifPelayanan));
        TCari.setDocument(new batasInput((int)100).getKata(TCari));
        
        ChkInput.setSelected(false);
        isForm();
        jam();
        
        HTMLEditorKit kit = new HTMLEditorKit();
        LoadHTML.setEditable(true);
        LoadHTML.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule(
                ".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi2 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#323232;}"+
                ".isi3 td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi5 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#AA0000;}"+
                ".isi6 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#FF0000;}"+
                ".isi7 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#C8C800;}"+
                ".isi8 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#00AA00;}"+
                ".isi9 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#969696;}"
        );
        Document doc = kit.createDefaultDocument();
        LoadHTML.setDocument(doc);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        MnPenilaianPasienTerminal = new javax.swing.JMenuItem();
        LoadHTML = new widget.editorpane();
        JK = new widget.TextBox();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbObat = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        BtnPrint = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar = new widget.Button();
        panelGlass9 = new widget.panelisi();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        PanelInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        scrollInput = new widget.ScrollPane();
        FormInput = new widget.PanelBiasa();
        jLabel4 = new widget.Label();
        TNoRw = new widget.TextBox();
        TPasien = new widget.TextBox();
        Tanggal = new widget.Tanggal();
        TNoRM = new widget.TextBox();
        jLabel16 = new widget.Label();
        Jam = new widget.ComboBox();
        Menit = new widget.ComboBox();
        Detik = new widget.ComboBox();
        ChkKejadian = new widget.CekBox();
        jLabel18 = new widget.Label();
        KdPetugas = new widget.TextBox();
        NmPetugas = new widget.TextBox();
        BtnPetugas = new widget.Button();
        jLabel8 = new widget.Label();
        TglLahir = new widget.TextBox();
        jSeparator2 = new javax.swing.JSeparator();
        KetNyeri = new widget.TextBox();
        jLabel53 = new widget.Label();
        jLabel54 = new widget.Label();
        KehilanganTanusOtot = new widget.ComboBox();
        jLabel55 = new widget.Label();
        KegawatanPernafasan = new widget.ComboBox();
        jLabel56 = new widget.Label();
        Nyeri = new widget.ComboBox();
        jLabel57 = new widget.Label();
        jLabel58 = new widget.Label();
        PerlambatanSirkulasi = new widget.ComboBox();
        jLabel59 = new widget.Label();
        jLabel60 = new widget.Label();
        jLabel61 = new widget.Label();
        MasalahRespon = new widget.ComboBox();
        jLabel62 = new widget.Label();
        PelayananSpiritual = new widget.ComboBox();
        jLabel63 = new widget.Label();
        KetSpiritual = new widget.TextBox();
        jLabel64 = new widget.Label();
        jLabel65 = new widget.Label();
        jLabel66 = new widget.Label();
        MasalahReaksiPasien = new widget.ComboBox();
        jLabel67 = new widget.Label();
        BimbinganRohani = new widget.ComboBox();
        jLabel68 = new widget.Label();
        PendampinganRohani = new widget.ComboBox();
        jLabel69 = new widget.Label();
        jLabel70 = new widget.Label();
        PerluDidoakan = new widget.ComboBox();
        jLabel71 = new widget.Label();
        jLabel72 = new widget.Label();
        KetInginDihubungi = new widget.TextBox();
        jLabel73 = new widget.Label();
        NoHP = new widget.TextBox();
        jLabel74 = new widget.Label();
        jLabel75 = new widget.Label();
        jLabel76 = new widget.Label();
        AlamatDihubungi = new widget.TextBox();
        jLabel77 = new widget.Label();
        jLabel78 = new widget.Label();
        scrollPane1 = new widget.ScrollPane();
        PerawatanLanjutan = new widget.TextArea();
        scrollPane2 = new widget.ScrollPane();
        FaktorGejala = new widget.TextArea();
        HubunganPasien = new widget.TextBox();
        jLabel79 = new widget.Label();
        InginDihubungi = new widget.ComboBox();
        jLabel80 = new widget.Label();
        ReaksiPasien = new widget.ComboBox();
        jLabel81 = new widget.Label();
        ReaksiKeluarga = new widget.ComboBox();
        jLabel82 = new widget.Label();
        MasalahReaksiKeluarga = new widget.ComboBox();
        jLabel83 = new widget.Label();
        jLabel84 = new widget.Label();
        jLabel85 = new widget.Label();
        scrollPane3 = new widget.ScrollPane();
        AlternatifPelayanan = new widget.TextArea();
        jLabel86 = new widget.Label();
        scrollPane4 = new widget.ScrollPane();
        DukunganPelayanan = new widget.TextArea();
        jLabel87 = new widget.Label();
        jLabel88 = new widget.Label();
        MasalahResiko = new widget.ComboBox();
        jLabel89 = new widget.Label();
        InformasiResiko = new widget.ComboBox();
        jLabel20 = new widget.Label();
        KdDokter = new widget.TextBox();
        NmDokter = new widget.TextBox();
        BtnDokter = new widget.Button();
        jSeparator1 = new javax.swing.JSeparator();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnPenilaianPasienTerminal.setBackground(new java.awt.Color(255, 255, 254));
        MnPenilaianPasienTerminal.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnPenilaianPasienTerminal.setForeground(new java.awt.Color(50, 50, 50));
        MnPenilaianPasienTerminal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnPenilaianPasienTerminal.setText("Formulir Pengkajian Pasien Terminal");
        MnPenilaianPasienTerminal.setName("MnPenilaianPasienTerminal"); // NOI18N
        MnPenilaianPasienTerminal.setPreferredSize(new java.awt.Dimension(290, 26));
        MnPenilaianPasienTerminal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnPenilaianPasienTerminalActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnPenilaianPasienTerminal);

        LoadHTML.setBorder(null);
        LoadHTML.setName("LoadHTML"); // NOI18N

        JK.setHighlighter(null);
        JK.setName("JK"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Pengkajian Pasien Terminal ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);
        Scroll.setPreferredSize(new java.awt.Dimension(452, 200));

        tbObat.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbObat.setComponentPopupMenu(jPopupMenu1);
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

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
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

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass8.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass8.add(LCount);

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

        jPanel3.add(panelGlass8, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel19.setText("Tanggal :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "20-05-2026" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari1);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("s.d.");
        jLabel21.setName("jLabel21"); // NOI18N
        jLabel21.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass9.add(jLabel21);

        DTPCari2.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "20-05-2026" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari2);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(310, 23));
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

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(28, 23));
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
        panelGlass9.add(BtnAll);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 326));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('I');
        ChkInput.setText(".: Input Data");
        ChkInput.setToolTipText("Alt+I");
        ChkInput.setBorderPainted(true);
        ChkInput.setBorderPaintedFlat(true);
        ChkInput.setFocusable(false);
        ChkInput.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput.setName("ChkInput"); // NOI18N
        ChkInput.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInputActionPerformed(evt);
            }
        });
        PanelInput.add(ChkInput, java.awt.BorderLayout.PAGE_END);

        scrollInput.setName("scrollInput"); // NOI18N
        scrollInput.setPreferredSize(new java.awt.Dimension(102, 557));

        FormInput.setBackground(new java.awt.Color(250, 255, 245));
        FormInput.setBorder(null);
        FormInput.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 935));
        FormInput.setLayout(null);

        jLabel4.setText("No.Rawat :");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput.add(jLabel4);
        jLabel4.setBounds(0, 10, 80, 23);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw);
        TNoRw.setBounds(84, 10, 136, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        TPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TPasienKeyPressed(evt);
            }
        });
        FormInput.add(TPasien);
        TPasien.setBounds(336, 10, 285, 23);

        Tanggal.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "20-05-2026" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);
        Tanggal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalKeyPressed(evt);
            }
        });
        FormInput.add(Tanggal);
        Tanggal.setBounds(84, 40, 90, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        TNoRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRMKeyPressed(evt);
            }
        });
        FormInput.add(TNoRM);
        TNoRM.setBounds(222, 10, 112, 23);

        jLabel16.setText("Tanggal :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setVerifyInputWhenFocusTarget(false);
        FormInput.add(jLabel16);
        jLabel16.setBounds(0, 40, 80, 23);

        Jam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        Jam.setName("Jam"); // NOI18N
        Jam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JamKeyPressed(evt);
            }
        });
        FormInput.add(Jam);
        Jam.setBounds(178, 40, 62, 23);

        Menit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Menit.setName("Menit"); // NOI18N
        Menit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MenitKeyPressed(evt);
            }
        });
        FormInput.add(Menit);
        Menit.setBounds(243, 40, 62, 23);

        Detik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Detik.setName("Detik"); // NOI18N
        Detik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DetikKeyPressed(evt);
            }
        });
        FormInput.add(Detik);
        Detik.setBounds(308, 40, 62, 23);

        ChkKejadian.setBorder(null);
        ChkKejadian.setSelected(true);
        ChkKejadian.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkKejadian.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkKejadian.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkKejadian.setName("ChkKejadian"); // NOI18N
        FormInput.add(ChkKejadian);
        ChkKejadian.setBounds(373, 40, 23, 23);

        jLabel18.setText("Petugas :");
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput.add(jLabel18);
        jLabel18.setBounds(400, 40, 70, 23);

        KdPetugas.setEditable(false);
        KdPetugas.setHighlighter(null);
        KdPetugas.setName("KdPetugas"); // NOI18N
        KdPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdPetugasKeyPressed(evt);
            }
        });
        FormInput.add(KdPetugas);
        KdPetugas.setBounds(480, 40, 94, 23);

        NmPetugas.setEditable(false);
        NmPetugas.setName("NmPetugas"); // NOI18N
        FormInput.add(NmPetugas);
        NmPetugas.setBounds(580, 40, 187, 23);

        BtnPetugas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPetugas.setMnemonic('2');
        BtnPetugas.setToolTipText("ALt+2");
        BtnPetugas.setName("BtnPetugas"); // NOI18N
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
        BtnPetugas.setBounds(770, 40, 28, 23);

        jLabel8.setText("Tgl.Lahir :");
        jLabel8.setName("jLabel8"); // NOI18N
        FormInput.add(jLabel8);
        jLabel8.setBounds(625, 10, 60, 23);

        TglLahir.setHighlighter(null);
        TglLahir.setName("TglLahir"); // NOI18N
        FormInput.add(TglLahir);
        TglLahir.setBounds(689, 10, 100, 23);

        jSeparator2.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator2.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator2.setName("jSeparator2"); // NOI18N
        FormInput.add(jSeparator2);
        jSeparator2.setBounds(0, 100, 810, 1);

        KetNyeri.setFocusTraversalPolicyProvider(true);
        KetNyeri.setName("KetNyeri"); // NOI18N
        KetNyeri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KetNyeriActionPerformed(evt);
            }
        });
        KetNyeri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetNyeriKeyPressed(evt);
            }
        });
        FormInput.add(KetNyeri);
        KetNyeri.setBounds(200, 160, 200, 23);

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel53.setText("b. Kehilangan Tanus Otot :");
        jLabel53.setName("jLabel53"); // NOI18N
        FormInput.add(jLabel53);
        jLabel53.setBounds(410, 130, 160, 23);

        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel54.setText("IX. FAKTOR RESIKO BAGI KELUARGA YANG DITINGGALKAN");
        jLabel54.setName("jLabel54"); // NOI18N
        FormInput.add(jLabel54);
        jLabel54.setBounds(20, 850, 320, 23);

        KehilanganTanusOtot.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "Mual", "Sulit Menelan", "Inkontinensia Alvi", "Penurunan Pergerakan Tubuh", "Distensi Abdomen", "TAK", "Sulit Berbicara", "Inkontinensia Urine" }));
        KehilanganTanusOtot.setName("KehilanganTanusOtot"); // NOI18N
        KehilanganTanusOtot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KehilanganTanusOtotActionPerformed(evt);
            }
        });
        KehilanganTanusOtot.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KehilanganTanusOtotKeyPressed(evt);
            }
        });
        FormInput.add(KehilanganTanusOtot);
        KehilanganTanusOtot.setBounds(540, 130, 230, 23);

        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel55.setText("a. Masalah Keperawatan :");
        jLabel55.setName("jLabel55"); // NOI18N
        FormInput.add(jLabel55);
        jLabel55.setBounds(40, 330, 140, 20);

        KegawatanPernafasan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "Dyspnoe", "Nafas Tak Teratur", "Ada Sekret", "Nafas Cepat dan Dangkal", "Nafas Melalui Mulut", "SPO2 < Normal", "Nafas Lambat", "Mukosa Oral Kering", "TAK" }));
        KegawatanPernafasan.setName("KegawatanPernafasan"); // NOI18N
        KegawatanPernafasan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KegawatanPernafasanActionPerformed(evt);
            }
        });
        KegawatanPernafasan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KegawatanPernafasanKeyPressed(evt);
            }
        });
        FormInput.add(KegawatanPernafasan);
        KegawatanPernafasan.setBounds(160, 130, 230, 23);

        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel56.setText("Jika, Ya :");
        jLabel56.setName("jLabel56"); // NOI18N
        FormInput.add(jLabel56);
        jLabel56.setBounds(150, 160, 60, 20);

        Nyeri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        Nyeri.setName("Nyeri"); // NOI18N
        Nyeri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NyeriActionPerformed(evt);
            }
        });
        Nyeri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NyeriKeyPressed(evt);
            }
        });
        FormInput.add(Nyeri);
        Nyeri.setBounds(80, 160, 60, 20);

        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel57.setText("a.Perlu Pelayanan Spritual :");
        jLabel57.setName("jLabel57"); // NOI18N
        FormInput.add(jLabel57);
        jLabel57.setBounds(440, 330, 140, 20);

        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel58.setText("d.Perlambatan Sirkulasi :");
        jLabel58.setName("jLabel58"); // NOI18N
        FormInput.add(jLabel58);
        jLabel58.setBounds(410, 160, 130, 23);

        PerlambatanSirkulasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "Bercak Sianosis Pada Ekstremitas", "Gelisah", "Nadi Lambat dan Lemah", "Tekanan Darah Menurun", "Kulit Dingin dan Berkeringat", "Lemas", "TAK" }));
        PerlambatanSirkulasi.setName("PerlambatanSirkulasi"); // NOI18N
        PerlambatanSirkulasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PerlambatanSirkulasiActionPerformed(evt);
            }
        });
        PerlambatanSirkulasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PerlambatanSirkulasiKeyPressed(evt);
            }
        });
        FormInput.add(PerlambatanSirkulasi);
        PerlambatanSirkulasi.setBounds(540, 160, 230, 23);

        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel59.setText("I. GEJALA MAU MUNTAH DAN KESULITAN BERNAFAS");
        jLabel59.setName("jLabel59"); // NOI18N
        FormInput.add(jLabel59);
        jLabel59.setBounds(20, 110, 350, 23);

        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel60.setText("II. FAKTOR - FAKTOR YANG MENINGKATKAN DAN MEMBANGKITKAN GEJALA FISIK");
        jLabel60.setName("jLabel60"); // NOI18N
        FormInput.add(jLabel60);
        jLabel60.setBounds(20, 190, 400, 23);

        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel61.setText("a. Kegawatan Pernafasan :");
        jLabel61.setName("jLabel61"); // NOI18N
        FormInput.add(jLabel61);
        jLabel61.setBounds(30, 130, 140, 23);

        MasalahRespon.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "Mual", "Perubahan Persepsi Sensori", "Nyeri Akut", "Pola Nafas Tidak Efektif", "Konstipasi", "Nyeri Kronis", "Bersihan Jalan Nafas Tidak Efektif", "Defisit Perawatan Diri" }));
        MasalahRespon.setName("MasalahRespon"); // NOI18N
        MasalahRespon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MasalahResponKeyPressed(evt);
            }
        });
        FormInput.add(MasalahRespon);
        MasalahRespon.setBounds(170, 330, 230, 23);

        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel62.setText("III. MANAJEMEN GEJALA SAAT INI DAN RESPON PASIEN");
        jLabel62.setName("jLabel62"); // NOI18N
        FormInput.add(jLabel62);
        jLabel62.setBounds(20, 300, 290, 23);

        PelayananSpiritual.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PelayananSpiritual.setName("PelayananSpiritual"); // NOI18N
        PelayananSpiritual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PelayananSpiritualActionPerformed(evt);
            }
        });
        PelayananSpiritual.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PelayananSpiritualKeyPressed(evt);
            }
        });
        FormInput.add(PelayananSpiritual);
        PelayananSpiritual.setBounds(580, 330, 60, 20);

        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel63.setText("Jika, Ya :");
        jLabel63.setName("jLabel63"); // NOI18N
        FormInput.add(jLabel63);
        jLabel63.setBounds(650, 330, 60, 20);

        KetSpiritual.setFocusTraversalPolicyProvider(true);
        KetSpiritual.setName("KetSpiritual"); // NOI18N
        KetSpiritual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KetSpiritualActionPerformed(evt);
            }
        });
        KetSpiritual.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetSpiritualKeyPressed(evt);
            }
        });
        FormInput.add(KetSpiritual);
        KetSpiritual.setBounds(690, 330, 100, 23);

        jLabel64.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel64.setText("c. Nyeri :");
        jLabel64.setName("jLabel64"); // NOI18N
        FormInput.add(jLabel64);
        jLabel64.setBounds(30, 160, 60, 20);

        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel65.setText("IV. ORIENTASI SPIRITUAL PASIEN DAN KELUARGA");
        jLabel65.setName("jLabel65"); // NOI18N
        FormInput.add(jLabel65);
        jLabel65.setBounds(430, 300, 290, 23);

        jLabel66.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel66.setText("Hubungan Dengan Pasen :");
        jLabel66.setName("jLabel66"); // NOI18N
        FormInput.add(jLabel66);
        jLabel66.setBounds(420, 450, 140, 20);

        MasalahReaksiPasien.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "Anxietas", "Distress Spiritual" }));
        MasalahReaksiPasien.setName("MasalahReaksiPasien"); // NOI18N
        MasalahReaksiPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MasalahReaksiPasienActionPerformed(evt);
            }
        });
        MasalahReaksiPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MasalahReaksiPasienKeyPressed(evt);
            }
        });
        FormInput.add(MasalahReaksiPasien);
        MasalahReaksiPasien.setBounds(540, 590, 90, 20);

        jLabel67.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel67.setText("b.Perlu Bimbingan Rohani :");
        jLabel67.setName("jLabel67"); // NOI18N
        FormInput.add(jLabel67);
        jLabel67.setBounds(300, 390, 130, 20);

        BimbinganRohani.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        BimbinganRohani.setName("BimbinganRohani"); // NOI18N
        BimbinganRohani.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BimbinganRohaniActionPerformed(evt);
            }
        });
        BimbinganRohani.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BimbinganRohaniKeyPressed(evt);
            }
        });
        FormInput.add(BimbinganRohani);
        BimbinganRohani.setBounds(430, 390, 60, 20);

        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel68.setText("c.Perlu Pendampingan Rohani :");
        jLabel68.setName("jLabel68"); // NOI18N
        FormInput.add(jLabel68);
        jLabel68.setBounds(560, 390, 160, 20);

        PendampinganRohani.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PendampinganRohani.setName("PendampinganRohani"); // NOI18N
        PendampinganRohani.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PendampinganRohaniActionPerformed(evt);
            }
        });
        PendampinganRohani.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PendampinganRohaniKeyPressed(evt);
            }
        });
        FormInput.add(PendampinganRohani);
        PendampinganRohani.setBounds(710, 390, 60, 20);

        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel69.setText("V. URUSAN DAN KEBUTUHAN SPIRITUAL PASIEN DAN KELUARGA SEPERTI PUTUS ASA,PENDERITAAN,RASA BERSALAH ATAU PENGAMPUNAN");
        jLabel69.setName("jLabel69"); // NOI18N
        FormInput.add(jLabel69);
        jLabel69.setBounds(20, 360, 700, 23);

        jLabel70.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel70.setText("a.Perlu Didoakan :");
        jLabel70.setName("jLabel70"); // NOI18N
        FormInput.add(jLabel70);
        jLabel70.setBounds(40, 390, 100, 20);

        PerluDidoakan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PerluDidoakan.setName("PerluDidoakan"); // NOI18N
        PerluDidoakan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PerluDidoakanActionPerformed(evt);
            }
        });
        PerluDidoakan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PerluDidoakanKeyPressed(evt);
            }
        });
        FormInput.add(PerluDidoakan);
        PerluDidoakan.setBounds(130, 390, 60, 20);

        jLabel71.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel71.setText("Masalah :");
        jLabel71.setName("jLabel71"); // NOI18N
        FormInput.add(jLabel71);
        jLabel71.setBounds(490, 590, 60, 20);

        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel72.setText(",");
        jLabel72.setName("jLabel72"); // NOI18N
        FormInput.add(jLabel72);
        jLabel72.setBounds(390, 480, 20, 20);

        KetInginDihubungi.setFocusTraversalPolicyProvider(true);
        KetInginDihubungi.setName("KetInginDihubungi"); // NOI18N
        KetInginDihubungi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KetInginDihubungiActionPerformed(evt);
            }
        });
        KetInginDihubungi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetInginDihubungiKeyPressed(evt);
            }
        });
        FormInput.add(KetInginDihubungi);
        KetInginDihubungi.setBounds(300, 450, 110, 23);

        jLabel73.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel73.setText("No.Hp Yang Dihubungi :");
        jLabel73.setName("jLabel73"); // NOI18N
        FormInput.add(jLabel73);
        jLabel73.setBounds(400, 480, 130, 20);

        NoHP.setFocusTraversalPolicyProvider(true);
        NoHP.setName("NoHP"); // NOI18N
        NoHP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoHPActionPerformed(evt);
            }
        });
        NoHP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoHPKeyPressed(evt);
            }
        });
        FormInput.add(NoHP);
        NoHP.setBounds(520, 480, 210, 23);

        jLabel74.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel74.setText("Jika, Ya :");
        jLabel74.setName("jLabel74"); // NOI18N
        FormInput.add(jLabel74);
        jLabel74.setBounds(250, 450, 60, 20);

        jLabel75.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel75.setText(",");
        jLabel75.setName("jLabel75"); // NOI18N
        FormInput.add(jLabel75);
        jLabel75.setBounds(410, 450, 20, 20);

        jLabel76.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel76.setText("Alamat  Yang Dihubungi:");
        jLabel76.setName("jLabel76"); // NOI18N
        FormInput.add(jLabel76);
        jLabel76.setBounds(50, 480, 130, 20);

        AlamatDihubungi.setFocusTraversalPolicyProvider(true);
        AlamatDihubungi.setName("AlamatDihubungi"); // NOI18N
        AlamatDihubungi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AlamatDihubungiActionPerformed(evt);
            }
        });
        AlamatDihubungi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AlamatDihubungiKeyPressed(evt);
            }
        });
        FormInput.add(AlamatDihubungi);
        AlamatDihubungi.setBounds(170, 480, 210, 23);

        jLabel77.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel77.setText(",");
        jLabel77.setName("jLabel77"); // NOI18N
        FormInput.add(jLabel77);
        jLabel77.setBounds(720, 450, 20, 20);

        jLabel78.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel78.setText("a.Ada Yang Ingin Dihubungi :");
        jLabel78.setName("jLabel78"); // NOI18N
        FormInput.add(jLabel78);
        jLabel78.setBounds(40, 450, 150, 20);

        scrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane1.setName("scrollPane1"); // NOI18N

        PerawatanLanjutan.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        PerawatanLanjutan.setColumns(20);
        PerawatanLanjutan.setRows(5);
        PerawatanLanjutan.setName("PerawatanLanjutan"); // NOI18N
        PerawatanLanjutan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PerawatanLanjutanKeyPressed(evt);
            }
        });
        scrollPane1.setViewportView(PerawatanLanjutan);

        FormInput.add(scrollPane1);
        scrollPane1.setBounds(160, 520, 630, 60);

        scrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane2.setName("scrollPane2"); // NOI18N

        FaktorGejala.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        FaktorGejala.setColumns(20);
        FaktorGejala.setRows(5);
        FaktorGejala.setName("FaktorGejala"); // NOI18N
        FaktorGejala.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                FaktorGejalaKeyPressed(evt);
            }
        });
        scrollPane2.setViewportView(FaktorGejala);

        FormInput.add(scrollPane2);
        scrollPane2.setBounds(50, 220, 725, 70);

        HubunganPasien.setFocusTraversalPolicyProvider(true);
        HubunganPasien.setName("HubunganPasien"); // NOI18N
        HubunganPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HubunganPasienActionPerformed(evt);
            }
        });
        HubunganPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HubunganPasienKeyPressed(evt);
            }
        });
        FormInput.add(HubunganPasien);
        HubunganPasien.setBounds(550, 450, 170, 23);

        jLabel79.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel79.setText("b.Perawatan Lanjutan :");
        jLabel79.setName("jLabel79"); // NOI18N
        FormInput.add(jLabel79);
        jLabel79.setBounds(40, 530, 120, 20);

        InginDihubungi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        InginDihubungi.setName("InginDihubungi"); // NOI18N
        InginDihubungi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InginDihubungiActionPerformed(evt);
            }
        });
        InginDihubungi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                InginDihubungiKeyPressed(evt);
            }
        });
        FormInput.add(InginDihubungi);
        InginDihubungi.setBounds(180, 450, 60, 20);

        jLabel80.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel80.setText("Assesmen Informasi :");
        jLabel80.setName("jLabel80"); // NOI18N
        FormInput.add(jLabel80);
        jLabel80.setBounds(240, 620, 120, 20);

        ReaksiPasien.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "Menyangkal", "Rasa Bersalah", "Sedih / Menangis", "Takut", "Marah", "Ketidakberdayaan" }));
        ReaksiPasien.setName("ReaksiPasien"); // NOI18N
        ReaksiPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReaksiPasienActionPerformed(evt);
            }
        });
        ReaksiPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ReaksiPasienKeyPressed(evt);
            }
        });
        FormInput.add(ReaksiPasien);
        ReaksiPasien.setBounds(330, 590, 140, 20);

        jLabel81.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel81.setText("d.Reaksi Keluarga Atas Penyakit Pasien :");
        jLabel81.setName("jLabel81"); // NOI18N
        FormInput.add(jLabel81);
        jLabel81.setBounds(40, 620, 210, 20);

        ReaksiKeluarga.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "Marah", "Letih/lelah", "Gangguan Tidur", "Rasa Bersalah", "Penurunan Konsentrasi", "Penurunan Konsentrasi", "Perubahan Kebiasaan Pada Pola Komunikasi", "Ketidakmampuan Peran Yang Diharapkan", "Keluarga Kurang Berpartisipasi Membuat Keputusan dalam Perawatan Pasien", "Keluarga Kurang Berkomunikasi Dengan Pasien" }));
        ReaksiKeluarga.setName("ReaksiKeluarga"); // NOI18N
        ReaksiKeluarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReaksiKeluargaActionPerformed(evt);
            }
        });
        ReaksiKeluarga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ReaksiKeluargaKeyPressed(evt);
            }
        });
        FormInput.add(ReaksiKeluarga);
        ReaksiKeluarga.setBounds(350, 620, 140, 20);

        jLabel82.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel82.setText("Masalah :");
        jLabel82.setName("jLabel82"); // NOI18N
        FormInput.add(jLabel82);
        jLabel82.setBounds(500, 620, 60, 20);

        MasalahReaksiKeluarga.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "Koping Individu Kurang Efektif", "Distress Spiritual" }));
        MasalahReaksiKeluarga.setName("MasalahReaksiKeluarga"); // NOI18N
        MasalahReaksiKeluarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MasalahReaksiKeluargaActionPerformed(evt);
            }
        });
        MasalahReaksiKeluarga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MasalahReaksiKeluargaKeyPressed(evt);
            }
        });
        FormInput.add(MasalahReaksiKeluarga);
        MasalahReaksiKeluarga.setBounds(550, 620, 180, 20);

        jLabel83.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel83.setText("c.Reaksi Pasien Atas Penyakitnya :");
        jLabel83.setName("jLabel83"); // NOI18N
        FormInput.add(jLabel83);
        jLabel83.setBounds(40, 590, 170, 20);

        jLabel84.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel84.setText("Assesmen Informasi :");
        jLabel84.setName("jLabel84"); // NOI18N
        FormInput.add(jLabel84);
        jLabel84.setBounds(220, 590, 120, 20);

        jLabel85.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel85.setText("Vi. STATUS PSIKOSOSIAL DAN KELUARGA");
        jLabel85.setName("jLabel85"); // NOI18N
        FormInput.add(jLabel85);
        jLabel85.setBounds(20, 430, 220, 23);

        scrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane3.setName("scrollPane3"); // NOI18N

        AlternatifPelayanan.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        AlternatifPelayanan.setColumns(20);
        AlternatifPelayanan.setRows(5);
        AlternatifPelayanan.setName("AlternatifPelayanan"); // NOI18N
        AlternatifPelayanan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AlternatifPelayananKeyPressed(evt);
            }
        });
        scrollPane3.setViewportView(AlternatifPelayanan);

        FormInput.add(scrollPane3);
        scrollPane3.setBounds(40, 780, 730, 60);

        jLabel86.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel86.setText("VII. KEBUTUHAN DUKUNGAN ATAU KELONGGARAN PELAYANAN BAGI PASIEN,KELUARGA DAN PEMBERI PELAYANAN LAIN");
        jLabel86.setName("jLabel86"); // NOI18N
        FormInput.add(jLabel86);
        jLabel86.setBounds(20, 650, 590, 23);

        scrollPane4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane4.setName("scrollPane4"); // NOI18N

        DukunganPelayanan.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        DukunganPelayanan.setColumns(20);
        DukunganPelayanan.setRows(5);
        DukunganPelayanan.setName("DukunganPelayanan"); // NOI18N
        DukunganPelayanan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DukunganPelayananKeyPressed(evt);
            }
        });
        scrollPane4.setViewportView(DukunganPelayanan);

        FormInput.add(scrollPane4);
        scrollPane4.setBounds(40, 680, 730, 60);

        jLabel87.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel87.setText("VIII. KEBUTUHAN ALTERNATIF ATAU TINGKAT PELAYANAN LAIN");
        jLabel87.setName("jLabel87"); // NOI18N
        FormInput.add(jLabel87);
        jLabel87.setBounds(20, 750, 320, 23);

        jLabel88.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel88.setText("Masalah :");
        jLabel88.setName("jLabel88"); // NOI18N
        FormInput.add(jLabel88);
        jLabel88.setBounds(480, 880, 60, 20);

        MasalahResiko.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "Menyangkal", "Rasa Bersalah", "Sedih / Menangis", "Takut", "Marah", "Ketidakberdayaan" }));
        MasalahResiko.setName("MasalahResiko"); // NOI18N
        MasalahResiko.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MasalahResikoActionPerformed(evt);
            }
        });
        MasalahResiko.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MasalahResikoKeyPressed(evt);
            }
        });
        FormInput.add(MasalahResiko);
        MasalahResiko.setBounds(530, 880, 140, 20);

        jLabel89.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel89.setText("Assesmen Informasi Resiko:");
        jLabel89.setName("jLabel89"); // NOI18N
        FormInput.add(jLabel89);
        jLabel89.setBounds(40, 880, 140, 20);

        InformasiResiko.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "Menyangkal", "Rasa Bersalah", "Sedih / Menangis", "Takut", "Marah", "Ketidakberdayaan" }));
        InformasiResiko.setName("InformasiResiko"); // NOI18N
        InformasiResiko.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InformasiResikoActionPerformed(evt);
            }
        });
        InformasiResiko.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                InformasiResikoKeyPressed(evt);
            }
        });
        FormInput.add(InformasiResiko);
        InformasiResiko.setBounds(180, 880, 140, 20);

        jLabel20.setText("Dokter :");
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput.add(jLabel20);
        jLabel20.setBounds(400, 70, 70, 14);

        KdDokter.setEditable(false);
        KdDokter.setHighlighter(null);
        KdDokter.setName("KdDokter"); // NOI18N
        KdDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdDokterKeyPressed(evt);
            }
        });
        FormInput.add(KdDokter);
        KdDokter.setBounds(480, 70, 94, 24);

        NmDokter.setEditable(false);
        NmDokter.setName("NmDokter"); // NOI18N
        FormInput.add(NmDokter);
        NmDokter.setBounds(580, 70, 187, 24);

        BtnDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnDokter.setMnemonic('2');
        BtnDokter.setToolTipText("ALt+2");
        BtnDokter.setName("BtnDokter"); // NOI18N
        BtnDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDokterActionPerformed(evt);
            }
        });
        BtnDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDokterKeyPressed(evt);
            }
        });
        FormInput.add(BtnDokter);
        BtnDokter.setBounds(770, 70, 28, 22);

        jSeparator1.setName("jSeparator1"); // NOI18N
        FormInput.add(jSeparator1);
        jSeparator1.setBounds(0, 190, 800, 0);

        scrollInput.setViewportView(FormInput);

        PanelInput.add(scrollInput, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"pasien");
        }else if(KdPetugas.getText().trim().equals("")||NmPetugas.getText().trim().equals("")){
            Valid.textKosong(KdPetugas,"Petugas");
        }else if(PerawatanLanjutan.getText().trim().equals("")){
            Valid.textKosong(PerawatanLanjutan,"Perawatan Lanjutan Dirumah");
        }else if(AlternatifPelayanan.getText().trim().equals("")){
            Valid.textKosong(AlternatifPelayanan,"Riwayat Penyakit/Kondisi Sebelumnya");
        }else if(KdDokter.getText().trim().equals("")){
            Valid.textKosong(KdDokter,"Diagnosa");
        }else{
            if(Sequel.menyimpantf("penilaian_awal_pasien_terminal","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","Data",30,new String[]{
                TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),
                KegawatanPernafasan.getSelectedItem().toString(),KehilanganTanusOtot.getSelectedItem().toString(),Nyeri.getSelectedItem().toString(),KetNyeri.getText(),PerlambatanSirkulasi.getSelectedItem().toString(),
                FaktorGejala.getText(),MasalahRespon.getSelectedItem().toString(),PelayananSpiritual.getSelectedItem().toString(),KetSpiritual.getText(),PerluDidoakan.getSelectedItem().toString(),BimbinganRohani.getSelectedItem().toString(), 
                PendampinganRohani.getSelectedItem().toString(),InginDihubungi.getSelectedItem().toString(),KetInginDihubungi.getText(),HubunganPasien.getText(),AlamatDihubungi.getText(),NoHP.getText(),PerawatanLanjutan.getText(),
                ReaksiPasien.getSelectedItem().toString(),MasalahReaksiPasien.getSelectedItem().toString(),ReaksiKeluarga.getSelectedItem().toString(),MasalahReaksiKeluarga.getSelectedItem().toString(),DukunganPelayanan.getText(),AlternatifPelayanan.getText(),InformasiResiko.getSelectedItem().toString(),MasalahResiko.getSelectedItem().toString(),KdDokter.getText(),KdPetugas.getText()
            })==true){
                tabMode.addRow(new Object[]{
                TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),
                KegawatanPernafasan.getSelectedItem().toString(),KehilanganTanusOtot.getSelectedItem().toString(),Nyeri.getSelectedItem().toString(),KetNyeri.getText(),PerlambatanSirkulasi.getSelectedItem().toString(),
                FaktorGejala.getText(),MasalahRespon.getSelectedItem().toString(),PelayananSpiritual.getSelectedItem().toString(),KetSpiritual.getText(),PerluDidoakan.getSelectedItem().toString(),BimbinganRohani.getSelectedItem().toString(), 
                PendampinganRohani.getSelectedItem().toString(),InginDihubungi.getSelectedItem().toString(),KetInginDihubungi.getText(),HubunganPasien.getText(),AlamatDihubungi.getText(),NoHP.getText(),PerawatanLanjutan.getText(),
                ReaksiPasien.getSelectedItem().toString(),MasalahReaksiPasien.getSelectedItem().toString(),ReaksiKeluarga.getSelectedItem().toString(),MasalahReaksiKeluarga.getSelectedItem().toString(),DukunganPelayanan.getText(),AlternatifPelayanan.getText(),InformasiResiko.getSelectedItem().toString(),MasalahResiko.getSelectedItem().toString(),KdDokter.getText(),KdPetugas.getText()
                });
                emptTeks();
                LCount.setText(""+tabMode.getRowCount());
            }  
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
//            Valid.pindah(evt,KebutuhanSpiritual,BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        emptTeks();
        ChkInput.setSelected(true);
        isForm(); 
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            emptTeks();
        }else{Valid.pindah(evt, BtnSimpan, BtnHapus);}
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(tbObat.getSelectedRow()>-1){
            if(akses.getkode().equals("Admin Utama")){
                hapus();
            }else{
                if(KdPetugas.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),32).toString())){
                    hapus();
                }else{
                    JOptionPane.showMessageDialog(null,"Hanya bisa dihapus oleh petugas yang bersangkutan..!!");
                }
            }
        }else{
            JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
        }   
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBatal, BtnEdit);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"pasien");
        }else if(KdPetugas.getText().trim().equals("")||NmPetugas.getText().trim().equals("")){
            Valid.textKosong(KdPetugas,"Petugas");
        }else if(PerawatanLanjutan.getText().trim().equals("")){
            Valid.textKosong(PerawatanLanjutan,"Perawatan Lanjutan Dirumah");
        }else if(AlternatifPelayanan.getText().trim().equals("")){
            Valid.textKosong(AlternatifPelayanan,"Riwayat Penyakit/Kondisi Sebelumnya");
        }else if(KdDokter.getText().trim().equals("")){
            Valid.textKosong(KdDokter,"Diagnosa");
        }else{
            if(tbObat.getSelectedRow()>-1){
                if(akses.getkode().equals("Admin Utama")){
                    ganti();
                }else{
                    if(KdPetugas.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),32).toString())){
                        ganti();
                    }else{
                        JOptionPane.showMessageDialog(null,"Hanya bisa diganti oleh petugas yang bersangkutan..!!");
                    }
                }
            }else{
                JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
            }
        }
}//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEditActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnPrint);
        }
}//GEN-LAST:event_BtnEditKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnKeluarActionPerformed(null);
        }else{Valid.pindah(evt,BtnEdit,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            BtnBatal.requestFocus();
        }else if(tabMode.getRowCount()!=0){
            try{
                htmlContent = new StringBuilder();
                htmlContent.append(                             
                        "<tr class='isi'>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>No.Rawat</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>No.R.M.</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Nama Pasien</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Tgl.Lahir</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>JK</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Tanggal</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Kegawatan Pernafasan</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Kehilangan Tanus Otot</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Nyeri</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Keterangan Nyeri</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Perlambatan Sirkulasi</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Faktor Yang Meningkatkan Gejala Fisik</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Masalah Keperawatan Pasien</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Pelayanan Spiritual</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Ket.Pelayanan Spiritual</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Perlu Didoakan</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Perlu Bimbingan Rohani</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Perlu Pendampingan Rohani</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Orang Yang Ingin Dihubungi</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Ket Yang Ingin Dihubungi</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Hubungan Dengan Pasien</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Alamat Yang Dihubungi</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>No.Hp Yang Dihubungi</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Perawatan Selanjutnya Dirumah</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Reaksi Pasien Tas Penyakitnya</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Masalah Keperawatan</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Reaksi Keluarga</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Masalah Keperawatan</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Dukungan Atau Kelonggaran Pelayanan</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Kebutuhan Akan Alternatif/Tingkat Pelayann</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Faktor Resiko Bagi Keluarga</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Masalah Keperawatan Resiko</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>NIP</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Petugas</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Kode Dokter</b></td>"
                        + "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Dokter</b></td>"
                        + "</tr>"
                );
                for (i = 0; i < tabMode.getRowCount(); i++) {
                    htmlContent.append(
                            "<tr class='isi'>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 0).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 1).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 2).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 3).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 4).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 5).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 6).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 7).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 8).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 9).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 10).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 11).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 12).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 13).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 14).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 15).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 16).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 17).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 18).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 19).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 20).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 21).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 22).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 23).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 24).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 25).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 26).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 27).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 28).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 29).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 30).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 31).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 32).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 33).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 34).toString() + "</td>"
                            + "<td valign='top'>" + tbObat.getValueAt(i, 35).toString() + "</td>"
                            + "</tr>");
                }
                LoadHTML.setText(
                    "<html>"+
                      "<table width='2400px' border='0' align='center' cellpadding='1px' cellspacing='0' class='tbl_form'>"+
                       htmlContent.toString()+
                      "</table>"+
                    "</html>"
                );

                File g = new File("file2.css");            
                BufferedWriter bg = new BufferedWriter(new FileWriter(g));
                bg.write(
                    ".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                    ".isi2 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#323232;}"+
                    ".isi3 td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                    ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                    ".isi5 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#AA0000;}"+
                    ".isi6 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#FF0000;}"+
                    ".isi7 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#C8C800;}"+
                    ".isi8 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#00AA00;}"+
                    ".isi9 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#969696;}"
                );
                bg.close();

                File f = new File("DataPenilaianPasienTerminal.html");            
                BufferedWriter bw = new BufferedWriter(new FileWriter(f));            
                bw.write(LoadHTML.getText().replaceAll("<head>","<head>"+
                            "<link href=\"file2.css\" rel=\"stylesheet\" type=\"text/css\" />"+
                            "<table width='2400px' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                "<tr class='isi2'>"+
                                    "<td valign='top' align='center'>"+
                                        "<font size='4' face='Tahoma'>"+akses.getnamars()+"</font><br>"+
                                        akses.getalamatrs()+", "+akses.getkabupatenrs()+", "+akses.getpropinsirs()+"<br>"+
                                        akses.getkontakrs()+", E-mail : "+akses.getemailrs()+"<br><br>"+
                                        "<font size='2' face='Tahoma'>DATA PENGKAJIAN PASIEN TERMINAL<br><br></font>"+        
                                    "</td>"+
                               "</tr>"+
                            "</table>")
                );
                bw.close();                         
                Desktop.getDesktop().browse(f.toURI());
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
            }
        }
        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnEdit, BtnKeluar);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
}//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        runBackground(() ->tampil());
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        runBackground(() ->tampil());
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            TCari.setText("");
            runBackground(() ->tampil());
        }else{
            Valid.pindah(evt, BtnCari, TPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbObatMouseClicked

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbObatKeyPressed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void MnPenilaianPasienTerminalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnPenilaianPasienTerminalActionPerformed
        if(tbObat.getSelectedRow()>-1){
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbObat.getValueAt(tbObat.getSelectedRow(),20).toString());
            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+tbObat.getValueAt(tbObat.getSelectedRow(),21).toString()+"\nID "+(finger.equals("")?tbObat.getValueAt(tbObat.getSelectedRow(),20).toString():finger)+"\n"+Tanggal.getSelectedItem());
            Valid.MyReportqry("rptFormulirPenilaianPasienTerminal.jasper","report","::[ Formulir Pengkajian Pasien Terminal ]::",
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tgl_lahir,penilaian_awal_pasien_terminal.tanggal,"+
                    "penilaian_awal_pasien_terminal.kegawatan_pernafasan,penilaian_awal_pasien_terminal.kehilangan_tonus_otot,penilaian_awal_pasien_terminal.nyeri,penilaian_awal_pasien_terminal.ket_nyeri,"+
                    "penilaian_awal_pasien_terminal.perlambatan_sirkulasi,penilaian_awal_pasien_terminal.faktor_meningkatkan_gejala_fisik,penilaian_awal_pasien_terminal.masalah_keperawatan,penilaian_awal_pasien_terminal.pelayanan_spiritual,"+
                    "penilaian_awal_pasien_terminal.ket_spiritual,penilaian_awal_pasien_terminal.perlu_didoakan,penilaian_awal_pasien_terminal.perlu_bimbingan_rohani,penilaian_awal_pasien_terminal.perlu_pendampingan_rohani,penilaian_awal_pasien_terminal.orang_ingin_dihubungi,"+
                    "penilaian_awal_pasien_terminal.ket_orang_ingin_dihubungi,penilaian_awal_pasien_terminal.hubungan_dengan_pasien,penilaian_awal_pasien_terminal.alamat_yang_dihubungi,penilaian_awal_pasien_terminal.nohp_yang_dihubungi,penilaian_awal_pasien_terminal.perawatan_lanjutan,penilaian_awal_pasien_terminal.assesmen_informasi_pasien,penilaian_awal_pasien_terminal.masalah_keperawatan_pasien,penilaian_awal_pasien_terminal.assesmen_informasi_keluarga,penilaian_awal_pasien_terminal.masalah_keperawatan_keluarga,penilaian_awal_pasien_terminal.dukungan_pelayanan,penilaian_awal_pasien_terminal.kebutuhan_altrnatif,penilaian_awal_pasien_terminal.assesmen_informasi_resiko,penilaian_awal_pasien_terminal.masalah_keperawatan_resiko,penilaian_awal_pasien_terminal.kd_dokter,dokter.nm_dokter,penilaian_awal_pasien_terminal.nip,petugas.nama "+
                    "from penilaian_awal_pasien_terminal inner join reg_periksa on penilaian_awal_pasien_terminal.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join dokter on penilaian_awal_pasien_terminal.kd_dokter=dokter.kd_dokter "+
                    "inner join petugas on penilaian_awal_pasien_terminal.nip=petugas.nip where reg_periksa.no_rawat='"+tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()+"'",param);
        }
    }//GEN-LAST:event_MnPenilaianPasienTerminalActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        runBackground(() ->tampil());
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        runBackground(() ->tampil());
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        runBackground(() ->tampil());
                    }
                }
            });
        }
    }//GEN-LAST:event_formWindowOpened

    private void KetNyeriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetNyeriKeyPressed
//        Valid.pindah(evt,Kesadaran,Nadi);
    }//GEN-LAST:event_KetNyeriKeyPressed

    private void BtnPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPetugasKeyPressed
//        Valid.pindah(evt,Detik,RPS);
    }//GEN-LAST:event_BtnPetugasKeyPressed

    private void BtnPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPetugasActionPerformed
        if (petugas == null || !petugas.isDisplayable()) {
            petugas=new DlgCariPetugas(null,false);
            petugas.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            petugas.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    if(petugas.getTable().getSelectedRow()!= -1){
                        KdPetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                        NmPetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());
                    }
                    BtnPetugas.requestFocus();
                    petugas=null;
                }
            });

            petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            petugas.setLocationRelativeTo(internalFrame1);
        }
        if (petugas == null) return;
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

    private void KdPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdPetugasKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            NmPetugas.setText(Sequel.CariPetugas(KdPetugas.getText()));
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            Detik.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            //GCS.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnPetugasActionPerformed(null);
        }
    }//GEN-LAST:event_KdPetugasKeyPressed

    private void DetikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DetikKeyPressed
        Valid.pindah(evt,Menit,BtnPetugas);
    }//GEN-LAST:event_DetikKeyPressed

    private void MenitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MenitKeyPressed
        Valid.pindah(evt,Jam,Detik);
    }//GEN-LAST:event_MenitKeyPressed

    private void JamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JamKeyPressed
        Valid.pindah(evt,Tanggal,Menit);
    }//GEN-LAST:event_JamKeyPressed

    private void TNoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRMKeyPressed
        // Valid.pindah(evt, TNm, BtnSimpan);
    }//GEN-LAST:event_TNoRMKeyPressed

    private void TanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalKeyPressed
        Valid.pindah(evt,TCari,Jam);
    }//GEN-LAST:event_TanggalKeyPressed

    private void TPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPasienKeyPressed
        Valid.pindah(evt,TCari,BtnSimpan);
    }//GEN-LAST:event_TPasienKeyPressed

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat();
        }else{
            Valid.pindah(evt,TCari,Tanggal);
        }
    }//GEN-LAST:event_TNoRwKeyPressed

    private void KehilanganTanusOtotKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KehilanganTanusOtotKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KehilanganTanusOtotKeyPressed

    private void KegawatanPernafasanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KegawatanPernafasanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KegawatanPernafasanKeyPressed

    private void NyeriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NyeriKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NyeriKeyPressed

    private void KehilanganTanusOtotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KehilanganTanusOtotActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KehilanganTanusOtotActionPerformed

    private void NyeriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NyeriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NyeriActionPerformed

    private void KetNyeriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KetNyeriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KetNyeriActionPerformed

    private void PerlambatanSirkulasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PerlambatanSirkulasiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PerlambatanSirkulasiActionPerformed

    private void PerlambatanSirkulasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PerlambatanSirkulasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PerlambatanSirkulasiKeyPressed

    private void MasalahResponKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MasalahResponKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_MasalahResponKeyPressed

    private void KegawatanPernafasanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KegawatanPernafasanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KegawatanPernafasanActionPerformed

    private void PelayananSpiritualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PelayananSpiritualActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PelayananSpiritualActionPerformed

    private void PelayananSpiritualKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PelayananSpiritualKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PelayananSpiritualKeyPressed

    private void KetSpiritualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KetSpiritualActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KetSpiritualActionPerformed

    private void KetSpiritualKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetSpiritualKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KetSpiritualKeyPressed

    private void MasalahReaksiPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MasalahReaksiPasienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MasalahReaksiPasienActionPerformed

    private void MasalahReaksiPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MasalahReaksiPasienKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_MasalahReaksiPasienKeyPressed

    private void BimbinganRohaniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BimbinganRohaniActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BimbinganRohaniActionPerformed

    private void BimbinganRohaniKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BimbinganRohaniKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BimbinganRohaniKeyPressed

    private void PendampinganRohaniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PendampinganRohaniActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PendampinganRohaniActionPerformed

    private void PendampinganRohaniKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PendampinganRohaniKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PendampinganRohaniKeyPressed

    private void PerluDidoakanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PerluDidoakanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PerluDidoakanActionPerformed

    private void PerluDidoakanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PerluDidoakanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PerluDidoakanKeyPressed

    private void KetInginDihubungiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KetInginDihubungiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KetInginDihubungiActionPerformed

    private void KetInginDihubungiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetInginDihubungiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KetInginDihubungiKeyPressed

    private void NoHPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NoHPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoHPActionPerformed

    private void NoHPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoHPKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoHPKeyPressed

    private void AlamatDihubungiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AlamatDihubungiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AlamatDihubungiActionPerformed

    private void AlamatDihubungiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AlamatDihubungiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_AlamatDihubungiKeyPressed

    private void PerawatanLanjutanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PerawatanLanjutanKeyPressed
//        Valid.pindah2(evt, HubunganDenganPasien, PrenatalG);
    }//GEN-LAST:event_PerawatanLanjutanKeyPressed

    private void FaktorGejalaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FaktorGejalaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_FaktorGejalaKeyPressed

    private void HubunganPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HubunganPasienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_HubunganPasienActionPerformed

    private void HubunganPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HubunganPasienKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_HubunganPasienKeyPressed

    private void InginDihubungiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InginDihubungiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InginDihubungiActionPerformed

    private void InginDihubungiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_InginDihubungiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_InginDihubungiKeyPressed

    private void ReaksiPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReaksiPasienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ReaksiPasienActionPerformed

    private void ReaksiPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ReaksiPasienKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ReaksiPasienKeyPressed

    private void ReaksiKeluargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReaksiKeluargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ReaksiKeluargaActionPerformed

    private void ReaksiKeluargaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ReaksiKeluargaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ReaksiKeluargaKeyPressed

    private void MasalahReaksiKeluargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MasalahReaksiKeluargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MasalahReaksiKeluargaActionPerformed

    private void MasalahReaksiKeluargaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MasalahReaksiKeluargaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_MasalahReaksiKeluargaKeyPressed

    private void AlternatifPelayananKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AlternatifPelayananKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_AlternatifPelayananKeyPressed

    private void DukunganPelayananKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DukunganPelayananKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DukunganPelayananKeyPressed

    private void MasalahResikoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MasalahResikoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MasalahResikoActionPerformed

    private void MasalahResikoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MasalahResikoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_MasalahResikoKeyPressed

    private void InformasiResikoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InformasiResikoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InformasiResikoActionPerformed

    private void InformasiResikoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_InformasiResikoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_InformasiResikoKeyPressed

    private void KdDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdDokterKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KdDokterKeyPressed

    private void BtnDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokterActionPerformed
        if (dokter == null || !dokter.isDisplayable()) {
            dokter = new DlgCariDokter(null, false);
            dokter.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dokter.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    if (dokter.getTable().getSelectedRow() != -1) {
                        KdDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 0).toString());
                        NmDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 1).toString());
                    }
                    BtnDokter.requestFocus();
                    dokter = null;
                }
            });
            dokter.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
            dokter.setLocationRelativeTo(internalFrame1);
        }
        if (dokter == null) {
            return;
        }
        dokter.isCek();
        if (dokter.isVisible()) {
            dokter.toFront();
            return;
        }
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnDokterActionPerformed

    private void BtnDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDokterKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnDokterKeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMPenilaianPasienTerminal dialog = new RMPenilaianPasienTerminal(new javax.swing.JFrame(), true);
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
    private widget.TextBox AlamatDihubungi;
    private widget.TextArea AlternatifPelayanan;
    private widget.ComboBox BimbinganRohani;
    private widget.Button BtnAll;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnDokter;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPetugas;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.CekBox ChkInput;
    private widget.CekBox ChkKejadian;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.ComboBox Detik;
    private widget.TextArea DukunganPelayanan;
    private widget.TextArea FaktorGejala;
    private widget.PanelBiasa FormInput;
    private widget.TextBox HubunganPasien;
    private widget.ComboBox InformasiResiko;
    private widget.ComboBox InginDihubungi;
    private widget.TextBox JK;
    private widget.ComboBox Jam;
    private widget.TextBox KdDokter;
    private widget.TextBox KdPetugas;
    private widget.ComboBox KegawatanPernafasan;
    private widget.ComboBox KehilanganTanusOtot;
    private widget.TextBox KetInginDihubungi;
    private widget.TextBox KetNyeri;
    private widget.TextBox KetSpiritual;
    private widget.Label LCount;
    private widget.editorpane LoadHTML;
    private widget.ComboBox MasalahReaksiKeluarga;
    private widget.ComboBox MasalahReaksiPasien;
    private widget.ComboBox MasalahResiko;
    private widget.ComboBox MasalahRespon;
    private widget.ComboBox Menit;
    private javax.swing.JMenuItem MnPenilaianPasienTerminal;
    private widget.TextBox NmDokter;
    private widget.TextBox NmPetugas;
    private widget.TextBox NoHP;
    private widget.ComboBox Nyeri;
    private javax.swing.JPanel PanelInput;
    private widget.ComboBox PelayananSpiritual;
    private widget.ComboBox PendampinganRohani;
    private widget.TextArea PerawatanLanjutan;
    private widget.ComboBox PerlambatanSirkulasi;
    private widget.ComboBox PerluDidoakan;
    private widget.ComboBox ReaksiKeluarga;
    private widget.ComboBox ReaksiPasien;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.Tanggal Tanggal;
    private widget.TextBox TglLahir;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel16;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel20;
    private widget.Label jLabel21;
    private widget.Label jLabel4;
    private widget.Label jLabel53;
    private widget.Label jLabel54;
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
    private widget.Label jLabel69;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ScrollPane scrollInput;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollPane2;
    private widget.ScrollPane scrollPane3;
    private widget.ScrollPane scrollPane4;
    private widget.Table tbObat;
    // End of variables declaration//GEN-END:variables
    
    private void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            if(TCari.getText().toString().trim().equals("")){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tgl_lahir,penilaian_awal_pasien_terminal.tanggal,"+
                    "penilaian_awal_pasien_terminal.kegawatan_pernafasan,penilaian_awal_pasien_terminal.kehilangan_tonus_otot,penilaian_awal_pasien_terminal.nyeri,penilaian_awal_pasien_terminal.ket_nyeri,"+
                    "penilaian_awal_pasien_terminal.perlambatan_sirkulasi,penilaian_awal_pasien_terminal.faktor_meningkatkan_gejala_fisik,penilaian_awal_pasien_terminal.masalah_keperawatan,penilaian_awal_pasien_terminal.pelayanan_spiritual,"+
                    "penilaian_awal_pasien_terminal.ket_spiritual,penilaian_awal_pasien_terminal.perlu_didoakan,penilaian_awal_pasien_terminal.perlu_bimbingan_rohani,penilaian_awal_pasien_terminal.perlu_pendampingan_rohani,penilaian_awal_pasien_terminal.orang_ingin_dihubungi,"+
                    "penilaian_awal_pasien_terminal.ket_orang_ingin_dihubungi,penilaian_awal_pasien_terminal.hubungan_dengan_pasien,penilaian_awal_pasien_terminal.alamat_yang_dihubungi,penilaian_awal_pasien_terminal.nohp_yang_dihubungi,penilaian_awal_pasien_terminal.perawatan_lanjutan,penilaian_awal_pasien_terminal.assesmen_informasi_pasien,penilaian_awal_pasien_terminal.masalah_keperawatan_pasien,penilaian_awal_pasien_terminal.assesmen_informasi_keluarga,penilaian_awal_pasien_terminal.masalah_keperawatan_keluarga,penilaian_awal_pasien_terminal.dukungan_pelayanan,penilaian_awal_pasien_terminal.kebutuhan_altrnatif,penilaian_awal_pasien_terminal.assesmen_informasi_resiko,penilaian_awal_pasien_terminal.masalah_keperawatan_resiko,penilaian_awal_pasien_terminal.kd_dokter,dokter.nm_dokter,penilaian_awal_pasien_terminal.nip,petugas.nama "+
                    "from penilaian_awal_pasien_terminal inner join reg_periksa on penilaian_awal_pasien_terminal.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join dokter on penilaian_awal_pasien_terminal.kd_dokter=dokter.kd_dokter "+
                    "inner join petugas on penilaian_awal_pasien_terminal.nip=petugas.nip where "+
                    "penilaian_awal_pasien_terminal.tanggal between ? and ? order by penilaian_awal_pasien_terminal.tanggal");
            }else{
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tgl_lahir,penilaian_awal_pasien_terminal.tanggal,"+
                    "penilaian_awal_pasien_terminal.kegawatan_pernafasan,penilaian_awal_pasien_terminal.kehilangan_tonus_otot,penilaian_awal_pasien_terminal.nyeri,penilaian_awal_pasien_terminal.ket_nyeri,"+
                    "penilaian_awal_pasien_terminal.perlambatan_sirkulasi,penilaian_awal_pasien_terminal.faktor_meningkatkan_gejala_fisik,penilaian_awal_pasien_terminal.masalah_keperawatan,penilaian_awal_pasien_terminal.pelayanan_spiritual,"+
                    "penilaian_awal_pasien_terminal.ket_spiritual,penilaian_awal_pasien_terminal.perlu_didoakan,penilaian_awal_pasien_terminal.perlu_bimbingan_rohani,penilaian_awal_pasien_terminal.perlu_pendampingan_rohani,penilaian_awal_pasien_terminal.orang_ingin_dihubungi,"+
                    "penilaian_awal_pasien_terminal.ket_orang_ingin_dihubungi,penilaian_awal_pasien_terminal.hubungan_dengan_pasien,penilaian_awal_pasien_terminal.alamat_yang_dihubungi,penilaian_awal_pasien_terminal.nohp_yang_dihubungi,penilaian_awal_pasien_terminal.perawatan_lanjutan,penilaian_awal_pasien_terminal.assesmen_informasi_pasien,penilaian_awal_pasien_terminal.masalah_keperawatan_pasien,penilaian_awal_pasien_terminal.assesmen_informasi_keluarga,penilaian_awal_pasien_terminal.masalah_keperawatan_keluarga,penilaian_awal_pasien_terminal.dukungan_pelayanan,penilaian_awal_pasien_terminal.kebutuhan_altrnatif,penilaian_awal_pasien_terminal.assesmen_informasi_resiko,penilaian_awal_pasien_terminal.masalah_keperawatan_resiko,penilaian_awal_pasien_terminal.kd_dokter,dokter.nm_dokter,penilaian_awal_pasien_terminal.nip,petugas.nama "+
                    "from penilaian_awal_pasien_terminal inner join reg_periksa on penilaian_awal_pasien_terminal.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join dokter on penilaian_awal_pasien_terminal.kd_dokter=dokter.kd_dokter "+
                    "inner join petugas on penilaian_awal_pasien_terminal.nip=petugas.nip where "+
                    "penilaian_awal_pasien_terminal.tanggal between ? and ? and (reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or penilaian_awal_pasien_terminal.nip like ? or petugas.nama like ?) "+
                    "order by penilaian_awal_pasien_terminal.tanggal ");
            }
                
            try {
                if(TCari.getText().toString().trim().equals("")){
                    ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                }else{
                    ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                }
                
                rs=ps.executeQuery();
                while(rs.next()){
                    tabMode.addRow(new Object[]{
                        rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getDate("tgl_lahir"),rs.getString("jk"),rs.getString("tanggal"),
                        rs.getString("kegawatan_pernafasan"),rs.getString("kehilangan_tonus_otot"),rs.getString("nyeri"),rs.getString("ket_nyeri"),rs.getString("perlambatan_sirkulasi"),rs.getString("faktor_meningkatkan_gejala_fisik"),
                        rs.getString("masalah_keperawatan"),rs.getString("pelayanan_spiritual"),rs.getString("ket_spiritual"),rs.getString("perlu_didoakan"),rs.getString("perlu_bimbingan_rohani"),rs.getString("perlu_pendampingan_rohani"),
                        rs.getString("orang_ingin_dihubungi"),rs.getString("ket_orang_ingin_dihubungi"),rs.getString("hubungan_dengan_pasien"),rs.getString("alamat_yang_dihubungi"),rs.getString("nohp_yang_dihubungi"),rs.getString("perawatan_lanjutan"),
                        rs.getString("assesmen_informasi_pasien"),rs.getString("masalah_keperawatan_pasien"),rs.getString("assesmen_informasi_keluarga"),rs.getString("masalah_keperawatan_keluarga"),rs.getString("dukungan_pelayanan"),rs.getString("kebutuhan_altrnatif"),
                        rs.getString("assesmen_informasi_resiko"),rs.getString("masalah_keperawatan_resiko"),rs.getString("nip"),rs.getString("nama"),rs.getString("kd_dokter"),rs.getString("nm_dokter")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabMode.getRowCount());
    }
    
    public void emptTeks() {
        Tanggal.setDate(new Date());
        KegawatanPernafasan.setSelectedIndex(0);
        KehilanganTanusOtot.setSelectedIndex(0);
        Nyeri.setSelectedIndex(0);
        KetNyeri.setText("");
        PerlambatanSirkulasi.setSelectedIndex(0);
        FaktorGejala.setText("");
        MasalahRespon.setSelectedIndex(0);
        PelayananSpiritual.setSelectedIndex(0);
        PerluDidoakan.setSelectedIndex(0);
        BimbinganRohani.setSelectedIndex(0);
        PendampinganRohani.setSelectedIndex(0);
        InginDihubungi.setSelectedIndex(0);
        KetInginDihubungi.setText("");
        HubunganPasien.setText("");
        AlamatDihubungi.setText("");
        NoHP.setText("");
        PerawatanLanjutan.setText("");
        ReaksiPasien.setSelectedIndex(0);
        MasalahReaksiPasien.setSelectedIndex(0);
        ReaksiKeluarga.setSelectedIndex(0);
        MasalahReaksiKeluarga.setSelectedIndex(0);
        AlternatifPelayanan.setText("");
        InformasiResiko.setSelectedIndex(0);
        MasalahResiko.setSelectedIndex(0);
        
        
        KegawatanPernafasan.requestFocus();
    } 

    private void getData() {
        if(tbObat.getSelectedRow()!= -1){
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()); 
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
            TglLahir.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString());
            JK.setText(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString());
            KegawatanPernafasan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString());
            KehilanganTanusOtot.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString());
            Nyeri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
            KetNyeri.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
            PerlambatanSirkulasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());
            FaktorGejala.setText(tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());
            MasalahRespon.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());
            PelayananSpiritual.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString());
            KetSpiritual.setText(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            PerluDidoakan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),15).toString());
            BimbinganRohani.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),16).toString());
            PendampinganRohani.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),17).toString());
            InginDihubungi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString());
            KetInginDihubungi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),19).toString());
            HubunganPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),20).toString());
            AlamatDihubungi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),21).toString());
            NoHP.setText(tbObat.getValueAt(tbObat.getSelectedRow(),22).toString());
            PerawatanLanjutan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),23).toString());
            ReaksiPasien.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),24).toString());
            MasalahReaksiPasien.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),25).toString());
            ReaksiKeluarga.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),26).toString());
            MasalahReaksiKeluarga.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),27).toString());
            DukunganPelayanan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),28).toString());
            AlternatifPelayanan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),29).toString());
            InformasiResiko.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),30).toString());
            MasalahResiko.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),31).toString());
            KdDokter.setText(tbObat.getValueAt(tbObat.getSelectedRow(),34).toString());
            NmDokter.setText(tbObat.getValueAt(tbObat.getSelectedRow(),35).toString());
            Jam.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString().substring(11,13));
            Menit.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString().substring(14,16));
            Detik.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString().substring(17,19));
            Valid.SetTgl(Tanggal,tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
        }
    }
    
    private void isRawat() {
        try {
            ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tgl_lahir,reg_periksa.tgl_registrasi "+
                    "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "where reg_periksa.no_rawat=?");
            try {
                ps.setString(1,TNoRw.getText());
                rs=ps.executeQuery();
                if(rs.next()){
                    TNoRM.setText(rs.getString("no_rkm_medis"));
                    DTPCari1.setDate(rs.getDate("tgl_registrasi"));
                    TPasien.setText(rs.getString("nm_pasien"));
                    JK.setText(rs.getString("jk"));
                    TglLahir.setText(rs.getString("tgl_lahir"));
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }
    }
    
    public void setNoRm(String norwt, Date tgl2) {
        TNoRw.setText(norwt);
        TCari.setText(norwt);
        DTPCari2.setDate(tgl2);
        isRawat();
        ChkInput.setSelected(true);
        isForm();
        runBackground(() ->tampil());
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            if(internalFrame1.getHeight()>798){
                ChkInput.setVisible(false);
                PanelInput.setPreferredSize(new Dimension(WIDTH,326));
                FormInput.setVisible(true);      
                ChkInput.setVisible(true);
            }else{
                ChkInput.setVisible(false);
                PanelInput.setPreferredSize(new Dimension(WIDTH,internalFrame1.getHeight()-175));
                FormInput.setVisible(true);      
                ChkInput.setVisible(true);
            }
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            FormInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
    public void isCek(){
        BtnSimpan.setEnabled(akses.getpenilaian_pasien_terminal());
        BtnHapus.setEnabled(akses.getpenilaian_pasien_terminal());
        BtnEdit.setEnabled(akses.getpenilaian_pasien_terminal());
        BtnPrint.setEnabled(akses.getpenilaian_pasien_terminal()); 
        if(akses.getjml2()>=1){
            KdPetugas.setEditable(false);
            BtnPetugas.setEnabled(false);
            KdPetugas.setText(akses.getkode());
            NmPetugas.setText(Sequel.CariPetugas(KdPetugas.getText()));
            if(NmPetugas.getText().equals("")){
                KdPetugas.setText("");
                JOptionPane.showMessageDialog(null,"User login bukan petugas...!!");
            }
        }            
    }

    private void jam(){
        ActionListener taskPerformer = new ActionListener(){
            private int nilai_jam;
            private int nilai_menit;
            private int nilai_detik;
            public void actionPerformed(ActionEvent e) {
                String nol_jam = "";
                String nol_menit = "";
                String nol_detik = "";
                
                Date now = Calendar.getInstance().getTime();

                // Mengambil nilaj JAM, MENIT, dan DETIK Sekarang
                if(ChkKejadian.isSelected()==true){
                    nilai_jam = now.getHours();
                    nilai_menit = now.getMinutes();
                    nilai_detik = now.getSeconds();
                }else if(ChkKejadian.isSelected()==false){
                    nilai_jam =Jam.getSelectedIndex();
                    nilai_menit =Menit.getSelectedIndex();
                    nilai_detik =Detik.getSelectedIndex();
                }

                // Jika nilai JAM lebih kecil dari 10 (hanya 1 digit)
                if (nilai_jam <= 9) {
                    // Tambahkan "0" didepannya
                    nol_jam = "0";
                }
                // Jika nilai MENIT lebih kecil dari 10 (hanya 1 digit)
                if (nilai_menit <= 9) {
                    // Tambahkan "0" didepannya
                    nol_menit = "0";
                }
                // Jika nilai DETIK lebih kecil dari 10 (hanya 1 digit)
                if (nilai_detik <= 9) {
                    // Tambahkan "0" didepannya
                    nol_detik = "0";
                }
                // Membuat String JAM, MENIT, DETIK
                String jam = nol_jam + Integer.toString(nilai_jam);
                String menit = nol_menit + Integer.toString(nilai_menit);
                String detik = nol_detik + Integer.toString(nilai_detik);
                // Menampilkan pada Layar
                //tampil_jam.setText("  " + jam + " : " + menit + " : " + detik + "  ");
                Jam.setSelectedItem(jam);
                Menit.setSelectedItem(menit);
                Detik.setSelectedItem(detik);
            }
        };
        // Timer
        new Timer(1240, taskPerformer).start();
    }

    private void ganti() {
        if(Sequel.mengedittf("penilaian_awal_pasien_terminal","no_rawat=?","no_rawat=?,tanggal=?,kegawatan_pernafasan=?,kehilangan_tonus_otot=?,nyeri=?,ket_nyeri=?,perlambatan_sirkulasi=?,faktor_meningkatkan_gejala_fisik=?,masalah_keperawatan=?,pelayanan_spiritual=?,"+
           "ket_spiritual=?,perlu_didoakan=?,perlu_bimbingan_rohani=?,perlu_pendampingan_rohani=?,orang_ingin_dihubungi=?,ket_orang_ingin_dihubungi=?,hubungan_dengan_pasien=?,alamat_yang_dihubungi=?,nohp_yang_dihubungi=?,perawatan_lanjutan=?,assesmen_informasi_pasien=?,"+
           "masalah_keperawatan_pasien=?,assesmen_informasi_keluarga=?,masalah_keperawatan_keluarga=?,dukungan_pelayanan=?,kebutuhan_altrnatif=?,assesmen_informasi_resiko=?,masalah_keperawatan_resiko=?,kd_dokter=?,nip=?",31,new String[]{
            TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),
                KegawatanPernafasan.getSelectedItem().toString(),KehilanganTanusOtot.getSelectedItem().toString(),Nyeri.getSelectedItem().toString(),KetNyeri.getText(),PerlambatanSirkulasi.getSelectedItem().toString(),
                FaktorGejala.getText(),MasalahRespon.getSelectedItem().toString(),PelayananSpiritual.getSelectedItem().toString(),KetSpiritual.getText(),PerluDidoakan.getSelectedItem().toString(),BimbinganRohani.getSelectedItem().toString(), 
                PendampinganRohani.getSelectedItem().toString(),InginDihubungi.getSelectedItem().toString(),KetInginDihubungi.getText(),HubunganPasien.getText(),AlamatDihubungi.getText(),NoHP.getText(),PerawatanLanjutan.getText(),
                ReaksiPasien.getSelectedItem().toString(),MasalahReaksiPasien.getSelectedItem().toString(),ReaksiKeluarga.getSelectedItem().toString(),MasalahReaksiKeluarga.getSelectedItem().toString(),DukunganPelayanan.getText(),AlternatifPelayanan.getText(),InformasiResiko.getSelectedItem().toString(),MasalahResiko.getSelectedItem().toString(),KdDokter.getText(),KdPetugas.getText(),tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
        })==true){
            tbObat.setValueAt(TNoRw.getText(),tbObat.getSelectedRow(),0);
            tbObat.setValueAt(TNoRM.getText(),tbObat.getSelectedRow(),1);
            tbObat.setValueAt(TPasien.getText(),tbObat.getSelectedRow(),2);
            tbObat.setValueAt(TglLahir.getText(),tbObat.getSelectedRow(),3);
            tbObat.setValueAt(JK.getText(),tbObat.getSelectedRow(),4);
            tbObat.setValueAt(Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),tbObat.getSelectedRow(),5);
            tbObat.setValueAt(KegawatanPernafasan.getSelectedItem().toString(),tbObat.getSelectedRow(),6);
            tbObat.setValueAt(KehilanganTanusOtot.getSelectedItem().toString(),tbObat.getSelectedRow(),7);
            tbObat.setValueAt(Nyeri.getSelectedItem().toString(),tbObat.getSelectedRow(),8);
            tbObat.setValueAt(KetNyeri.getText(),tbObat.getSelectedRow(),9);
            tbObat.setValueAt(PerlambatanSirkulasi.getSelectedItem().toString(),tbObat.getSelectedRow(),10);
            tbObat.setValueAt(FaktorGejala.getText(),tbObat.getSelectedRow(),11);
            tbObat.setValueAt(MasalahRespon.getSelectedItem().toString(),tbObat.getSelectedRow(),12);
            tbObat.setValueAt(PelayananSpiritual.getSelectedItem().toString(),tbObat.getSelectedRow(),13);
            tbObat.setValueAt(KetSpiritual.getText(),tbObat.getSelectedRow(),14);
            tbObat.setValueAt(PerluDidoakan.getSelectedItem().toString(),tbObat.getSelectedRow(),15);
            tbObat.setValueAt(BimbinganRohani.getSelectedItem().toString(),tbObat.getSelectedRow(),16);
            tbObat.setValueAt(PendampinganRohani.getSelectedItem().toString(),tbObat.getSelectedRow(),17);
            tbObat.setValueAt(InginDihubungi.getSelectedItem().toString(),tbObat.getSelectedRow(),18);
            tbObat.setValueAt(KetInginDihubungi.getText(),tbObat.getSelectedRow(),19);
            tbObat.setValueAt(HubunganPasien.getText(),tbObat.getSelectedRow(),20);
            tbObat.setValueAt(AlamatDihubungi.getText(),tbObat.getSelectedRow(),21);
            tbObat.setValueAt(NoHP.getText().toString(),tbObat.getSelectedRow(),22);
            tbObat.setValueAt(PerawatanLanjutan.getText(),tbObat.getSelectedRow(),23);
            tbObat.setValueAt(ReaksiPasien.getSelectedItem().toString(),tbObat.getSelectedRow(),24);
            tbObat.setValueAt(MasalahReaksiPasien.getSelectedItem().toString(),tbObat.getSelectedRow(),25);
            tbObat.setValueAt(ReaksiKeluarga.getSelectedItem().toString(),tbObat.getSelectedRow(),26);
            tbObat.setValueAt(MasalahReaksiKeluarga.getSelectedItem().toString(),tbObat.getSelectedRow(),27);
            tbObat.setValueAt(DukunganPelayanan.getText(),tbObat.getSelectedRow(),28);
            tbObat.setValueAt(AlternatifPelayanan.getText(),tbObat.getSelectedRow(),29);
            tbObat.setValueAt(InformasiResiko.getSelectedItem().toString(),tbObat.getSelectedRow(),30);
            tbObat.setValueAt(MasalahResiko.getSelectedItem().toString(),tbObat.getSelectedRow(),31);
            tbObat.setValueAt(KdPetugas.getText(),tbObat.getSelectedRow(),32);
            tbObat.setValueAt(NmPetugas.getText(),tbObat.getSelectedRow(),33);
            tbObat.setValueAt(KdDokter.getText(),tbObat.getSelectedRow(),34);
            tbObat.setValueAt(NmDokter.getText(),tbObat.getSelectedRow(),35);
            emptTeks();
        }
    }

    private void hapus() {
        if(Sequel.queryu2tf("delete from penilaian_awal_pasien_terminal where no_rawat=?",1,new String[]{
            tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
        })==true){
            tabMode.removeRow(tbObat.getSelectedRow());
            LCount.setText(""+tabMode.getRowCount());
            emptTeks();
        }else{
            JOptionPane.showMessageDialog(null,"Gagal menghapus..!!");
        }
    }
    
    private void runBackground(Runnable task) {
        if (ceksukses) return;
        if (executor.isShutdown() || executor.isTerminated()) return;
        if (!isDisplayable()) return;

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
