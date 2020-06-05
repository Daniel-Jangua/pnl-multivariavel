import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.util.Vector;

public class Janela extends JFrame{
    
    JTextField txtFx, txtN, txtEpsilon,txtFxF;
    JButton btnGo, btnClear;
    String metodos[] = {"Coordenadas Cíclicas","Hooke e Jeeves","Gradiente Descendente","Newton",
                        "Gradiente Conjugado","Fletcher e Reeves","Davidon-Fletcher-Powell"};
    JComboBox<String> cbMet;
    JTable tblX0, tblXF;
    JLabel lblFx,lblN,lblE,lblFxF,lblIt;
    Image imgGo, imgClear;
    JPanel pnSup,pnInf;
    JScrollPane pnX0,pnXF;
    int n = 0;
    Janela(String titulo){
        super(titulo);
       
        //intanciando labels
        lblFx = new JLabel("f(x) = ");
        lblN = new JLabel("n = ");
        lblE = new JLabel("ε = ");
        lblFxF = new JLabel("f(x*) = ");
        lblIt = new JLabel("Iterações = 0");
        //lendo imagens de ícone
        try {
            imgGo = ImageIO.read(getClass().getResource("Imagens/forward.gif"));
            imgClear = ImageIO.read(getClass().getResource("Imagens/delete.gif"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Imagens não encontradas!","Erro",JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        //instanciando botoes
        btnGo = new JButton();
        btnGo.setIcon(new ImageIcon(imgGo));
        btnGo.setContentAreaFilled(false);
        btnClear = new JButton();
        btnClear.setIcon(new ImageIcon(imgClear));
        btnClear.setContentAreaFilled(false);
        //instanciando textfields
        txtFx = new JTextField();
        txtN = new JTextField();
        txtEpsilon = new JTextField();
        txtFxF = new JTextField();
        //instanciando combobox
        cbMet = new JComboBox<String>(metodos);
        //instanciando tables
        tblX0 = new JTable();
        tblXF = new JTable();
        criaTabela(tblX0, n, "x⁰[i]");
        criaTabela(tblXF, n, "x*[i]");
        //instanciando panels
        pnSup = new JPanel(null);
        pnInf = new JPanel(null);
        pnX0 = new JScrollPane(tblX0, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pnXF = new JScrollPane(tblXF, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        //definindo dimensões e posições - pnSup
        lblFx.setBounds(10, 20, 40, 16);
        txtFx.setBounds(55, 20, 250, 20);
        lblN.setBounds(318, 20, 30, 16);
        txtN.setBounds(350, 20, 64, 20);
        lblE.setBounds(427, 20, 30, 16);
        txtEpsilon.setBounds(458, 20, 64, 20);
        btnGo.setBounds(532, 20, 25, 20);
        btnClear.setBounds(532, 50, 25, 20);
        cbMet.setBounds(55, 50, 250, 20);
        tblX0.setBounds(0, 0, 547, 60);
        tblX0.setRowHeight(20);
        tblX0.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        pnX0.setBounds(10, 80, tblX0.getWidth(), tblX0.getHeight());
        //adicionando itens no panel
        pnSup.add(lblFx);
        pnSup.add(txtFx);
        pnSup.add(lblN);
        pnSup.add(txtN);
        pnSup.add(lblE);
        pnSup.add(txtEpsilon);
        pnSup.add(btnGo);
        pnSup.add(btnClear);
        pnSup.add(cbMet);
        pnSup.add(pnX0);
        pnSup.setBorder(BorderFactory
        .createTitledBorder("Função, método, ponto inicial e precisão"));
        UIManager.put("TitledBorder.border", new LineBorder(new Color(0, 0, 0), 1));
        pnSup.setBounds(10, 10, 567, 170);
        JFrame isso = this;
        //quando tirar foco do txtN atualiza a tabela
        txtN.addFocusListener(new FocusAdapter(){
            public void focusLost(FocusEvent e){
                int old_n = n;
                try{
                    n = Integer.parseInt(txtN.getText());
                    if(n < 0)
                        throw new Exception();
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(isso, "Valor de n inválido! Insira um valor válido!","Atenção!",JOptionPane.WARNING_MESSAGE);
                    txtN.grabFocus();
                    return;
                }
                if(old_n != n)
                    criaTabela(tblX0, n, "x⁰[i]");
            }
        });

        //definições da janela
        this.setLayout(null);
        this.add(pnSup);
        this.setSize(587,300);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    void criaTabela(JTable t,int numColunas,String varName){
        Vector<String> id = new Vector<String>();
        id.add("i");
        for(int i = 1; i < numColunas+1; i++){// num colunas
            id.add(""+i);
        }
        DefaultTableModel dtm = new DefaultTableModel(1, numColunas+1) {
            public boolean isCellEditable(int row, int column) {
                if(varName.equals("x⁰[i]") && column != 0)
                    return true;
                return false;
            }
        };
        
        t.setModel(dtm);
        DefaultTableModel model = (DefaultTableModel)t.getModel();
        model.setColumnIdentifiers(id);
        model.setValueAt(varName, 0, 0);
    
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
        for(int x=1;x< numColunas+1;x++){// - num colunas
            t.getColumnModel().getColumn(x).setCellRenderer( centerRenderer );
            t.getColumnModel().getColumn(x).setPreferredWidth(100);
        }
        DefaultTableCellRenderer colorRenderer = new DefaultTableCellRenderer();
        colorRenderer.setHorizontalAlignment( SwingConstants.CENTER );
        colorRenderer.setBackground(Color.LIGHT_GRAY);
        t.getColumnModel().getColumn(0).setCellRenderer( colorRenderer );
    }

    public static void main(String args[]){
        new Janela("Trabalho PO II - PNL Multivariável");
    }

}