package com.example.touchdispeargame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

public class TouchView extends ConstraintLayout {


    private ConstraintLayout rootView;

    private ArrayList<Integer> indexRecorderArray;

    private float maxRightX, maxBottomY, singleWidth, singleHeight;

    private ImageView ivIcon;

    private static final int FIRE = 0;

    private static final int WATER = 1;

    private Random random = new Random();
    ;

    private static final int GREEN = 2;

    private static final int DARK = 3;

    private static final int LIGHT = 4;

    private static final String FIRE_TAG = "fire";

    private static final String WATER_TAG = "water";

    private static final String GREEN_TAG = "green";

    private static final String DARK_TAG = "dark";

    private static final String LIGHT_TAG = "light";

    private float x = 0, y = 0;

    private boolean isCalculating;

    private View itemView;

    private Handler handler = new Handler(Looper.getMainLooper());

    private ArrayList<ItemData> itemDataArray;


    public TouchView(@NonNull Context context) {
        super(context);
        initView();
    }

    public TouchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TouchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

        rootView = (ConstraintLayout) View.inflate(getContext(), R.layout.touch_view, this);
        itemView = View.inflate(getContext(), R.layout.item_layout, null);
        ivIcon = itemView.findViewById(R.id.icon);
        rootView.post(viewPostRunnable);
    }

    private ArrayList<Integer> getIconArray() {
        ArrayList<Integer> iconArray = new ArrayList<>();
        iconArray.add(R.drawable.fire);
        iconArray.add(R.drawable.water);
        iconArray.add(R.drawable.green);
        iconArray.add(R.drawable.dark);
        iconArray.add(R.drawable.light);
        return iconArray;
    }


    private Runnable viewPostRunnable = new Runnable() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void run() {


            //取得最右邊座標與做下面座標
            maxBottomY = rootView.getBottom();
            maxRightX = rootView.getRight();

            final float middleScreenY = maxBottomY / 2;

            singleWidth = maxRightX / 5;
            singleHeight = middleScreenY / 5;
            rootView.removeAllViews();
            itemDataArray = new ArrayList<>();

            int textCount = 0;
            //目前先做五層
            for (int j = 0; j < 5; j++) {

                for (int i = 0; i < 5; i++) {
                    textCount++;
                    itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout, rootView, false);
                    ivIcon = itemView.findViewById(R.id.icon);


                    //賦予珠子圖片
//                    if (iconIndex > 3){
//                        iconIndex = 0;
//                    }
//                    ivIcon.setImageResource(getIconArray().get(iconIndex));
//                    iconIndex ++;

                    //隨機出現珠子
                    int randomIndex = random.nextInt(getIconArray().size());
                    ivIcon.setImageResource(getIconArray().get(randomIndex));


                    ConstraintLayout.LayoutParams params = new Constraints.LayoutParams((int) singleWidth, (int) singleHeight);
                    ConstraintLayout itemRoot = itemView.findViewById(R.id.item_root);
                    itemRoot.setLayoutParams(params);
                    itemRoot.setId(textCount);
                    String tag = getItemTag(randomIndex);

                    itemRoot.setTag(tag);

                    //蒐集item資料
                    ItemData data = new ItemData();
                    data.setIvIcon(ivIcon);
                    data.setItemRoot(itemRoot);
                    data.setId(textCount);
                    if (i == 0) {
                        itemView.setX(10f);
                        data.setLeftX(10f);
                        data.setRightX(10f + singleWidth);
                        float middleX = (((10f + singleWidth) - 10f) / 2f) + 10f;
                        data.setMiddleX(middleX);
                    } else {
                        itemView.setX(singleWidth * i);
                        data.setLeftX(singleWidth * i);
                        data.setRightX(singleWidth * i + singleWidth);
                        float middleX = (((singleWidth * i + singleWidth) - singleWidth * i) / 2f) + singleWidth * i;
                        data.setMiddleX(middleX);
                    }
                    if (j == 0) {
                        data.setTopY(middleScreenY);
                        data.setBottomY(middleScreenY + singleHeight);

                        //畫面的
                        itemView.setY(middleScreenY);
                    } else {
                        data.setTopY(middleScreenY + (singleHeight * j));
                        data.setBottomY(data.getTopY() + singleHeight);

                        //畫面的
                        itemView.setY(middleScreenY + (singleHeight * j));
                    }
                    float middleY = ((data.getBottomY() - data.getTopY()) / 2f) + data.getTopY();
                    data.setMiddleY(middleY);

                    itemDataArray.add(data);


                    rootView.addView(itemView);
//                itemRoot.setOnClickListener(handleOnClickListener(itemRoot));
                    itemRoot.setOnTouchListener(handleOnTouchListener(itemRoot));

                }

            }


            MichaelLog.i("maxBottomY : " + maxBottomY + " , maxRightX : " + maxRightX + " , array size : " + itemDataArray.size());
        }
    };




    //先測試第一排固定住
    @SuppressLint("ClickableViewAccessibility")
    private OnTouchListener handleOnTouchListener(final ConstraintLayout itemRoot) {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (isCalculating){
                    return false;
                }

                if (v.getId() == itemRoot.getId()) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            x = v.getX() - event.getRawX();
                            y = v.getY() - event.getRawY();
                            MichaelLog.i("x : " + x + " , y : " + y);
                            break;
                        case MotionEvent.ACTION_MOVE:

                            float moveX = event.getRawX() + x;
                            float moveY = event.getRawY() + y;
                            v.animate().x(moveX)
                                    .y(moveY)
                                    .setDuration(0)
                                    .start();

                            MichaelLog.i("移動x : " + (event.getRawX() + x) + " , y : " + (event.getRawY() + y));

                            //在移動的同時也判斷是否要換方格
                            checkNeedToMove(moveX, moveY, itemRoot.getId());
                            break;
                        case MotionEvent.ACTION_UP:
                            MichaelLog.i("action Up 開始計算");
                            isCalculating = true;

                            int currentId = itemRoot.getId();
                            v.animate().x(itemDataArray.get(currentId - 1).getLeftX()).y(itemDataArray.get(currentId - 1).getTopY()).setDuration(0).start();

                            //開始執行消除
                            handler.postDelayed(checkDisappearRunnable, 100);

                            break;
                        default:
                            return false;

                    }
                    return true;
                }
                return true;
            }
        };
    }

    private Runnable checkDisappearRunnable = new Runnable() {
        @Override
        public void run() {
            checkDisappearItem();
        }
    };


    private void checkDisappearItem() {
        indexRecorderArray = new ArrayList<>();
        for (int i = 0; i < 21; i += 5) {
            //先橫的全版 與值得全版
            String rootTag = (String) itemDataArray.get(i).getItemRoot().getTag();
            String firstTag = (String) itemDataArray.get(i + 1).getItemRoot().getTag();
            String secondTag = (String) itemDataArray.get(i + 2).getItemRoot().getTag();
            String thirdTag = (String) itemDataArray.get(i + 3).getItemRoot().getTag();
            String fourthTag = (String) itemDataArray.get(i + 4).getItemRoot().getTag();
            if (rootTag.equals(firstTag) && rootTag.equals(secondTag) && rootTag.equals(thirdTag) && rootTag.equals(fourthTag)) {
                MichaelLog.i("有整行的要消除");
                for (int j = i; j <= i + 4; j++) {
                    indexRecorderArray.add(j);
                    rootView.removeView(itemDataArray.get(j).getItemRoot());
                }
            }
        }

        //再來是直版
        for (int i = 0; i < 5; i++) {
            String rootTag = (String) itemDataArray.get(i).getItemRoot().getTag();
            String firstTag = (String) itemDataArray.get(i + 5).getItemRoot().getTag();
            String secondTag = (String) itemDataArray.get(i + 10).getItemRoot().getTag();
            String thirdTag = (String) itemDataArray.get(i + 15).getItemRoot().getTag();
            String fourthTag = (String) itemDataArray.get(i + 20).getItemRoot().getTag();
            if (rootTag.equals(firstTag) && rootTag.equals(secondTag) && rootTag.equals(thirdTag) && rootTag.equals(fourthTag)) {
                MichaelLog.i("有整行的要消除");
                for (int j = i; j <= i + 20; j += 5) {
                    indexRecorderArray.add(j);
                    rootView.removeView(itemDataArray.get(j).getItemRoot());
                }
            }
        }

        //再來是三格以上的,先找橫排
        for (int i = 0; i < 25; i++) {
            if (i == 3 || i == 4 || i == 8 || i == 9 || i == 13 || i == 14 || i == 18 || i == 19 || i == 23 || i == 24) {
                continue;
            }
            int firstIndex = i + 1;
            int secondIndex = i + 2;
            int thirdIndex = i + 3;
            if (firstIndex > 24 || secondIndex > 24) {
                break;
            }
            String rootTag = (String) itemDataArray.get(i).getItemRoot().getTag();
            String firstTag = (String) itemDataArray.get(firstIndex).getItemRoot().getTag();
            String secondTag = (String) itemDataArray.get(secondIndex).getItemRoot().getTag();
            String thirdTag = thirdIndex > 24 ? "" : (String) itemDataArray.get(thirdIndex).getItemRoot().getTag();
            if (rootTag.equals(firstTag) && rootTag.equals(secondTag)) {
                indexRecorderArray.add(i);
                indexRecorderArray.add(firstIndex);
                indexRecorderArray.add(secondIndex);
                rootView.removeView(itemDataArray.get(i).getItemRoot());
                rootView.removeView(itemDataArray.get(secondIndex).getItemRoot());
                rootView.removeView(itemDataArray.get(firstIndex).getItemRoot());
            }
            if (rootTag.equals(firstTag) && rootTag.equals(secondTag) && rootTag.equals(thirdTag)) {
                indexRecorderArray.add(thirdIndex);
                rootView.removeView(itemDataArray.get(thirdIndex).getItemRoot());
            }
        }

        //再來是三格以上的,先找直排
        for (int i = 0; i < 15; i++) {
            int firstIndex = i + 5;
            int secondIndex = i + 10;
            int thirdIndex = i + 15;
            String rootTag = (String) itemDataArray.get(i).getItemRoot().getTag();
            String firstTag = (String) itemDataArray.get(firstIndex).getItemRoot().getTag();
            String secondTag = (String) itemDataArray.get(secondIndex).getItemRoot().getTag();
            String thirdTag = thirdIndex > 24 ? "" : (String) itemDataArray.get(thirdIndex).getItemRoot().getTag();
            if (rootTag.equals(firstTag) && rootTag.equals(secondTag)) {
                indexRecorderArray.add(i);
                indexRecorderArray.add(firstIndex);
                indexRecorderArray.add(secondIndex);
                rootView.removeView(itemDataArray.get(i).getItemRoot());
                rootView.removeView(itemDataArray.get(secondIndex).getItemRoot());
                rootView.removeView(itemDataArray.get(firstIndex).getItemRoot());

            }
            if (rootTag.equals(firstTag) && rootTag.equals(secondTag) && rootTag.equals(thirdTag)) {
                indexRecorderArray.add(thirdIndex);
                rootView.removeView(itemDataArray.get(thirdIndex).getItemRoot());
            }
        }


        handler.postDelayed(addRandomView, 50);


    }


    private Runnable addRandomView = new Runnable() {
        @Override
        public void run() {
            doAddRandomView();
        }
    };

    private String getItemTag(int randomIndex) {
        String tag = "";
        switch (randomIndex) {
            case FIRE:
                tag = FIRE_TAG;
                break;
            case WATER:
                tag = WATER_TAG;
                break;
            case GREEN:
                tag = GREEN_TAG;
                break;
            case DARK:
                tag = DARK_TAG;
                break;
            case LIGHT:
                tag = LIGHT_TAG;
                break;
        }
        return tag;
    }

    //長出來
    private void doAddRandomView() {
        if (indexRecorderArray.isEmpty()) {
            isCalculating = false;
            MichaelLog.i("找不到相同的珠子不消除");
            return;
        }
        //先判斷index有沒有重複
        Iterator<Integer> indexIterator = indexRecorderArray.iterator();
        while (indexIterator.hasNext()) {
            int index = indexIterator.next();
            int sameIndexCount = 0;
            for (int loopIndex : indexRecorderArray) {
                if (index == loopIndex) {
                    sameIndexCount++;
                }
                if (sameIndexCount == 2) {
                    indexIterator.remove();
                    break;
                }
            }
        }
        //這邊要想辦法讓剩餘的珠子往下掉下來
        ArrayList<Integer> restIndexArray = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            boolean isFoundRestItem = true;
            for (Integer index : indexRecorderArray) {
                if (index == i) {
                    isFoundRestItem = false;
                    break;
                }
            }
            if (isFoundRestItem) {
                restIndexArray.add(i);
            }
        }


        Collections.sort(restIndexArray, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {

                if (o1 > o2) {
                    return -1;
                }
                return 1;
            }
        });
        Collections.sort(indexRecorderArray);

        for (Integer restIndex : restIndexArray) {
            MichaelLog.i("restIndex : " + restIndex);

            int firstLineIndex = restIndex + 5;
            int secondLineIndex = restIndex + 10;
            int thirdLineIndex = restIndex + 15;
            int fourthLineIndex = restIndex + 20;

            boolean isFoundFirstSpace = false;
            boolean isFoundSecondSpace = false;
            boolean isFoundThirdSpace = false;
            boolean isFoundFourthSpace = false;
            int lastIndex = 0;
            if (firstLineIndex >= 24){
                continue;
            }

            for (Integer spaceIndex : indexRecorderArray) {
                if (firstLineIndex == spaceIndex){
                    lastIndex = spaceIndex;
                    isFoundFirstSpace = true;
                }
                if (secondLineIndex == spaceIndex){
                    lastIndex = spaceIndex;
                    isFoundSecondSpace = true;
                }
                if (thirdLineIndex == spaceIndex){
                    lastIndex = spaceIndex;
                    isFoundThirdSpace = true;
                }
                if (fourthLineIndex == spaceIndex){
                    MichaelLog.i("fourthLineIndex : "+fourthLineIndex + " , spaceIndex : "+spaceIndex);
                    lastIndex = spaceIndex;
                    isFoundFourthSpace = true;
                }
            }

            if (isFoundFourthSpace){

                switchItem(restIndex,lastIndex);
                continue;
            }
            if (isFoundThirdSpace){

                switchItem(restIndex,lastIndex);
                continue;
            }
            if (isFoundSecondSpace){

                switchItem(restIndex,lastIndex);
                continue;
            }
            if (isFoundFirstSpace){

                switchItem(restIndex,lastIndex);
            }
        }

        handler.postDelayed(addViewRunnable,1000);

    }

    private Runnable addViewRunnable = new Runnable() {
        @Override
        public void run() {
            for (Integer index : indexRecorderArray) {
                MichaelLog.i("num : "+index);
                int randomIndex = random.nextInt(getIconArray().size());
                itemDataArray.get(index).getIvIcon().setImageResource(getIconArray().get(randomIndex));
                itemDataArray.get(index).getItemRoot().setTag(getItemTag(randomIndex));
                rootView.addView(itemDataArray.get(index).getItemRoot(),index);

//                AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f,1.0f);
//                alphaAnimation.setDuration(500);
//                itemDataArray.get(index).getItemRoot().startAnimation(alphaAnimation);

                TranslateAnimation animation = new TranslateAnimation(0f,0f,-400f,0f);
                animation.setDuration(250);
                itemDataArray.get(index).getItemRoot().startAnimation(animation);

            }
            handler.postDelayed(checkAgainDisappearRunnable,1000);
        }
    };

    private Runnable checkAgainDisappearRunnable = new Runnable() {
        @Override
        public void run() {
            checkDisappearItem();
        }
    };


    private void switchItem(Integer restIndex, int lastIndex) {

        ItemData currentItem = itemDataArray.get(restIndex);

        itemDataArray.get(lastIndex).getItemRoot().setTag(currentItem.getItemRoot().getTag());
        itemDataArray.get(lastIndex).getIvIcon().setImageResource(getTagIcon((String) currentItem.getItemRoot().getTag()));

        rootView.removeView(itemDataArray.get(restIndex).getItemRoot());
        rootView.addView(itemDataArray.get(lastIndex).getItemRoot());

        TranslateAnimation animation = new TranslateAnimation(0f,0f,-200f,0f);
        animation.setDuration(200);
        itemDataArray.get(lastIndex).getItemRoot().startAnimation(animation);

        int index = 0;

        for (int i = 0 ; i < indexRecorderArray.size() ; i ++){
            if (lastIndex == indexRecorderArray.get(i)){
                index = i;
                break;
            }
        }

        indexRecorderArray.remove(index);
        indexRecorderArray.add(restIndex);
        Collections.sort(indexRecorderArray);
    }

    private int getTagIcon(String tag) {
        int iconId = 0;

        switch (tag){
            case GREEN_TAG:
                iconId = R.drawable.green;
                break;
            case LIGHT_TAG:
                iconId = R.drawable.light;
                break;
            case DARK_TAG:
                iconId = R.drawable.dark;
                break;
            case WATER_TAG:
                iconId = R.drawable.water;
                break;
            case FIRE_TAG:
                iconId = R.drawable.fire;
                break;
        }
        return iconId;
    }

    private void checkNeedToMove(float currentMoveX, float currentMoveY, int id) {


        //斜轉先暫停
//        if (isTurnSlanting(currentMoveX,currentMoveY,id)){
//            return;
//        }

        if (isTurnLefOrRight(currentMoveX, id)) {
            return;
        }

        switchDownOrTop(id, currentMoveY);
    }

    private boolean isTurnSlanting(float currentMoveX, float currentMoveY, int id) {

        int currentId = id - 1;
        int leftUpId = currentId - 6;
        int leftBottomId = currentId + 4;
        int rightUpId = currentId - 4;
        int rightBottomId = currentId + 6;

        ItemData currentItem = itemDataArray.get(currentId);
        ItemData leftUpItem = leftUpId < 0 ? null : itemDataArray.get(leftUpId);
        ItemData leftBottomItem = leftBottomId > 24 ? null : itemDataArray.get(leftBottomId);
        ItemData rightUpItem = rightUpId < 0 ? null : itemDataArray.get(rightUpId);
        ItemData rightBottomItem = rightBottomId > 24 ? null : itemDataArray.get(rightBottomId);

        float testRightX = currentMoveX + singleWidth;
        float testBottomY = currentMoveY + singleHeight;

        MichaelLog.i("testRightX : " + testRightX + " , testBottomY : " + testBottomY + " , item leftX "
                + rightBottomItem.getLeftX() + " ,item topY : " + rightBottomItem.getTopY() + " rightBottomId : " + rightBottomId);

        if (id == 1 && rightBottomItem != null && testRightX - rightBottomItem.getLeftX() >= 100f && testBottomY - rightBottomItem.getTopY() >= 100f) {
            MichaelLog.i("往上挪");
            int currentViewId = currentItem.getItemRoot().getId();
            float leftX = currentItem.getLeftX();
            float rightX = currentItem.getRightX();
            float middleX = currentItem.getMiddleX();
            float topY = currentItem.getTopY();
            float bottomY = currentItem.getBottomY();
            float middleY = currentItem.getMiddleY();

            rightBottomItem.getItemRoot().animate().setDuration(0)
                    .x(leftX).y(topY).start();

            //再將自己賦予新直
            currentItem.getItemRoot().setId(rightBottomItem.getItemRoot().getId());
            currentItem.setId(rightBottomItem.getId());
            currentItem.setLeftX(rightBottomItem.getLeftX());
            currentItem.setRightX(rightBottomItem.getRightX());
            currentItem.setMiddleX(rightBottomItem.getMiddleX());
            currentItem.setTopY(rightBottomItem.getTopY());
            currentItem.setBottomY(rightBottomItem.getBottomY());
            currentItem.setMiddleY(rightBottomItem.getMiddleY());
            itemDataArray.set(rightBottomId, currentItem);

            //再將左邊的item變成我原本的直
            rightBottomItem.getItemRoot().setId(currentViewId);
            rightBottomItem.setId(currentViewId);
            rightBottomItem.setLeftX(leftX);
            rightBottomItem.setRightX(rightX);
            rightBottomItem.setMiddleX(middleX);
            rightBottomItem.setTopY(topY);
            rightBottomItem.setBottomY(bottomY);
            rightBottomItem.setMiddleY(middleY);
            itemDataArray.set(currentId, rightBottomItem);
        }


        return false;
    }

    private void switchDownOrTop(int id, float currentMoveY) {
        ItemData currentItem = itemDataArray.get(id - 1);
        int downId = id + 5 - 1 >= 25 ? 24 : id + 5 - 1;
        ItemData downItem = itemDataArray.get(downId);
        int topId = Math.max(id - 5 - 1, 0);
        MichaelLog.i("topId : " + topId);
        MichaelLog.i("downMiddelY : " + downItem.getMiddleY());
        ItemData topItem = itemDataArray.get(topId);

        if (currentMoveY <= topItem.getMiddleY() && id >= 6) {
            MichaelLog.i("往上挪");
            int currentId = currentItem.getItemRoot().getId();
            float leftX = currentItem.getLeftX();
            float rightX = currentItem.getRightX();
            float middleX = currentItem.getMiddleX();
            float topY = currentItem.getTopY();
            float bottomY = currentItem.getBottomY();
            float middleY = currentItem.getMiddleY();

            topItem.getItemRoot().animate().setDuration(0)
                    .x(leftX).y(topY).start();

            //再將自己賦予新直
            currentItem.getItemRoot().setId(topItem.getItemRoot().getId());
            currentItem.setId(topItem.getId());
            currentItem.setLeftX(topItem.getLeftX());
            currentItem.setRightX(topItem.getRightX());
            currentItem.setMiddleX(topItem.getMiddleX());
            currentItem.setTopY(topItem.getTopY());
            currentItem.setBottomY(topItem.getBottomY());
            currentItem.setMiddleY(topItem.getMiddleY());
            itemDataArray.set(topId, currentItem);

            //再將左邊的item變成我原本的直
            topItem.getItemRoot().setId(currentId);
            topItem.setId(currentId);
            topItem.setLeftX(leftX);
            topItem.setRightX(rightX);
            topItem.setMiddleX(middleX);
            topItem.setTopY(topY);
            topItem.setBottomY(bottomY);
            topItem.setMiddleY(middleY);
            itemDataArray.set(id - 1, topItem);
            return;
        }


        if (currentMoveY + singleHeight >= downItem.getMiddleY() && id < 21) {
            MichaelLog.i("往下挪");
            int currentId = currentItem.getItemRoot().getId();
            float leftX = currentItem.getLeftX();
            float rightX = currentItem.getRightX();
            float middleX = currentItem.getMiddleX();
            float topY = currentItem.getTopY();
            float bottomY = currentItem.getBottomY();
            float middleY = currentItem.getMiddleY();

            downItem.getItemRoot().animate().setDuration(0)
                    .x(leftX).y(topY).start();

            //再將自己賦予新直
            currentItem.getItemRoot().setId(downItem.getItemRoot().getId());
            currentItem.setId(downItem.getId());
            currentItem.setLeftX(downItem.getLeftX());
            currentItem.setRightX(downItem.getRightX());
            currentItem.setMiddleX(downItem.getMiddleX());
            currentItem.setTopY(downItem.getTopY());
            currentItem.setBottomY(downItem.getBottomY());
            currentItem.setMiddleY(downItem.getMiddleY());
            itemDataArray.set(downId, currentItem);

            //再將左邊的item變成我原本的直
            downItem.getItemRoot().setId(currentId);
            downItem.setId(currentId);
            downItem.setLeftX(leftX);
            downItem.setRightX(rightX);
            downItem.setMiddleX(middleX);
            downItem.setTopY(topY);
            downItem.setBottomY(bottomY);
            downItem.setMiddleY(middleY);
            itemDataArray.set(id - 1, downItem);
        }
    }

    private boolean isTurnLefOrRight(float currentMoveX, int id) {
        ItemData currentItem = itemDataArray.get(id - 1);
        int rightId = id == 25 ? 24 : id;
        ItemData rightItem = itemDataArray.get(rightId);
        int leftId = Math.max(id - 2, 0);
        ItemData leftItem = itemDataArray.get(leftId);

        if (currentMoveX <= leftItem.getMiddleX() && id != 1 && id != 6 && id != 11 && id != 16 && id != 21) {
            //重新賦予左邊的 item位置 與 id
            //先記下自己的資料
            int currentId = currentItem.getId();
            float leftX = currentItem.getLeftX();
            float rightX = currentItem.getRightX();
            float middleX = currentItem.getMiddleX();
            float topY = currentItem.getTopY();
            float bottomY = currentItem.getBottomY();
            float middleY = currentItem.getMiddleY();

            leftItem.getItemRoot().animate().setDuration(0)
                    .x(leftX).y(topY).start();

            //再將自己賦予新直
            currentItem.getItemRoot().setId(leftItem.getId());
            currentItem.setId(leftItem.getId());
            currentItem.setLeftX(leftItem.getLeftX());
            currentItem.setRightX(leftItem.getRightX());
            currentItem.setMiddleX(leftItem.getMiddleX());
            currentItem.setTopY(leftItem.getTopY());
            currentItem.setBottomY(leftItem.getBottomY());
            currentItem.setMiddleY(leftItem.getMiddleY());
            itemDataArray.set(leftId, currentItem);

            //再將左邊的item變成我原本的直
            leftItem.getItemRoot().setId(currentId);
            leftItem.setId(currentId);
            leftItem.setLeftX(leftX);
            leftItem.setRightX(rightX);
            leftItem.setMiddleX(middleX);
            leftItem.setTopY(topY);
            leftItem.setBottomY(bottomY);
            leftItem.setMiddleY(middleY);
            itemDataArray.set(id - 1, leftItem);
            return true;
        }


        if (currentMoveX + singleWidth >= rightItem.getMiddleX() && id != 10 && id != 5 && id != 15 && id != 20 && id != 25) {
            //重新賦予右邊的 item位置 與 id
            //先記下自己的資料
            int currentId = currentItem.getId();
            float leftX = currentItem.getLeftX();
            float rightX = currentItem.getRightX();
            float middleX = currentItem.getMiddleX();
            float topY = currentItem.getTopY();
            float bottomY = currentItem.getBottomY();
            float middleY = currentItem.getMiddleY();

            rightItem.getItemRoot().animate().setDuration(0)
                    .x(leftX).y(topY).start();

            //再將自己賦予新直
            currentItem.getItemRoot().setId(rightItem.getId());
            currentItem.setId(rightItem.getId());
            currentItem.setLeftX(rightItem.getLeftX());
            currentItem.setRightX(rightItem.getRightX());
            currentItem.setMiddleX(rightItem.getMiddleX());
            currentItem.setTopY(rightItem.getTopY());
            currentItem.setBottomY(rightItem.getBottomY());
            currentItem.setMiddleY(rightItem.getMiddleY());
            itemDataArray.set(rightId, currentItem);

            //再將左邊的item變成我原本的直
            rightItem.getItemRoot().setId(currentId);
            rightItem.setId(currentId);
            rightItem.setLeftX(leftX);
            rightItem.setRightX(rightX);
            rightItem.setMiddleX(middleX);
            rightItem.setTopY(topY);
            rightItem.setBottomY(bottomY);
            rightItem.setMiddleY(middleY);
            itemDataArray.set(id - 1, rightItem);
            return true;
        }
        return false;
    }

//    //這個目前用不到
//    private OnClickListener handleOnClickListener(final ConstraintLayout itemRoot) {
//
//        return new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        };
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }
}
