package se.eloff.ultimatemovielibrary;

import javax.swing.JToggleButton;

public class SortButton extends JToggleButton {

    private static final long serialVersionUID = -904152409006570238L;

    private String columnName;

    enum State {
        Asc, Desc, Unsorted
    }

    public SortButton(String label, String column, State defaultOrder) {
        super(label);
        columnName = column;
        setBorderPainted(false);
        setModel(new SortOrderButtonModel(defaultOrder));
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
        private State defaultOrder;

        public SortOrderButtonModel(State defaultOrder) {
            this.defaultOrder = defaultOrder;
            state = State.Unsorted;
        }

        public void setUnselected() {
            super.setSelected(false);
            state = State.Unsorted;
            setIcon(null);
        }

        @Override
        public void setSelected(boolean b) {
            switch (state) {
            case Asc:
                state = State.Desc;
                break;
            case Desc:
                state = State.Asc;
                break;
            case Unsorted:
                state = defaultOrder;
                break;
            }

            if (state == State.Asc)
                setIcon(Localization.resultPanelArrowDown);
            else
                setIcon(Localization.resultPanelArrowUp);

            super.setSelected(true);
        }
    }
}
