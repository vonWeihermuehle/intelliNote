package net.mbmedia.intellinote;

import javax.swing.*;

public class BaseGui extends JPanel {

    protected boolean edit = false;

    public boolean isEdited(){
        return this.edit;
    }

    public void resetEditState(){
        this.edit = false;
    }


}
