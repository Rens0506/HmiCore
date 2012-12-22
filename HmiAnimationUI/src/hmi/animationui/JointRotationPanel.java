package hmi.animationui;

import java.awt.Dimension;
import java.awt.GridLayout;

import hmi.math.Quat4f;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import lombok.Getter;

/**
 * UI element for the rotation of a single joint
 * @author hvanwelbergen
 */
public class JointRotationPanel
{
    @Getter
    private final JPanel panel = new JPanel();
    private final String jointName;
    private final JSlider pitchSlider;
    private final JSlider yawSlider;
    private final JSlider rollSlider;
    private final Viewer jointView;

    private void setupSlider(final JSlider s)
    {
        JPanel sliderPanel = new JPanel();
        final JLabel sliderLabel = new JLabel("0");
        sliderLabel.setPreferredSize(new Dimension(30, 20));
        s.setPreferredSize(new Dimension(100, 20));
        sliderPanel.add(sliderLabel);
        sliderPanel.add(s);
        panel.add(sliderPanel);
        s.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                jointView.update();
                sliderLabel.setText(""+s.getValue());
            }
        });
    }
    
    public JointRotationPanel(String jointName, Viewer jointView)
    {
        this.jointName = jointName;
        this.jointView = jointView;
        JLabel label = new JLabel(jointName);
        panel.setLayout(new GridLayout());
        panel.add(label);

        pitchSlider = new JSlider(JSlider.HORIZONTAL, -180, 180, 0);
        yawSlider = new JSlider(JSlider.HORIZONTAL, -180, 180, 0);
        rollSlider = new JSlider(JSlider.HORIZONTAL, -180, 180, 0);
        
        setupSlider(pitchSlider);
        setupSlider(yawSlider);
        setupSlider(rollSlider);
    }
    
    public int getPitch()
    {
        return pitchSlider.getValue();
    }
    
    public int getYaw()
    {
        return yawSlider.getValue();
    }
    
    public int getRoll()
    {
        return rollSlider.getValue();
    }    

    public JointRotationConfiguration getRotationConfiguration()
    {
        float q[] = Quat4f.getQuat4f();
        Quat4f.setFromRollPitchYawDegrees(q, rollSlider.getValue(), pitchSlider.getValue(), yawSlider.getValue());
        return new JointRotationConfiguration(jointName, q);
    }
}
