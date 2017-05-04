# pinyinSearch
根据拼音匹配汉字


### usage

maven
```
<dependency>
  <groupId>me.sfce</groupId>
  <artifactId>pinyinSearch</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```

gradle

```
compile 'me.sfce:pinyinSearch:1.0.1'
```

 Ivy
```
<dependency org='me.sfce' name='pinyinSearch' rev='1.0.1'>
  <artifact name='pinyinSearch' ext='pom' ></artifact>
</dependency>
```

Call this method
```
PinyinUtil.match(src, pinyin);
```
