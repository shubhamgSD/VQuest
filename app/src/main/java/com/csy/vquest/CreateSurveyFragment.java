package com.csy.vquest;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;


public class CreateSurveyFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private int options = 2;
    private ListView optionsListView;
    String[] hints = {"1", "2", "3", "4"};
    public CreateSurveyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_survey, container, false);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.survey_option_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        optionsListView = (ListView) view.findViewById(R.id.survey_listView);

        return view;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        options = Integer.parseInt(parent.getItemAtPosition(position).toString());
        //listviewadapter adpter = new listviewadapter(getActivity(), R.layout.survey_option_item,hints);
        ArrayAdapter<String> adpter = new ArrayAdapter(getActivity(),R.layout.survey_option_item,R.id.input_option,hints);
        optionsListView.setAdapter(adpter);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        options = 2;
    }
}

