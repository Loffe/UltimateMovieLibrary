package se.eloff.ultimatemovielibrary;

import javax.swing.JToggleButton;

public class SortButton extends JToggleButton {

    private static final long serialVersionUID = -904152409006570238L;

    private String columnName;

    enum State {
        Asc, Desc, Unsorted
    }

    public SortButton(String label, String column) {
        super(label);
        columnName = column;
        setBorderPainted(false);
        setModel(new SortOrderButtonModel());
    }

    public void setUnselected() {
        ((SortOrderButtonModel) getModel()).setUnselected();
    }

    public String getColumnName() {
        return columnName;
    }

    public boolean isOrderAscending() {
        return ((SortOrderButtonModel) getModel()).state == State.Asc;
    }

    class SortOrderButtonModel extends JToggleButton.ToggleButtonModel {

        private static final long serialVersionUID = 844969173203386981L;

        private State state;

        public SortOrderButtonModel() {
            state = State.Unsorted;
        }

        public void setUnselected() {
            super.setSelected(false);
        }

        @Override
        public void setSelected(boolean b) {
            switch (state) {
            case Asc:
                state = State.Desc;
                setIcon(Localization.resultPanelArrowDown);
                break;
            case Desc:
            default:
                state = State.Asc;
                setIcon(Localization.resultPanelArrowUp);
            }
            super.setSelected(true);
        }
    }
}
