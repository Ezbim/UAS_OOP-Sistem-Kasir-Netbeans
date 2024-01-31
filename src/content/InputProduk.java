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
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author ASUS
 */
public class InputProduk extends javax.swing.JPanel {

   private Connection con;

    private Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/sistemkasir";
            String user = "root";
            String pass = "";
            return DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "KONEKSI ERROR: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
            return null; // unreachable, but required for compilation
        }
    }

    public InputProduk() {
        initComponents();

        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = jTable1.getSelectedRow();
                    if (selectedRow != -1) {
                        // Populate input fields with selected row data
                        idProduk.setText(jTable1.getValueAt(selectedRow, 0).toString());
                        namaProduk.setText(jTable1.getValueAt(selectedRow, 1).toString());
                        jumlahStok.setText(jTable1.getValueAt(selectedRow, 2).toString());
                        harga.setText(jTable1.getValueAt(selectedRow, 3).toString());
                    }
                }
            }
        });

        try {
            con = connect();
            RefreshTableProduk();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "KONEKSI ERROR: " + ex.getMessage());
            System.exit(1);
        }
    }

    private void insertProduk(int productId, String productName, int stock, int price) {
        try {
            String sql = "INSERT INTO produk (id_produk, nama_produk, jumlah_stok, harga) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                preparedStatement.setInt(1, productId);
                preparedStatement.setString(2, productName);
                preparedStatement.setInt(3, stock);
                preparedStatement.setInt(4, price);
                preparedStatement.executeUpdate();
            }
            RefreshTableProduk();
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(null, "Produk dengan ID tersebut sudah ada dalam database.");
            } else {
                JOptionPane.showMessageDialog(null, "Gagal menambahkan produk. Error: " + ex.getMessage());
            }
            ex.printStackTrace();
        }
    }

   

    private void editProduk(int productId, String productName, int stock, int price) {
        try (Connection connection = connect()) {
            String sql = "UPDATE produk SET nama_produk=?, jumlah_stok=?, harga=? WHERE id_produk=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, productName);
                preparedStatement.setInt(2, stock);
                preparedStatement.setInt(3, price);
                preparedStatement.setInt(4, productId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        RefreshTableProduk();
    }

    private void deleteProduk(int productId) {
        try (Connection connection = connect()) {
            String sql = "DELETE FROM produk WHERE id_produk=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, productId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        RefreshTableProduk();
    }

    public void RefreshTableProduk() {
        try (Connection connection = connect()) {
            String sql = "SELECT id_produk, nama_produk, jumlah_stok, harga FROM produk";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                    model.setRowCount(0); // Clear existing rows

                    while (resultSet.next()) {
                        int productId = resultSet.getInt("id_produk");
                        String productName = resultSet.getString("nama_produk");
                        int stock = resultSet.getInt("jumlah_stok");
                        int price = resultSet.getInt("harga");

                        Object[] row = {productId, productName, stock, price};
                        model.addRow(row);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        namaProduk = new javax.swing.JTextField();
        jumlahStok = new javax.swing.JTextField();
        harga = new javax.swing.JTextField();
        edit = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        create = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        idProduk = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        jLabel7.setText("contoh : 5");

        setPreferredSize(new java.awt.Dimension(552, 443));

        jPanel1.setPreferredSize(new java.awt.Dimension(552, 443));
        jPanel1.setRequestFocusEnabled(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Input Produk");

        jLabel2.setText("Nama Produk");

        jLabel3.setText("Harga Satuan");

        jLabel4.setText("Jumlah Stock");

        namaProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaProdukActionPerformed(evt);
            }
        });

        jumlahStok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jumlahStokActionPerformed(evt);
            }
        });

        harga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hargaActionPerformed(evt);
            }
        });

        edit.setText("Edit");
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });

        delete.setText("Delete");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        create.setText("Tambah");
        create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "id_produk", "nama_produk", "jumlah_stok", "harga_satuan"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel5.setText("ID Produk");

        idProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idProdukActionPerformed(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("contoh : 1123");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("contoh : bakso");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("contoh: 100");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("contoh: 10000");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(203, 203, 203)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(231, 231, 231)
                        .addComponent(delete)
                        .addGap(51, 51, 51)
                        .addComponent(create))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel4))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(edit)
                                            .addComponent(jLabel3))
                                        .addGap(3, 3, 3)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jumlahStok)
                                    .addComponent(namaProduk)
                                    .addComponent(idProduk)
                                    .addComponent(harga, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(idProduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(namaProduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jumlahStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(harga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edit)
                    .addComponent(delete)
                    .addComponent(create))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    
    
    private void idProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idProdukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idProdukActionPerformed

    private void namaProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaProdukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaProdukActionPerformed

    private void jumlahStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jumlahStokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jumlahStokActionPerformed

    private void hargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hargaActionPerformed
    private void clearInputFields() {
    idProduk.setText("");
    namaProduk.setText("");
    jumlahStok.setText("");
    harga.setText("");
}
    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
           try {
        int productId = Integer.parseInt(idProduk.getText());
        String productName = namaProduk.getText();
        int stock = Integer.parseInt(jumlahStok.getText());
        int price = Integer.parseInt(harga.getText());

     
        editProduk(productId, productName, stock, price);

        
        clearInputFields();
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Masukkan nilai numerik yang valid untuk ID Produk.");
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Gagal mengedit produk. Error: " + ex.getMessage());
    }
    }//GEN-LAST:event_editActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
       try {
       
        int productId = Integer.parseInt(idProduk.getText());

        deleteProduk(productId);
        clearInputFields();
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Masukkan nilai numerik yang valid untuk ID Produk.");
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Gagal menghapus produk. Error: " + ex.getMessage());
    }
    }//GEN-LAST:event_deleteActionPerformed

    private void createActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createActionPerformed
         try {
        int productId = Integer.parseInt(idProduk.getText());
        String productName = namaProduk.getText();
        int stock = Integer.parseInt(jumlahStok.getText());
        int price = Integer.parseInt(harga.getText());

  
        insertProduk(productId, productName, stock, price);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Masukkan nilai numerik yang valid untuk ID Produk.");
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Gagal menambahkan produk baru. Error: " + ex.getMessage());
    }
    }//GEN-LAST:event_createActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton create;
    private javax.swing.JButton delete;
    private javax.swing.JButton edit;
    private javax.swing.JTextField harga;
    private javax.swing.JTextField idProduk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jumlahStok;
    private javax.swing.JTextField namaProduk;
    // End of variables declaration//GEN-END:variables
}
