package com.decard.zj.founctiontest.calendar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.decard.calendarlibs.CalendarView;
import com.decard.calendarlibs.CalendarViewWrapper;
import com.decard.calendarlibs.MonthTitleViewCallBack;
import com.decard.calendarlibs.OnCalendarSelectDayListener;
import com.decard.calendarlibs.SelectionMode;
import com.decard.calendarlibs.model.CalendarDay;
import com.decard.calendarlibs.model.CalendarSelectDay;
import com.decard.zj.founctiontest.R;
import com.decard.zj.founctiontest.TestApplication;
import com.example.commonlibs.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CalendarDialogFragment extends DialogFragment {

    private View contentView;
    private CalendarSelectDay<CalendarDay> calendarSelectDay;
    private CalendarView calendarView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_calendar_dialog, container);

//        initSelectCalendar();
        initView();
        return contentView;
    }


    private void initView() {
        calendarView = contentView.findViewById(R.id.calendar_view);

        long time = DateUtils.convertToLong("2019/08/08 12:00:05", "yyyy/MM/dd HH:mm:ss");
        Date minDate = new Date(time);
        Date currentDate = new Date(System.currentTimeMillis());

//        Calendar calendar = Calendar.getInstance();
//        Date minDate = calendar.getTime();
//        calendar.add(Calendar.MONTH, 12);
//        Date maxDate = calendar.getTime();
        CalendarViewWrapper.wrap(calendarView)
                //设置最大最小日期范围 展示三个月数据
                .setDateRange(minDate, currentDate)
                //设置默认选中日期
                .setCalendarSelectDay(calendarSelectDay)
                //设置选择模式为范围选择
                .setSelectionMode(SelectionMode.RANGE)
                //设置选中回调
                .setOnCalendarSelectDayListener(new OnCalendarSelectDayListener<CalendarDay>() {
                    @Override
                    public void onCalendarSelectDay(CalendarSelectDay<CalendarDay> calendarSelectDay) {
                        CalendarDay firstSelectDay = calendarSelectDay.getFirstSelectDay();
                        CalendarDay lastSelectDay = calendarSelectDay.getLastSelectDay();
                    }
                })
                //头部月份是否悬停
                .setStick(true)
                //是否展示头部月份
                .setShowMonthTitleView(true)
                //设置展示头部月份的回调用于创建头部月份View
                .setMonthTitleViewCallBack(new MonthTitleViewCallBack() {
                    @Override
                    public View getMonthTitleView(int position, Date date) {
                        View view = View.inflate(TestApplication.instance, R.layout.layout_calendar_month_title, null);
                        TextView tvMonthTitle = view.findViewById(R.id.tv_month_title);
                        tvMonthTitle.setText(formatDate("yyyy年MM月", date));
                        return view;
                    }
                })
                .display();

        //根据指定日期得到position位置
//        int position = calendarView.covertToPosition(calendarSelectDay.getFirstSelectDay());
//        if (position != -1) {
//            //滚动到指定位置
//            calendarView.smoothScrollToPosition(position);
//        }
    }


    /**
     * 默认选择当前日期到下一个月1号
     */
    private void initSelectCalendar() {
        calendarSelectDay = new CalendarSelectDay<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendarSelectDay.setFirstSelectDay(new CalendarDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)));
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        calendarSelectDay.setLastSelectDay(new CalendarDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)));
    }


    public String formatDate(String format, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        return dateString;
    }
}
