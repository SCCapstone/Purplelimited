package edu.sc.purplelimited.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import edu.sc.purplelimited.R;

public class PopupListAdapter extends BaseExpandableListAdapter {

  private Context context;
  private ArrayList<String> sectionsList;
  private HashMap<String, ArrayList<String>> contentList;

  public PopupListAdapter(Context context, ArrayList<String> sectionsList, HashMap<String,
                          ArrayList<String>> contentList) {
    this.context = context;
    this.sectionsList = sectionsList;
    this.contentList = contentList;
  }

  @Override
  public int getGroupCount() {
    return this.sectionsList.size();
  }

  @Override
  public int getChildrenCount(int i) {
    return this.contentList.get(this.sectionsList.get(i)).size();
  }

  @Override
  public Object getGroup(int i) {
    return this.sectionsList.get(i);
  }

  @Override
  public Object getChild(int i, int i1) {
    return this.contentList.get(this.sectionsList.get(i)).get(i1);
  }

  @Override
  public long getGroupId(int i) {
    return i;
  }

  @Override
  public long getChildId(int i, int i1) {
    return i1;
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }

  @Override
  public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
    String sectionTitle = (String) getGroup(i);
    if (view == null) {
      LayoutInflater inflater =
              (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.sections_list, null);
    }
    TextView chapterTv = view.findViewById(R.id.section_textview);
    chapterTv.setText(sectionTitle);
    return view;
  }

  @Override
  public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
    String sectionContent = (String) getChild(i, i1);
    if (view == null) {
      LayoutInflater inflater =
              (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.section_content_list, null);
    }
    TextView topicTv = view.findViewById(R.id.content_textview);
    topicTv.setText(sectionContent);
    return view;
  }

  @Override
  public boolean isChildSelectable(int i, int i1) {
    return true;
  }
}
