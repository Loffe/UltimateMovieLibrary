package se.eloff.ultimatemovielibrary;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class homePanel extends JPanel{
    
    
    private JButton searchItem = new JButton(Localization.menuSearchText);
    private JButton recommendItem = new JButton(Localization.menuRecommendText);
    private JButton profileItem = new JButton(Localization.menuProfileText);
    
    private final int xsize = 600;
    private final int ysize = 150;
    
    private final int gapsize = 100;
    
    
    homePanel(){
        
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
     
        searchItem.setAlignmentX(CENTER_ALIGNMENT);
        recommendItem.setAlignmentX(CENTER_ALIGNMENT);
        profileItem.setAlignmentX(CENTER_ALIGNMENT);
        
        searchItem.setMinimumSize(new Dimension(xsize, ysize));
        recommendItem.setMinimumSize(new Dimension(xsize, ysize));
        profileItem.setMinimumSize(new Dimension(xsize, ysize));
        
        searchItem.setPreferredSize(new Dimension(xsize, ysize));
        profileItem.setPreferredSize(new Dimension(xsize, ysize));
        recommendItem.setPreferredSize(new Dimension(xsize, ysize));

        searchItem.setMaximumSize(new Dimension(xsize, ysize));
        recommendItem.setMaximumSize(new Dimension(xsize, ysize));
        profileItem.setMaximumSize(new Dimension(xsize, ysize));
        
        Font f = new Font(searchItem.getFont().getName(), searchItem.getFont().getStyle(), 40);
       
        searchItem.setFont(f);
        recommendItem.setFont(f);
        profileItem.setFont(f);
        
        
        
        this.add(Box.createVerticalGlue());

        this.add(searchItem);
        this.add(Box.createRigidArea(new Dimension(0, gapsize)));
        this.add(recommendItem);
        this.add(Box.createRigidArea(new Dimension(0, gapsize)));
        this.add(profileItem);
        this.add(Box.createVerticalGlue());
        
    }


}
