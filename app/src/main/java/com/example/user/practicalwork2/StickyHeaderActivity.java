package com.example.user.practicalwork2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.practicalwork2.Adapters.GalaxyCell;
import com.example.user.practicalwork2.Adapters.mAdapter;
import com.example.user.practicalwork2.Models.Category;
import com.example.user.practicalwork2.Models.Galaxy;
import com.example.user.practicalwork2.Models.ModelCompanies;
import com.example.user.practicalwork2.Models.ModelExpandAble;
import com.example.user.practicalwork2.Models.ModelMainMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleRecyclerView;
import com.jaychang.srv.decoration.SectionHeaderProvider;
import com.jaychang.srv.decoration.SimpleSectionHeaderProvider;
import com.kodmap.library.kmrecyclerviewstickyheader.KmRecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StickyHeaderActivity extends AppCompatActivity {

    SimpleRecyclerView simpleRecyclerView;

    ModelCompanies mc;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbReferenceMainMenu;
    DatabaseReference dbReferenceStarter1;

    ArrayList<ModelExpandAble> listStarterData1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_header);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbReferenceStarter1 = firebaseDatabase.getReference("Starter1");
        dbReferenceMainMenu = firebaseDatabase.getReference("COMPANYMENU");

        listStarterData1 = new ArrayList<>();

        simpleRecyclerView = findViewById(R.id.recyclerView);
        this.addRecyclerHeaders();
        this.bindData();

        //this.showHeader();
        //this.bindMyData();
    }

    public void showHeader() {
        Toast.makeText(this, "showHeader function", Toast.LENGTH_SHORT).show();

        SectionHeaderProvider<ModelExpandAble> sh = new SimpleSectionHeaderProvider<ModelExpandAble>() {

            @NonNull
            @Override
            public View getSectionHeaderView(@NonNull ModelExpandAble item, int position) {

                Toast.makeText(StickyHeaderActivity.this, "in getSectionHeaderView", Toast.LENGTH_SHORT).show();
                //the header layout above child to make a section
                View view = LayoutInflater.from(StickyHeaderActivity.this).inflate(R.layout.simple_header, null, false);
                final TextView textView = view.findViewById(R.id.headerTxt);

                textView.setText("Spiral");

                dbReferenceMainMenu.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot menuSnapshot : dataSnapshot.getChildren()) {
                            ModelMainMenu dataMenu = menuSnapshot.getValue(ModelMainMenu.class);
                            String companyTitle = dataMenu.getMmcompanyTitle();

                            if (companyTitle.equals(mc.getCompanyTitle())) {
                                textView.setText(dataMenu.getMmName());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                return view;
            }

            @Override
            public boolean isSameSection(@NonNull ModelExpandAble item, @NonNull ModelExpandAble nextItem) {
                Toast.makeText(StickyHeaderActivity.this, "inSameSectionFunction", Toast.LENGTH_SHORT).show();
                return item.getmName().equals(nextItem.getmName());
            }

            @Override
            public boolean isSticky() {
                return true;
            }
        };

        simpleRecyclerView.setSectionHeader(sh);
    }

    private void addRecyclerHeaders() {
        SectionHeaderProvider<Galaxy> sh = new SimpleSectionHeaderProvider<Galaxy>() {
            @NonNull
            @Override
            public View getSectionHeaderView(@NonNull Galaxy Galaxy, int i) {
                View view = LayoutInflater.from(StickyHeaderActivity.this).inflate(R.layout.simple_header, null, false);
                TextView textView = view.findViewById(R.id.headerTxt);
                textView.setText(Galaxy.getCategoryName());
                return view;
            }

            @Override
            public boolean isSameSection(@NonNull Galaxy Galaxy, @NonNull Galaxy nextGalaxy) {
                return Galaxy.getCategoryId() == nextGalaxy.getCategoryId();
            }

            // Optional, whether the header is sticky, default false
            @Override
            public boolean isSticky() {
                return true;
            }
        };
        simpleRecyclerView.setSectionHeader(sh);
    }

    public void bindMyData() {

        List<ModelExpandAble> modelExpandAbles = getmData();

        /*Collections.sort(modelExpandAbles, new Comparator<ModelExpandAble>() {
            @Override
            public int compare(ModelExpandAble obj1, ModelExpandAble obj2) {
                return obj1.getmId() - obj2.getmId();
            }
        });*/

        List<mAdapter> cells = new ArrayList<>();

        for (ModelExpandAble modelExpandAble : modelExpandAbles) {
            mAdapter cell = new mAdapter(modelExpandAble);

            cell.setOnCellClickListener(new SimpleCell.OnCellClickListener<ModelExpandAble>() {
                @Override
                public void onCellClicked(@NonNull ModelExpandAble item) {
                    Toast.makeText(StickyHeaderActivity.this, item.getmName(), Toast.LENGTH_SHORT).show();
                }
            });
            cell.setOnCellLongClickListener(new SimpleCell.OnCellLongClickListener<ModelExpandAble>() {
                @Override
                public void onCellLongClicked(@NonNull ModelExpandAble item) {
                    Toast.makeText(StickyHeaderActivity.this, item.getmDescription(), Toast.LENGTH_SHORT).show();
                }
            });

            cells.add(cell);
        }

        simpleRecyclerView.addCells(cells);
    }

    /*
    - Bind data to our RecyclerView
     */
    private void bindData() {
        List<Galaxy> Galaxys = getData();
        //CUSTOM SORT ACCORDING TO CATEGORIES
        Collections.sort(Galaxys, new Comparator<Galaxy>() {
            public int compare(Galaxy Galaxy, Galaxy nextGalaxy) {
                return Galaxy.getCategoryId() - nextGalaxy.getCategoryId();
            }
        });
        List<GalaxyCell> cells = new ArrayList<>();

        //LOOP THROUGH GALAXIES INSTANTIATING THEIR CELLS AND ADDING TO CELLS COLLECTION
        for (Galaxy galaxy : Galaxys) {
            GalaxyCell cell = new GalaxyCell(galaxy);

            // There are two default cell listeners: OnCellClickListener<CELL, VH, T> and OnCellLongClickListener<CELL, VH, T>
            cell.setOnCellClickListener(new SimpleCell.OnCellClickListener<Galaxy>() {
                @Override
                public void onCellClicked(@NonNull Galaxy item) {
                    Toast.makeText(StickyHeaderActivity.this, item.getName(), Toast.LENGTH_SHORT).show();

                }
            });
            cell.setOnCellLongClickListener(new SimpleCell.OnCellLongClickListener<Galaxy>() {
                @Override
                public void onCellLongClicked(@NonNull Galaxy item) {
                    Toast.makeText(StickyHeaderActivity.this, item.getDescription(), Toast.LENGTH_SHORT).show();

                }
            });

            cells.add(cell);
        }
        simpleRecyclerView.addCells(cells);
    }

    public ArrayList<ModelExpandAble> getmData() {

        //CREATE CATEGORIES
        Category elliptical = new Category(0, "Elliptical");
        Category irregular = new Category(1, "Irregular");
        Category spiral = new Category(2, "Spiral");

        dbReferenceStarter1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelExpandAble modelExpandAble = snapshot.getValue(ModelExpandAble.class);

                    String name = modelExpandAble.getmCompanyName();

                    listStarterData1.add(modelExpandAble);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return listStarterData1;
    }

    /*
    - Data Source
    - Returns an arraylist of galaxies.
     */
    private ArrayList<Galaxy> getData() {
        ArrayList<Galaxy> galaxies = new ArrayList<>();

        //CREATE CATEGORIES
        Category elliptical = new Category(0, "Elliptical");
        Category irregular = new Category(1, "Irregular");
        Category spiral = new Category(2, "Spiral");

        //INSTANTIATE GALAXY OBJECTS AND ADD THEM TO GALAXY LIST
        Galaxy g = new Galaxy("Whirlpool",
                "The Whirlpool Galaxy, also known as Messier 51a, M51a, and NGC 5194, is an interacting grand-design spiral Galaxy with a Seyfert 2 active galactic nucleus in the constellation Canes Venatici.",
                R.drawable.img_kababs, spiral);
        galaxies.add(g);

        g = new Galaxy("Ring Nebular",
                "The Ring Nebula is a planetary nebula in the northern constellation of Lyra. Such objects are formed when a shell of ionized gas is expelled into the surrounding interstellar medium by a red giant star.",
                R.drawable.img_kababs, elliptical);
        galaxies.add(g);

        g = new Galaxy("IC 1011",
                "C 1011 is a compact elliptical galaxy with apparent magnitude of 14.7, and with a redshift of z=0.02564 or 0.025703, yielding a distance of 100 to 120 Megaparsecs. Its light has taken 349.5 million years to travel to Earth.",
                R.drawable.img_kababs, elliptical);
        galaxies.add(g);

        g = new Galaxy("Cartwheel",
                "The Cartwheel Galaxy is a lenticular galaxy and ring galaxy about 500 million light-years away in the constellation Sculptor. It is an estimated 150,000 light-years diameter, and a mass of about 2.9–4.8 × 10⁹ solar masses; it rotates at 217 km/s.",
                R.drawable.img_kababs, irregular);
        galaxies.add(g);

        g = new Galaxy("Triangulumn",
                "The Triangulum Galaxy is a spiral Galaxy approximately 3 million light-years from Earth in the constellation Triangulum",
                R.drawable.img_kababs, spiral);
        galaxies.add(g);

        g = new Galaxy("Small Magellonic Cloud",
                "The Small Magellanic Cloud, or Nubecula Minor, is a dwarf galaxy near the Milky Way. It is classified as a dwarf irregular galaxy.",
                R.drawable.img_kababs, irregular);
        galaxies.add(g);

        g = new Galaxy("Centaurus A",
                " Centaurus A or NGC 5128 is a galaxy in the constellation of Centaurus. It was discovered in 1826 by Scottish astronomer James Dunlop from his home in Parramatta, in New South Wales, Australia.",
                R.drawable.img_kababs, elliptical);
        galaxies.add(g);

        g = new Galaxy("Ursa Minor",
                "The Milky Way is the Galaxy that contains our Solar System." +
                        " The descriptive milky is derived from the appearance from Earth of the Galaxy – a band of light seen in the night sky formed from stars",
                R.drawable.img_kababs, irregular);
        galaxies.add(g);

        g = new Galaxy("Large Magellonic Cloud",
                " The Large Magellanic Cloud is a satellite galaxy of the Milky Way. At a distance of 50 kiloparsecs, the LMC is the third-closest galaxy to the Milky Way, after the Sagittarius Dwarf Spheroidal and the.",
                R.drawable.img_kababs, irregular);
        galaxies.add(g);

        g = new Galaxy("Milky Way",
                "The Milky Way is the Galaxy that contains our Solar System." +
                        " The descriptive milky is derived from the appearance from Earth of the Galaxy – a band of light seen in the night sky formed from stars",
                R.drawable.img_kababs, spiral);
        galaxies.add(g);

        g = new Galaxy("Andromeda",
                "The Andromeda Galaxy, also known as Messier 31, M31, or NGC 224, is a spiral Galaxy approximately 780 kiloparsecs from Earth. It is the nearest major Galaxy to the Milky Way and was often referred to as the Great Andromeda Nebula in older texts.",
                R.drawable.img_kababs, irregular);
        galaxies.add(g);

        g = new Galaxy("Messier 81",
                "Messier 81 is a spiral Galaxy about 12 million light-years away in the constellation Ursa Major. Due to its proximity to Earth, large size and active galactic nucleus, Messier 81 has been studied extensively by professional astronomers.",
                R.drawable.img_kababs, elliptical);
        galaxies.add(g);

        g = new Galaxy("Own Nebular",
                " The Owl Nebula is a planetary nebula located approximately 2,030 light years away in the constellation Ursa Major. It was discovered by French astronomer Pierre Méchain on February 16, 1781",
                R.drawable.img_kababs, elliptical);
        galaxies.add(g);

        g = new Galaxy("Messier 87",
                "Messier 87 is a supergiant elliptical galaxy in the constellation Virgo. One of the most massive galaxies in the local universe, it is notable for its large population of globular clusters—M87 contains",
                R.drawable.img_kababs, elliptical);
        galaxies.add(g);

        g = new Galaxy("Cosmos Redshift",
                "Cosmos Redshift 7 is a high-redshift Lyman-alpha emitter Galaxy, in the constellation Sextans, about 12.9 billion light travel distance years from Earth, reported to contain the first stars —formed ",
                R.drawable.img_kababs, irregular);
        galaxies.add(g);

        g = new Galaxy("StarBust",
                "A starburst Galaxy is a Galaxy undergoing an exceptionally high rate of star formation, as compared to the long-term average rate of star formation in the Galaxy or the star formation rate observed in most other galaxies. ",
                R.drawable.img_kababs, irregular);
        galaxies.add(g);

        g = new Galaxy("Sombrero",
                "Sombrero Galaxy is an unbarred spiral galaxy in the constellation Virgo located 31 million light-years from Earth. The galaxy has a diameter of approximately 50,000 light-years, 30% the size of the Milky Way.",
                R.drawable.img_kababs, spiral);
        galaxies.add(g);

        g = new Galaxy("Pinwheel",
                "The Pinwheel Galaxy is a face-on spiral galaxy distanced 21 million light-years away from earth in the constellation Ursa Major. ",
                R.drawable.img_kababs, spiral);
        galaxies.add(g);

        g = new Galaxy("Canis Majos Overdensity",
                "The Canis Major Dwarf Galaxy or Canis Major Overdensity is a disputed dwarf irregular galaxy in the Local Group, located in the same part of the sky as the constellation Canis Major. ",
                R.drawable.img_kababs, irregular);
        galaxies.add(g);

        g = new Galaxy("Virgo Stella Stream",
                " Group, located in the same part of the sky as the constellation Canis Major. ",
                R.drawable.img_kababs, spiral);
        galaxies.add(g);

        return galaxies;
    }


}
