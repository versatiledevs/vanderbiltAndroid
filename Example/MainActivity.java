package edu.mtsu.csci3033.groceryimproved;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.text.Layout;
        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.app.ListActivity;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.PopupMenu;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final String GROCERIES = "items";

    private EditText itemEditText;
    private SharedPreferences savedItems;
    private ArrayList<String> ITEMS;
    private ArrayList<String> People;
    private ArrayList<CustomObject> objectList;
    private ArrayAdapter<CustomObject>  adapter;
    private TextView anchor;

    //private ListView christList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView christList =(ListView) findViewById(R.id.listView);

        savedItems = getSharedPreferences("items", MODE_PRIVATE);



        People = new ArrayList<String>(savedItems.getAll().keySet());
        Collections.sort(People, String.CASE_INSENSITIVE_ORDER);

        objectList = new ArrayList<CustomObject>();

        for (int i = 0; i < People.size();  i++){
            String PersonName = People.get(i);

            ITEMS =new ArrayList<String>( Arrays.asList(getItemList(this, PersonName)));

            for (int j = 0; j < ITEMS.size(); j++){
                CustomObject newObject = new CustomObject(ITEMS.get(j), PersonName);
                objectList.add(newObject);
            }

        }

            adapter = new ChristmasAdapter(this, R.layout.dual_list_item, R.id.item_name, R.id.family_member, objectList);
            christList.setAdapter(adapter);


            adapter.notifyDataSetChanged();

       /* adapter = new ArrayAdapter<String>(this, R.layout.list_item, ITEMS);
        christList.setAdapter(adapter);

        adapter.notifyDataSetChanged();
       */

        ImageButton addButton =
                (ImageButton) findViewById(R.id.add_button);
        addButton.setOnClickListener(addButtonListener);

        //christList.setOnItemClickListener(itemClickListener);

       // christList.setOnItemLongClickListener(itemLongClickListener);
    }

    public  String[] getItemList(Activity activity,String Key){
        String ItemsList = getStringsFromPreferences(activity, null, Key);
        return convertStringToArray(ItemsList);
    }

    private  String getStringsFromPreferences(Activity activity, String nick, String key){
        String temp = savedItems.getString(key, "");
        return temp;
    }

    private  String[] convertStringToArray(String str){
     String[] arr = str.split(",");
        return arr;
    }

    public void sendMessage(View view){
        Intent intent = new Intent(this,Main2Activity.class);
        startActivity(intent);
    }

    public class CustomObject {

        private String Item;
        private String FamilyMem;

        public CustomObject(String prop1, String prop2) {
            this.Item = prop1;
            this.FamilyMem = prop2;
        }

        public String getItem1() {
            return Item ;
        }

        public String getFamilyMem() {
            return FamilyMem;
        }
    };


    class ChristmasAdapter extends ArrayAdapter <CustomObject>{

        private LayoutInflater layoutinflater;

        public ChristmasAdapter(Context context, int id, int itemId,
                                int famId,
                                ArrayList<CustomObject> objects){
            super(context, id, objects);
            layoutinflater = LayoutInflater.from(getContext());

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            View view = layoutinflater.inflate(R.layout.dual_list_item, null);
            CustomObject newItem = getItem(position);

            TextView Item =(TextView) view.findViewById(R.id.item_name);
            TextView Fam = (TextView) view.findViewById(R.id.family_member);

            Item.setText(newItem.getItem1());
            Fam.setText(newItem.getFamilyMem());

            return view;

                    //super.getView(position,convertView,parent);
        }

    }

    /*public class CustomAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ArrayList<CustomObject> objects;

        private class ViewHolder {
            TextView textView1;
            TextView textView2;
        }

        public CustomAdapter(Context context, ArrayList<CustomObject> objects) {
            inflater = LayoutInflater.from(context);
            this.objects = objects;
        }

        public int getCount() {
            return objects.size();
        }

        public CustomObject getItem(int position) {
            return objects.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, View parent, ViewGroup v) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.dual_list_item, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.item_name);
                holder.textView2 = (TextView) convertView.findViewById(R.id.family_member);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView1.setText(objects.get(position).getprop1());
            holder.textView2.setText(objects.get(position).getprop2());
            return convertView;
        }
    };*/



    public View.OnClickListener addButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v ){
           sendMessage(v);
        }
    };

  /*
    public View.OnClickListener saveButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(itemEditText.getText().length() > 0){
                addTaggedSearch(itemEditText.getText().toString());
                itemEditText.setText("");

                //((inputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromwindow(tagEdit)
            }
           else
           {
               AlertDialog.Builder builder =
                       new AlertDialog.Builder(MainActivity.this);

               builder.setMessage(R.string.missingMessage);

               builder.setPositiveButton("OK", null);

               AlertDialog errorDialog = builder.create();
               errorDialog.show();
           }
        }
    };*/

    /*
    private void addTaggedSearch(String item){

        SharedPreferences.Editor preferencesEditor = savedGroceries.edit();
        preferencesEditor.putString(item, item);
        preferencesEditor.apply();

        if(!groceries.contains(item))
        {
            groceries.add(item);
            Collections.sort(groceries, String.CASE_INSENSITIVE_ORDER);
            adapter.notifyDataSetChanged(); //rebinds tags to listview
        }
    }*/
