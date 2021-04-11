package com.example.danhba;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.danhba.databinding.InfoContactBinding;

public class InfoContact extends Fragment {
    InfoContactBinding binding;
    SQLHelper sqlHelper;
    String id,name,phone;

    public static InfoContact newInstance(String id,String name,String phone) {

        Bundle bundle = new Bundle();
        InfoContact fragment = new InfoContact();
        bundle.putString("id",id);
        bundle.putString("name",name);
        bundle.putString("phone",phone);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.info_contact,container,false);
        sqlHelper = new SQLHelper(getContext());
        name =getArguments().getString("name");
        binding.edtName.setText(name);
        phone = getArguments().getString("phone");
        binding.edtPhone.setText(phone);
        id = getArguments().getString("id");
        if(name==""){
            binding.btnSave.setText("Thêm liên lạc");
            binding.btnRemove.setVisibility(View.GONE);

        }
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = PhoneBook.newInstance();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.layout_fragment, fragment);
                fragmentTransaction.commit();
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = binding.edtName.getText().toString();
                phone = binding.edtPhone.getText().toString();
                if(name.equals("")||phone.equals("")){
                    Toast.makeText(getContext(),"Tên hoặc số điện thoại không được để trống",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(binding.btnSave.getText().toString().equals("Thêm liên lạc")) {
                        Contact contact = new Contact(name, phone);
                        sqlHelper.InsertContact(contact);
                        Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                        binding.edtName.setText("");
                        binding.edtPhone.setText("");
                        Fragment fragment = PhoneBook.newInstance();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.layout_fragment, fragment);
                        fragmentTransaction.commit();
                    }
                    else{
                        sqlHelper.updateContact(id,new Contact(name,phone));
                        Toast.makeText(getContext(),"Lưu thành công",Toast.LENGTH_SHORT).show();
                        Fragment fragment = PhoneBook.newInstance();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.layout_fragment, fragment);
                        fragmentTransaction.commit();
                    }
                }
            }
        });
        binding.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlHelper.deleteContact(id);
                Toast.makeText(getContext(),"Xóa thành công",Toast.LENGTH_SHORT).show();
                binding.edtName.setText("");
                binding.edtPhone.setText("");
                Fragment fragment = PhoneBook.newInstance();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.layout_fragment, fragment);
                fragmentTransaction.commit();
            }
        });
        return binding.getRoot();
    }

}
