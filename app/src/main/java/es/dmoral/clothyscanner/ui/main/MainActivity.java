package es.dmoral.clothyscanner.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.clothyscanner.R;
import es.dmoral.clothyscanner.data.access.DBAccessor;
import es.dmoral.clothyscanner.data.db.ClothyScannerDBHelper;
import es.dmoral.clothyscanner.data.model.Machine;
import es.dmoral.clothyscanner.ui.scanner.ContinuousCaptureActivity;

/**
 * This file is part of ClothyScanner.
 *
 * ClothyScanner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ClothyScanner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ClothyScanner.  If not, see <http://www.gnu.org/licenses/>.
 */

public class MainActivity extends AppCompatActivity implements MachineAdapter.OnMachineClickListener,
        MachineAdapter.OnMachineLongClickListener {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.machine_recycler_view) RecyclerView machineRecyclerView;

    private Machine machineToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        setListeners();
    }

    private void setupViews() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        machineRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(machineRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        machineRecyclerView.addItemDecoration(dividerItemDecoration);
        machineRecyclerView.setHasFixedSize(true);
        machineRecyclerView.setItemAnimator(new DefaultItemAnimator());
        machineRecyclerView.setAdapter(
                new MachineAdapter(DBAccessor.getMachines(ClothyScannerDBHelper.getClothyScannerDBHelper(this)), this, this));
        registerForContextMenu(machineRecyclerView);
    }

    private void setListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(R.string.add_machine_title)
                        .customView(R.layout.add_dialog_layout, true)
                        .autoDismiss(false)
                        .cancelable(false)
                        .positiveText(android.R.string.ok)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @SuppressWarnings("ConstantConditions")
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                View view = dialog.getCustomView();
                                String friendlyName;
                                String ip;
                                ip = ((TextInputEditText) view.findViewById(R.id.et_ip)).getText().toString();
                                if (ip.trim().isEmpty()) {
                                    ((TextInputLayout) dialog.getCustomView().findViewById(R.id.input_layout_ip)).setError(getString(R.string.empty_ip_error));
                                    view.findViewById(R.id.et_ip).requestFocus();
                                    return;
                                }
                                friendlyName = ((TextInputEditText) view.findViewById(R.id.et_friendly_name)).getText().toString();
                                if (friendlyName.trim().isEmpty()) {
                                    friendlyName = ip;
                                }
                                Machine machine = new Machine(ip, friendlyName, false);
                                if (((MachineAdapter) machineRecyclerView.getAdapter()).alreadyAdded(machine)) {
                                    ((TextInputLayout) dialog.getCustomView().findViewById(R.id.input_layout_ip)).setError(getString(R.string.ip_exists_error));
                                    view.findViewById(R.id.et_ip).requestFocus();
                                    return;
                                }
                                DBAccessor.addMachine(machine,
                                        ClothyScannerDBHelper.getClothyScannerDBHelper(MainActivity.this));
                                ((MachineAdapter) machineRecyclerView.getAdapter()).addMachine(machine);
                                dialog.dismiss();
                            }
                        })
                        .negativeText(android.R.string.cancel)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void onMachineClick(Machine machine) {
        Intent scanIntent = new Intent(this, ContinuousCaptureActivity.class);
        scanIntent.putExtra(ContinuousCaptureActivity.IP_ARG, machine.getIp());
        startActivity(scanIntent);
    }

    @Override
    public void onMachineLongClick(Machine machine) {
        machineToDelete = machine;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ((MachineAdapter) machineRecyclerView.getAdapter()).removeMachine(machineToDelete);
        DBAccessor.deleteMachine(machineToDelete, ClothyScannerDBHelper.getClothyScannerDBHelper(this));
        return true;
    }

    @Override
    public void onContextMenuClosed(Menu menu) {
        machineToDelete = null;
    }
}
