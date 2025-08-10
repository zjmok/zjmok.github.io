---
layout: post
tags: Android
---
```
    /**
     * 利用hint设置 末尾的省略号才会生效 EditText的直接设置是没有省略号的
     */
    public void setValueEllipsizeEnd(CharSequence text) {
        mEtInput.setText("");
        int currentTextColor = mEtInput.getCurrentTextColor();
        mEtInput.setHintTextColor(currentTextColor);
        mEtInput.setHint(text);
        mEtInput.setFocusable(false);
    }
```