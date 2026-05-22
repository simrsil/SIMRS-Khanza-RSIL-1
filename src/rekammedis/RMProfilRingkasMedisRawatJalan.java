/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgLhtBiaya.java
 *
 * Created on 12 Jul 10, 16:21:34
 */

package rekammedis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.WarnaTable;
import fungsi.akses;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.HyperlinkEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import simrskhanza.DlgCariPasien;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfWriter;
import java.util.Base64;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import javax.swing.SwingUtilities;
import org.apache.hc.core5.http.io.entity.StringEntity;



/**
 *
 * @author windiarto
 */
public final class RMProfilRingkasMedisRawatJalan extends javax.swing.JDialog {    
    private validasi Valid=new validasi();    
    private final sekuel Sequel=new sekuel();
    private final DefaultTableModel tabModeRegistrasi;
    private PreparedStatement ps,ps2;
    private ResultSet rs,rs2;
    private Connection koneksi=koneksiDB.condb();
    private int i=0;
    private String kddpjp="",dpjp="",poli="",obat="",diagnosa="";
    private StringBuilder htmlContent;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean ceksukses = false;

    /** Creates new form DlgLhtBiaya
     * @param parent
     * @param modal */
    public RMProfilRingkasMedisRawatJalan(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(885,674);
        WindowPhrase.setSize(320,100);
        WindowURLSertisign.setSize(570,100);
        
        tabModeRegistrasi=new DefaultTableModel(null,new Object[]{
                "No.","No.Rawat","Tanggal","Jam","Kd.Dokter","Dokter Dituju/DPJP","Umur","Poliklinik/Kamar","Jenis Bayar"
            }){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
       
        NoRM.setDocument(new batasInput((byte)20).getKata(NoRM));
        NoRawat.setDocument(new batasInput((byte)20).getKata(NoRawat));
        
        HTMLEditorKit kit = new HTMLEditorKit();
        LoadHTMLSOAPI.setEditorKit(kit);
      
        //----------------------------------------------------------------------
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule(".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}.isi a{text-decoration:none;color:#8b9b95;padding:0 0 0 0px;font-family: Tahoma;font-size: 8.5px;border: white;}");
        Document doc = kit.createDefaultDocument();
        
        LoadHTMLSOAPI.setDocument(doc);
        LoadHTMLSOAPI.setEditable(false);
        LoadHTMLSOAPI.addHyperlinkListener(e -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                Desktop desktop = Desktop.getDesktop();
                try {
                   desktop.browse(e.getURL().toURI());
                } catch (Exception ex) {
                  ex.printStackTrace();
                }
            }
        });
    }    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        Pekerjaan = new widget.TextBox();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        MnGeneratePDF = new javax.swing.JMenuItem();
        MnGeneratePDFESign = new javax.swing.JMenuItem();
        MnGeneratePDFSertiSign = new javax.swing.JMenuItem();
        WindowPhrase = new javax.swing.JDialog();
        internalFrame8 = new widget.InternalFrame();
        jLabel42 = new widget.Label();
        panelisi5 = new widget.panelisi();
        BtnClosePhrase = new widget.Button();
        BtnSimpanTandaTangan = new widget.Button();
        jLabel39 = new widget.Label();
        Phrase = new widget.PasswordBox();
        ChkTampilPhrase = new widget.CekBox();
        Tanggal = new widget.Tanggal();
        WindowURLSertisign = new javax.swing.JDialog();
        internalFrame9 = new widget.InternalFrame();
        jLabel43 = new widget.Label();
        panelisi6 = new widget.panelisi();
        BtnCloseUrl = new widget.Button();
        BtnBukaURL = new widget.Button();
        jLabel40 = new widget.Label();
        URLSertisign = new widget.TextBox();
        BtnDownloadFile = new widget.Button();
        BtnDownloadBukaFile = new widget.Button();
        internalFrame1 = new widget.InternalFrame();
        panelGlass5 = new widget.panelisi();
        R1 = new widget.RadioButton();
        R2 = new widget.RadioButton();
        R3 = new widget.RadioButton();
        Tgl1 = new widget.Tanggal();
        label18 = new widget.Label();
        Tgl2 = new widget.Tanggal();
        R4 = new widget.RadioButton();
        NoRawat = new widget.TextBox();
        BtnCari1 = new widget.Button();
        label19 = new widget.Label();
        BtnPrint = new widget.Button();
        BtnKeluar = new widget.Button();
        TabRawat = new javax.swing.JTabbedPane();
        Scroll2 = new widget.ScrollPane();
        LoadHTMLSOAPI = new widget.editorpane();
        PanelInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        FormInput = new widget.panelisi();
        label17 = new widget.Label();
        NoRM = new widget.TextBox();
        NmPasien = new widget.TextBox();
        BtnPasien = new widget.Button();
        label20 = new widget.Label();
        Jk = new widget.TextBox();
        label21 = new widget.Label();
        TempatLahir = new widget.TextBox();
        label22 = new widget.Label();
        Alamat = new widget.TextBox();
        label23 = new widget.Label();
        GD = new widget.TextBox();
        label24 = new widget.Label();
        IbuKandung = new widget.TextBox();
        TanggalLahir = new widget.TextBox();
        label25 = new widget.Label();
        Agama = new widget.TextBox();
        StatusNikah = new widget.TextBox();
        label26 = new widget.Label();
        Pendidikan = new widget.TextBox();
        label27 = new widget.Label();
        label28 = new widget.Label();
        Bahasa = new widget.TextBox();
        label29 = new widget.Label();
        CacatFisik = new widget.TextBox();

        Pekerjaan.setEditable(false);
        Pekerjaan.setName("Pekerjaan"); // NOI18N
        Pekerjaan.setPreferredSize(new java.awt.Dimension(100, 23));

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnGeneratePDF.setBackground(new java.awt.Color(255, 255, 254));
        MnGeneratePDF.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnGeneratePDF.setForeground(new java.awt.Color(50, 50, 50));
        MnGeneratePDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnGeneratePDF.setText("Jadikan File PDF");
        MnGeneratePDF.setName("MnGeneratePDF"); // NOI18N
        MnGeneratePDF.setPreferredSize(new java.awt.Dimension(220, 26));
        MnGeneratePDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnGeneratePDFActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnGeneratePDF);

        MnGeneratePDFESign.setBackground(new java.awt.Color(255, 255, 254));
        MnGeneratePDFESign.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnGeneratePDFESign.setForeground(new java.awt.Color(50, 50, 50));
        MnGeneratePDFESign.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnGeneratePDFESign.setText("Jadikan File PDF E-Sign");
        MnGeneratePDFESign.setName("MnGeneratePDFESign"); // NOI18N
        MnGeneratePDFESign.setPreferredSize(new java.awt.Dimension(220, 26));
        MnGeneratePDFESign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnGeneratePDFESignActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnGeneratePDFESign);

        MnGeneratePDFSertiSign.setBackground(new java.awt.Color(255, 255, 254));
        MnGeneratePDFSertiSign.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnGeneratePDFSertiSign.setForeground(new java.awt.Color(50, 50, 50));
        MnGeneratePDFSertiSign.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnGeneratePDFSertiSign.setText("Jadikan File PDF SertiSign");
        MnGeneratePDFSertiSign.setName("MnGeneratePDFSertiSign"); // NOI18N
        MnGeneratePDFSertiSign.setPreferredSize(new java.awt.Dimension(220, 26));
        MnGeneratePDFSertiSign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnGeneratePDFSertiSignActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnGeneratePDFSertiSign);

        WindowPhrase.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowPhrase.setModal(true);
        WindowPhrase.setName("WindowPhrase"); // NOI18N
        WindowPhrase.setUndecorated(true);
        WindowPhrase.setResizable(false);

        internalFrame8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ E-Sign / Tanda Tangan Elektronik ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame8.setName("internalFrame8"); // NOI18N
        internalFrame8.setLayout(new java.awt.BorderLayout());

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel42.setText("%");
        jLabel42.setName("jLabel42"); // NOI18N
        internalFrame8.add(jLabel42, java.awt.BorderLayout.CENTER);

        panelisi5.setName("panelisi5"); // NOI18N
        panelisi5.setPreferredSize(new java.awt.Dimension(100, 44));
        panelisi5.setLayout(null);

        BtnClosePhrase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/cross.png"))); // NOI18N
        BtnClosePhrase.setMnemonic('U');
        BtnClosePhrase.setText("Batal");
        BtnClosePhrase.setToolTipText("Alt+U");
        BtnClosePhrase.setName("BtnClosePhrase"); // NOI18N
        BtnClosePhrase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnClosePhraseActionPerformed(evt);
            }
        });
        panelisi5.add(BtnClosePhrase);
        BtnClosePhrase.setBounds(200, 40, 100, 30);

        BtnSimpanTandaTangan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpanTandaTangan.setMnemonic('S');
        BtnSimpanTandaTangan.setText("Simpan");
        BtnSimpanTandaTangan.setToolTipText("Alt+S");
        BtnSimpanTandaTangan.setName("BtnSimpanTandaTangan"); // NOI18N
        BtnSimpanTandaTangan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanTandaTanganActionPerformed(evt);
            }
        });
        panelisi5.add(BtnSimpanTandaTangan);
        BtnSimpanTandaTangan.setBounds(10, 40, 100, 30);

        jLabel39.setText("Masukkan Passphrase :");
        jLabel39.setName("jLabel39"); // NOI18N
        panelisi5.add(jLabel39);
        jLabel39.setBounds(0, 10, 125, 23);

        Phrase.setForeground(new java.awt.Color(0, 0, 0));
        Phrase.setName("Phrase"); // NOI18N
        Phrase.setOpaque(true);
        panelisi5.add(Phrase);
        Phrase.setBounds(129, 10, 146, 23);

        ChkTampilPhrase.setBorder(null);
        ChkTampilPhrase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/matatutup.png"))); // NOI18N
        ChkTampilPhrase.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkTampilPhrase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkTampilPhrase.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkTampilPhrase.setIconTextGap(1);
        ChkTampilPhrase.setName("ChkTampilPhrase"); // NOI18N
        ChkTampilPhrase.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/matabuka.png"))); // NOI18N
        ChkTampilPhrase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkTampilPhraseActionPerformed(evt);
            }
        });
        panelisi5.add(ChkTampilPhrase);
        ChkTampilPhrase.setBounds(275, 10, 23, 23);

        internalFrame8.add(panelisi5, java.awt.BorderLayout.CENTER);

        WindowPhrase.getContentPane().add(internalFrame8, java.awt.BorderLayout.CENTER);

        Tanggal.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "02-04-2026 09:29:18" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);

        WindowURLSertisign.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowURLSertisign.setModal(true);
        WindowURLSertisign.setName("WindowURLSertisign"); // NOI18N
        WindowURLSertisign.setUndecorated(true);
        WindowURLSertisign.setResizable(false);

        internalFrame9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ URL File Hasil Tanda Tangan Sertisign ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame9.setName("internalFrame9"); // NOI18N
        internalFrame9.setLayout(new java.awt.BorderLayout());

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel43.setText("%");
        jLabel43.setName("jLabel43"); // NOI18N
        internalFrame9.add(jLabel43, java.awt.BorderLayout.CENTER);

        panelisi6.setName("panelisi6"); // NOI18N
        panelisi6.setPreferredSize(new java.awt.Dimension(100, 44));
        panelisi6.setLayout(null);

        BtnCloseUrl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/cross.png"))); // NOI18N
        BtnCloseUrl.setMnemonic('T');
        BtnCloseUrl.setText("Tutup");
        BtnCloseUrl.setToolTipText("Alt+T");
        BtnCloseUrl.setName("BtnCloseUrl"); // NOI18N
        BtnCloseUrl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCloseUrlActionPerformed(evt);
            }
        });
        panelisi6.add(BtnCloseUrl);
        BtnCloseUrl.setBounds(450, 40, 100, 30);

        BtnBukaURL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnBukaURL.setMnemonic('B');
        BtnBukaURL.setText("Buka URL");
        BtnBukaURL.setToolTipText("Alt+B");
        BtnBukaURL.setName("BtnBukaURL"); // NOI18N
        BtnBukaURL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBukaURLActionPerformed(evt);
            }
        });
        panelisi6.add(BtnBukaURL);
        BtnBukaURL.setBounds(10, 40, 105, 30);

        jLabel40.setText("URL :");
        jLabel40.setName("jLabel40"); // NOI18N
        panelisi6.add(jLabel40);
        jLabel40.setBounds(0, 10, 40, 23);

        URLSertisign.setEditable(false);
        URLSertisign.setHighlighter(null);
        URLSertisign.setName("URLSertisign"); // NOI18N
        panelisi6.add(URLSertisign);
        URLSertisign.setBounds(44, 10, 505, 23);

        BtnDownloadFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnDownloadFile.setMnemonic('D');
        BtnDownloadFile.setText("Download File");
        BtnDownloadFile.setToolTipText("Alt+D");
        BtnDownloadFile.setName("BtnDownloadFile"); // NOI18N
        BtnDownloadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDownloadFileActionPerformed(evt);
            }
        });
        panelisi6.add(BtnDownloadFile);
        BtnDownloadFile.setBounds(125, 40, 130, 30);

        BtnDownloadBukaFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/preview-16x16.png"))); // NOI18N
        BtnDownloadBukaFile.setMnemonic('F');
        BtnDownloadBukaFile.setText("Download & Buka File");
        BtnDownloadBukaFile.setToolTipText("Alt+F");
        BtnDownloadBukaFile.setName("BtnDownloadBukaFile"); // NOI18N
        BtnDownloadBukaFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDownloadBukaFileActionPerformed(evt);
            }
        });
        panelisi6.add(BtnDownloadBukaFile);
        BtnDownloadBukaFile.setBounds(265, 40, 175, 30);

        internalFrame9.add(panelisi6, java.awt.BorderLayout.CENTER);

        WindowURLSertisign.getContentPane().add(internalFrame9, java.awt.BorderLayout.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Profil Ringkas Medis Pasien ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass5.setName("panelGlass5"); // NOI18N
        panelGlass5.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        R1.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R1);
        R1.setSelected(true);
        R1.setText("5 Riwayat Terakhir");
        R1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R1.setName("R1"); // NOI18N
        R1.setPreferredSize(new java.awt.Dimension(120, 23));
        panelGlass5.add(R1);

        R2.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R2);
        R2.setText("Semua Riwayat");
        R2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R2.setName("R2"); // NOI18N
        R2.setPreferredSize(new java.awt.Dimension(104, 23));
        panelGlass5.add(R2);

        R3.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R3);
        R3.setText("Tanggal :");
        R3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R3.setName("R3"); // NOI18N
        R3.setPreferredSize(new java.awt.Dimension(75, 23));
        panelGlass5.add(R3);

        Tgl1.setDisplayFormat("dd-MM-yyyy");
        Tgl1.setName("Tgl1"); // NOI18N
        Tgl1.setPreferredSize(new java.awt.Dimension(90, 23));
        Tgl1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tgl1KeyPressed(evt);
            }
        });
        panelGlass5.add(Tgl1);

        label18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label18.setText("s.d.");
        label18.setName("label18"); // NOI18N
        label18.setPreferredSize(new java.awt.Dimension(25, 23));
        panelGlass5.add(label18);

        Tgl2.setDisplayFormat("dd-MM-yyyy");
        Tgl2.setName("Tgl2"); // NOI18N
        Tgl2.setPreferredSize(new java.awt.Dimension(90, 23));
        Tgl2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tgl2KeyPressed(evt);
            }
        });
        panelGlass5.add(Tgl2);

        R4.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R4);
        R4.setText("Nomor :");
        R4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R4.setName("R4"); // NOI18N
        R4.setPreferredSize(new java.awt.Dimension(67, 23));
        panelGlass5.add(R4);

        NoRawat.setName("NoRawat"); // NOI18N
        NoRawat.setPreferredSize(new java.awt.Dimension(135, 23));
        NoRawat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoRawatKeyPressed(evt);
            }
        });
        panelGlass5.add(NoRawat);

        BtnCari1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari1.setMnemonic('2');
        BtnCari1.setToolTipText("Alt+2");
        BtnCari1.setName("BtnCari1"); // NOI18N
        BtnCari1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCari1ActionPerformed(evt);
            }
        });
        panelGlass5.add(BtnCari1);

        label19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label19.setName("label19"); // NOI18N
        label19.setPreferredSize(new java.awt.Dimension(15, 23));
        panelGlass5.add(label19);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        panelGlass5.add(BtnPrint);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(28, 23));
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
        panelGlass5.add(BtnKeluar);

        internalFrame1.add(panelGlass5, java.awt.BorderLayout.PAGE_END);

        TabRawat.setBackground(new java.awt.Color(255, 255, 254));
        TabRawat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(241, 246, 236)));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        Scroll2.setBorder(null);
        Scroll2.setName("Scroll2"); // NOI18N
        Scroll2.setOpaque(true);

        LoadHTMLSOAPI.setBorder(null);
        LoadHTMLSOAPI.setName("LoadHTMLSOAPI"); // NOI18N
        Scroll2.setViewportView(LoadHTMLSOAPI);

        TabRawat.addTab("Profil Ringkas Pasien", Scroll2);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        PanelInput.setBackground(new java.awt.Color(255, 255, 255));
        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('M');
        ChkInput.setSelected(true);
        ChkInput.setText(".: Tampilkan/Sembunyikan Data Pasien");
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

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 104));
        FormInput.setLayout(null);

        label17.setText("Pasien :");
        label17.setName("label17"); // NOI18N
        label17.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label17);
        label17.setBounds(5, 10, 55, 23);

        NoRM.setName("NoRM"); // NOI18N
        NoRM.setPreferredSize(new java.awt.Dimension(100, 23));
        NoRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoRMKeyPressed(evt);
            }
        });
        FormInput.add(NoRM);
        NoRM.setBounds(64, 10, 100, 23);

        NmPasien.setEditable(false);
        NmPasien.setName("NmPasien"); // NOI18N
        NmPasien.setPreferredSize(new java.awt.Dimension(220, 23));
        FormInput.add(NmPasien);
        NmPasien.setBounds(167, 10, 220, 23);

        BtnPasien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPasien.setMnemonic('3');
        BtnPasien.setToolTipText("Alt+3");
        BtnPasien.setName("BtnPasien"); // NOI18N
        BtnPasien.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPasienActionPerformed(evt);
            }
        });
        BtnPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPasienKeyPressed(evt);
            }
        });
        FormInput.add(BtnPasien);
        BtnPasien.setBounds(390, 10, 28, 23);

        label20.setText("J.K. :");
        label20.setName("label20"); // NOI18N
        label20.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label20);
        label20.setBounds(436, 10, 30, 23);

        Jk.setEditable(false);
        Jk.setName("Jk"); // NOI18N
        Jk.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Jk);
        Jk.setBounds(470, 10, 40, 23);

        label21.setText("Tempat & Tgl.Lahir :");
        label21.setName("label21"); // NOI18N
        label21.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label21);
        label21.setBounds(523, 10, 110, 23);

        TempatLahir.setEditable(false);
        TempatLahir.setName("TempatLahir"); // NOI18N
        TempatLahir.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(TempatLahir);
        TempatLahir.setBounds(637, 10, 140, 23);

        label22.setText("Alamat :");
        label22.setName("label22"); // NOI18N
        label22.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label22);
        label22.setBounds(5, 40, 55, 23);

        Alamat.setEditable(false);
        Alamat.setName("Alamat"); // NOI18N
        Alamat.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Alamat);
        Alamat.setBounds(64, 40, 354, 23);

        label23.setText("G.D. :");
        label23.setName("label23"); // NOI18N
        label23.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label23);
        label23.setBounds(436, 40, 30, 23);

        GD.setEditable(false);
        GD.setName("GD"); // NOI18N
        GD.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(GD);
        GD.setBounds(470, 40, 40, 23);

        label24.setText("Nama Ibu Kandung :");
        label24.setName("label24"); // NOI18N
        label24.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label24);
        label24.setBounds(523, 40, 110, 23);

        IbuKandung.setEditable(false);
        IbuKandung.setName("IbuKandung"); // NOI18N
        IbuKandung.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(IbuKandung);
        IbuKandung.setBounds(637, 40, 225, 23);

        TanggalLahir.setEditable(false);
        TanggalLahir.setName("TanggalLahir"); // NOI18N
        TanggalLahir.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(TanggalLahir);
        TanggalLahir.setBounds(779, 10, 83, 23);

        label25.setText("Agama :");
        label25.setName("label25"); // NOI18N
        label25.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label25);
        label25.setBounds(5, 70, 55, 23);

        Agama.setEditable(false);
        Agama.setName("Agama"); // NOI18N
        Agama.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Agama);
        Agama.setBounds(64, 70, 100, 23);

        StatusNikah.setEditable(false);
        StatusNikah.setName("StatusNikah"); // NOI18N
        StatusNikah.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(StatusNikah);
        StatusNikah.setBounds(245, 70, 100, 23);

        label26.setText("Stts.Nikah :");
        label26.setName("label26"); // NOI18N
        label26.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label26);
        label26.setBounds(176, 70, 65, 23);

        Pendidikan.setEditable(false);
        Pendidikan.setName("Pendidikan"); // NOI18N
        Pendidikan.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Pendidikan);
        Pendidikan.setBounds(429, 70, 80, 23);

        label27.setText("Pendidikan :");
        label27.setName("label27"); // NOI18N
        label27.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label27);
        label27.setBounds(355, 70, 70, 23);

        label28.setText("Bahasa :");
        label28.setName("label28"); // NOI18N
        label28.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label28);
        label28.setBounds(520, 70, 50, 23);

        Bahasa.setEditable(false);
        Bahasa.setName("Bahasa"); // NOI18N
        Bahasa.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Bahasa);
        Bahasa.setBounds(574, 70, 100, 23);

        label29.setText("Cacat Fisik :");
        label29.setName("label29"); // NOI18N
        label29.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label29);
        label29.setBounds(683, 70, 70, 23);

        CacatFisik.setEditable(false);
        CacatFisik.setName("CacatFisik"); // NOI18N
        CacatFisik.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(CacatFisik);
        CacatFisik.setBounds(757, 70, 105, 23);

        PanelInput.add(FormInput, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,Tgl1,NoRM);}
}//GEN-LAST:event_BtnKeluarKeyPressed

