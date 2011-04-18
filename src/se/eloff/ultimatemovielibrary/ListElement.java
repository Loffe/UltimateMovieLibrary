package se.eloff.ultimatemovielibrary;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;

public class ListElement extends JComponent {

    private Movie movie;
    /** Creates new form TheGui */
    public ListElement(Movie movie) {
        this.movie = movie;
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        PlayButton = new javax.swing.JButton();
        SeenButton = new javax.swing.JButton();
        TitleLabel = new javax.swing.JLabel();
        YearLabel = new javax.swing.JLabel();
        //RatingRadio = new javax.swing.JRadioButton();
        RatingRadio = new RatingButton();
        CoverPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

       // setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PlayButton.setText("Play");
        PlayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlayButtonActionPerformed(evt);
            }
        });

        SeenButton.setText("Sedd/Osedd");
        SeenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SeenButtonActionPerformed(evt);
            }
        });

        TitleLabel.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        TitleLabel.setText(movie.getName());
        TitleLabel.setAlignmentY(0.0F);

        YearLabel.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        YearLabel.setText(Integer.toString(movie.getYear()));
        YearLabel.setAlignmentY(0.0F);

        RatingRadio.setText("Rating");
        RatingRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RatingRadioActionPerformed(evt);
            }
        });

        CoverPanel.setBackground(new java.awt.Color(255, 238, 238));

        GroupLayout CoverPanelLayout = new GroupLayout(CoverPanel);
        CoverPanel.setLayout(CoverPanelLayout);
        CoverPanelLayout.setHorizontalGroup(
            CoverPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        CoverPanelLayout.setVerticalGroup(
            CoverPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jLabel1.setText("219 min - Action | Adventure | Sci-fi");
        
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CoverPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(YearLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(RatingRadio)
                                .addContainerGap())
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(TitleLabel, GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                                .addGap(152, 152, 152))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 253, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(PlayButton, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(SeenButton, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(CoverPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(TitleLabel)
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(RatingRadio)
                            .addComponent(YearLabel))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(PlayButton)
                            .addComponent(SeenButton))))
                .addContainerGap())
        );

        //pack();
    }// </editor-fold>

    private void PlayButtonActionPerformed(java.awt.event.ActionEvent evt) {
        //TODO better way to use the ExternalPlayerLauncher, dont instance it here
        new ExternalPlayerLauncher().playMovie(movie);
    }

    private void RatingRadioActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void SeenButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    // Variables declaration - do not modify
    private javax.swing.JPanel CoverPanel;
    private javax.swing.JButton PlayButton;
    private RatingButton RatingRadio;
    private javax.swing.JButton SeenButton;
    private javax.swing.JLabel TitleLabel;
    private javax.swing.JLabel YearLabel;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration
}
