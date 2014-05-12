「Apache Kafka入門」 サポートページ
===================

## サンプルコードについて
　サンプルコードはKafkaのバージョンごとに配置しています。ご利用の環境に合わせて選択ください。
また、各ファイルの右側に対応する章を記載していますので参考にしてください。

### 0.7.2 用

    Sample0.7.2/
      +helloworld/                             :Mavenプロジェクトディレクトリ
        +src/
          +main/

            +java/
              +com/
                +example/
                  - ConsumerSample.java         :６章 Consumer
                  - ConsumerSample2.java        :６章 Consumer
                  - ConsumerSample3.java        :６章 Consumer
                  - ConsumerSample4.java        :６章 Consumer
                  - ConsumerSample5.java        :６章 Consumer
                  - HelloWorldConsumer.java     :２章 Kafkaを使ってみよう
                  - HelloWorldProducer.java     :２章 Kafkaを使ってみよう
                  - Log4jAppenderTestApp.java   :２章 Kafkaを使ってみよう
                  - ProducerSample.java         :５章 Producer
                  - ProducerSample2.java        :５章 Producer

            +python/
              - RegisterProducer.py             :７章 Pythonでスーパーマーケット
              - UresujiConsumer.py              :７章 Pythonでスーパーマーケット
              - UriageConsumer.py               :７章 Pythonでスーパーマーケット

            +resources/
              - ConsumerSample4.properties      :６章 Consumer
              - ConsumerSample5.properties      :６章 Consumer
              - log4j.properties                :２章 Kafkaを使ってみよう
              - log4j.xml                       :２章 Kafkaを使ってみよう
              - ProducerSample.properties       :５章 Producer

            +ruby/
              - RegisterProducer.rb             :７章 Rubyでスーパーマーケット(レジ)をやってみる
              - UresujiConsumer.rb              :７章 Rubyでスーパーマーケット(レジ)をやってみる

        - pom.xml                               :２章 Kafkaを使ってみよう

### 0.8.1 用

　(準備中)

    Sample0.8.1/
      +helloworld/                             :プロジェクトディレクトリ
        +src/
          +main/

            +java/

　  
　  
　  

## 訂正情報
　次回更新時に下記内容を修正する予定です。ご迷惑おかけして申し訳ありません。

#### ■5と6章のサンプルコード
　書籍内のサンプルコードはトピック名が"Sample"と"sample"で混在していますが、"sample"に統一します。このページに登録しているサンプルソースは"sample"に統一してあります。

#### ■4.1.6設定
　log.roll.hoursの説明として、「ログ中のセグメントファイルが切り替わる間隔（分）を指定します。」としていますが、"（分）" ⇒ "（時間）"の誤りです。

　なお、Appendixの当該説明では正しく記載されています。