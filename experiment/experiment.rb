require 'travis' 
require 'squid'

Travis.access_token = "PrTI9ywO3k7-FwK9mBmteQ"
puts "Hello #{Travis::User.current.name}"

def experiment(buildNumber, filename)

	repo = Travis::Repository.find('idodin/skrape.it')

	puts "Accessed Repository: #{repo.slug}"

	puts "Running Build Number #{buildNumber} to output: #{filename}" 

	build = repo.build(buildNumber)

	caches = Hash.new(0)

	for i in 1..10
		build.restart
		sleep(5)
		build.reload
		state = ""
		until build.state == "passed" do
			puts "Build status is now: #{build.state}" unless state == build.state 
			sleep(5)
			state = build.state 
			build.reload
		end
		puts "Build took: #{build.duration} seconds"
		caches[i] = build.duration.to_i
	end

	data = {buildtimes: caches}
	Prawn::Document.generate filename do
		chart data, type: :two_axis
	end
end


experiment(1, "mavenbuildtimes-nocache.pdf")
experiment(2, "mavenbuildtimes-cached.pdf")
experiment(5, "gradlebuildtimes-nocache.pdf")
experiment(6, "gradlebuildtimes-cached.pdf")
experiment(7, "gradlebuildtimes-nologger.pdf")




