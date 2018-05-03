package com.sh.zsh.code.layout.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bigkoo.pickerview.OptionsPickerView;
import com.sh.zsh.code.layout.model.ViewData;

import java.util.ArrayList;


/**
 * Created by zhush on 2017/3/14.
 * E-mail 405086805@qq.com
 * PS
 */
public class FormSpinner extends AppCompatTextView {

    OptionsPickerView pvOptions;//选项选择器
    Object pvOptionsSelectValue;//选中的value
    Object pvOptionsSelectValueObject;//选中的value对象

    String titel;
    ArrayList<ViewData> options1Items ;
    private SelectedListener selectedListener; //选中监听
    public FormSpinner(Context context) {
        super(context);
        pvOptions = new OptionsPickerView(context);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                pvOptions.show();
            }
        });
    }
    int selectIndex;
    public FormSpinner(final Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, com.sh.shform.R.styleable.less_from_view, 0, 0);
        selectIndex = ta.getInt(com.sh.shform.R.styleable.less_from_view_less_form_spi_sel,999);
        pvOptions = new OptionsPickerView(context);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity=(Activity) context;
                InputMethodManager imm =  (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null) {
                    imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(),
                            0);
                }
                pvOptions.show();
            }
        });
    }
    public void setpvOptionsList(ArrayList<ViewData> options, String titel){

        this.titel =titel;
        setpvOptionsList(options);
    }
    /**
     * 设置选择器list
     */
    Boolean isinit=true;
    public void setpvOptionsList(ArrayList<ViewData> options){
        this.options1Items =options;

        //三级联动效果
        pvOptions.setPicker(options1Items);

        //pvOptions.setPicker(options1Items,options1Items,true);
        //设置选择的三级单位
        //pvOptions.setLabels("省", "市");


        pvOptions.setTitle(titel);
        super.setText(titel);
        pvOptions.setCyclic(false, true, true);
        //设置默认选中的三级项目
        //监听确定选择按钮
//        pvOptions.setSelectOptions(1, 1, 1);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                if(isinit){
                    options1=selectIndex;
                    isinit=false;
                }
                String tx = options1Items.get(options1).getPickerViewText();
                FormSpinner.super.setText(tx);
                pvOptionsSelectValue = options1Items.get(options1).getValue();
                pvOptionsSelectValueObject = options1Items.get(options1).getValueObject();
                if(selectedListener!=null){
                    selectedListener.pvOptions(tx,pvOptionsSelectValue,pvOptionsSelectValueObject);
                }
            }
        });

        if(selectIndex>=0&&selectIndex<options.size()){
            //默认选中第一个
            pvOptions.setSelecedItem(selectIndex,0,0);
        }


    }

    /**
     * 选中监听
     */
    public static interface SelectedListener{
        public void pvOptions(String key, Object value, Object pvOptionsSelectValueObject);
    }

    public void setSelectedListener(SelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    /**
     * 设置选中
     * @param indext
     */
    public void setSelect(int indext){
        pvOptions.setSelectOptions(indext);
    }

    public String getSelectKey(){
            return getText()+"";
    }
    public Object getSelectValue(){
        return pvOptionsSelectValue;
    }
    public Object getSelectValueObject(){
        return pvOptionsSelectValueObject;
    }
}
