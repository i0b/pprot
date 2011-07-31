#!/usr/bin/ruby1.9.1

# ===== includes +++++
require 'optparse'
require 'pp'
require 'find'
#require 'json'

if (RUBY_VERSION < "1.9")
 exit 1
end

# Example call: ruby1.9.1 examsAsJson.rb -f 2009,3,Rechnernetze,pdf

# ===== pre defined constants +++++
# TODO use the directories below and write a script that creates a directory structure based on these files - like Muendlich_Veranstaltung
examDirs = ['/home/fin/Pruefungsprotokolle/PProt/Dokumente/Klausur','/home/fin/Pruefungsprotokolle/PProt/Dokumente/Muendlich']
#examDirs = ['/home/fin/Pruefungsprotokolle/PProt_Sortiert/Schriftlich','/home/fin/Pruefungsprotokolle/PProt_Sortiert/Muendlich_Veranstaltung']

# ===== pre defined constants +++++
class Array
 def tail 
  self[1..-1]
 end
end

# ===== script handeling +++++
options = { :baseDirs => examDirs, :debug => false, :filter => ["[0-9]{4}-[0-9]{2}-[0-9]{2}","\.pdf"] }

# Input Example: 2004-08-26_ID00330_Muendlich_Dadam_ArchitekturUndImplementierungVonDatenbanksystemen,ObjektrelationaleUndErweiterbareDatenbanksysteme,SpeicherstrukturenUndZugriffspfadeInInformationssystemen.pdf
optparse = OptionParser.new do |opts|
   opts.on('-f', '--filter a,b,c', Array, 'List of parameters') do |list|
      options[:filter] = list
   end
   opts.on('--files', 'return the matching files instead of json') do |files|
      options[:files] = files
   end
   opts.on('-d', '--directory DIR1,DIR2,DIR3', Array, 'the base directories where the files are located') do |dirs|
      # Optional: do a check if the directory exists to avoid the exception
      options[:baseDirs] = dirs || examDirs
   end
   opts.on('--debug', 'show some additional information') do |debug|
      options[:debug] = debug
   end
end

optparse.parse!

# ===== debug output +++++
pp "Options:", options if options[:debug]


# ===== main +++++
fileArray = []

# fileArray entry after this loop:
# ["PATH","2004-08-26","ID00330","Muendlich","Schoening,Toran","Kryptologie,BerechenbarkeitUndKomplexitaet"]
options[:baseDirs].each { |dir|
   Find.find(dir) do |path|
      puts path if options[:debug]
      
      if ( options[:filter].inject (true) { |result, newTestElement| result && ( Regexp.new(newTestElement.downcase) === path.downcase )  } )
         fileArray.push( [ path ] + File.basename(path, '.pdf').split('_') )
      end
   end
}
# for version < 1.9 only: fileArray = fileArray.select { |examEntry| examEntry.size == 5 }
fileArray.keep_if { |examEntry| examEntry.size == 6 }

#TODO if the id finaly is obsolete remove the entry below
fileArray.map! { |examEntry| Hash["file"=>examEntry[0], "date"=>examEntry[1], "id"=>examEntry[2], "type"=>examEntry[3], "lecturer"=>examEntry[4], "courses"=>examEntry[5]] }

fileArray.each do |examEntry|
# examEntry.map! { |examEntryDetail| examEntryDetail.include?(',')? examEntryDetail.split(',') : examEntryDetail } }
  examEntry["lecturer"] = examEntry["lecturer"].split(',')
  
  if /\+Loesung/ === examEntry["courses"]
    examEntry["courses"].gsub!("+Loesung","")
    examEntry["solution"] = true
  end
  
  examEntry["courses"] = examEntry["courses"].split(',')
end

fileArray.keep_if { |examEntry| File.exist? (examEntry["file"]) }

# resolve links and put realpath name
fileArray.each { |examEntry| examEntry["file"] = File.realpath(examEntry["file"]) if File.ftype(examEntry["file"]) == "link" }

fileArray.uniq! { |examEntry| examEntry["file"] }

# fileArray.to_jason doesn't seem to be necessary as the output look well formated
if !options[:files] then
   pp fileArray.sample(10)
else
   puts fileArray.map{ |examEntry| examEntry["file"] }.join(' ')
end
