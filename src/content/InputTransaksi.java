/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package content;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Timestamp;
import java.util.Date;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class InputTransaksi extends javax.swing.JPanel {

    public InputTransaksi() {
        initComponents();
        addTableModelListener();
    }

    Connection con = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    Statement statBrg;
    Boolean ada = false;

    private void koneksi() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/sistemkasir";
            String user = "root";
            String pass = "";
            con = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error establishing database connection: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void insertTransaksi(int total) {
        try {

            koneksi();
            Timestamp timestamp = new Timestamp(new Date().getTime());

            String transaksiQuery = "INSERT INTO transaksi (total, created_at) VALUES (?, ?)";
            try (PreparedStatement transaksiStatement = con.prepareStatement(transaksiQuery,
                    Statement.RETURN_GENERATED_KEYS)) {
                transaksiStatement.setInt(1, total);
                transaksiStatement.setTimestamp(2, timestamp);

                transaksiStatement.executeUpdate();

                int idTransaksi;
                try (ResultSet generatedKeys = transaksiStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idTransaksi = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Masukan ID");
                    }
                }

                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                int rowCount = model.getRowCount();

                for (int i = 0; i < rowCount; i++) {
                    int idProduk = (int) model.getValueAt(i, 0);
                    int harga = (int) model.getValueAt(i, 2);
                    int jumlah = (int) model.getValueAt(i, 3);

                    String barangTransaksiQuery = "INSERT INTO barang_transaksi (id_transaksi, id_produk, harga, jumlah) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement barangTransaksiStatement = con.prepareStatement(barangTransaksiQuery)) {
                        barangTransaksiStatement.setInt(1, idTransaksi);
                        barangTransaksiStatement.setInt(2, idProduk);
                        barangTransaksiStatement.setInt(3, harga);
                        barangTransaksiStatement.setInt(4, jumlah);

                        barangTransaksiStatement.executeUpdate();
                    }

                    updateStockInProdukTable(idProduk, jumlah);
                }

                con.commit();

            } catch (SQLException exc) {
                con.rollback();
                System.err.println(exc.getMessage());
            } finally {

                con.close();
            }
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
        }
    }

    private void updateStockInProdukTable(int idProduk, int jumlah) {
        try {
            String updateStockQuery = "UPDATE produk SET jumlah_stok = jumlah_stok - ? WHERE id_produk = ?";
            try (PreparedStatement updateStockStatement = con.prepareStatement(updateStockQuery)) {
                updateStockStatement.setInt(1, jumlah);
                updateStockStatement.setInt(2, idProduk);

                updateStockStatement.executeUpdate();
            }
        } catch (SQLException exc) {
            System.err.println(exc.getMessage());
        }
    }

    private void updateRowValues(int row) {
        try {
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            int idProduk = (int) model.getValueAt(row, 0);
            int jumlah = (int) model.getValueAt(row, 3);

            String query = "SELECT *, harga FROM produk WHERE id_produk = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, idProduk);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                String namaProduk = result.getString("nama_produk");
                int hargaProduk = result.getInt("harga");
                int total = hargaProduk * jumlah;

                model.setValueAt(namaProduk, row, 1);
                model.setValueAt(hargaProduk, row, 2);
                model.setValueAt(total, row, 4);

                updateTotalLabel();
            } else {

                JOptionPane.showMessageDialog(this, "Barang Tidak Tersedia");
            }

            result.close();
            preparedStatement.close();
        } catch (Exception exc) {
            JOptionPane.showMessageDialog(this, "Barang Tidak Tersedia");
            System.err.println(exc.getMessage());
        }
    }

    private void addTableModelListener() {
        jTable1.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (column == 0 || column == 3) {
                    updateRowValues(row);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRadioButton1 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        jRadioButton1.setText("jRadioButton1");

        setPreferredSize(new java.awt.Dimension(552, 443));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Input Transaksi");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel2.setText("Kode Barang");

        jLabel5.setText("Jumlah");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "id_barang", "nama barang", "harga", "jumlah", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setAutoscrolls(false);
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Tambahkan");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Barang Belanjaan :");

        jButton2.setText("PAYMENT");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel4.setText("Total belanjaan");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("0");

        jButton3.setText("Delete");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel7.setText("contoh : 5");

        jLabel8.setText("contoh : 11245");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 43, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(65, 65, 65))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                                .addGap(116, 116, 116))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton1)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(38, 38, 38)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1)))
                .addGap(2, 2, 2)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jButton3))
                .addGap(1, 1, 1)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(2, 2, 2)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap(19, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private boolean isStockAvailable(int idProduk, int jumlah) throws SQLException {
        String stockCheckQuery = "SELECT jumlah_stok FROM produk WHERE id_produk = ?";
        try (PreparedStatement stockCheckStatement = con.prepareStatement(stockCheckQuery)) {
            stockCheckStatement.setInt(1, idProduk);
            ResultSet stockResult = stockCheckStatement.executeQuery();

            if (stockResult.next()) {
                int availableStock = stockResult.getInt("jumlah_stok");
                return availableStock >= jumlah;
            }
        }

        return false;
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
        koneksi();

        
        String idProdukText = jTextField1.getText();
        if (!idProdukText.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Kode Barang harus angka.");
            return; 
        }
        int idProduk = Integer.parseInt(idProdukText);

      
        String jumlahText = jTextField2.getText();
        if (!jumlahText.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Jumlah harus angka.");
            return; 
        }
        int jumlah = Integer.parseInt(jumlahText);

     
        if (isStockAvailable(idProduk, jumlah)) {
            
            String query = "SELECT *, harga FROM produk WHERE id_produk = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, idProduk);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                int idBarang = result.getInt("id_produk");
                String namaProduk = result.getString("nama_produk");
                int hargaProduk = result.getInt("harga");
                int total = hargaProduk * jumlah;

                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.insertRow(0, new Object[] { idBarang, namaProduk, hargaProduk, jumlah, total });
                updateTotalLabel();

                jTextField1.setText("");
                jTextField2.setText("");
            } else {
             
                JOptionPane.showMessageDialog(this, "Barang Tidak Tersedia");
            }

          
            result.close();
            preparedStatement.close();
        } else {
          
            JOptionPane.showMessageDialog(this, "Stok Tidak Tersedia, Silahkan Cek Ulang");
        }

    } catch (NumberFormatException nfe) {
       
        JOptionPane.showMessageDialog(this, "Masukkan angka yang valid.");
    } catch (Exception exc) {
        JOptionPane.showMessageDialog(this, "Barang Tidak Tersedia");
        System.err.println(exc.getMessage());
    }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
         try {
        updateTotalLabel();

        
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int rowCount = model.getRowCount();
        int total = Integer.parseInt(jLabel6.getText());

        if (rowCount > 0 && total > 0) {
         
            insertTransaksi(total);

            model.setRowCount(0);
            jLabel6.setText("0");
            JOptionPane.showMessageDialog(this, "PAYMENT BERHASIL");
        } else {
          
            JOptionPane.showMessageDialog(this, "Tidak Ada Barang Untuk Dibayar atau Total Belanjaan 0");
        }
    } catch (Exception exc) {
        System.err.println(exc.getMessage());
    }
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.removeRow(selectedRow);
            updateTotalLabel();
        } else {
            JOptionPane.showMessageDialog(this, "Pilih Baris Untuk Dihapus");
        } 
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
        
    }

    private void updateTotalLabel() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int rowCount = model.getRowCount();
        int total = 0;

        for (int i = 0; i < rowCount; i++) {
            Object value = model.getValueAt(i, 4);

            
            if (value != null && value instanceof Integer) {
                total += ((Integer) value).intValue();
            }
        }

        jLabel6.setText(String.valueOf(total));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
