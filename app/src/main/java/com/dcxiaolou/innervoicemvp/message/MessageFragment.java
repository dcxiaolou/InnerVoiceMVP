package com.dcxiaolou.innervoicemvp.message;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dcxiaolou.innervoicemvp.R;
import com.dcxiaolou.innervoicemvp.data.DataStore;
import com.dcxiaolou.innervoicemvp.login.LoginActivity;
import com.dcxiaolou.innervoicemvp.utils.Constants;

public class MessageFragment extends Fragment implements MessageContract.View {

    private DataStore mDataStore = DataStore.getINSTANCE();

    private TextView tvToLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view;
        if (mDataStore.getCurrentUser() == null)
            view = inflater.inflate(R.layout.fragment_unlogin, container, false);
        else
            view = inflater.inflate(R.layout.fragment_message, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mDataStore.getCurrentUser() == null) {
            tvToLogin = (TextView) view.findViewById(R.id.tv_to_login);

            tvToLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.putExtra(Constants.LOGIN_DISTINGUISH_ACTIVITY, MessageFragment.class.getName());
                    startActivity(intent);
                }
            });
        }
    }
}
