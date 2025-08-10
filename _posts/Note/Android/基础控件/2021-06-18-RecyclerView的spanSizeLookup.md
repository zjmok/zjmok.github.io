---
layout: post
tags: RecyclerView
---

```
layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int {
								//使用n倍item位置 不能超出spanCount
                                if (position == 0) {
                                    return 2 
                                }
                                return 1
                            }
                        }
```