private void NoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoRMKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isPasien();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            isPasien();
            BtnKeluar.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnPasienActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isPasien();
            BtnPrint.requestFocus();
        }
}//GEN-LAST:event_NoRMKeyPressed

private void BtnPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPasienActionPerformed
    if(akses.getpasien()==true){
        DlgCariPasien pasien=new DlgCariPasien(null,true);
        pasien.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(pasien.getTable().getSelectedRow()!= -1){                   
                    NoRM.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),0).toString());
                    NmPasien.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),1).toString());
                    Jk.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),3).toString());
                    TempatLahir.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),4).toString());
                    TanggalLahir.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),5).toString());
                    IbuKandung.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),6).toString());
                    Alamat.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),7).toString());
                    GD.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),8).toString());
                    StatusNikah.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),10).toString());
                    Agama.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),11).toString());
                    Pendidikan.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),15).toString());
                    Bahasa.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),26).toString());
                    CacatFisik.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),32).toString());
                }    
                NoRM.requestFocus();
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        pasien.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    pasien.dispose();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        pasien.isCek();
        pasien.emptTeks();
        pasien.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        pasien.setLocationRelativeTo(internalFrame1);
        pasien.setVisible(true);
    }   
}//GEN-LAST:event_BtnPasienActionPerformed

private void BtnPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPasienKeyPressed
    //Valid.pindah(evt,Tgl2,TKd);
}//GEN-LAST:event_BtnPasienKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        if(NoRM.getText().trim().equals("")||NmPasien.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Pasien masih kosong...!!!");
        }else{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            switch (TabRawat.getSelectedIndex()) {
//                case 0:
//                    if(tabModeRegistrasi.getRowCount()==0){
//                        JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
//                    }else if(tabModeRegistrasi.getRowCount()!=0){
//                        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));        
//                        Sequel.queryu("delete from temporary_resume");
//
//                        for(int i=0;i<tabModeRegistrasi.getRowCount();i++){  
//                            Sequel.menyimpan("temporary_resume","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",38,new String[]{
//                                "0",tabModeRegistrasi.getValueAt(i,0).toString(),tabModeRegistrasi.getValueAt(i,1).toString(),tabModeRegistrasi.getValueAt(i,2).toString(),
//                                tabModeRegistrasi.getValueAt(i,3).toString(),tabModeRegistrasi.getValueAt(i,4).toString(),tabModeRegistrasi.getValueAt(i,5).toString(),
//                                tabModeRegistrasi.getValueAt(i,6).toString(),tabModeRegistrasi.getValueAt(i,7).toString(),tabModeRegistrasi.getValueAt(i,8).toString(),
//                                "","","","","","","","","","","","","","","","","","","","","","","","","","","","",""
//                            });
//                        }
//
//                        Map<String, Object> param = new HashMap<>();  
//                            param.put("namars",akses.getnamars());
//                            param.put("alamatrs",akses.getalamatrs());
//                            param.put("kotars",akses.getkabupatenrs());
//                            param.put("propinsirs",akses.getpropinsirs());
//                            param.put("kontakrs",akses.getkontakrs());
//                            param.put("emailrs",akses.getemailrs());   
//                            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
//                        Valid.MyReport2("rptRiwayatRegistrasi.jasper","report","::[ Riwayat Registrasi ]::",param);
//                        this.setCursor(Cursor.getDefaultCursor());
//                    }
//                    break;
                case 0:
                    panggilLaporan(LoadHTMLSOAPI.getText()); 
                    break;
                default:
                    break;
            }
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_BtnPrintActionPerformed

    private void Tgl1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tgl1KeyPressed
        Valid.pindah(evt, BtnKeluar, Tgl2);
    }//GEN-LAST:event_Tgl1KeyPressed

    private void Tgl2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tgl2KeyPressed
        Valid.pindah(evt, Tgl1,NoRM);
    }//GEN-LAST:event_Tgl2KeyPressed

    private void BtnCari1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCari1ActionPerformed
        if (NoRM.getText().trim().isEmpty() || NmPasien.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pasien masih kosong...!!!");
            return;
        }

        switch (TabRawat.getSelectedIndex()) {
            case 0:
                runBackground(() -> tampilSoapi());
                break;
            default:
                break;
        }
    }//GEN-LAST:event_BtnCari1ActionPerformed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void NoRawatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoRawatKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCari1ActionPerformed(null);
        }
    }//GEN-LAST:event_NoRawatKeyPressed

    private void MnGeneratePDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnGeneratePDFActionPerformed
        
    }//GEN-LAST:event_MnGeneratePDFActionPerformed

    private void MnGeneratePDFESignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnGeneratePDFESignActionPerformed
        R4.setSelected(true);
        if(NoRawat.getText().equals("")){
            Valid.textKosong(Phrase,"No.Rawat");
        }else{
            WindowPhrase.setAlwaysOnTop(true);
            WindowPhrase.setLocationRelativeTo(internalFrame1);
            WindowPhrase.setVisible(true);
        } 
    }//GEN-LAST:event_MnGeneratePDFESignActionPerformed

    private void BtnClosePhraseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnClosePhraseActionPerformed
        Phrase.setText("");
        WindowPhrase.dispose();
    }//GEN-LAST:event_BtnClosePhraseActionPerformed

    private void BtnSimpanTandaTanganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanTandaTanganActionPerformed
        
    }//GEN-LAST:event_BtnSimpanTandaTanganActionPerformed

    private void MnGeneratePDFSertiSignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnGeneratePDFSertiSignActionPerformed
        
    }//GEN-LAST:event_MnGeneratePDFSertiSignActionPerformed

    private void BtnCloseUrlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseUrlActionPerformed
        URLSertisign.setText("");
        WindowURLSertisign.dispose();
    }//GEN-LAST:event_BtnCloseUrlActionPerformed

    private void BtnBukaURLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBukaURLActionPerformed
        try {
            Desktop.getDesktop().browse(new URI(URLSertisign.getText()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane,"File belum tersedia, silahkan tunggu beberapa saat lagi..!!");
            System.out.println("Notifikasi : " + e);
        }
    }//GEN-LAST:event_BtnBukaURLActionPerformed

    private void BtnDownloadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDownloadFileActionPerformed
        try {
            URL url = new URL(URLSertisign.getText());
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream("RPP"+NoRawat.getText().trim().replaceAll("/","")+".pdf");
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
            readableByteChannel.close();
            System.out.println("Download Selesai : " + "RPP"+NoRawat.getText().trim().replaceAll("/","")+".pdf");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(rootPane,"File belum tersedia, silahkan tunggu & ulangi beberapa saat lagi..!!");
            System.out.println("Notifikasi : " + e);
        }
    }//GEN-LAST:event_BtnDownloadFileActionPerformed

    private void BtnDownloadBukaFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDownloadBukaFileActionPerformed
        try {
            URL url = new URL(URLSertisign.getText());
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream("RPP"+NoRawat.getText().trim().replaceAll("/","")+".pdf");
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
            readableByteChannel.close();
            System.out.println("Download Selesai : " + "RPP"+NoRawat.getText().trim().replaceAll("/","")+".pdf");
            Desktop.getDesktop().browse(new File("RPP"+NoRawat.getText().trim().replaceAll("/","")+".pdf").toURI());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(rootPane,"File belum tersedia, silahkan tunggu & ulangi beberapa saat lagi..!!");
            System.out.println("Notifikasi : " + e);
        }
    }//GEN-LAST:event_BtnDownloadBukaFileActionPerformed

    private void ChkTampilPhraseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkTampilPhraseActionPerformed
        if(ChkTampilPhrase.isSelected()==true){
            Phrase.setEchoChar((char) 0);
        }else{
            Phrase.setEchoChar('\u2022');
        }
    }//GEN-LAST:event_ChkTampilPhraseActionPerformed

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        BtnCari1ActionPerformed(null);
    }//GEN-LAST:event_TabRawatMouseClicked

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMProfilRingkasMedisRawatJalan dialog = new RMProfilRingkasMedisRawatJalan(new javax.swing.JFrame(), true);
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
    private widget.TextBox Agama;
    private widget.TextBox Alamat;
    private widget.TextBox Bahasa;
    private widget.Button BtnBukaURL;
    private widget.Button BtnCari1;
    private widget.Button BtnClosePhrase;
    private widget.Button BtnCloseUrl;
    private widget.Button BtnDownloadBukaFile;
    private widget.Button BtnDownloadFile;
    private widget.Button BtnKeluar;
    private widget.Button BtnPasien;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpanTandaTangan;
    private widget.TextBox CacatFisik;
    private widget.CekBox ChkInput;
    private widget.CekBox ChkTampilPhrase;
    private widget.panelisi FormInput;
    private widget.TextBox GD;
    private widget.TextBox IbuKandung;
    private widget.TextBox Jk;
    private widget.editorpane LoadHTMLSOAPI;
    private javax.swing.JMenuItem MnGeneratePDF;
    private javax.swing.JMenuItem MnGeneratePDFESign;
    private javax.swing.JMenuItem MnGeneratePDFSertiSign;
    private widget.TextBox NmPasien;
    private widget.TextBox NoRM;
    private widget.TextBox NoRawat;
    private javax.swing.JPanel PanelInput;
    private widget.TextBox Pekerjaan;
    private widget.TextBox Pendidikan;
    private widget.PasswordBox Phrase;
    private widget.RadioButton R1;
    private widget.RadioButton R2;
    private widget.RadioButton R3;
    private widget.RadioButton R4;
    private widget.ScrollPane Scroll2;
    private widget.TextBox StatusNikah;
    private javax.swing.JTabbedPane TabRawat;
    private widget.Tanggal Tanggal;
    private widget.TextBox TanggalLahir;
    private widget.TextBox TempatLahir;
    private widget.Tanggal Tgl1;
    private widget.Tanggal Tgl2;
    private widget.TextBox URLSertisign;
    private javax.swing.JDialog WindowPhrase;
    private javax.swing.JDialog WindowURLSertisign;
    private javax.swing.ButtonGroup buttonGroup1;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame8;
    private widget.InternalFrame internalFrame9;
    private widget.Label jLabel39;
    private widget.Label jLabel40;
    private widget.Label jLabel42;
    private widget.Label jLabel43;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.Label label17;
    private widget.Label label18;
    private widget.Label label19;
    private widget.Label label20;
    private widget.Label label21;
    private widget.Label label22;
    private widget.Label label23;
    private widget.Label label24;
    private widget.Label label25;
    private widget.Label label26;
    private widget.Label label27;
    private widget.Label label28;
    private widget.Label label29;
    private widget.panelisi panelGlass5;
    private widget.panelisi panelisi5;
    private widget.panelisi panelisi6;
    // End of variables declaration//GEN-END:variables

    public void setNoRm(String norm,String nama) {
        NoRM.setText(norm);
        NmPasien.setText(nama);
        isPasien();
        BtnCari1ActionPerformed(null);
    }
    
    public void setNoRawat(String norawat) {
        TabRawat.setSelectedIndex(2);
        NoRawat.setText(norawat);
        R4.setSelected(true);
    }

    private void isPasien() {
        try{
            ps=koneksi.prepareStatement(
                    "select pasien.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tmp_lahir,pasien.tgl_lahir,pasien.agama,"+
                    "bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,pasien.gol_darah,pasien.nm_ibu,pasien.stts_nikah,pasien.pnd, "+
                    "concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) as alamat,pasien.pekerjaan "+
                    "from pasien inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "+
                    "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik "+
                    "inner join kelurahan on pasien.kd_kel=kelurahan.kd_kel "+
                    "inner join kecamatan on pasien.kd_kec=kecamatan.kd_kec "+
                    "inner join kabupaten on pasien.kd_kab=kabupaten.kd_kab "+
                    "where pasien.no_rkm_medis=?");
            try {
                ps.setString(1,NoRM.getText());
                rs=ps.executeQuery();
                if(rs.next()){
                    NoRM.setText(rs.getString("no_rkm_medis"));
                    NmPasien.setText(rs.getString("nm_pasien"));
                    Jk.setText(rs.getString("jk"));
                    TempatLahir.setText(rs.getString("tmp_lahir"));
                    TanggalLahir.setText(rs.getString("tgl_lahir"));
                    Alamat.setText(rs.getString("alamat"));
                    GD.setText(rs.getString("gol_darah"));
                    IbuKandung.setText(rs.getString("nm_ibu"));
                    Agama.setText(rs.getString("agama"));
                    StatusNikah.setText(rs.getString("stts_nikah"));
                    Pendidikan.setText(rs.getString("pnd"));
                    Bahasa.setText(rs.getString("nama_bahasa"));
                    CacatFisik.setText(rs.getString("nama_cacat"));
                    Pekerjaan.setText(rs.getString("pekerjaan"));
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
    

    private void tampilSoapi() {
        try {
            htmlContent = new StringBuilder();
            htmlContent.append("<tr class='isi'>").
                            append("<td valign='middle' bgcolor='#FFFAF8' align='center' width='5%'>Tgl.Reg</td>").
                            append("<td valign='middle' bgcolor='#FFFAF8' align='center' width='8%'>No.Rawat</td>").
                            append("<td valign='middle' bgcolor='#FFFAF8' align='center' width='3%'>Status</td>").
                            append("<td valign='middle' bgcolor='#FFFAF8' align='center' width='84%'>Profil Ringkas Rawat Jalan</td>").
                        append("</tr>");     
            if(R1.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.status_lanjut "+
                    "from reg_periksa where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? order by reg_periksa.tgl_registrasi desc limit 5");
            }else if(R2.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.status_lanjut "+
                    "from reg_periksa where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? order by reg_periksa.tgl_registrasi");
            }else if(R3.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.status_lanjut "+
                    "from reg_periksa where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? and "+
                    "reg_periksa.tgl_registrasi between ? and ? order by reg_periksa.tgl_registrasi");
            }else if(R4.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.status_lanjut "+
                    "from reg_periksa where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? and reg_periksa.no_rawat=?");
            }
            try {
                if(R1.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                }else if(R2.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                }else if(R3.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                    ps.setString(2,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                    ps.setString(3,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                }else if(R4.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                    ps.setString(2,NoRawat.getText().trim());
                }  
                rs=ps.executeQuery();
                while(rs.next()){
                    htmlContent.append("<tr class='isi'>").
                                    append("<td valign='top' align='center'>").append(rs.getString("tgl_registrasi")).append("</td>").
                                    append("<td valign='top' align='center'>").append(rs.getString("no_rawat")).append("</td>").
                                    append("<td valign='top' align='center'>").append(rs.getString("status_lanjut")).append("</td>").
                                    append("<td valign='top' align='center'>").
                                        append("<table width='100%' border='0' align='center' cellpadding='2px' cellspacing='0'>");
                    
                    ps2=koneksi.prepareStatement(
                                "select pemeriksaan_ralan.tgl_perawatan,pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.suhu_tubuh,pemeriksaan_ralan.tensi,pemeriksaan_ralan.nadi,pemeriksaan_ralan.respirasi,"+
                                "pemeriksaan_ralan.tinggi,pemeriksaan_ralan.berat,pemeriksaan_ralan.gcs,pemeriksaan_ralan.spo2,pemeriksaan_ralan.kesadaran,pemeriksaan_ralan.keluhan, "+
                                "pemeriksaan_ralan.pemeriksaan,pemeriksaan_ralan.alergi,pemeriksaan_ralan.lingkar_perut,pemeriksaan_ralan.rtl,pemeriksaan_ralan.penilaian,"+
                                "pemeriksaan_ralan.instruksi,pemeriksaan_ralan.evaluasi,pemeriksaan_ralan.nip,pegawai.nama,pegawai.jbtn from pemeriksaan_ralan inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik where "+
                                "pemeriksaan_ralan.no_rawat=? order by pemeriksaan_ralan.tgl_perawatan,pemeriksaan_ralan.jam_rawat");
                    try {
                        ps2.setString(1,rs.getString("no_rawat"));
                        poli=Sequel.cariIsi("select p.nm_poli from reg_periksa rp inner join poliklinik p on rp.kd_poli = p.kd_poli where rp.no_rawat='"+rs.getString("no_rawat")+"'");
                        obat=Sequel.cariIsi("select GROUP_CONCAT(concat(databarang.nama_brng,', Jml : ',resep_dokter.jml,', Aturan Pakai : ',resep_dokter.aturan_pakai) SEPARATOR '<br>') as namaresep from resep_dokter inner join databarang on databarang.kode_brng=resep_dokter.kode_brng  inner join resep_obat on resep_dokter.no_resep=resep_obat.no_resep where resep_obat.no_rawat='"+rs.getString("no_rawat")+"'");
                        diagnosa=Sequel.cariIsi("select GROUP_CONCAT(dp.kd_penyakit SEPARATOR ', ') FROM diagnosa_pasien dp where dp.no_rawat='"+rs.getString("no_rawat")+"'");
                        rs2=ps2.executeQuery();
                        if(rs2.next()){
                            htmlContent.append("<tr class='isi'>").
                                            append("<td valign='middle' bgcolor='#FFFFF8' align='center' width='7%'>Tanggal</td>").
                                            append("<td valign='middle' bgcolor='#FFFFF8' align='center' width='7%'>Poliklinik</td>").
                                            append("<td valign='middle' bgcolor='#FFFFF8' align='center' width='13%'>Dokter/Paramedis</td>").
                                            append("<td valign='middle' bgcolor='#FFFFF8' align='center' width='14%'>Uraian Klinis</td>").
                                            append("<td valign='middle' bgcolor='#FFFFF8' align='center' width='13%'>Rencana</td>").
                                            append("<td valign='middle' bgcolor='#FFFFF8' align='center' width='13%'>Riwayat Alergi Obat</td>").
                                            append("<td valign='middle' bgcolor='#FFFFF8' align='center' width='14%'>Pengobatan Saat Ini</td>").
                                            append("<td valign='middle' bgcolor='#FFFFF8' align='center' width='14%'>Catatan Penting</td>").
                                            append("<td valign='middle' bgcolor='#FFFFF8' align='center' width='7%'>Diagnosa</td>").
                                        append("</tr>");
                            do{
                                htmlContent.append("<tr class='isi'>").
                                                append("<td align='center'>").append(rs2.getString("tgl_perawatan")).append("<br>").append(rs2.getString("jam_rawat")).append("</td>").
                                                append("<td align='center'>").append(poli).append("</td>").
                                                append("<td align='center'>").append(rs2.getString("nip")).append("<br>").append(rs2.getString("nama")).append("</td>").
                                                append("<td align='left'>").append(rs2.getString("keluhan").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append(rs2.getString("suhu_tubuh").equals("")?"":"<br>Suhu(C) : "+rs2.getString("suhu_tubuh")).append(rs2.getString("tensi").equals("")?"":"<br>Tensi : "+rs2.getString("tensi")).append(rs2.getString("nadi").equals("")?"":"<br>Nadi(/menit) : "+rs2.getString("nadi")).append(rs2.getString("respirasi").equals("")?"":"<br>Respirasi(/menit) : "+rs2.getString("respirasi")).append(rs2.getString("tinggi").equals("")?"":"<br>Tinggi(Cm) : "+rs2.getString("tinggi")).append(rs2.getString("berat").equals("")?"":"<br>Berat(Kg) : "+rs2.getString("berat")).append(rs2.getString("spo2").equals("")?"":"<br>SpO2(%) : "+rs2.getString("spo2")).append(rs2.getString("gcs").equals("")?"":"<br>GCS(E,V,M) : "+rs2.getString("gcs")).append(rs2.getString("kesadaran").equals("")?"":"<br>Kesadaran : "+rs2.getString("kesadaran")).append("</td>").
                                                append("<td align='left'>").append(rs2.getString("penilaian").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").
                                                append("<td align='left'>").append(rs2.getString("alergi").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").
                                                append("<td align='left'>").append(obat).append("</td>").
                                                append("<td align='left'>").append(rs2.getString("evaluasi").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").
                                                append("<td align='left'>").append(diagnosa).append("</td>").
                                            append("</tr>");
                            }while(rs2.next());
                        }       
                    } catch (Exception e) {
                        System.out.println("Notifikasi : "+e);
                    } finally{
                        if(rs2!=null){
                            rs2.close();
                        }
                        if(ps2!=null){
                            ps2.close();
                        }
                    }
                    
                    htmlContent.append("</table>").
                               append("</td>").
                            append("</tr>");
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
            
            LoadHTMLSOAPI.setText(
                    "<html>"+
                      "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                       htmlContent.toString()+
                      "</table>"+
                    "</html>");
            htmlContent=null;
        } catch (Exception e) {
            System.out.println("Notif SOAPI : "+e);
        } 
    }

    //CUSTOMM (TAMBAH)
    //--------------------------------------------------------------------------

    private void panggilLaporan(String teks) {
        try{
            File g = new File("file.css");            
            BufferedWriter bg = new BufferedWriter(new FileWriter(g));
            bg.write(".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}.isi a{text-decoration:none;color:#8b9b95;padding:0 0 0 0px;font-family: Tahoma;font-size: 8.5px;border: white;}");
            bg.close();

            File f = new File("riwayat.html");            
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(
                 teks.replaceAll("<head>","<head><link href=\"file.css\" rel=\"stylesheet\" type=\"text/css\" />").
                      replaceAll("<body>",
                                 "<body>"+
                                    "<table width='100%' align='center' border='0' class='tbl_form' cellspacing='0' cellpadding='0'>" +
                                        "<tr>" +
                                            "<td width='15%' border='0'>" +
                                                "<img width='50' height='50' src='data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(Sequel.cariGambar("select setting.logo from setting").readAllBytes())+"'/>" +
                                            "</td>" +
                                            "<td width='85%' border='0'>" +
                                                "<center>" +
                                                    "<font color='000000' size='3'  face='Tahoma'>"+akses.getnamars()+"</font><br>"+
                                                    "<font color='000000' size='1'  face='Tahoma'>"+
                                                        akses.getalamatrs()+", "+akses.getkabupatenrs()+", "+akses.getpropinsirs()+"<br/>" +
                                                        akses.getkontakrs()+", E-mail : "+akses.getemailrs()+
                                                        "<br>Profil Ringkas Medis Rawat Jalan" +
                                                    "</font> " +
                                                "</center>" +
                                            "</td>" +
                                        "</tr>" +
                                    "</table><br>"+
                                    "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>No.RM</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+NoRM.getText().trim()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Nama Pasien</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+NmPasien.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Alamat</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+Alamat.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Jenis Kelamin</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+Jk.getText().replaceAll("L","Laki-Laki").replaceAll("P","Perempuan")+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Tempat & Tanggal Lahir</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+TempatLahir.getText()+" "+TanggalLahir.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Ibu Kandung</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+IbuKandung.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Golongan Darah</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+GD.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Status Nikah</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+StatusNikah.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Agama</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+Agama.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Pendidikan Terakhir</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+Pendidikan.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Bahasa Dipakai</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+Bahasa.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Cacat Fisik</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+CacatFisik.getText()+"</td>"+
                                       "</tr>"+
                                    "</table>"            
                      ).
                      replaceAll((getClass().getResource("/picture/"))+"","./gambar/")
            );  
            bw.close();
            Desktop.getDesktop().browse(f.toURI());
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }   
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,126));
            FormInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            FormInput.setVisible(false);      
            ChkInput.setVisible(true);
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
