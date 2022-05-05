package com.imagemachine.view.dashboard.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.imagemachine.R;
import com.imagemachine.database.DBHelper;
import com.imagemachine.databinding.FragmentMachineBinding;
import com.imagemachine.view.dashboard.ui.home.adapter.AdapterListMachine;
import com.imagemachine.view.dashboard.ui.home.model.MachineModel;
import com.imagemachine.view.inputmachine.InputMachineActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class MachineFragment extends Fragment {

    private MachineViewModel machineViewModel;
    private FragmentMachineBinding binding;
    private Button btnSortedList, btnAddMachineData;
    private RecyclerView rvListMachine;
    private com.imagemachine.viewmodel.MachineViewModel machineVM;
    private AdapterListMachine adapterListMachine;
    private ArrayList<MachineModel> listMachine = new ArrayList<>();
    private DBHelper dbHelper;

    Dialog dialogPreview;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        machineViewModel =
                new ViewModelProvider(this).get(MachineViewModel.class);

        binding = FragmentMachineBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbHelper = new DBHelper(getActivity());

        if (dbHelper.databaseExists(getActivity())) {
            machineVM = new com.imagemachine.viewmodel.MachineViewModel(getActivity());
            machineVM.getAllMachine();
        }

        btnAddMachineData = binding.btnAddMachine;
        btnSortedList = binding.btnSortedList;
        rvListMachine = binding.rvListMachineFragMachine;

        btnAddMachineData.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), InputMachineActivity.class);
            intent.putExtra("from", "add");
            startActivity(intent);
        });

        btnSortedList.setOnClickListener(v -> {
            openSortedDialog();
        });

        initLiveData();

        return root;
    }

    void initLiveData() {
        machineVM.getListMachine().observe(Objects.requireNonNull(getActivity()), machineModels -> {
            Collections.sort(machineModels, new Comparator<MachineModel>() {
                @Override
                public int compare(MachineModel o1, MachineModel o2) {
                    String title1 = o1.getNameMachine();
                    String title2 = o2.getNameMachine();
                    return title1.compareToIgnoreCase(title2);
                }
            });
            adapterListMachine = new AdapterListMachine(getActivity(), machineModels);
            rvListMachine.setItemAnimator(new DefaultItemAnimator());
            rvListMachine.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvListMachine.setAdapter(adapterListMachine);
            adapterListMachine.notifyDataSetChanged();

            listMachine = machineModels;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void openSortedDialog(){
        dialogPreview = new Dialog(getActivity());
        dialogPreview.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPreview.setContentView(R.layout.dialog_sort);
        dialogPreview.setCancelable(true);
        Window window = dialogPreview.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent));

        Button btnSortedName = dialogPreview.findViewById(R.id.btnSortedByName);
        Button btnSortedType = dialogPreview.findViewById(R.id.btnSortedByType);

        btnSortedName.setOnClickListener(v -> {
            sortedByName();
            dialogPreview.dismiss();
        });

        btnSortedType.setOnClickListener(v -> {
            sortedByType();
            dialogPreview.dismiss();
        });

        dialogPreview.show();
    }

    public void sortedByName() {
        Collections.sort(listMachine, new Comparator<MachineModel>() {
            @Override
            public int compare(MachineModel o1, MachineModel o2) {
                String title1 = o1.getNameMachine();
                String title2 = o2.getNameMachine();
                return title1.compareToIgnoreCase(title2);
            }
        });
        adapterListMachine = new AdapterListMachine(getActivity(), listMachine);
        rvListMachine.setItemAnimator(new DefaultItemAnimator());
        rvListMachine.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvListMachine.setAdapter(adapterListMachine);
        adapterListMachine.notifyDataSetChanged();
    }

    public void sortedByType() {
        Collections.sort(listMachine, new Comparator<MachineModel>() {
            @Override
            public int compare(MachineModel o1, MachineModel o2) {
                String type1 = o1.getTypeMachine();
                String type2 = o2.getTypeMachine();
                return type1.compareToIgnoreCase(type2);
            }
        });
        adapterListMachine = new AdapterListMachine(getActivity(), listMachine);
        rvListMachine.setItemAnimator(new DefaultItemAnimator());
        rvListMachine.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvListMachine.setAdapter(adapterListMachine);
        adapterListMachine.notifyDataSetChanged();
    }
}