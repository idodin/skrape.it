FROM ruby:2.6

RUN mkdir /app
WORKDIR /app

ENV GEM_HOME="/usr/local/bundle"
ENV PATH $GEM_HOME/bin:$GEM_HOME/gems/bin:$PATH

ADD Gemfile /app/
RUN bundle install

COPY experiment.rb /app/experiment.rb

CMD ruby /app/experiment.rb
