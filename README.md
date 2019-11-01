# KotlinRxJava

RxJava
Rx stands for Reactive Extensions.
RxJava is a JVM implementation of Reactive Extensions.
Reactive Extension is a library that’s used for writing asynchronous event-based reactive code by using observables. We’ll see what are observables shortly.
RxJava is useful and very powerful in the sense that it takes care of multithreading very well.
If you’re a Java developer, you’ll be well aware of the fact that multithreading can get tricky. RxJava takes care of multi-threading by doing complex thread operations, keeping everything synchronized and returning the relevant things to the main thread.
In Android, the main thread is the UI thread. RxJava handles multithreading with a level of abstraction. We need to write less code and the underlying methods do the rest for us.
RxJava Basics
The basic building blocks of RxJava are:

Observables: That emits data streams
Observers and Subscribers: That consume the data stream. The only difference between an Observer and a Subscriber is that a Subscriber class has the methods to unsubscribe/resubscribe independently without the need of the observerable methods.
Operators: That transform the data stream
rxjava basics flow

Observables -----> Operators ------> Observers

Before we get into the details of each of the above, let’s analyze each of them which an interesting analogy.
Let’s say the Observables is me the tutor, flowing the data.
You are the Observer/Subscriber who receives the data.
Additionally, Operators can transform the data that you receive from me. Example: An operator can change the default language of this tutorial data from English to any other language.