/*
    AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener(){

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            final String item = ((TextView) view).getText().toString();

            anchor = (TextView) view;

            PopupMenu popup = new PopupMenu(MainActivity.this, view);
            popup.setOnMenuItemClickListener(deleteMenuItemClickListener);
            popup.inflate(R.menu.delete_menu);
            popup.show();


            return true;
        }

    };
*/
/*
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            final String item = ((TextView) view).getText().toString();

            anchor = () view;

            PopupMenu popup = new PopupMenu(MainActivity.this, view);
            popup.setOnMenuItemClickListener(menuItemClickListener);
            popup.inflate(R.menu.item_options_menu);
            popup.show();


        }
    };

    PopupMenu.OnMenuItemClickListener deleteMenuItemClickListener = new PopupMenu.OnMenuItemClickListener(){
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            String christ_item = ((TextView)anchor).getText().toString();

            deleteSearch(christ_item);

            return true;
        }
    };

    private void deleteSearch(String item){
        ITEMS.remove(item);

        SharedPreferences.Editor preferencesEditor =
                savedItems.edit();

        preferencesEditor.remove(item);
        preferencesEditor.apply();

        adapter.notifyDataSetChanged();


    }

    PopupMenu.OnMenuItemClickListener menuItemClickListener = new PopupMenu.OnMenuItemClickListener(){

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()){

                case R.id.Amazon:
                    OpenAmazon();
                    return true;
                case R.id.Ebay:
                    OpenEbay();
                    return true;
                case R.id.WalMart:
                    OpenWalmart();
                    return true;
                default:
                    return false;

            }
            
        }
    };

    public void OpenAmazon(){
        String item = ((TextView) anchor).getText().toString();
        String urlString = getString(R.string.searchURL1)+ "&" + getString(R.string.searchURL2)
                + Uri.encode(savedItems.getString(item, ""), "UTF-8");

        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));

        startActivity(webIntent);
    }

    public void OpenEbay(){
        String item = ((TextView) anchor).getText().toString();
        String urlString = getString(R.string.ebaySearch1) + "&" + getString(R.string.ebaySearch2) + "&"
                + getString(R.string.ebaySearch3)
                + Uri.encode(savedItems.getString(item, ""), "UTF-8");

        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));

        startActivity(webIntent);
    }

    public void  OpenWalmart(){
        String item = ((TextView) anchor).getText().toString();
        String urlString = getString(R.string.walmartSearch)+ Uri.encode(savedItems.getString(item, ""), "UTF-8");

        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));

        startActivity(webIntent);
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}