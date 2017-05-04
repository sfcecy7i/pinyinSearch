# pinyinSearch
根据拼音匹配汉字


### usage

maven
```
<dependency>
  <groupId>me.sfce</groupId>
  <artifactId>app</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

gradle

```
compile 'me.sfce:app:1.0.0'
```

 Ivy
```
<dependency org='me.sfce' name='app' rev='1.0.0'>
  <artifact name='app' ext='pom' ></artifact>
</dependency>
```

Call this method
```
PinyinUtil.match(src, pinyin);
```
