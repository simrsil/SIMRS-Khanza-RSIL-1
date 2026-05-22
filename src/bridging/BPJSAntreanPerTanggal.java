
/*
 * Kontribusi Oleh Ferry Ardiansyah - RSIAP 3326051
 *
 * Created on May 22, 2010, 11:58:21 PM
 */

package bridging;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.WarnaTable;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
//CUSTOM RSIL
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dosen
 */
public final class BPJSAntreanPerTanggal extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private validasi Valid=new validasi();
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private PreparedStatement ps;
    private ResultSet rs;    
    private int i=0,tot_belum=0,tot_selesai=0,jkn_capaian_angka=0,mjkn_capaian_angka=0;
    private double jkn_capaian,mjkn_capaian,jkn_belum,jkn_selesai,mjkn_belum,mjkn_selesai,umum_belum,umum_selesai,sep;
    private ApiMobileJKN api=new ApiMobileJKN();
    private String URL="",link="",utc="";
    private HttpHeaders headers;
    private HttpEntity requestEntity;
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNode root;
    private JsonNode nameNode;
    private JsonNode response;
    
    //CUSTOM RSIL
    private String requestJson, datajam = "", datacari = "", datatask = "", datacaritask = "", noresep = "", cariNoRawat = "";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Date parsedDate;
    
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean ceksukses = false;

    /** Creates new form DlgJnsPerawatanRalan
     * @param parent
     * @param modal */
    public BPJSAntreanPerTanggal(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setLocation(8,1);
        setSize(628,674);

        tabMode=new DefaultTableModel(null,new Object[]{
            //CUSTOM RSIL menambahkan kolom centang atau tidak centang
                "P","Kode Booking","Tanggal","Kode Poli","Kode Dokter","Jam Praktek","NIK","Noka","No. HP","RM","Jenis Kunjungan","No. Ref","Sumber Data","Peserta","No. Antrean","Estimasi Dilayani","Created Time","Status"
            }){
            //CUSTOM RSIL
            //@Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
                @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class, java.lang.String.class, java.lang.String.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbJnsPerawatan.setModel(tabMode);

        tbJnsPerawatan.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbJnsPerawatan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        //CUSTOM RSIL
        for (i = 0; i < 18; i++) {
            TableColumn column = tbJnsPerawatan.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(110);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(70);
            }else if(i==4){
                column.setPreferredWidth(83);
            }else if(i==5){
                column.setPreferredWidth(90);
            }else if(i==6){
                column.setPreferredWidth(120);
            }else if(i==7){
                column.setPreferredWidth(100);
            }else if(i==8){
                column.setPreferredWidth(100);
            }else if(i==9){
                column.setPreferredWidth(60);
            }else if(i==10){
                column.setPreferredWidth(100);
            }else if(i==11){
                column.setPreferredWidth(140);
            }else if(i==12){
                column.setPreferredWidth(100);
            }else if(i==13){
                column.setPreferredWidth(70);
            }else if(i==14){
                column.setPreferredWidth(70);
            }else if(i==15){
                column.setPreferredWidth(120);
            }else if(i==16){
                column.setPreferredWidth(120);
            }else if(i==17){
                column.setPreferredWidth(90);
            }
        }
        tbJnsPerawatan.setDefaultRenderer(Object.class, new WarnaTable());
        
        try {
            link=koneksiDB.URLAPIMOBILEJKN();
        } catch (Exception e) {
            System.out.println("E : "+e);
        }
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
        MnCekKodeBooking = new javax.swing.JMenuItem();
        MnPilihSemua = new javax.swing.JMenuItem();
        MnKirimManualAntrol = new javax.swing.JMenuItem();
        MnKirimOtomatisAntrol = new javax.swing.JMenuItem();
        WindowGantiDokterParamedis = new javax.swing.JDialog();
        internalFrame5 = new widget.InternalFrame();
        FormInput = new widget.panelisi();
        BtnSimpan4 = new widget.Button();
        BtnCloseIn4 = new widget.Button();
        jLabel17 = new widget.Label();
        jLabel20 = new widget.Label();
        jLabel23 = new widget.Label();
        jLabel24 = new widget.Label();
        jLabel25 = new widget.Label();
        jLabel26 = new widget.Label();
        NoRawatKirim = new widget.TextBox();
        Taskid3 = new widget.Tanggal();
        Taskid4 = new widget.Tanggal();
        Taskid5 = new widget.Tanggal();
        Taskid6 = new widget.Tanggal();
        Taskid7 = new widget.Tanggal();
        jLabel27 = new widget.Label();
        Norawat = new widget.TextBox();
        Status = new widget.TextBox();
        jLabel29 = new widget.Label();
        checkbox1 = new java.awt.Checkbox();
        checkbox2 = new java.awt.Checkbox();
        checkbox3 = new java.awt.Checkbox();
        checkbox4 = new java.awt.Checkbox();
        checkbox5 = new java.awt.Checkbox();
        jLabel22 = new widget.Label();
        Taskid99 = new widget.Tanggal();
        checkbox6 = new java.awt.Checkbox();
        JamSoapie = new widget.TextBox();
        jLabel28 = new widget.Label();
        JamResep = new widget.TextBox();
        jLabel30 = new widget.Label();
        jLabel31 = new widget.Label();
        StatusReg = new widget.TextBox();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbJnsPerawatan = new widget.Table();
        jPanel2 = new javax.swing.JPanel();
        panelGlass9 = new widget.panelisi();
        jLabel12 = new widget.Label();
        MJknBelum = new widget.Label();
        MJknCapaian = new widget.Label();
        jLabel13 = new widget.Label();
        MJknSelesai = new widget.Label();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        BtnCari = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar1 = new widget.Button();
        BtnKirim = new widget.Button();
        panelGlass8 = new widget.panelisi();
        jLabel8 = new widget.Label();
        TotBelum = new widget.Label();
        jLabel9 = new widget.Label();
        TotSelesai = new widget.Label();
        jLabel14 = new widget.Label();
        SEPTerbit = new widget.Label();
        jLabel10 = new widget.Label();
        JknBelum = new widget.Label();
        jLabel11 = new widget.Label();
        JknSelesai = new widget.Label();
        JknCapaian = new widget.Label();
        jLabel15 = new widget.Label();
        NonJKNBelum = new widget.Label();
        jLabel16 = new widget.Label();
        NonJKNSelesai = new widget.Label();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnCekKodeBooking.setBackground(new java.awt.Color(255, 255, 254));
        MnCekKodeBooking.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnCekKodeBooking.setForeground(new java.awt.Color(50, 50, 50));
        MnCekKodeBooking.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnCekKodeBooking.setText("Cek Kode Booking");
        MnCekKodeBooking.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnCekKodeBooking.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnCekKodeBooking.setName("MnCekKodeBooking"); // NOI18N
        MnCekKodeBooking.setPreferredSize(new java.awt.Dimension(160, 26));
        MnCekKodeBooking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnCekKodeBookingActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnCekKodeBooking);

        MnPilihSemua.setBackground(new java.awt.Color(255, 255, 254));
        MnPilihSemua.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnPilihSemua.setForeground(new java.awt.Color(50, 50, 50));
        MnPilihSemua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnPilihSemua.setText("Pilih Semua");
        MnPilihSemua.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnPilihSemua.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnPilihSemua.setName("MnPilihSemua"); // NOI18N
        MnPilihSemua.setPreferredSize(new java.awt.Dimension(160, 26));
        MnPilihSemua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnPilihSemuaActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnPilihSemua);

        MnKirimManualAntrol.setBackground(new java.awt.Color(255, 255, 254));
        MnKirimManualAntrol.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnKirimManualAntrol.setForeground(new java.awt.Color(50, 50, 50));
        MnKirimManualAntrol.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnKirimManualAntrol.setText("Kirim Manual Antrol");
        MnKirimManualAntrol.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnKirimManualAntrol.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnKirimManualAntrol.setName("MnKirimManualAntrol"); // NOI18N
        MnKirimManualAntrol.setPreferredSize(new java.awt.Dimension(160, 26));
        MnKirimManualAntrol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnKirimManualAntrolActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnKirimManualAntrol);

        MnKirimOtomatisAntrol.setBackground(new java.awt.Color(255, 255, 254));
        MnKirimOtomatisAntrol.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnKirimOtomatisAntrol.setForeground(new java.awt.Color(50, 50, 50));
        MnKirimOtomatisAntrol.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnKirimOtomatisAntrol.setText("Kirim Otomatis Antrol");
        MnKirimOtomatisAntrol.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnKirimOtomatisAntrol.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnKirimOtomatisAntrol.setName("MnKirimOtomatisAntrol"); // NOI18N
        MnKirimOtomatisAntrol.setPreferredSize(new java.awt.Dimension(160, 26));
        MnKirimOtomatisAntrol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnKirimOtomatisAntrolActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnKirimOtomatisAntrol);

        WindowGantiDokterParamedis.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowGantiDokterParamedis.setName("WindowGantiDokterParamedis"); // NOI18N
        WindowGantiDokterParamedis.setUndecorated(true);
        WindowGantiDokterParamedis.setResizable(false);

        internalFrame5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)), "::[ Ubah P.J.Rad, Perujuk & Petugas ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame5.setName("internalFrame5"); // NOI18N
        internalFrame5.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(89, 434));
        FormInput.setLayout(null);

        BtnSimpan4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan4.setMnemonic('S');
        BtnSimpan4.setText("Kirim");
        BtnSimpan4.setToolTipText("Alt+S");
        BtnSimpan4.setName("BtnSimpan4"); // NOI18N
        BtnSimpan4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpan4ActionPerformed(evt);
            }
        });
        FormInput.add(BtnSimpan4);
        BtnSimpan4.setBounds(280, 180, 100, 30);

        BtnCloseIn4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/cross.png"))); // NOI18N
        BtnCloseIn4.setMnemonic('U');
        BtnCloseIn4.setText("Tutup");
        BtnCloseIn4.setToolTipText("Alt+U");
        BtnCloseIn4.setName("BtnCloseIn4"); // NOI18N
        BtnCloseIn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCloseIn4ActionPerformed(evt);
            }
        });
        FormInput.add(BtnCloseIn4);
        BtnCloseIn4.setBounds(280, 220, 100, 30);

        jLabel17.setText("Status :");
        jLabel17.setName("jLabel17"); // NOI18N
        FormInput.add(jLabel17);
        jLabel17.setBounds(-40, 70, 110, 23);

        jLabel20.setText("Task ID 99 :");
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput.add(jLabel20);
        jLabel20.setBounds(0, 250, 70, 23);

        jLabel23.setText("Task ID 3 :");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput.add(jLabel23);
        jLabel23.setBounds(-20, 100, 92, 23);

        jLabel24.setText("Task ID 4 :");
        jLabel24.setName("jLabel24"); // NOI18N
        FormInput.add(jLabel24);
        jLabel24.setBounds(-20, 130, 92, 23);

        jLabel25.setText("Task ID 5 :");
        jLabel25.setName("jLabel25"); // NOI18N
        FormInput.add(jLabel25);
        jLabel25.setBounds(-20, 160, 92, 23);

        jLabel26.setText("Task ID 6 :");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput.add(jLabel26);
        jLabel26.setBounds(-20, 190, 92, 23);

        NoRawatKirim.setName("NoRawatKirim"); // NOI18N
        NoRawatKirim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoRawatKirimActionPerformed(evt);
            }
        });
        NoRawatKirim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoRawatKirimKeyPressed(evt);
            }
        });
        FormInput.add(NoRawatKirim);
        NoRawatKirim.setBounds(80, 10, 140, 23);

        Taskid3.setForeground(new java.awt.Color(50, 70, 50));
        Taskid3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2026-05-13 09:05:06" }));
        Taskid3.setDisplayFormat("yyyy-MM-dd HH:mm:ss");
        Taskid3.setDoubleBuffered(true);
        Taskid3.setName("Taskid3"); // NOI18N
        Taskid3.setOpaque(false);
        Taskid3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Taskid3ActionPerformed(evt);
            }
        });
        Taskid3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Taskid3KeyPressed(evt);
            }
        });
        FormInput.add(Taskid3);
        Taskid3.setBounds(90, 100, 145, 23);

        Taskid4.setForeground(new java.awt.Color(50, 70, 50));
        Taskid4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2026-05-13 09:05:06" }));
        Taskid4.setDisplayFormat("yyyy-MM-dd HH:mm:ss");
        Taskid4.setName("Taskid4"); // NOI18N
        Taskid4.setOpaque(false);
        Taskid4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Taskid4ActionPerformed(evt);
            }
        });
        Taskid4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Taskid4KeyPressed(evt);
            }
        });
        FormInput.add(Taskid4);
        Taskid4.setBounds(90, 130, 145, 23);

        Taskid5.setForeground(new java.awt.Color(50, 70, 50));
        Taskid5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2026-05-13 09:05:06" }));
        Taskid5.setDisplayFormat("yyyy-MM-dd HH:mm:ss");
        Taskid5.setName("Taskid5"); // NOI18N
        Taskid5.setOpaque(false);
        Taskid5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Taskid5ActionPerformed(evt);
            }
        });
        Taskid5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Taskid5KeyPressed(evt);
            }
        });
        FormInput.add(Taskid5);
        Taskid5.setBounds(90, 160, 145, 23);

        Taskid6.setForeground(new java.awt.Color(50, 70, 50));
        Taskid6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2026-05-13 09:05:06" }));
        Taskid6.setDisplayFormat("yyyy-MM-dd HH:mm:ss");
        Taskid6.setName("Taskid6"); // NOI18N
        Taskid6.setOpaque(false);
        Taskid6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Taskid6ActionPerformed(evt);
            }
        });
        Taskid6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Taskid6KeyPressed(evt);
            }
        });
        FormInput.add(Taskid6);
        Taskid6.setBounds(90, 190, 145, 23);

        Taskid7.setForeground(new java.awt.Color(50, 70, 50));
        Taskid7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2026-05-13 09:05:06" }));
        Taskid7.setDisplayFormat("yyyy-MM-dd HH:mm:ss");
        Taskid7.setName("Taskid7"); // NOI18N
        Taskid7.setOpaque(false);
        Taskid7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Taskid7ActionPerformed(evt);
            }
        });
        Taskid7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Taskid7KeyPressed(evt);
            }
        });
        FormInput.add(Taskid7);
        Taskid7.setBounds(90, 220, 145, 23);

        jLabel27.setText("No. Booking :");
        jLabel27.setName("jLabel27"); // NOI18N
        FormInput.add(jLabel27);
        jLabel27.setBounds(-40, 10, 110, 23);

        Norawat.setName("Norawat"); // NOI18N
        Norawat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NorawatActionPerformed(evt);
            }
        });
        Norawat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NorawatKeyPressed(evt);
            }
        });
        FormInput.add(Norawat);
        Norawat.setBounds(80, 40, 140, 23);

        Status.setName("Status"); // NOI18N
        Status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StatusActionPerformed(evt);
            }
        });
        Status.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                StatusKeyPressed(evt);
            }
        });
        FormInput.add(Status);
        Status.setBounds(80, 70, 140, 23);

        jLabel29.setText("No. Rawat :");
        jLabel29.setName("jLabel29"); // NOI18N
        FormInput.add(jLabel29);
        jLabel29.setBounds(-40, 40, 110, 23);

        checkbox1.setBackground(new java.awt.Color(255, 255, 255));
        checkbox1.setForeground(new java.awt.Color(255, 255, 255));
        checkbox1.setName("checkbox1"); // NOI18N
        FormInput.add(checkbox1);
        checkbox1.setBounds(240, 250, 20, 20);

        checkbox2.setBackground(new java.awt.Color(255, 255, 255));
        checkbox2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        checkbox2.setForeground(new java.awt.Color(255, 255, 255));
        checkbox2.setName("checkbox2"); // NOI18N
        checkbox2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                checkbox2PropertyChange(evt);
            }
        });
        FormInput.add(checkbox2);
        checkbox2.setBounds(240, 100, 20, 20);

        checkbox3.setBackground(new java.awt.Color(255, 255, 255));
        checkbox3.setForeground(new java.awt.Color(255, 255, 255));
        checkbox3.setName("checkbox3"); // NOI18N
        FormInput.add(checkbox3);
        checkbox3.setBounds(240, 130, 20, 20);

        checkbox4.setBackground(new java.awt.Color(255, 255, 255));
        checkbox4.setForeground(new java.awt.Color(255, 255, 255));
        checkbox4.setName("checkbox4"); // NOI18N
        FormInput.add(checkbox4);
        checkbox4.setBounds(240, 160, 20, 20);

        checkbox5.setBackground(new java.awt.Color(255, 255, 255));
        checkbox5.setForeground(new java.awt.Color(255, 255, 255));
        checkbox5.setName("checkbox5"); // NOI18N
        FormInput.add(checkbox5);
        checkbox5.setBounds(240, 190, 20, 20);

        jLabel22.setText("Task ID 7 :");
        jLabel22.setName("jLabel22"); // NOI18N
        FormInput.add(jLabel22);
        jLabel22.setBounds(-20, 220, 92, 23);

        Taskid99.setForeground(new java.awt.Color(50, 70, 50));
        Taskid99.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2026-05-13 09:05:06" }));
        Taskid99.setDisplayFormat("yyyy-MM-dd HH:mm:ss");
        Taskid99.setName("Taskid99"); // NOI18N
        Taskid99.setOpaque(false);
        Taskid99.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Taskid99ActionPerformed(evt);
            }
        });
        Taskid99.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Taskid99KeyPressed(evt);
            }
        });
        FormInput.add(Taskid99);
        Taskid99.setBounds(90, 250, 145, 23);

        checkbox6.setBackground(new java.awt.Color(255, 255, 255));
        checkbox6.setForeground(new java.awt.Color(255, 255, 255));
        checkbox6.setName("checkbox6"); // NOI18N
        FormInput.add(checkbox6);
        checkbox6.setBounds(240, 220, 20, 20);

        JamSoapie.setName("JamSoapie"); // NOI18N
        JamSoapie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JamSoapieActionPerformed(evt);
            }
        });
        JamSoapie.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JamSoapieKeyPressed(evt);
            }
        });
        FormInput.add(JamSoapie);
        JamSoapie.setBounds(330, 10, 140, 23);

        jLabel28.setText("SOAPIE :");
        jLabel28.setName("jLabel28"); // NOI18N
        FormInput.add(jLabel28);
        jLabel28.setBounds(240, 10, 80, 23);

        JamResep.setName("JamResep"); // NOI18N
        JamResep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JamResepActionPerformed(evt);
            }
        });
        JamResep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JamResepKeyPressed(evt);
            }
        });
        FormInput.add(JamResep);
        JamResep.setBounds(330, 40, 140, 23);

        jLabel30.setText("Resep :");
        jLabel30.setName("jLabel30"); // NOI18N
        FormInput.add(jLabel30);
        jLabel30.setBounds(240, 40, 80, 23);

        jLabel31.setText("Status Reg :");
        jLabel31.setName("jLabel31"); // NOI18N
        FormInput.add(jLabel31);
        jLabel31.setBounds(240, 70, 80, 23);

        StatusReg.setName("StatusReg"); // NOI18N
        StatusReg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StatusRegActionPerformed(evt);
            }
        });
        StatusReg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                StatusRegKeyPressed(evt);
            }
        });
        FormInput.add(StatusReg);
        StatusReg.setBounds(330, 70, 140, 23);

        internalFrame5.add(FormInput, java.awt.BorderLayout.CENTER);

        WindowGantiDokterParamedis.getContentPane().add(internalFrame5, java.awt.BorderLayout.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Antrean Per Tanggal Mobile JKN ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbJnsPerawatan.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbJnsPerawatan.setComponentPopupMenu(jPopupMenu1);
        tbJnsPerawatan.setName("tbJnsPerawatan"); // NOI18N
        Scroll.setViewportView(tbJnsPerawatan);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel2.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel12.setForeground(new java.awt.Color(0, 153, 0));
        jLabel12.setText("MJKN Belum :");
        jLabel12.setName("jLabel12"); // NOI18N
        jLabel12.setPreferredSize(new java.awt.Dimension(75, 23));
        panelGlass9.add(jLabel12);

        MJknBelum.setForeground(new java.awt.Color(0, 153, 0));
        MJknBelum.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MJknBelum.setText("0");
        MJknBelum.setName("MJknBelum"); // NOI18N
        MJknBelum.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass9.add(MJknBelum);

        MJknCapaian.setForeground(new java.awt.Color(0, 153, 0));
        MJknCapaian.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MJknCapaian.setText("0");
        MJknCapaian.setName("MJknCapaian"); // NOI18N
        MJknCapaian.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass9.add(MJknCapaian);

        jLabel13.setForeground(new java.awt.Color(0, 153, 0));
        jLabel13.setText("MJKN Selesai :");
        jLabel13.setName("jLabel13"); // NOI18N
        jLabel13.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass9.add(jLabel13);

        MJknSelesai.setForeground(new java.awt.Color(0, 153, 0));
        MJknSelesai.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MJknSelesai.setText("0");
        MJknSelesai.setName("MJknSelesai"); // NOI18N
        MJknSelesai.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass9.add(MJknSelesai);

        jLabel19.setText("Tanggal :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(55, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "13-05-2026" }));
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
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "13-05-2026" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(DTPCari2);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('2');
        BtnCari.setToolTipText("Alt+2");
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
        jLabel7.setPreferredSize(new java.awt.Dimension(85, 23));
        panelGlass9.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(75, 23));
        panelGlass9.add(LCount);

        BtnKeluar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar1.setMnemonic('K');
        BtnKeluar1.setText("Keluar");
        BtnKeluar1.setToolTipText("Alt+K");
        BtnKeluar1.setName("BtnKeluar1"); // NOI18N
        BtnKeluar1.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluar1ActionPerformed(evt);
            }
        });
        BtnKeluar1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluar1KeyPressed(evt);
            }
        });
        panelGlass9.add(BtnKeluar1);

        BtnKirim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/34.png"))); // NOI18N
        BtnKirim.setMnemonic('K');
        BtnKirim.setText("Kirim");
        BtnKirim.setToolTipText("Alt+K");
        BtnKirim.setName("BtnKirim"); // NOI18N
        BtnKirim.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKirim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKirimActionPerformed(evt);
            }
        });
        BtnKirim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKirimKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnKirim);

        jPanel2.add(panelGlass9, java.awt.BorderLayout.CENTER);

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel8.setForeground(new java.awt.Color(255, 153, 0));
        jLabel8.setText("Total Belum :");
        jLabel8.setName("jLabel8"); // NOI18N
        jLabel8.setPreferredSize(new java.awt.Dimension(72, 23));
        panelGlass8.add(jLabel8);

        TotBelum.setForeground(new java.awt.Color(255, 153, 0));
        TotBelum.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TotBelum.setText("0");
        TotBelum.setName("TotBelum"); // NOI18N
        TotBelum.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass8.add(TotBelum);

        jLabel9.setForeground(new java.awt.Color(102, 153, 0));
        jLabel9.setText("Total Selesai :");
        jLabel9.setName("jLabel9"); // NOI18N
        jLabel9.setPreferredSize(new java.awt.Dimension(77, 23));
        panelGlass8.add(jLabel9);

        TotSelesai.setForeground(new java.awt.Color(102, 153, 0));
        TotSelesai.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TotSelesai.setText("0");
        TotSelesai.setName("TotSelesai"); // NOI18N
        TotSelesai.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass8.add(TotSelesai);

        jLabel14.setForeground(new java.awt.Color(0, 153, 255));
        jLabel14.setText("SEP Terbit :");
        jLabel14.setName("jLabel14"); // NOI18N
        jLabel14.setPreferredSize(new java.awt.Dimension(65, 23));
        panelGlass8.add(jLabel14);

        SEPTerbit.setForeground(new java.awt.Color(0, 153, 255));
        SEPTerbit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        SEPTerbit.setText("0");
        SEPTerbit.setName("SEPTerbit"); // NOI18N
        SEPTerbit.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass8.add(SEPTerbit);

        jLabel10.setForeground(new java.awt.Color(204, 204, 0));
        jLabel10.setText("JKN Belum :");
        jLabel10.setName("jLabel10"); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(65, 23));
        panelGlass8.add(jLabel10);

        JknBelum.setForeground(new java.awt.Color(204, 204, 0));
        JknBelum.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        JknBelum.setText("0");
        JknBelum.setName("JknBelum"); // NOI18N
        JknBelum.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass8.add(JknBelum);

        jLabel11.setForeground(new java.awt.Color(204, 204, 0));
        jLabel11.setText("JKN Selesai :");
        jLabel11.setName("jLabel11"); // NOI18N
        jLabel11.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass8.add(jLabel11);

        JknSelesai.setForeground(new java.awt.Color(204, 204, 0));
        JknSelesai.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        JknSelesai.setText("0");
        JknSelesai.setName("JknSelesai"); // NOI18N
        JknSelesai.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass8.add(JknSelesai);

        JknCapaian.setForeground(new java.awt.Color(204, 204, 0));
        JknCapaian.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        JknCapaian.setText("0 ");
        JknCapaian.setName("JknCapaian"); // NOI18N
        JknCapaian.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass8.add(JknCapaian);

        jLabel15.setForeground(new java.awt.Color(0, 153, 153));
        jLabel15.setText("Non JKN Belum :");
        jLabel15.setName("jLabel15"); // NOI18N
        jLabel15.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass8.add(jLabel15);

        NonJKNBelum.setForeground(new java.awt.Color(0, 153, 153));
        NonJKNBelum.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        NonJKNBelum.setText("0");
        NonJKNBelum.setName("NonJKNBelum"); // NOI18N
        NonJKNBelum.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass8.add(NonJKNBelum);

        jLabel16.setForeground(new java.awt.Color(0, 153, 153));
        jLabel16.setText("Non JKN Selesai :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setPreferredSize(new java.awt.Dimension(92, 23));
        panelGlass8.add(jLabel16);

        NonJKNSelesai.setForeground(new java.awt.Color(0, 153, 153));
        NonJKNSelesai.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        NonJKNSelesai.setText("0");
        NonJKNSelesai.setName("NonJKNSelesai"); // NOI18N
        NonJKNSelesai.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass8.add(NonJKNSelesai);

        jPanel2.add(panelGlass8, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        emptTeks();
        runBackground(() ->tampil());
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void BtnKeluar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluar1ActionPerformed
       dispose();
    }//GEN-LAST:event_BtnKeluar1ActionPerformed

    private void BtnKeluar1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluar1KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }
    }//GEN-LAST:event_BtnKeluar1KeyPressed

    private void MnCekKodeBookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnCekKodeBookingActionPerformed
        if(tbJnsPerawatan.getSelectedRow()!= -1){
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            BPJSCekKodeBooking detail=new BPJSCekKodeBooking(null,false);
            detail.tampil(tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),0).toString());
            detail.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            detail.setLocationRelativeTo(internalFrame1);
            detail.setVisible(true);
            this.setCursor(Cursor.getDefaultCursor());
        }else{
            JOptionPane.showMessageDialog(null,"Maaf, silahkan pilih data yang mau dicek...!!!!");
            tbJnsPerawatan.requestFocus();
        }
    }//GEN-LAST:event_MnCekKodeBookingActionPerformed

    private void BtnKirimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKirimActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        kirimAntrian();
        emptTeks();
        tampil();
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnKirimActionPerformed

    private void BtnKirimKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKirimKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnKirimKeyPressed

    private void MnPilihSemuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnPilihSemuaActionPerformed
        for(i=0;i<tbJnsPerawatan.getRowCount();i++){
            tbJnsPerawatan.setValueAt(true,i,0);
        }
    }//GEN-LAST:event_MnPilihSemuaActionPerformed

    private void MnKirimManualAntrolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnKirimManualAntrolActionPerformed
        // TODO add your handling code here:
        NoRawatKirim.setText(tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 1).toString());
        Norawat.setText(Sequel.cariIsi("select referensi_mobilejkn_bpjs.no_rawat from referensi_mobilejkn_bpjs where nobooking = '" + tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 1).toString() + "'"));
        if (Norawat.getText() != null) {
            Status.setText("Ada");
        } else {
            Status.setText("Tidak Ada");
        }
        if (Norawat.getText().equals("")) {
            cariNoRawat = NoRawatKirim.getText();
        } else {
            cariNoRawat = Norawat.getText();
        }

        JamSoapie.setText(Sequel.cariIsi("SELECT  pemeriksaan_ralan.jam_rawat FROM pemeriksaan_ralan WHERE pemeriksaan_ralan.no_rawat = '" + cariNoRawat + "' ORDER BY pemeriksaan_ralan.jam_rawat ASC LIMIT 1"));
        JamResep.setText(Sequel.cariIsi("SELECT resep_obat.jam_peresepan FROM resep_obat WHERE resep_obat.no_rawat = '" + cariNoRawat + "' ORDER BY resep_obat.jam_peresepan ASC LIMIT 1"));
        StatusReg.setText(Sequel.cariIsi("SELECT reg_periksa.stts FROM reg_periksa WHERE reg_periksa.no_rawat = '" + cariNoRawat + "'"));

        /*NmPerujuk.setText(dokter.tampil3(rs5.getString("dokter_perujuk")));
        KodePj.setText(rs5.getString("kd_dokter"));
        NmDokterPj.setText(rs5.getString("nm_dokter"));
        KdPtgUbah.setText(rs5.getString("nip"));
        NmPtgUbah.setText(rs5.getString("nama"));*/
        WindowGantiDokterParamedis.setSize(600, 300);
        WindowGantiDokterParamedis.setLocationRelativeTo(internalFrame1);
        WindowGantiDokterParamedis.setVisible(true);
    }//GEN-LAST:event_MnKirimManualAntrolActionPerformed

    private void MnKirimOtomatisAntrolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnKirimOtomatisAntrolActionPerformed
        String nobooking = tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 0).toString();
        boolean adaBooking = Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs where nobooking=?", nobooking) > 0;
        String noRawat = adaBooking
        ? Sequel.cariIsi("select no_rawat from referensi_mobilejkn_bpjs where nobooking='" + nobooking + "'")
        : nobooking;

        if (Sequel.cariInteger("select count(reg_periksa.no_rawat) from reg_periksa where reg_periksa.no_rawat='" + noRawat + "' and reg_periksa.stts_daftar='Baru'") >= 1) {
            //TASK ID 1
            datajam = Sequel.cariIsi(
                "select str_to_date(date_add(concat(p.tgl_perawatan,' ',p.jam_rawat), interval (cast(rand()*(56-56+1)-56 as int)) minute), '%Y-%m-%d %H:%i:%s') "
                + "from pemeriksaan_ralan p where p.no_rawat='" + noRawat + "' order by p.jam_rawat limit 1"
            );

            if (!datajam.isEmpty()) {
                try {
                    LocalDateTime ldt = LocalDateTime.parse(datajam, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
                    long timestamp = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

                    requestJson = "{"
                    + "\"kodebooking\": \"" + nobooking + "\","
                    + "\"taskid\": \"1\","
                    + "\"waktu\": \"" + timestamp + "\""
                    + "}";

                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = link + "/antrean/updatewaktu";
                    System.out.println("Task Id 1 : " + URL);
                    System.out.println(" JSON : " + requestJson);
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    System.out.println(" code : " + nameNode.path("code").asText());
                    System.out.println(" pesan : " + nameNode.path("message").asText());
                    JOptionPane.showMessageDialog(null, "Task ID 1: " + nameNode.path("message").asText());

                    if (nameNode.path("code").asText().equals("200")) {
                        //Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='1' and no_rawat='" + noRawat + "'");
                        Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{noRawat, "1", datajam});
                    }
                } catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : " + ex);
                }
            }

            //TASK ID 2
            datajam = Sequel.cariIsi(
                "select str_to_date(date_add(concat(p.tgl_perawatan,' ',p.jam_rawat), interval (cast(rand()*(54-54+1)-54 as int)) minute), '%Y-%m-%d %H:%i:%s') "
                + "from pemeriksaan_ralan p where p.no_rawat='" + noRawat + "' order by p.jam_rawat limit 1"
            );

            if (!datajam.isEmpty()) {
                try {
                    LocalDateTime ldt = LocalDateTime.parse(datajam, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
                    long timestamp = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

                    requestJson = "{"
                    + "\"kodebooking\": \"" + nobooking + "\","
                    + "\"taskid\": \"2\","
                    + "\"waktu\": \"" + timestamp + "\""
                    + "}";

                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = link + "/antrean/updatewaktu";
                    System.out.println("Task Id 2 : " + URL);
                    System.out.println(" JSON : " + requestJson);
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    System.out.println(" code : " + nameNode.path("code").asText());
                    System.out.println(" pesan : " + nameNode.path("message").asText());
                    JOptionPane.showMessageDialog(null, "Task ID 2: " + nameNode.path("message").asText());

                    if (nameNode.path("code").asText().equals("200")) {
                        //Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='1' and no_rawat='" + noRawat + "'");
                        Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{noRawat, "2", datajam});
                    }
                } catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : " + ex);
                }
            }
        }

        //TASK ID 3
        datajam = Sequel.cariIsi(
            "select str_to_date(date_add(concat(p.tgl_perawatan,' ',p.jam_rawat), interval (cast(rand()*(50-50+1)-50 as int)) minute), '%Y-%m-%d %H:%i:%s') "
            + "from pemeriksaan_ralan p where p.no_rawat='" + noRawat + "' order by p.jam_rawat limit 1"
        );

        if (!datajam.isEmpty()) {
            try {
                LocalDateTime ldt = LocalDateTime.parse(datajam, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
                long timestamp = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

                requestJson = "{"
                + "\"kodebooking\": \"" + nobooking + "\","
                + "\"taskid\": \"3\","
                + "\"waktu\": \"" + timestamp + "\""
                + "}";

                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/updatewaktu";
                System.out.println("Task Id 3 : " + URL);
                System.out.println(" JSON : " + requestJson);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                System.out.println(" code : " + nameNode.path("code").asText());
                System.out.println(" pesan : " + nameNode.path("message").asText());
                JOptionPane.showMessageDialog(null, "Task ID 3: " + nameNode.path("message").asText());

                if (nameNode.path("code").asText().equals("200")) {
                    //Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='3' and no_rawat='" + noRawat + "'");
                    Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{noRawat, "3", datajam});
                }
            } catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        }

        //TASK ID 4
        datajam = Sequel.cariIsi(
            "select str_to_date(date_add(concat(p.tgl_perawatan,' ',p.jam_rawat), interval (cast(rand()*(12-9+1)-9 as int)) minute), '%Y-%m-%d %H:%i:%s') "
            + "from pemeriksaan_ralan p where p.no_rawat='" + noRawat + "' order by p.jam_rawat limit 1"
        );
        if (!datajam.isEmpty()) {
            try {
                LocalDateTime ldt = LocalDateTime.parse(datajam, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
                long timestamp = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

                requestJson = "{"
                + "\"kodebooking\": \"" + nobooking + "\","
                + "\"taskid\": \"4\","
                + "\"waktu\": \"" + timestamp + "\""
                + "}";

                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/updatewaktu";
                System.out.println("Task Id 4 : " + URL);
                System.out.println(" JSON : " + requestJson);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                System.out.println(" code : " + nameNode.path("code").asText());
                System.out.println(" pesan : " + nameNode.path("message").asText());
                JOptionPane.showMessageDialog(null, "Task ID 4: " + nameNode.path("message").asText());

                if (nameNode.path("code").asText().equals("200")) {
                    //Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='4' and no_rawat='" + noRawat + "'");
                    Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{noRawat, "4", datajam});
                }
            } catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        }

        //TASK ID 5
        datajam = Sequel.cariIsi(
            "select str_to_date(date_add(concat(p.tgl_perawatan,' ',p.jam_rawat), interval (cast(rand()*(1-1+1)-1 as int)) minute), '%Y-%m-%d %H:%i:%s') "
            + "from pemeriksaan_ralan p where p.no_rawat='" + noRawat + "' order by p.jam_rawat limit 1"
        );

        if (!datajam.isEmpty()) {
            try {
                LocalDateTime ldt = LocalDateTime.parse(datajam, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
                long timestamp = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

                requestJson = "{"
                + "\"kodebooking\": \"" + nobooking + "\","
                + "\"taskid\": \"5\","
                + "\"waktu\": \"" + timestamp + "\""
                + "}";

                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/updatewaktu";
                System.out.println("Task Id 5 : " + URL);
                System.out.println(" JSON : " + requestJson);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                System.out.println(" code : " + nameNode.path("code").asText());
                System.out.println(" pesan : " + nameNode.path("message").asText());
                JOptionPane.showMessageDialog(null, "Task ID 5: " + nameNode.path("message").asText());
                if (nameNode.path("code").asText().equals("200")) {
                    //Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='5' and no_rawat='" + noRawat + "'");
                    Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{noRawat, "5", datajam});
                }
            } catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        }

        //KIRIM RESEP
        noresep = Sequel.cariIsi("select resep_obat.no_resep from resep_obat where resep_obat.no_rawat='" + noRawat + "'");

        if (!noresep.equals("")) {
            try {
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                requestJson = "{"
                + "\"kodebooking\": \"" + nobooking + "\","
                + "\"jenisresep\": \"" + (Sequel.cariInteger("select count(resep_dokter_racikan.no_resep) from resep_dokter_racikan where resep_dokter_racikan.no_resep=?", noresep) > 0 ? "Racikan" : "Non Racikan") + "\","
                + "\"nomorantrean\": " + Integer.parseInt(StringUtils.right(noresep, 4)) + ","
                + "\"keterangan\": \"Resep dibuat secara elektronik di poli\""
                + "}";
                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/farmasi/add";
                System.out.println("Jenis Resep: " + URL);
                System.out.println(" JSON : " + requestJson);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                System.out.println(" code : " + nameNode.path("code").asText());
                System.out.println(" pesan : " + nameNode.path("message").asText());
            } catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        }

        //TASK ID 6
        if (Sequel.cariInteger("select count(no_resep) as task6 from resep_obat where resep_obat.status='ralan' and resep_obat.no_rawat='" + noRawat + "' and concat(resep_obat.tgl_perawatan,' ',resep_obat.jam)<>'0000-00-00 00:00:00'") >= 1) {
            datajam = Sequel.cariIsi("select str_to_date(date_add(concat(referensi_mobilejkn_bpjs_taskid.waktu), interval (cast(rand()*(360-120+1)+120 as int)) second), '%Y-%m-%d %H:%i:%s') as task6 from referensi_mobilejkn_bpjs_taskid where referensi_mobilejkn_bpjs_taskid.no_rawat='" + noRawat + "' and referensi_mobilejkn_bpjs_taskid.taskid='5'");
        } else {
            datajam = "";
        }

        if (!datajam.isEmpty()) {
            try {
                LocalDateTime ldt = LocalDateTime.parse(datajam, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
                long timestamp = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

                requestJson = "{"
                + "\"kodebooking\": \"" + nobooking + "\","
                + "\"taskid\": \"6\","
                + "\"waktu\": \"" + timestamp + "\""
                + "}";

                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/updatewaktu";
                System.out.println("Task Id 6 : " + URL);
                System.out.println(" JSON : " + requestJson);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                System.out.println(" code : " + nameNode.path("code").asText());
                System.out.println(" pesan : " + nameNode.path("message").asText());
                JOptionPane.showMessageDialog(null, "Task ID 6: " + nameNode.path("message").asText());
                if (nameNode.path("code").asText().equals("200")) {
                    //Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='6' and no_rawat='" + noRawat + "'");
                    Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{noRawat, "6", datajam});
                }
            } catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        }
        //TASK ID 7
        datajam = Sequel.cariIsi("select str_to_date(date_add(concat(referensi_mobilejkn_bpjs_taskid.waktu), interval (cast(rand()*(55-11+1)+11 as int)) minute), '%Y-%m-%d %H:%i:%s') as task7 from referensi_mobilejkn_bpjs_taskid WHERE referensi_mobilejkn_bpjs_taskid.no_rawat='" + noRawat + "' and referensi_mobilejkn_bpjs_taskid.taskid='6'");

        if (!datajam.isEmpty()) {
            try {
                LocalDateTime ldt = LocalDateTime.parse(datajam, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
                long timestamp = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

                requestJson = "{"
                + "\"kodebooking\": \"" + nobooking + "\","
                + "\"taskid\": \"7\","
                + "\"waktu\": \"" + timestamp + "\""
                + "}";

                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/updatewaktu";
                System.out.println("Task Id 7 : " + URL);
                System.out.println(" JSON : " + requestJson);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                System.out.println(" code : " + nameNode.path("code").asText());
                System.out.println(" pesan : " + nameNode.path("message").asText());
                JOptionPane.showMessageDialog(null, "Task ID 7: " + nameNode.path("message").asText());
                if (nameNode.path("code").asText().equals("200")) {
                    //Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='7' and no_rawat='" + noRawat + "'");
                    Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{noRawat, "7", datajam});
                }
            } catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        }

        //TASK ID 99 Batal
        datajam = Sequel.cariIsi("select now() from reg_periksa where reg_periksa.stts='Batal' and reg_periksa.no_rawat=?", noRawat);
        if (!datajam.equals("")) {
            try {
                LocalDateTime ldt = LocalDateTime.parse(datajam, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
                long timestamp = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                requestJson = "{"
                + "\"kodebooking\": \"" + nobooking + "\","
                + "\"taskid\": \"99\","
                + "\"waktu\": \"" + timestamp + "\""
                + "}";
                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/updatewaktu";
                System.out.println("Task Id 99 : " + URL);
                System.out.println(" JSON : " + requestJson);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                System.out.println(" code : " + nameNode.path("code").asText());
                System.out.println(" pesan : " + nameNode.path("message").asText());
                JOptionPane.showMessageDialog(null, "Task ID 99 Batal: " + nameNode.path("message").asText());
                if (nameNode.path("code").asText().equals("200")) {
                    //Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='99' and no_rawat='" + noRawat + "'");
                    Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{noRawat, "2", datajam});
                }
            } catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        }
        emptTeks();
        tampil();
    }//GEN-LAST:event_MnKirimOtomatisAntrolActionPerformed

    private void BtnSimpan4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpan4ActionPerformed

        if (checkbox2.getState() == true) {
            datajam = Taskid3.getSelectedItem().toString();
            datacaritask = Norawat.getText();
            if (datacaritask.equals("")) {
                datacaritask = NoRawatKirim.getText();
            }
            if (Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{datacaritask, "3", datajam}) == true) {
                //parsedDate = dateFormat.parse(datajam);
                String dateTimeString = datajam;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);
                Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                long timestamp3 = instant.toEpochMilli();

                try {
                    //TeksArea.append("Menjalankan WS taskid mulai tunggu poli Mobile JKN Pasien Non BPJS/BPS Onsite\n");
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson = "{"
                    + "\"kodebooking\": \"" + tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 1).toString() + "\","
                    + "\"taskid\": \"3\","
                    + "\"waktu\": \"" + timestamp3 + "\""
                    + "}";
                    //TeksArea.append("JSON : " + requestJson + "\n");
                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = link + "/antrean/updatewaktu";
                    System.out.println("Task Id 3 : " + URL);
                    //System.out.println(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    System.out.println(" JSON : " + requestJson);
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    System.out.println(" code : " + nameNode.path("code").asText());
                    System.out.println(" pesan : " + nameNode.path("message").asText());
                    JOptionPane.showMessageDialog(null, "Task ID 3: " + nameNode.path("message").asText());
                    if (!nameNode.path("code").asText().equals("200")) {
                        Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='3' and no_rawat='" + datacaritask + "'");
                    }
                } catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : " + ex);

                }
            }

        }
        if (checkbox3.getState() == true) {
            //JOptionPane.showMessageDialog(null, "Task Id 4");
            datajam = Taskid4.getSelectedItem().toString();
            datacaritask = Norawat.getText();
            if (datacaritask.equals("")) {
                datacaritask = NoRawatKirim.getText();
            }
            if (Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{datacaritask, "4", datajam}) == true) {
                //parsedDate = dateFormat.parse(datajam);
                String dateTimeString = datajam;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);
                Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                long timestamp4 = instant.toEpochMilli();
                try {
                    //TeksArea.append("Menjalankan WS taskid mulai pelayanan poli Mobile JKN Pasien Non BPJS/BPS Onsite\n");
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson = "{"
                    + "\"kodebooking\": \"" + tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 1).toString() + "\","
                    + "\"taskid\": \"4\","
                    + "\"waktu\": \"" + timestamp4 + "\""
                    + "}";
                    //TeksArea.append("JSON : " + requestJson + "\n");
                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = link + "/antrean/updatewaktu";
                    System.out.println("Task Id 4: " + URL);
                    //System.out.println(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    System.out.println(" JSON : " + requestJson);
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    System.out.println(" code : " + nameNode.path("code").asText());
                    System.out.println(" pesan : " + nameNode.path("message").asText());
                    JOptionPane.showMessageDialog(null, "Task ID 4: " + nameNode.path("message").asText());
                    if (!nameNode.path("code").asText().equals("200")) {
                        Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='4' and no_rawat='" + datacaritask + "'");
                    }
                } catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : " + ex);
                }
            }

        }
        if (checkbox4.getState() == true) {
            //JOptionPane.showMessageDialog(null, "Task Id 5");
            datajam = Taskid5.getSelectedItem().toString();
            datacaritask = Norawat.getText();
            if (datacaritask.equals("")) {
                datacaritask = NoRawatKirim.getText();
            }
            if (Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{datacaritask, "5", datajam}) == true) {
                String dateTimeString = datajam;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);
                Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                long timestamp5 = instant.toEpochMilli();
                try {
                    //TeksArea.append("Menjalankan WS taskid selesai pelayanan poli Mobile JKN Pasien Non BPJS/BPS Onsite\n");
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson = "{"
                    + "\"kodebooking\": \"" + tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 1).toString() + "\","
                    + "\"taskid\": \"5\","
                    + "\"waktu\": \"" + timestamp5 + "\""
                    + "}";
                    //TeksArea.append("JSON : " + requestJson + "\n");
                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = link + "/antrean/updatewaktu";
                    System.out.println("Task Id 5: " + URL);
                    //System.out.println(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    System.out.println(" JSON : " + requestJson);
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    System.out.println(" code : " + nameNode.path("code").asText());
                    System.out.println(" pesan : " + nameNode.path("message").asText());
                    JOptionPane.showMessageDialog(null, "Task ID 5: " + nameNode.path("message").asText());
                    if (!nameNode.path("code").asText().equals("200")) {
                        Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='5' and no_rawat='" + datacaritask + "'");
                    }
                } catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : " + ex);
                }
            }

        }
        if (checkbox5.getState() == true) {
            //KIRIM JENIS RESEP
            datacaritask = Norawat.getText();
            if (datacaritask.equals("")) {
                datacaritask = NoRawatKirim.getText();
            }
            noresep = Sequel.cariIsi("select resep_obat.no_resep from resep_obat where resep_obat.no_rawat=?", datacaritask);
            if (!noresep.equals("")) {

                try {
                    //TeksArea.append("Menjalankan WS tambah antrian farmasi Mobile JKN Pasien Non BPJS/BPS Onsite\n");
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson = "{"
                    + "\"kodebooking\": \"" + tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 1).toString() + "\","
                    + "\"jenisresep\": \"" + (Sequel.cariInteger("select count(resep_dokter_racikan.no_resep) from resep_dokter_racikan where resep_dokter_racikan.no_resep=?", noresep) > 0 ? "Racikan" : "Non Racikan") + "\","
                    + "\"nomorantrean\": " + Integer.parseInt(StringUtils.right(noresep, 4)) + ","
                    + "\"keterangan\": \"Resep dibuat secara elektronik di poli\""
                    + "}";
                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = link + "/antrean/farmasi/add";
                    System.out.println("Jenis Resep: " + URL);
                    //System.out.println(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    System.out.println(" JSON : " + requestJson);
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    System.out.println(" code : " + nameNode.path("code").asText());
                    System.out.println(" pesan : " + nameNode.path("message").asText());
                } catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : " + ex);
                }
            }
            datajam = Taskid6.getSelectedItem().toString();
            //            datacaritask = tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 0).toString();
            if (Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{datacaritask, "6", datajam}) == true) {
                //                parsedDate = dateFormat.parse(datajam);
                try {
                    //                    TeksArea.append("Menjalankan WS taskid permintaan resep poli Mobile JKN Pasien Non BPJS/BPS Onsite\n");
                    String dateTimeString = datajam;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);
                    Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                    long timestamp6 = instant.toEpochMilli();
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson = "{"
                    + "\"kodebooking\": \"" + tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 1).toString() + "\","
                    + "\"taskid\": \"6\","
                    + "\"waktu\": \"" + timestamp6 + "\""
                    + "}";
                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = link + "/antrean/updatewaktu";
                    System.out.println("Task Id 6: " + URL);
                    //System.out.println(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    System.out.println(" JSON : " + requestJson);
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    System.out.println(" code : " + nameNode.path("code").asText());
                    System.out.println(" pesan : " + nameNode.path("message").asText());
                    JOptionPane.showMessageDialog(null, "Task ID 6: " + nameNode.path("message").asText());
                    if (!nameNode.path("code").asText().equals("200")) {
                        Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='6' and no_rawat='" + datacaritask + "'");
                    }
                } catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : " + ex);
                }
            }
        }
        if (checkbox6.getState() == true) {
            datajam = Taskid7.getSelectedItem().toString();
            datacaritask = Norawat.getText();
            if (datacaritask.equals("")) {
                datacaritask = NoRawatKirim.getText();
            }
            if (Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{datacaritask, "7", datajam}) == true) {
                String dateTimeString = datajam;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);
                Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                long timestamp7 = instant.toEpochMilli();
                try {
                    //                    TeksArea.append("Menjalankan WS taskid validasi resep poli Mobile JKN Pasien Non BPJS/BPS Onsite\n");
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson = "{"
                    + "\"kodebooking\": \"" + tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 1).toString() + "\","
                    + "\"taskid\": \"7\","
                    + "\"waktu\": \"" + timestamp7 + "\""
                    + "}";
                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = link + "/antrean/updatewaktu";
                    System.out.println("Task Id 7: " + URL);
                    //System.out.println(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    System.out.println(" JSON : " + requestJson);
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    System.out.println(" code : " + nameNode.path("code").asText());
                    System.out.println(" pesan : " + nameNode.path("message").asText());
                    JOptionPane.showMessageDialog(null, "Task ID 7: " + nameNode.path("message").asText());
                    if (!nameNode.path("code").asText().equals("200")) {
                        Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='7' and no_rawat='" + datacaritask + "'");
                    }
                } catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : " + ex);
                }
            }

        }
        if (checkbox1.getState() == true) {
            try {
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                requestJson = "{"
                + "\"kodebooking\": \"" + NoRawatKirim.getText() + "\","
                + "\"keterangan\": \"Batal\""
                + "}";
                //            TeksArea.append("JSON : " + requestJson + "\n");
                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/batal";
                System.out.println("URL : " + URL);
                //System.out.println(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                System.out.println(" code : " + nameNode.path("code").asText());
                System.out.println(" pesan : " + nameNode.path("message").asText());
                JOptionPane.showMessageDialog(null, nameNode.path("message").asText());
                if (nameNode.path("code").asText().equals("200")) {
                    Sequel.queryu2("update referensi_mobilejkn_bpjs_batal set statuskirim='Sudah' where nomorreferensi='" + tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 11).toString() + "'");
                    datajam = Taskid99.getSelectedItem().toString();
                    if (!datajam.equals("")) {
                        if (Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{ datacaritask, "99", datajam}) == true) {
                            //                        parsedDate = dateFormat.parse(datajam);
                            String dateTimeString = datajam;
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);
                            Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                            long timestamp99 = instant.toEpochMilli();
                            try {
                                //TeksArea.append("Menjalankan WS taskid batal pelayanan poli Mobile JKN Pasien BPJS\n");
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                                utc = String.valueOf(api.GetUTCdatetimeAsString());
                                headers.add("x-timestamp", utc);
                                headers.add("x-signature", api.getHmac(utc));
                                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                                requestJson = "{"
                                + "\"kodebooking\": \"" + NoRawatKirim.getText() + "\","
                                + "\"taskid\": \"99\","
                                + "\"waktu\": \"" + timestamp99 + "\""
                                + "}";
                                //TeksArea.append("JSON : " + requestJson + "\n");
                                requestEntity = new HttpEntity(requestJson, headers);
                                URL = link + "/antrean/updatewaktu";
                                System.out.println("URL : " + URL);
                                //System.out.println(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                                System.out.println("JSON : " + requestJson);
                                requestEntity = new HttpEntity(requestJson, headers);
                                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                                nameNode = root.path("metaData");
                                System.out.println(" code : " + nameNode.path("code").asText());
                                System.out.println(" pesan : " + nameNode.path("message").asText());
                                JOptionPane.showMessageDialog(null, "Task ID 99: " + nameNode.path("message").asText());
                                if (!nameNode.path("code").asText().equals("200")) {
                                    Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='99' and no_rawat='" + tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 1).toString() + "'");
                                } else {
                                    JOptionPane.showMessageDialog(null, "Berhasil dibatalkan...!!");
                                }
                                //TeksArea.append("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                            } catch (Exception ex) {
                                System.out.println("Notifikasi Bridging : " + ex);
                            }
                        }
                    }
                }
                //TeksArea.append("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
            } catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        }
    }//GEN-LAST:event_BtnSimpan4ActionPerformed

    private void BtnCloseIn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseIn4ActionPerformed
        WindowGantiDokterParamedis.dispose();
    }//GEN-LAST:event_BtnCloseIn4ActionPerformed

    private void NoRawatKirimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NoRawatKirimActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoRawatKirimActionPerformed

    private void NoRawatKirimKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoRawatKirimKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoRawatKirimKeyPressed

    private void Taskid3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Taskid3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Taskid3ActionPerformed

    private void Taskid3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Taskid3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Taskid3KeyPressed

    private void Taskid4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Taskid4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Taskid4ActionPerformed

    private void Taskid4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Taskid4KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Taskid4KeyPressed

    private void Taskid5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Taskid5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Taskid5ActionPerformed

    private void Taskid5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Taskid5KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Taskid5KeyPressed

    private void Taskid6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Taskid6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Taskid6ActionPerformed

    private void Taskid6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Taskid6KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Taskid6KeyPressed

    private void Taskid7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Taskid7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Taskid7ActionPerformed

    private void Taskid7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Taskid7KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Taskid7KeyPressed

    private void NorawatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NorawatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NorawatActionPerformed

    private void NorawatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NorawatKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NorawatKeyPressed

    private void StatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_StatusActionPerformed

    private void StatusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StatusKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_StatusKeyPressed

    private void checkbox2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_checkbox2PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_checkbox2PropertyChange

    private void Taskid99ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Taskid99ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Taskid99ActionPerformed

    private void Taskid99KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Taskid99KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Taskid99KeyPressed

    private void JamSoapieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JamSoapieActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JamSoapieActionPerformed

    private void JamSoapieKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JamSoapieKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_JamSoapieKeyPressed

    private void JamResepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JamResepActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JamResepActionPerformed

    private void JamResepKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JamResepKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_JamResepKeyPressed

    private void StatusRegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StatusRegActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_StatusRegActionPerformed

    private void StatusRegKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StatusRegKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_StatusRegKeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            BPJSAntreanPerTanggal dialog = new BPJSAntreanPerTanggal(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }
    
    
    private void emptTeks(){
        SEPTerbit.setText("");
        TotBelum.setText("");
        TotSelesai.setText("");
        JknBelum.setText("");
        JknSelesai.setText("");
        MJknBelum.setText("");
        MJknSelesai.setText("");
        NonJKNBelum.setText("");
        NonJKNSelesai.setText("");
        JknCapaian.setText("");
        MJknCapaian.setText("");
        sep = 0;
        tot_belum = 0;
        tot_selesai = 0;
        jkn_belum = 0;
        jkn_selesai = 0;
        mjkn_belum = 0;
        mjkn_selesai = 0;
        umum_belum = 0;
        umum_selesai = 0;
        jkn_capaian = 0;
        mjkn_capaian = 0;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.Button BtnCari;
    private widget.Button BtnCloseIn4;
    private widget.Button BtnKeluar1;
    private widget.Button BtnKirim;
    private widget.Button BtnSimpan4;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.panelisi FormInput;
    private widget.TextBox JamResep;
    private widget.TextBox JamSoapie;
    private widget.Label JknBelum;
    private widget.Label JknCapaian;
    private widget.Label JknSelesai;
    private widget.Label LCount;
    private widget.Label MJknBelum;
    private widget.Label MJknCapaian;
    private widget.Label MJknSelesai;
    private javax.swing.JMenuItem MnCekKodeBooking;
    private javax.swing.JMenuItem MnKirimManualAntrol;
    private javax.swing.JMenuItem MnKirimOtomatisAntrol;
    private javax.swing.JMenuItem MnPilihSemua;
    private widget.TextBox NoRawatKirim;
    private widget.Label NonJKNBelum;
    private widget.Label NonJKNSelesai;
    private widget.TextBox Norawat;
    private widget.Label SEPTerbit;
    private widget.ScrollPane Scroll;
    private widget.TextBox Status;
    private widget.TextBox StatusReg;
    private widget.Tanggal Taskid3;
    private widget.Tanggal Taskid4;
    private widget.Tanggal Taskid5;
    private widget.Tanggal Taskid6;
    private widget.Tanggal Taskid7;
    private widget.Tanggal Taskid99;
    private widget.Label TotBelum;
    private widget.Label TotSelesai;
    private javax.swing.JDialog WindowGantiDokterParamedis;
    private java.awt.Checkbox checkbox1;
    private java.awt.Checkbox checkbox2;
    private java.awt.Checkbox checkbox3;
    private java.awt.Checkbox checkbox4;
    private java.awt.Checkbox checkbox5;
    private java.awt.Checkbox checkbox6;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame5;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel19;
    private widget.Label jLabel20;
    private widget.Label jLabel21;
    private widget.Label jLabel22;
    private widget.Label jLabel23;
    private widget.Label jLabel24;
    private widget.Label jLabel25;
    private widget.Label jLabel26;
    private widget.Label jLabel27;
    private widget.Label jLabel28;
    private widget.Label jLabel29;
    private widget.Label jLabel30;
    private widget.Label jLabel31;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.Table tbJnsPerawatan;
    // End of variables declaration//GEN-END:variables

    private void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            ps=koneksi.prepareStatement(
                   "SELECT reg_periksa.tgl_registrasi FROM reg_periksa WHERE reg_periksa.tgl_registrasi BETWEEN ? AND ? group by reg_periksa.tgl_registrasi");
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                rs=ps.executeQuery();
                while(rs.next()){
                    try {
                        headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("x-cons-id",koneksiDB.CONSIDAPIMOBILEJKN());
                        utc=String.valueOf(api.GetUTCdatetimeAsString());
                        headers.add("x-timestamp",utc);
                        headers.add("x-signature",api.getHmac(utc));
                        headers.add("user_key",koneksiDB.USERKEYAPIMOBILEJKN());
                        requestEntity = new HttpEntity(headers);
                        URL = link+"/antrean/pendaftaran/tanggal/"+rs.getString("tgl_registrasi");	
                        System.out.println("URL : "+URL);
                        root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.GET, requestEntity, String.class).getBody());
                        nameNode = root.path("metadata");
                        if(nameNode.path("code").asText().equals("200")){
                            response = mapper.readTree(api.Decrypt(root.path("response").asText(),utc));
                            if(response.isArray()){
                                for(JsonNode list:response){
                                    tabMode.addRow(new Object[]{
                                        //CUSTOM RSIL
                                        false,
                                        list.path("kodebooking").asText(),list.path("tanggal").asText(),list.path("kodepoli").asText(),
                                        list.path("kodedokter").asText(),list.path("jampraktek").asText(),list.path("nik").asText(),list.path("nokapst").asText(),list.path("nohp").asText(),list.path("norekammedis").asText(),list.path("jeniskunjungan").asText(),list.path("nomorreferensi").asText(),list.path("sumberdata").asText(),list.path("ispeserta").asText().equals("true")?"Ya":"Tidak",list.path("noantrean").asText(),list.path("estimasidilayani").asText(),list.path("createdtime").asText(),list.path("status").asText()
                                    });
                                    if (list.path("status").asText().equals("Belum dilayani")) {
                                        tot_belum += 1;
                                    }
                                    if (list.path("status").asText().equals("Selesai dilayani")) {
                                        tot_selesai += 1;
                                    }
                                    if (list.path("status").asText().equals("Belum dilayani") && list.path("sumberdata").asText().equals("Bridging Antrean") && list.path("ispeserta").asText().equals("true")) {
                                        jkn_belum += 1;
                                    }
                                    if (list.path("status").asText().equals("Selesai dilayani") && list.path("sumberdata").asText().equals("Bridging Antrean") && list.path("ispeserta").asText().equals("true")) {
                                        jkn_selesai += 1;
                                    }
                                    if (list.path("status").asText().equals("Belum dilayani") && list.path("sumberdata").asText().equals("Mobile JKN")) {
                                        mjkn_belum += 1;
                                    }
                                    if (list.path("status").asText().equals("Selesai dilayani") && list.path("sumberdata").asText().equals("Mobile JKN")) {
                                        mjkn_selesai += 1;
                                    }
                                    if (list.path("status").asText().equals("Belum dilayani") && list.path("ispeserta").asText().equals("false")) {
                                        umum_belum += 1;
                                    }
                                    if (list.path("status").asText().equals("Selesai dilayani") && list.path("ispeserta").asText().equals("false")) {
                                        umum_selesai += 1;
                                    }
                                }
                            }
                        }else {
                            System.out.println("Notif : "+nameNode.path("message").asText());               
                        }   
                    } catch (Exception ex) {
                        System.out.println("Notifikasi : "+ex);
                        if(ex.toString().contains("UnknownHostException")){
                            JOptionPane.showMessageDialog(rootPane,"Koneksi ke server BPJS terputus...!");
                        }
                    }
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
        

        sep = Sequel.cariInteger("select count(bridging_sep.no_rawat) from bridging_sep where bridging_sep.tglsep between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' and bridging_sep.jnspelayanan = '2' and bridging_sep.kdpolitujuan <> 'IGD'")+
                Sequel.cariInteger("select count(bridging_sep_internal.no_rawat) from bridging_sep_internal where bridging_sep_internal.tglsep between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' and bridging_sep_internal.jnspelayanan = '2' and bridging_sep_internal.kdpolitujuan <> 'IGD'");
        jkn_capaian = (jkn_selesai/sep)*100;
        mjkn_capaian = (mjkn_selesai/sep)*100;
        jkn_capaian_angka = (int)jkn_capaian;
        mjkn_capaian_angka = (int)mjkn_capaian;
        LCount.setText(""+tabMode.getRowCount());
        SEPTerbit.setText(""+sep);
        TotBelum.setText(""+tot_belum);
        TotSelesai.setText(""+tot_selesai);
        JknBelum.setText(""+jkn_belum);
        JknSelesai.setText(""+jkn_selesai);
        JknCapaian.setText("("+jkn_capaian_angka+"%)");
        MJknBelum.setText(""+mjkn_belum);
        MJknSelesai.setText(""+mjkn_selesai);
        MJknCapaian.setText("("+mjkn_capaian_angka+"%)");
        NonJKNBelum.setText(""+umum_belum);
        NonJKNSelesai.setText(""+umum_selesai);
    }
    
    private void kirimAntrian(){
        for (i = 0; i < tbJnsPerawatan.getRowCount(); i++) {
            try{
                if (tbJnsPerawatan.getValueAt(i, 0).toString().equals("true")&&(tbJnsPerawatan.getValueAt(i, 17).toString().equals("Belum dilayani"))) {
                    String nobooking = tbJnsPerawatan.getValueAt(i, 1).toString();
                    boolean adaBooking = Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs where nobooking=?", nobooking) > 0;
                    String noRawat = adaBooking
                            ? Sequel.cariIsi("select no_rawat from referensi_mobilejkn_bpjs where nobooking='" + nobooking + "'")
                            : nobooking;

                    if (Sequel.cariInteger("select count(reg_periksa.no_rawat) from reg_periksa where reg_periksa.no_rawat='" + noRawat + "' and reg_periksa.stts_daftar='Baru'") >= 1) {
                        //TASK ID 1
                        datajam = Sequel.cariIsi(
                                "select str_to_date(date_add(concat(p.tgl_perawatan,' ',p.jam_rawat), interval (cast(rand()*(56-56+1)-56 as int)) minute), '%Y-%m-%d %H:%i:%s') "
                                + "from pemeriksaan_ralan p where p.no_rawat='" + noRawat + "' order by p.jam_rawat limit 1"
                        );

                        if (!datajam.isEmpty()) {
                            try {
                                LocalDateTime ldt = LocalDateTime.parse(datajam, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
                                long timestamp = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                                utc = String.valueOf(api.GetUTCdatetimeAsString());
                                headers.add("x-timestamp", utc);
                                headers.add("x-signature", api.getHmac(utc));
                                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

                                requestJson = "{"
                                        + "\"kodebooking\": \"" + nobooking + "\","
                                        + "\"taskid\": \"1\","
                                        + "\"waktu\": \"" + timestamp + "\""
                                        + "}";

                                requestEntity = new HttpEntity(requestJson, headers);
                                URL = link + "/antrean/updatewaktu";
                                System.out.println("Task Id 1 : " + URL);
                                System.out.println(" JSON : " + requestJson);
                                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                                nameNode = root.path("metadata");
                                System.out.println(" code : " + nameNode.path("code").asText());
                                System.out.println(" pesan : " + nameNode.path("message").asText());
                                //JOptionPane.showMessageDialog(null, "Task ID 1: " + nameNode.path("message").asText());

                                if (nameNode.path("code").asText().equals("200")) {
                                    //Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='1' and no_rawat='" + noRawat + "'");
                                    Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{noRawat, "1", datajam});
                                }
                            } catch (Exception ex) {
                                System.out.println("Notifikasi Bridging : " + ex);
                            }
                        }

                        //TASK ID 2
                        datajam = Sequel.cariIsi(
                                "select str_to_date(date_add(concat(p.tgl_perawatan,' ',p.jam_rawat), interval (cast(rand()*(54-54+1)-54 as int)) minute), '%Y-%m-%d %H:%i:%s') "
                                + "from pemeriksaan_ralan p where p.no_rawat='" + noRawat + "' order by p.jam_rawat limit 1"
                        );

                        if (!datajam.isEmpty()) {
                            try {
                                LocalDateTime ldt = LocalDateTime.parse(datajam, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
                                long timestamp = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                                utc = String.valueOf(api.GetUTCdatetimeAsString());
                                headers.add("x-timestamp", utc);
                                headers.add("x-signature", api.getHmac(utc));
                                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

                                requestJson = "{"
                                        + "\"kodebooking\": \"" + nobooking + "\","
                                        + "\"taskid\": \"2\","
                                        + "\"waktu\": \"" + timestamp + "\""
                                        + "}";

                                requestEntity = new HttpEntity(requestJson, headers);
                                URL = link + "/antrean/updatewaktu";
                                System.out.println("Task Id 2 : " + URL);
                                System.out.println(" JSON : " + requestJson);
                                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                                nameNode = root.path("metadata");
                                System.out.println(" code : " + nameNode.path("code").asText());
                                System.out.println(" pesan : " + nameNode.path("message").asText());
                                //JOptionPane.showMessageDialog(null, "Task ID 2: " + nameNode.path("message").asText());

                                if (nameNode.path("code").asText().equals("200")) {
                                    //Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='1' and no_rawat='" + noRawat + "'");
                                    Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{noRawat, "2", datajam});
                                }
                            } catch (Exception ex) {
                                System.out.println("Notifikasi Bridging : " + ex);
                            }
                        }
                    }

                    //TASK ID 3
                    datajam = Sequel.cariIsi(
                            "select str_to_date(date_add(concat(p.tgl_perawatan,' ',p.jam_rawat), interval (cast(rand()*(50-50+1)-50 as int)) minute), '%Y-%m-%d %H:%i:%s') "
                            + "from pemeriksaan_ralan p where p.no_rawat='" + noRawat + "' order by p.jam_rawat limit 1"
                    );

                    if (!datajam.isEmpty()) {
                        try {
                            LocalDateTime ldt = LocalDateTime.parse(datajam, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
                            long timestamp = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                            utc = String.valueOf(api.GetUTCdatetimeAsString());
                            headers.add("x-timestamp", utc);
                            headers.add("x-signature", api.getHmac(utc));
                            headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

                            requestJson = "{"
                                    + "\"kodebooking\": \"" + nobooking + "\","
                                    + "\"taskid\": \"3\","
                                    + "\"waktu\": \"" + timestamp + "\""
                                    + "}";

                            requestEntity = new HttpEntity(requestJson, headers);
                            URL = link + "/antrean/updatewaktu";
                            System.out.println("Task Id 3 : " + URL);
                            System.out.println(" JSON : " + requestJson);
                            root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                            nameNode = root.path("metadata");
                            System.out.println(" code : " + nameNode.path("code").asText());
                            System.out.println(" pesan : " + nameNode.path("message").asText());
                            //JOptionPane.showMessageDialog(null, "Task ID 3: " + nameNode.path("message").asText());

                            if (nameNode.path("code").asText().equals("200")) {
                                //Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='3' and no_rawat='" + noRawat + "'");
                                Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{noRawat, "3", datajam});
                            }
                        } catch (Exception ex) {
                            System.out.println("Notifikasi Bridging : " + ex);
                        }
                    }

                    //TASK ID 4
                    datajam = Sequel.cariIsi(
                            "select str_to_date(date_add(concat(p.tgl_perawatan,' ',p.jam_rawat), interval (cast(rand()*(12-9+1)-9 as int)) minute), '%Y-%m-%d %H:%i:%s') "
                            + "from pemeriksaan_ralan p where p.no_rawat='" + noRawat + "' order by p.jam_rawat limit 1"
                    );
                    if (!datajam.isEmpty()) {
                        try {
                            LocalDateTime ldt = LocalDateTime.parse(datajam, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
                            long timestamp = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                            utc = String.valueOf(api.GetUTCdatetimeAsString());
                            headers.add("x-timestamp", utc);
                            headers.add("x-signature", api.getHmac(utc));
                            headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

                            requestJson = "{"
                                    + "\"kodebooking\": \"" + nobooking + "\","
                                    + "\"taskid\": \"4\","
                                    + "\"waktu\": \"" + timestamp + "\""
                                    + "}";

                            requestEntity = new HttpEntity(requestJson, headers);
                            URL = link + "/antrean/updatewaktu";
                            System.out.println("Task Id 4 : " + URL);
                            System.out.println(" JSON : " + requestJson);
                            root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                            nameNode = root.path("metadata");
                            System.out.println(" code : " + nameNode.path("code").asText());
                            System.out.println(" pesan : " + nameNode.path("message").asText());
                            //JOptionPane.showMessageDialog(null, "Task ID 4: " + nameNode.path("message").asText());

                            if (nameNode.path("code").asText().equals("200")) {
                                //Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='4' and no_rawat='" + noRawat + "'");
                                Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{noRawat, "4", datajam});
                            }
                        } catch (Exception ex) {
                            System.out.println("Notifikasi Bridging : " + ex);
                        }
                    }

                    //TASK ID 5
                    datajam = Sequel.cariIsi(
                            "select str_to_date(date_add(concat(p.tgl_perawatan,' ',p.jam_rawat), interval (cast(rand()*(1-1+1)-1 as int)) minute), '%Y-%m-%d %H:%i:%s') "
                            + "from pemeriksaan_ralan p where p.no_rawat='" + noRawat + "' order by p.jam_rawat limit 1"
                    );

                    if (!datajam.isEmpty()) {
                        try {
                            LocalDateTime ldt = LocalDateTime.parse(datajam, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
                            long timestamp = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                            utc = String.valueOf(api.GetUTCdatetimeAsString());
                            headers.add("x-timestamp", utc);
                            headers.add("x-signature", api.getHmac(utc));
                            headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

                            requestJson = "{"
                                    + "\"kodebooking\": \"" + nobooking + "\","
                                    + "\"taskid\": \"5\","
                                    + "\"waktu\": \"" + timestamp + "\""
                                    + "}";

                            requestEntity = new HttpEntity(requestJson, headers);
                            URL = link + "/antrean/updatewaktu";
                            System.out.println("Task Id 5 : " + URL);
                            System.out.println(" JSON : " + requestJson);
                            root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                            nameNode = root.path("metadata");
                            System.out.println(" code : " + nameNode.path("code").asText());
                            System.out.println(" pesan : " + nameNode.path("message").asText());
                            //JOptionPane.showMessageDialog(null, "Task ID 5: " + nameNode.path("message").asText());
                            if (nameNode.path("code").asText().equals("200")) {
                                //Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='5' and no_rawat='" + noRawat + "'");
                                Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{noRawat, "5", datajam});
                            }
                        } catch (Exception ex) {
                            System.out.println("Notifikasi Bridging : " + ex);
                        }
                    }

                    //KIRIM RESEP
                    noresep = Sequel.cariIsi("select resep_obat.no_resep from resep_obat where resep_obat.no_rawat='" + noRawat + "'");

                    if (!noresep.equals("")) {
                        try {
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                            utc = String.valueOf(api.GetUTCdatetimeAsString());
                            headers.add("x-timestamp", utc);
                            headers.add("x-signature", api.getHmac(utc));
                            headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                            requestJson = "{"
                                    + "\"kodebooking\": \"" + nobooking + "\","
                                    + "\"jenisresep\": \"" + (Sequel.cariInteger("select count(resep_dokter_racikan.no_resep) from resep_dokter_racikan where resep_dokter_racikan.no_resep=?", noresep) > 0 ? "Racikan" : "Non Racikan") + "\","
                                    + "\"nomorantrean\": " + Integer.parseInt(StringUtils.right(noresep, 4)) + ","
                                    + "\"keterangan\": \"Resep dibuat secara elektronik di poli\""
                                    + "}";
                            requestEntity = new HttpEntity(requestJson, headers);
                            URL = link + "/antrean/farmasi/add";
                            System.out.println("Jenis Resep: " + URL);
                            System.out.println(" JSON : " + requestJson);
                            root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                            nameNode = root.path("metadata");
                            System.out.println(" code : " + nameNode.path("code").asText());
                            System.out.println(" pesan : " + nameNode.path("message").asText());
                        } catch (Exception ex) {
                            System.out.println("Notifikasi Bridging : " + ex);
                        }
                    }

                    //TASK ID 6
                    if (Sequel.cariInteger("select count(no_resep) as task6 from resep_obat where resep_obat.status='ralan' and resep_obat.no_rawat='" + noRawat + "' and concat(resep_obat.tgl_perawatan,' ',resep_obat.jam)<>'0000-00-00 00:00:00'") >= 1) {
                        datajam = Sequel.cariIsi("select str_to_date(date_add(concat(referensi_mobilejkn_bpjs_taskid.waktu), interval (cast(rand()*(360-120+1)+120 as int)) second), '%Y-%m-%d %H:%i:%s') as task6 from referensi_mobilejkn_bpjs_taskid where referensi_mobilejkn_bpjs_taskid.no_rawat='" + noRawat + "' and referensi_mobilejkn_bpjs_taskid.taskid='5'");
                    } else {
                        datajam = "";
                    }

                    if (!datajam.isEmpty()) {
                        try {
                            LocalDateTime ldt = LocalDateTime.parse(datajam, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
                            long timestamp = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                            utc = String.valueOf(api.GetUTCdatetimeAsString());
                            headers.add("x-timestamp", utc);
                            headers.add("x-signature", api.getHmac(utc));
                            headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

                            requestJson = "{"
                                    + "\"kodebooking\": \"" + nobooking + "\","
                                    + "\"taskid\": \"6\","
                                    + "\"waktu\": \"" + timestamp + "\""
                                    + "}";

                            requestEntity = new HttpEntity(requestJson, headers);
                            URL = link + "/antrean/updatewaktu";
                            System.out.println("Task Id 6 : " + URL);
                            System.out.println(" JSON : " + requestJson);
                            root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                            nameNode = root.path("metadata");
                            System.out.println(" code : " + nameNode.path("code").asText());
                            System.out.println(" pesan : " + nameNode.path("message").asText());
                            //JOptionPane.showMessageDialog(null, "Task ID 6: " + nameNode.path("message").asText());
                            if (nameNode.path("code").asText().equals("200")) {
                                //Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='6' and no_rawat='" + noRawat + "'");
                                Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{noRawat, "6", datajam});
                            }
                        } catch (Exception ex) {
                            System.out.println("Notifikasi Bridging : " + ex);
                        }
                    }
                    //TASK ID 7
                    datajam = Sequel.cariIsi("select str_to_date(date_add(concat(referensi_mobilejkn_bpjs_taskid.waktu), interval (cast(rand()*(55-11+1)+11 as int)) minute), '%Y-%m-%d %H:%i:%s') as task7 from referensi_mobilejkn_bpjs_taskid WHERE referensi_mobilejkn_bpjs_taskid.no_rawat='" + noRawat + "' and referensi_mobilejkn_bpjs_taskid.taskid='6'");

                    if (!datajam.isEmpty()) {
                        try {
                            LocalDateTime ldt = LocalDateTime.parse(datajam, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
                            long timestamp = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                            utc = String.valueOf(api.GetUTCdatetimeAsString());
                            headers.add("x-timestamp", utc);
                            headers.add("x-signature", api.getHmac(utc));
                            headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

                            requestJson = "{"
                                    + "\"kodebooking\": \"" + nobooking + "\","
                                    + "\"taskid\": \"7\","
                                    + "\"waktu\": \"" + timestamp + "\""
                                    + "}";

                            requestEntity = new HttpEntity(requestJson, headers);
                            URL = link + "/antrean/updatewaktu";
                            System.out.println("Task Id 7 : " + URL);
                            System.out.println(" JSON : " + requestJson);
                            root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                            nameNode = root.path("metadata");
                            System.out.println(" code : " + nameNode.path("code").asText());
                            System.out.println(" pesan : " + nameNode.path("message").asText());
                            //JOptionPane.showMessageDialog(null, "Task ID 7: " + nameNode.path("message").asText());
                            if (nameNode.path("code").asText().equals("200")) {
                                //Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='7' and no_rawat='" + noRawat + "'");
                                Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{noRawat, "7", datajam});
                            }
                        } catch (Exception ex) {
                            System.out.println("Notifikasi Bridging : " + ex);
                        }
                    }

                    //TASK ID 99 Batal
                    datajam = Sequel.cariIsi("select now() from reg_periksa where reg_periksa.stts='Batal' and reg_periksa.no_rawat=?", noRawat);
                    if (!datajam.equals("")) {
                        try {
                            LocalDateTime ldt = LocalDateTime.parse(datajam, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
                            long timestamp = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                            utc = String.valueOf(api.GetUTCdatetimeAsString());
                            headers.add("x-timestamp", utc);
                            headers.add("x-signature", api.getHmac(utc));
                            headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                            requestJson = "{"
                                    + "\"kodebooking\": \"" + nobooking + "\","
                                    + "\"taskid\": \"99\","
                                    + "\"waktu\": \"" + timestamp + "\""
                                    + "}";
                            requestEntity = new HttpEntity(requestJson, headers);
                            URL = link + "/antrean/updatewaktu";
                            System.out.println("Task Id 99 : " + URL);
                            System.out.println(" JSON : " + requestJson);
                            root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                            nameNode = root.path("metadata");
                            System.out.println(" code : " + nameNode.path("code").asText());
                            System.out.println(" pesan : " + nameNode.path("message").asText());
                            //JOptionPane.showMessageDialog(null, "Task ID 99 Batal: " + nameNode.path("message").asText());
                            if (nameNode.path("code").asText().equals("200")) {
                                //Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='99' and no_rawat='" + noRawat + "'");
                                Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{noRawat, "2", datajam});
                            }
                        } catch (Exception ex) {
                            System.out.println("Notifikasi Bridging : " + ex);
                        }
                    }
                }
            } catch (Exception ex) {
                // tangani error per baris, tapi teruskan loop
                System.out.println("Error saat memproses baris " + i + " : " + ex);
            }
        }
    }
    
    private void runBackground(Runnable task) {
        if (ceksukses) return;
        ceksukses = true;

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

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
    }

    @Override
    public void dispose() {
        executor.shutdownNow();
        super.dispose();
    }
}
