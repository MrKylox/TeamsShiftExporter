def longestConsecutiveSequence(nums):
    nums.sort()
    print(nums)
    newArray = []
    sequenceLength = 0
    tempSequence = 0
 # I want to increment temp sequence when it's a duplicate at the beginning as long as it's not at the end.

 
    if(len(nums) == 1):
        return 1
    
    if(len(nums)==2) and nums[0] == nums[1]:
        return 1
    
    for i in range(len(nums)-1):


        if(nums[i + 1] == nums[i]+1) and nums[i] not in newArray:
            newArray.append(nums[i])
            newArray.append(nums[i+1])
            tempSequence += 2

        elif(i == len(nums)-2 and nums[i]+1 == nums[i+1]):
            newArray.append(nums[i+1])
            tempSequence += 1
        
        if nums[i] == nums[i+1] and nums[i] not in newArray:
            # newArray.append(nums[i])
            # tempSequence += 1
            continue
 
        

        if (nums[i+1] != nums[i]+1) is not (nums[i] == nums[i+1]):
            tempSequence = 0

        if sequenceLength < tempSequence:
            sequenceLength = tempSequence

    return sequenceLength


 
nums = [9,1,4,7,3,-1,0,5,8,-1,6]
nums2 = [0,3,7,2,5,8,4,6,0,1]
nums3 = [100,4,200,1,3,2]
nums4 = [1,2,0,1]
nums5 = [-7,-1,3,-9,-4,7,-3,2,4,9,4,-9,8,-7,5,-1,-7]
nums6 = [0,0]
nums7 = [4,0,-4,-2,2,5,2,0,-8,-8,-8,-8,-1,7,4,5,5,-4,6,6,-3]
nums8 = [0,-1]

print(longestConsecutiveSequence(nums7))