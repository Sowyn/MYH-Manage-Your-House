package manageyourhouse.myh_manageyourhouse;

import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MyAdapterParametres extends RecyclerView.Adapter<MyAdapterParametres.MyViewHolder> {

    List<Pieces> Pieces = Arrays.asList(MainActivity.Salon, MainActivity.Toilette, MainActivity.Chambre);

    private final List<String> characters = Arrays.asList(
            MainActivity.Salon.getName(),
            MainActivity.Toilette.getName(),
            MainActivity.Chambre.getName()
    );

    @Override
    public int getItemCount() {
        return characters.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_cell, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String piece = characters.get(position);
        holder.display(piece);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        final private TextView name;
        private String currentPair;


        public MyViewHolder(final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);

            final ArrayList seletedItems = new ArrayList();
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Pieces piece = Pieces.get(1);
                    for (int i = 0; i <Pieces.size(); i++){
                        if (currentPair == Pieces.get(i).getName()){
                            piece = Pieces.get(i);
                        }
                    }
                    final boolean[] Notif = new boolean[]{piece.getNotifiation()};
                    final CharSequence[] items = {"Notification"};
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(itemView.getContext());
                    LayoutInflater factory = LayoutInflater.from(itemView.getContext());
                    final View dialogView = factory.inflate(R.layout.alertdialog, null);
                    final EditText edit=(EditText) dialogView.findViewById(R.id.minutes);
                    String min = String.valueOf(piece.getMinutes());
                    edit.setText(min);
                    dialogBuilder.setView(dialogView);
                    dialogBuilder.setTitle(currentPair);
                    dialogBuilder.setMultiChoiceItems(items, Notif, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                            if (isChecked) {
                                // If the user checked the item, add it to the selected items
                                Pieces piece = Pieces.get(1);
                                for (int i = 0; i <Pieces.size(); i++){
                                    if (currentPair == Pieces.get(i).getName()){
                                        piece = Pieces.get(i);
                                    }
                                }
                                String text = edit.getText().toString();
                                piece.setMinutes(Integer.parseInt(text));
                                piece.setNotification(true);
                                if(piece.getEtat()==true) {
                                    ServiceSocketSonnette.scheduleNotification(itemView.getContext(), piece.getMinutes() * 60000);
                                }
                                seletedItems.add(indexSelected);
                            }
                            else if (seletedItems.contains(indexSelected)) {
                                // Else, if the item is already in the array, remove it
                                Pieces piece = Pieces.get(1);
                                for (int i = 0; i <Pieces.size(); i++){
                                    if (currentPair == Pieces.get(i).getName()){
                                        piece = Pieces.get(i);
                                    }
                                }
                                seletedItems.remove(Integer.valueOf(indexSelected));
                                piece.setNotification(false);
                            }
                        }
                    });

                    dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Pieces piece = Pieces.get(1);
                            for (int i = 0; i <Pieces.size(); i++){
                                if (currentPair == Pieces.get(i).getName()){
                                    piece = Pieces.get(i);
                                }
                            }
                            String text = edit.getText().toString();
                            piece.setMinutes(Integer.parseInt(text));
                        }
                    });
                    AlertDialog b = dialogBuilder.create();
                    b.show();
                    if (seletedItems.size() != 0) {
                        piece.setNotification(true);
                    }
                    else{
                        piece.setNotification(false);
                    }
                }
            });

        }

        public void display(String pair) {
            currentPair = pair;
            name.setText(pair);
        }
    }
}
